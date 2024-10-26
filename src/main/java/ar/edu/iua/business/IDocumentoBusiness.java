package ar.edu.iua.business;

import java.util.List;
import java.util.Map;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface IDocumentoBusiness {
	public RespuestaGenerica<Documento> nuevoDocumento(Documento documento) throws BusinessException,NotFoundException;
	public List<Documento> list(int idEstado,int idUser) throws BusinessException;
	public Documento load(int nro) throws BusinessException, NotFoundException;
	public Documento cambiarEstado(int idDocumento, int estado) throws BusinessException, NotFoundException;
	public Map<String, Integer> estdisticasPorRol() throws BusinessException;
	public Map<String, Integer> estadisticasPorMinisterio() throws BusinessException;

}
