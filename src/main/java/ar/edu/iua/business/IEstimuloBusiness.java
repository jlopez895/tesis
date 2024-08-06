package ar.edu.iua.business;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface IEstimuloBusiness {
	
	public RespuestaGenerica<Estimulo> nuevoEstimulo(Estimulo estimulo) throws BusinessException;

	public Estimulo load(int nro) throws BusinessException, NotFoundException;
	
	public List<Estimulo> list() throws BusinessException;
	
	public Estimulo cerrarEstimulo(int id) throws BusinessException, NotFoundException;

	public List<Estimulo> listOld() throws BusinessException;

	public Map<String, Integer> estadisticasPorEstimulo() throws BusinessException;
}
