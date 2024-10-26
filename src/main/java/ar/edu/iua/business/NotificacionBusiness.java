package ar.edu.iua.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.RolPrincipalHolder;
import ar.edu.iua.model.User;
import ar.edu.iua.model.persistence.DocumentoRepository;
import ar.edu.iua.model.persistence.NotificacionRepository;
import ar.edu.iua.rest.Constantes;

@Service
public class NotificacionBusiness implements INotificacionBusiness{

	@Autowired
	private NotificacionRepository notificacionDAO;
    @Autowired
    private HttpSession session;
	@Override
	public void nuevaNotificacion(Notificacion not) throws BusinessException{
		
		try {

			notificacionDAO.save(not);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	@Override
	public List<Notificacion> list(int idRol) throws BusinessException {
		try {
			return notificacionDAO.findByUser(idRol);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	
	@Autowired
	private SimpMessagingTemplate wSock;
	
	@Override
	public void pushOrderData() {
		try {
			Integer idRolPrincipal = RolPrincipalHolder.getInstance().getIdRolPrincipal();

			wSock.convertAndSend(Constantes.TOPIC_SEND_WEBSOCKET_GRAPH, notificacionDAO.findByUser(idRolPrincipal));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Notificacion get(int id) throws BusinessException {
		try {
			return notificacionDAO.obtenerPorId(id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
