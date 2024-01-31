package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;

import ar.edu.iua.model.Producto;

public interface IProductoBusiness {
	
	
	Producto findById(Long id)throws NotFoundException, BusinessException;

	public Producto load(long id, String nombre) throws NotFoundException, BusinessException;

	public List<Producto> list() throws BusinessException;

	public Producto add(Producto producto) throws BusinessException;

	public Producto update(Producto producto, long id) throws NotFoundException, BusinessException;

	public void delete(long id) throws NotFoundException, BusinessException;

	public Producto load(String codigoExterno) throws NotFoundException, BusinessException;

	public Producto asegurarProducto(Producto producto) throws BusinessException;
	Producto findByCodigoExterno(String p)throws NotFoundException, BusinessException;

}
