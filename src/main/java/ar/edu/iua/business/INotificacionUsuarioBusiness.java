package ar.edu.iua.business;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Noticia;
import ar.edu.iua.model.NotificacionUsuario;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface INotificacionUsuarioBusiness {
	
	public void nuevaNot(NotificacionUsuario not) throws BusinessException;

	public NotificacionUsuario leida(int idNot, int idUser)throws BusinessException, NotFoundException;

}
