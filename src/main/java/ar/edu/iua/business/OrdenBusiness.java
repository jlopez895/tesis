package ar.edu.iua.business;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ar.edu.iua.rest.Constantes;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.eventos.OrdenEvent;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.dto.ConciliacionDTO;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.OrdenRepository;

@Service
public class OrdenBusiness implements IOrdenBusiness {

	@Autowired
	private IChoferBusiness choferService;

	@Autowired
	private ICamionBusiness camionService;

	@Autowired
	private IClienteBusiness clienteService;

	@Autowired
	private IProductoBusiness productoService;

	@Autowired
	private OrdenRepository ordenDAO;
	
	private float temperaturaMaxima = 80;
	

	@Override
	public RespuestaGenerica<Orden> recibirEstadoUno(Orden orden) throws BusinessException {
		System.out.println(orden.getNumeroOrden());
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Orden> rg = new RespuestaGenerica<Orden>(orden, m);

		String mensajeCheck = orden.checkBasicDataStatusOne();

		if (mensajeCheck != "Ok para estado 1") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			System.out.println("estoy en un error");
			return rg;
		}

		try {
			System.out.println("estoy aca");
			orden.setNumeroOrden(orden.getNumeroOrden());
			orden.setChofer(choferService.asegurarChofer(orden.getChofer()));
			orden.setCamion(camionService.asegurarCamion(orden.getCamion()));
			orden.setCliente(clienteService.asegurarCliente(orden.getCliente()));
			orden.setProducto(productoService.asegurarProducto(orden.getProducto()));
			orden.setTurno(orden.getTurno());
			orden.setPreset(orden.getPreset());
			orden.setEstado(1);
			ordenDAO.save(orden);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;

	}

	@Override
	public RespuestaGenerica<Orden> recibirEstadoDos(Orden orden, int nroOrden)
			throws BusinessException, NotFoundException {

		Orden ordenVieja = load(nroOrden);
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Orden> rg = new RespuestaGenerica<Orden>(orden, m);

		String mensajeCheck = orden.checkBasicDataStatusTwo(ordenVieja.getEstado(), ordenVieja.getTurno());

		if (mensajeCheck != "Ok para estado 2") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return rg;
		}

		try {

			ordenVieja.setPesajeInicial(orden.getPesajeInicial());
			ordenVieja.setPassword(orden.getPassword());
			ordenVieja.setEstado(2);
			ordenVieja.setFrecuenciaAlmacenamiento(orden.getFrecuenciaAlmacenamiento());
			ordenVieja.setFechaHoraPesajeInicial(new Date());
			ordenDAO.save(ordenVieja);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;

	}

	@Override
	public Orden load(int nroOrden) throws BusinessException, NotFoundException {
		Optional<Orden> orden = null;
		try {

			orden = ordenDAO.findByNumeroOrden(nroOrden);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!orden.isPresent())
			throw new NotFoundException("El orden no se encuentra en la BD");

		return orden.get();
	}

	@Override
	public RespuestaGenerica<Orden> pesajeFinal(Orden orden, int nroOrden) throws BusinessException, NotFoundException {

		Orden ordenVieja = load(nroOrden);
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Orden> rg = new RespuestaGenerica<Orden>(orden, m);

		String mensajeCheck = orden.checkBasicDataStatusFour(ordenVieja, nroOrden);

		if (mensajeCheck != "Ok para pesaje final") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return rg;
		}

		try {

			ordenVieja.setEstado(4);
			ordenVieja.setPesajeFinal(orden.getPesajeFinal());
			ordenVieja.setFechaHoraPesajeFinal(new Date());
			ordenVieja.setPromedioCaudal(ordenDAO.promedioCaudal(ordenVieja.getId()));
			ordenVieja.setPromedioDensidad(ordenDAO.promedioDensidad(ordenVieja.getId()));
			ordenVieja.setPromedioTemperatura(ordenDAO.promedioTemperatura(ordenVieja.getId()));
			ordenDAO.save(ordenVieja);

			rg = generarConciliacion(nroOrden);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;
	}

	@Override
	public RespuestaGenerica<Orden> generarConciliacion(int nroOrden) throws BusinessException, NotFoundException {

		Orden ordenVieja = load(nroOrden);
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Orden> rg = new RespuestaGenerica<Orden>(ordenVieja, m);

		if (ordenVieja.getEstado() != 4) {
			m.setCodigo(-1);
			m.setMensaje("Para pedir la conciliacion la orden debe estar en estado 4");
			return rg;
		}

		try {
			ConciliacionDTO conciliacion = new ConciliacionDTO(ordenVieja.getPesajeInicial(),
					ordenVieja.getPesajeFinal(), ordenVieja.getUltimaMasaAcumulada(),
					ordenVieja.getPesajeFinal() - ordenVieja.getPesajeInicial(),
					(ordenVieja.getPesajeFinal() - ordenVieja.getPesajeInicial()) - ordenVieja.getUltimaMasaAcumulada(),
					ordenVieja.getPromedioTemperatura(), ordenVieja.getPromedioCaudal(),
					ordenVieja.getPromedioDensidad());

			m.setCodigo(0);
			m.setMensaje(conciliacion.toString());

		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;

	}

	@Override
	public List<Orden> list() throws BusinessException {
		try {
			return ordenDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RespuestaGenerica<Orden> cerrarOrden(int nroOrden) throws BusinessException, NotFoundException {
	
			Orden orden = load(nroOrden);
			MensajeRespuesta m = new MensajeRespuesta();
			RespuestaGenerica<Orden> rg = new RespuestaGenerica<Orden>(orden, m);



			String mensajeCheck = orden.checkBasicDataStatusThree(orden); 

			if (mensajeCheck != "Ok para cerrar orden") {
				m.setCodigo(-1);
				m.setMensaje(mensajeCheck);
				return rg;
			}

			try {
				orden.setEstado(3);
				ordenDAO.save(orden);
			} catch (Exception e) {
				throw new BusinessException();
			}

			return rg;
		}
		
	public float getTemperaturaMaxima() {
		return temperaturaMaxima; 
	}

	public void cambiarUmbralTemperatura(Float temp) {
		temperaturaMaxima = temp;
	}
	
	@Autowired
	private ApplicationEventPublisher appEventPublisher;

	private void generaEvento(Orden orden, OrdenEvent.Tipo tipo) {
		appEventPublisher.publishEvent(new OrdenEvent(orden, tipo));
	}
	
	@Autowired
	private SimpMessagingTemplate wSock;
	
	@Override
	public void pushOrderData() {
		try {
			wSock.convertAndSend(Constantes.TOPIC_SEND_WEBSOCKET_GRAPH, ordenDAO.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

	
	

