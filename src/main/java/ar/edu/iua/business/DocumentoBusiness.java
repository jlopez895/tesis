package ar.edu.iua.business;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
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
	@Override
	public List<Documento> list(int idEstimulo) throws BusinessException {
		List<Documento> list=null;
		try {

			list=documentoDAO.findByIdEstimulo(idEstimulo);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		if(list!=null&&list.size()>0)
			return list;
		else
			return null;
	}
	@Override
	public Documento load(int nro) throws BusinessException, NotFoundException {
		Optional<Documento> doc = null;
		try {

			doc = documentoDAO.obtenerPorId(nro);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!doc.isPresent())
			throw new NotFoundException("El documento no se encuentra en la BD");

		return doc.get();
	}

}
