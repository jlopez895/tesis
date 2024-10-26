package ar.edu.iua.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.NotificacionUsuario;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;
import ar.edu.iua.model.dto.Estadistica2DTO;
import ar.edu.iua.model.dto.EstadisticaDTO;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.RolRepository;
import ar.edu.iua.model.persistence.UserRepository;

@Service
public class EstimuloBusiness implements IEstimuloBusiness {

	@Autowired
	private IEstimuloBusiness estimuloService;

	@Autowired
	private EstimuloRepository estimuloDAO;
	
	@Autowired
	private RolRepository rolDAO;
	
	@Autowired
	private INotificacionBusiness notificacionService;
	
	@Autowired
	private IEstimuloSimpleAccessBusiness estimuloSimpleAccessService;
	
	@Autowired
	private INotificacionUsuarioBusiness notificacionUsuarioService;
	@Autowired
	private UserRepository userDAO;

	@Override
	public RespuestaGenerica<Estimulo> nuevoEstimulo(Estimulo estimulo) throws BusinessException {
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Estimulo> rg = new RespuestaGenerica<Estimulo>(estimulo, m);

		String mensajeCheck = estimulo.checkBasicInfo();

		if (mensajeCheck != "OK") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return rg;
		}

		try {
			estimulo.setDescripcion(estimulo.getDescripcion());
			estimulo.setEstado(1);
			estimulo.setTiempoEstmado(estimulo.getTiempoEstmado());
			estimulo.setTitulo(estimulo.getTitulo());
			estimulo.setFechaInicio(new Date());
			estimulo.setUsuarioCreador(estimulo.getUsuarioCreador());
			estimuloDAO.save(estimulo);
			Integer idGenerado = estimulo.getId(); 
			
			//guardando estimulo en tabla de acceso rapido
			estimuloSimpleAccessService.nuevoEstimulo(estimulo);
			//creando notificacion
			Notificacion not=new Notificacion();
			not.setDescripcion("Se ha creado un nuevo estímulo");
			not.setFecha(new Date());
			not.setTipo(1);
			not.setIdAsoc(idGenerado);
			notificacionService.nuevaNotificacion(not);

			NotificacionUsuario n=new NotificacionUsuario();
			
			List<User> listaUsers = userDAO.findAll();
			for(User us:listaUsers)
			{
				
				n.setLeido(false);
				n.setIdNotificacion(not.getId());
				n.setIdUsuario(us.getId());
				notificacionUsuarioService.nuevaNot(n);
				n=new NotificacionUsuario();
				
			}
			
			
			
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

	@Override
	public List<Estimulo> list() throws BusinessException {
		List<Estimulo> list = null;
		try {

			list = estimuloDAO.findByEstado();

		} catch (Exception e) {
			throw new BusinessException(e);
		}

		if (list != null && list.size() > 0)
			return list;
		else
			return null;
	}

	@Override
	public Estimulo cerrarEstimulo(int id) throws BusinessException, NotFoundException {
		Optional<Estimulo> estimulo = null;
		try {

			estimulo = estimuloDAO.findById(id);
			if (estimulo != null) {
				try {
					Estimulo estimuloNew=estimulo.get();
					estimuloNew.setEstado(2);
					estimuloNew.setFechaFin(new Date());
					estimuloDAO.save(estimuloNew);
					//creando nuevo acceso rapido
					List<Estimulo> e=estimuloDAO.findByEstado();
					if(e.size()>0)
						estimuloSimpleAccessService.nuevoEstimulo(e.get(0));
					else
						estimuloSimpleAccessService.delete();
					//creando notificacion
					Notificacion not=new Notificacion();
					not.setDescripcion("El estímulo "+id+" ha sido cerrado.");
					not.setTipo(1);
					not.setIdAsoc(id);
					not.setFecha(new Date());
					
					notificacionService.nuevaNotificacion(not);
					
					List<NotificacionUsuario> nu=new ArrayList<>();
					NotificacionUsuario n=new NotificacionUsuario();
					
					List<User> listaUsers = userDAO.findAll();
					for(User us:listaUsers)
					{
						n.setLeido(false);
						n.setIdNotificacion(not.getId());
						n.setIdUsuario(us.getId());
						nu.add(n);
						notificacionUsuarioService.nuevaNot(n);
						n=new NotificacionUsuario();
					}
					
					return estimulo.get();
				} catch (Exception e) {
					throw new BusinessException(e);
				}
			}
			else
				throw new NotFoundException("El estimulo no se encuentra en la BD");

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		

	}

	@Override
	public List<Estimulo> listOld() throws BusinessException {
		List<Estimulo> list = null;
		try {

			list = estimuloDAO.findCerradas();

		} catch (Exception e) {
			throw new BusinessException(e);
		}

		if (list != null && list.size() > 0)
			return list;
		else
			return null;
	}

	@Override
	public Map<String, Integer> estadisticasPorEstimulo() throws BusinessException {
		Map<String, Integer> response = new HashMap<>();
		List<Object[]> results = estimuloDAO.estadisticasPorEstimulo();
		List<Estadistica2DTO> dtos = results.stream()
			    .map(result -> new Estadistica2DTO(((Number) result[0]).intValue(),((Number) result[1]).intValue()))
			    .collect(Collectors.toList());
		if (dtos != null) {
			for (Estadistica2DTO d : dtos) {
				response.put("TIEMPO > AL ESTIMADO", d.getLabel());
				response.put("TIEMPO < AL ESTIMADO", d.getValue());
			}
		}
		return response;
	}



}
