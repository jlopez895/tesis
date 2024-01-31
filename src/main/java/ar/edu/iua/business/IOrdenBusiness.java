package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface IOrdenBusiness {

	public RespuestaGenerica<Orden> recibirEstadoUno(Orden orden) throws BusinessException;

	public RespuestaGenerica<Orden> recibirEstadoDos(Orden orden, int nroOrden)
			throws BusinessException, NotFoundException;

	public RespuestaGenerica<Orden> pesajeFinal(Orden orden, int nroOrden) throws BusinessException, NotFoundException;

	public RespuestaGenerica<Orden> generarConciliacion(int nroOrden) throws BusinessException, NotFoundException;

	public Orden load(int nroOrden) throws BusinessException, NotFoundException;
	
	public List<Orden> list() throws BusinessException;
	public RespuestaGenerica<Orden> cerrarOrden(int nroOrden) throws BusinessException,NotFoundException;
	
	public void pushOrderData();
	

}
