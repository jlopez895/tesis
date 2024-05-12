package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Ministerio;

public interface IMinisterioBusiness {
	

	public List<Ministerio> list() throws BusinessException;

}
