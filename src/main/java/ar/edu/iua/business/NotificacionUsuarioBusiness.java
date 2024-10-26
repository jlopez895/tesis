package ar.edu.iua.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.NotificacionUsuario;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.NotificacionUsuarioRepository;

@Service
public class NotificacionUsuarioBusiness implements INotificacionUsuarioBusiness{

	@Autowired
	private NotificacionUsuarioRepository dao;
	@Override
	public void nuevaNot(NotificacionUsuario not) throws BusinessException {
		try {

			dao.save(not);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	@Override
	public NotificacionUsuario leida(int idNot, int idUser) throws BusinessException, NotFoundException {
		Optional<NotificacionUsuario> notificacion = null;
		try {

			notificacion = dao.findByIdNotUser(idNot,idUser);
			if (notificacion != null) {
				try {
					NotificacionUsuario notificacionNew=notificacion.get();
					notificacionNew.setLeida(true);
					dao.save(notificacionNew);
					return notificacion.get();
				} catch (Exception e) {
					throw new BusinessException(e);
				}
			}
			else
				throw new NotFoundException("La notificacion no se encuentra en la BD");

		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	

}
