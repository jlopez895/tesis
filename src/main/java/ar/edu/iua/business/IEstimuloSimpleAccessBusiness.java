package ar.edu.iua.business;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.EstimuloSimpleAccess;


public interface IEstimuloSimpleAccessBusiness {
	
	public void nuevoEstimulo(Estimulo estimulo) throws BusinessException;

	public EstimuloSimpleAccess load() throws BusinessException;

	public void delete() throws BusinessException;

}
