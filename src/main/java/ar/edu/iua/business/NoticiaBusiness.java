package ar.edu.iua.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Noticia;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.NoticiaRepository;
import ar.edu.iua.model.persistence.NotificacionRepository;
import ar.edu.iua.model.persistence.RolRepository;

@Service
public class NoticiaBusiness implements INoticiaBusiness{
	@Autowired
	private NoticiaRepository noticiaDAO;
	@Autowired
	private INotificacionBusiness notificacionService;
	@Autowired
	private RolRepository rolDAO;
	
	@Override
	public List<Noticia> list() throws BusinessException {
		try {
			return noticiaDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RespuestaGenerica<Noticia> nuevaNoticia(Noticia noti) throws BusinessException {
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Noticia> rg = new RespuestaGenerica<Noticia>(noti, m);

		try {
			
			noticiaDAO.save(noti);
			
			//creando notificacion
			Notificacion not=new Notificacion();
			not.setDescripcion("Se ha creado una nueva noticia");
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

}
