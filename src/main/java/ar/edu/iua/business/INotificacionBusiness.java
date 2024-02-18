package ar.edu.iua.business;

import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Notificacion;


public interface INotificacionBusiness {
	
	public void nuevaNotificacion(Notificacion not)throws BusinessException ;

}
