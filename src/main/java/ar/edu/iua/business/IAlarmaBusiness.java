package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Alarma;


public interface IAlarmaBusiness {
	
	Alarma load(Long id) throws BusinessException, NotFoundException;

	 List<Alarma> list() throws BusinessException;

	 Alarma save(Alarma alarma) throws BusinessException, IllegalArgumentException;

	 void delete(Long id) throws BusinessException, NotFoundException;

}
