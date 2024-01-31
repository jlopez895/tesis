package ar.edu.iua.business;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.DetalleOrden;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface IDetalleOrdenBusiness {

	
	public DetalleOrden load(long id) throws BusinessException, NotFoundException;
	public RespuestaGenerica<DetalleOrden> cargarCamion(DetalleOrden detalleOrden, int nroOrden) throws BusinessException,NotFoundException;

}
