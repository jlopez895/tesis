package ar.edu.iua.business;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Notificacion;
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
	@Autowired
	private INotificacionBusiness notificacionService;
	
	@Override
	public RespuestaGenerica<Documento> nuevoDocumento(Documento documento) throws BusinessException, NotFoundException {
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Documento> rg = new RespuestaGenerica<Documento>(documento, m);

		
		Estimulo estimulo = estimuloService.load(documento.getEstimulo());
		String mensajeCheck = documento.checkBasicData(estimulo);


		if (mensajeCheck != "OK") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			System.out.println("estoy en un error");
			return rg;
		}

		try {
			System.out.println("estoy aca");
			documento.setDescripcion(documento.getDescripcion());
			documento.setEsFinal(documento.isEsFinal());
			documento.setEstimulo(documento.getEstimulo());
			documento.setFecha(new Date());
			documento.setRol(documento.getRol());
			documento.setTipo(documento.getTipo());
			documento.setUsuario(documento.getUsuario());
			documento.setTitulo(documento.getTitulo());
			
			documentoDAO.save(documento);
			
			//creando notificacion
			Notificacion not=new Notificacion();
			not.setDescripcion("Se ha creado un nuevo documento para el estímulo "+estimulo.getId()+": '"+estimulo.getTitulo()+"'");
			not.setFecha(new Date());
			Rol rol = documento.get;
			Set<Rol> setRoles = new HashSet<>(listaRoles);
			not.setRoles(setRoles);
			notificacionService.nuevaNotificacion(not);
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
