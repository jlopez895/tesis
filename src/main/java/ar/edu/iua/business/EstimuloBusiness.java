package ar.edu.iua.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.OrdenRepository;

@Service
public class EstimuloBusiness implements IEstimuloBusiness{
	
	@Autowired
	private IEstimuloBusiness estimuloService;
	
	@Autowired
	private EstimuloRepository estimuloDAO;

	@Override
	public RespuestaGenerica<Estimulo> nuevoEstimulo(Estimulo estimulo) throws BusinessException {
	
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Estimulo> rg = new RespuestaGenerica<Estimulo>(estimulo, m);

		String mensajeCheck = estimulo.checkBasicInfo();

		if (mensajeCheck != "Ok") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			System.out.println("estoy en un error");
			return rg;
		}

		try {
			System.out.println("estoy aca");
			estimulo.setDescripcion(estimulo.getDescripcion());
			estimulo.setEstado(1);
			estimulo.setTiempoEstmado(estimulo.getTiempoEstmado());
			estimulo.setTitulo(estimulo.getTitulo());
			
			estimuloDAO.save(estimulo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;
	}
	
	@Override
	public Estimulo load(int nro) throws BusinessException, NotFoundException {
		Optional<Estimulo> estimulo = null;
		try {

			estimulo = estimuloDAO.findById(nro);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!estimulo.isPresent())
			throw new NotFoundException("El estimulo no se encuentra en la BD");

		return estimulo.get();
	}
	

}
