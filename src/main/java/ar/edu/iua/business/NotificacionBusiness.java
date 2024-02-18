package ar.edu.iua.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.persistence.DocumentoRepository;
import ar.edu.iua.model.persistence.NotificacionRepository;

@Service
public class NotificacionBusiness implements INotificacionBusiness{

	@Autowired
	private NotificacionRepository notificacionDAO;
	@Override
	public void nuevaNotificacion(Notificacion not) throws BusinessException{
		
		try {

			notificacionDAO.save(not);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
