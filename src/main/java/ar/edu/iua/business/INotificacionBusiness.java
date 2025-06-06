package ar.edu.iua.business;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Permiso;


public interface INotificacionBusiness {
	
	public void nuevaNotificacion(Notificacion not)throws BusinessException ;
	
	public List<Object> list(int idUser) throws BusinessException;

	void pushOrderData();

	//public Notificacion leida(int id) throws BusinessException, NotFoundException;

	public Notificacion get(int id) throws BusinessException;

}
