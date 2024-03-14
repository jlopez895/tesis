package ar.edu.iua.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.RolRepository;

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

			estimuloDAO.save(estimulo);
			
			//creando notificacion
			Notificacion not=new Notificacion();
			not.setDescripcion("Se ha creado un nuevo estímulo");
			not.setFecha(new Date());
			List<Rol> listaRoles = rolDAO.findAll();
			Set<Rol> setRoles = new HashSet<>(listaRoles);
			not.setRoles(setRoles);
			notificacionService.nuevaNotificacion(not);
			
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
					estimuloDAO.cambiarEstado(id);
					estimulo.get().setEstado(2);
					
					//creando notificacion
					Notificacion not=new Notificacion();
					not.setDescripcion("El estímulo "+id+" ha sido cerrado.");
					not.setFecha(new Date());
					List<Rol> listaRoles = rolDAO.findAll();
					Set<Rol> setRoles = new HashSet<>(listaRoles);
					not.setRoles(setRoles);
					notificacionService.nuevaNotificacion(not);
					
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



}
