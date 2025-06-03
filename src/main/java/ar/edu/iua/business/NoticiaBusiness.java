package ar.edu.iua.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Noticia;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.NotificacionUsuario;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.NoticiaRepository;
import ar.edu.iua.model.persistence.NotificacionRepository;
import ar.edu.iua.model.persistence.RolRepository;
import ar.edu.iua.model.persistence.UserRepository;

@Service
public class NoticiaBusiness implements INoticiaBusiness{
	@Autowired
	private NoticiaRepository noticiaDAO;
	@Autowired
	private INotificacionUsuarioBusiness notificacionUsuarioService;
	@Autowired
	private INotificacionBusiness notificacionService;
	@Autowired
	private UserRepository userDAO;
	@Autowired
	private EstimuloRepository estimuloDAO;
	
	@Override
	public List<Object> list() throws BusinessException {
		try {
	
			return noticiaDAO.obtenerTodas();
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
			Integer idGenerado = noti.getId(); 
			//creando notificacion
			Notificacion not=new Notificacion();
			Optional<Estimulo> est=estimuloDAO.findById(noti.getEstimulo());
			not.setDescripcion("Se ha creado una nueva noticia vinculada al estímulo "+est.get().getTitulo());
			not.setTipo(3);
			not.setIdAsoc(idGenerado);
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
				
			}

			
			
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return rg;
	}

}
