package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.RespuestaGenerica;

public interface IDocumentoBusiness {
	public RespuestaGenerica<Documento> nuevoDocumento(Documento documento) throws BusinessException,NotFoundException;
	public List<Documento> list(int idEstado) throws BusinessException;
	public Documento load(int nro) throws BusinessException, NotFoundException;

}
