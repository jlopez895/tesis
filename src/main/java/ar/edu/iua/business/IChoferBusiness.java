package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Chofer;

public interface IChoferBusiness {

	public Chofer load(long id, long dni) throws NotFoundException, BusinessException;

	public List<Chofer> list() throws BusinessException;

	public Chofer add(Chofer chofer) throws BusinessException;

	public Chofer update(Chofer chofer, long id) throws NotFoundException, BusinessException;

	public void delete(long id) throws NotFoundException, BusinessException;

	public Chofer load(String codigoExterno) throws NotFoundException, BusinessException;

	public Chofer asegurarChofer(Chofer chofer) throws BusinessException;

	Chofer load(Long id) throws BusinessException, NotFoundException;

	Chofer findByCodigoExterno(String c) throws NotFoundException, BusinessException;

}
