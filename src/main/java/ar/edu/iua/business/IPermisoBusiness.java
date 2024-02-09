package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Permiso;

public interface IPermisoBusiness {
	public List<Permiso> list(int idUser) throws BusinessException;
}
