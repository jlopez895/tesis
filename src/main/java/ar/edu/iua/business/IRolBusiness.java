package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Ministerio;
import ar.edu.iua.model.Rol;

public interface IRolBusiness {
	public List<Rol> list() throws BusinessException;
	
	public List<Rol> findByMinisterio(int ministerio) throws BusinessException;

}
