package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Cliente;

public interface IClienteBusiness {

	public Cliente load(long id, String razonSocial) throws NotFoundException, BusinessException;

	public List<Cliente> list() throws BusinessException;

	public Cliente add(Cliente cliente) throws BusinessException;

	public Cliente update(Cliente cliente, long id) throws NotFoundException, BusinessException;

	public void delete(long id) throws NotFoundException, BusinessException;

	public Cliente load(String codigoExterno) throws NotFoundException, BusinessException;

	public Cliente asegurarCliente(Cliente cliente) throws BusinessException;

}
