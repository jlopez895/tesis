package ar.edu.iua.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.DetalleOrden;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.DocumentoRepository;
import ar.edu.iua.model.persistence.EstimuloRepository;

@Service
public class DocumentoBusiness implements IDocumentoBusiness{

	@Autowired
	private IEstimuloBusiness estimuloService;
	
	@Autowired
	private IUserBusiness userService;
	
	@Autowired
	private DocumentoRepository documentoDAO;
	@Override
	public RespuestaGenerica<Documento> nuevoDocumento(Documento documento, int nroEstimulo, int nroUsuario, int nroRol) throws BusinessException, NotFoundException {
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Documento> rg = new RespuestaGenerica<Documento>(documento, m);

		Estimulo estimulo = estimuloService.load(nroEstimulo);
		
		User usuario=userService.load(nroUsuario);
		
		String mensajeCheck = documento.checkBasicData(estimulo);


		if (mensajeCheck != "Ok") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			System.out.println("estoy en un error");
			return rg;
		}

		try {
			System.out.println("estoy aca");
			documento.setDescripcion(documento.getDescripcion());
			documento.setEsFinal(documento.isEsFinal());
			documento.setEstimulo(estimulo);
			documento.setFecha(new Date());
			documento.setRol(null);
			documento.setTipo(documento.getTipo());
			documento.setUsuario(usuario);
			documento.setTitulo(documento.getTitulo());
			
			documentoDAO.save(documento);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;
	}

}
