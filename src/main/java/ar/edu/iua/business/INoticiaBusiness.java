package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Noticia;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;



public interface INoticiaBusiness {
	
	public List<Noticia> list() throws BusinessException;

	public RespuestaGenerica<Noticia> nuevaNoticia(Noticia noticia) throws BusinessException;
	
}
