package ar.edu.iua.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Ministerio;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.NotificacionUsuario;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;
import ar.edu.iua.model.dto.EstadisticaDTO;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.DocumentoRepository;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.MinisterioRepository;
import ar.edu.iua.model.persistence.RolRepository;

@Service
public class DocumentoBusiness implements IDocumentoBusiness {

	@Autowired
	private IEstimuloBusiness estimuloService;

	@Autowired
	private IUserBusiness userService;

	@Autowired
	private DocumentoRepository documentoDAO;

	@Autowired
	private INotificacionBusiness notificacionService;

	@Autowired
	private IRolBusiness rolService;

	@Autowired
	private RolRepository rolDAO;
	
	@Autowired
	private MinisterioRepository ministerioDAO;

	@Autowired
	private INotificacionUsuarioBusiness notificacionUsuarioService;

	@Override
	public RespuestaGenerica<Documento> nuevoDocumento(Documento documento)
			throws BusinessException, NotFoundException {
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
			
			documento.setDescripcion(documento.getDescripcion());
			documento.setEsFinal(documento.isEsFinal());
			documento.setEstimulo(documento.getEstimulo());
			documento.setFecha(new Date());
			documento.setMinisterio(documento.getMinisterio());
			documento.setTipo(documento.getTipo());
			documento.setEstado(1);
			documento.setUsuario(documento.getUsuario());
			documento.setTitulo(documento.getTitulo());
			documento.setRol(documento.getRol());
			documentoDAO.save(documento);
			// Obtener el ID generado automáticamente
		    Integer idGenerado = documento.getId();  // Asumiendo que el ID está en un campo "id"
			// creando notificacion
			Notificacion not = new Notificacion();
			not.setDescripcion("Se ha creado un nuevo documento para el estímulo " + estimulo.getId() + ": '"
					+ estimulo.getTitulo() + "'");
			not.setFecha(new Date());
			not.setTipo(2);
			not.setIdAsoc(documento.getEstimulo());
			notificacionService.nuevaNotificacion(not);
			//obtenemos la lista de usuarios que contienen la combinacion de rol - ministerio
			String ministerio="";
			if(documento.getMinisterio()!=0)
				ministerio=String.valueOf(documento.getMinisterio());
			else 
				ministerio="";
				
			List<User> listaUsers=userService.findByRol(documento.getRol(), ministerio);
			NotificacionUsuario n=new NotificacionUsuario();
			if(listaUsers!=null)
			{
				for(User us:listaUsers)
				{
					n.setLeido(false);
					n.setIdNotificacion(not.getId());
					n.setIdUsuario(us.getId());
					notificacionUsuarioService.nuevaNot(n);
					n=new NotificacionUsuario();
					
				}
			}
			
			User user=userService.load(documento.getUsuario());
			ministerio="";
			if(user.getMinisterioPrincipal()!=null)
				ministerio=user.getMinisterioPrincipal().getId().toString();
			listaUsers=userService.findByRol(documento.getRol(), ministerio);
			
			if(listaUsers!=null)
			{
				for(User us:listaUsers)
				{
					n.setLeido(false);
					n.setIdNotificacion(not.getId());
					n.setIdUsuario(us.getId());
					notificacionUsuarioService.nuevaNot(n);
					n=new NotificacionUsuario();
				}
			}
			
			List<User> u=userService.getAdmins();
			for(User us:u)
			{
				n.setLeido(false);
				n.setIdNotificacion(not.getId());
				n.setIdUsuario(us.getId());
				notificacionUsuarioService.nuevaNot(n);
				n=new NotificacionUsuario();
			}
			
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;
	}

	@Override
	public List<Documento> list(int idEstimulo, int idUser) throws BusinessException {
		List<Documento> list = null;
		try {
			boolean esAdmin=false;
			User u=userService.load(idUser);
			String ministerio="";
			if(u.getMinisterioPrincipal()!=null)
				ministerio=u.getMinisterioPrincipal().getId().toString();
			if(u.getRolPrincipal().getId()==1)
				esAdmin=true;
			list = documentoDAO.findByIdEstimulo(idEstimulo,u.getRolPrincipal().getId(),ministerio,esAdmin);

		} catch (Exception e) {
			throw new BusinessException(e);
		}

		if (list != null && list.size() > 0)
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

	@Override
	public Documento cambiarEstado(int idDocumentos, int estado) throws BusinessException, NotFoundException {
		Optional<Documento> documento = null;
		try {

			documento = documentoDAO.findById(idDocumentos);
			if (documento != null) {
				try {
					Documento documentoNew = documento.get();
					documentoNew.setEstado(estado);

					documentoDAO.save(documentoNew);

					Estimulo est=estimuloService.load(documentoNew.getEstimulo());
					// creando notificacion
					Notificacion not = new Notificacion();
					String aux = "";
					if (estado == 2)
						aux = "aceptado";
					else
						aux = "rechazado";
					not.setDescripcion("El documento '" + documentoNew.getTitulo() + "' del estímulo '"+est.getTitulo()+"' ha sido " + aux);
					not.setFecha(new Date());
					not.setTipo(2);
					not.setIdAsoc(documentoNew.getEstimulo());
					notificacionService.nuevaNotificacion(not);

					//obtenemos la lista de usuarios que contienen la combinacion de rol - ministerio
					String ministerio="";
					if(documentoNew.getMinisterio()!=0)
						ministerio=String.valueOf(documentoNew.getMinisterio());
					else 
						ministerio="";
						
					List<User> listaUsers=userService.findByRol(documentoNew.getRol(), ministerio);
					NotificacionUsuario n=new NotificacionUsuario();
					
					for(User us:listaUsers)
					{
						n.setLeido(false);
						n.setIdNotificacion(not.getId());
						n.setIdUsuario(us.getId());
						notificacionUsuarioService.nuevaNot(n);
						
					}
					User user=userService.load(documentoNew.getUsuario());
					ministerio="";
					if(user.getMinisterioPrincipal()!=null)
						ministerio=user.getMinisterioPrincipal().getId().toString();
					listaUsers=userService.findByRol(documentoNew.getRol(), ministerio);
					
					if(listaUsers!=null)
					{
						for(User us:listaUsers)
						{
							n.setLeido(false);
							n.setIdNotificacion(not.getId());
							n.setIdUsuario(us.getId());
							notificacionUsuarioService.nuevaNot(n);
							
						}
					}
					List<User> u=userService.getAdmins();
					for(User us:u)
					{
						n.setLeido(false);
						n.setIdNotificacion(not.getId());
						n.setIdUsuario(us.getId());
						notificacionUsuarioService.nuevaNot(n);
						
					}
					return documento.get();
				} catch (Exception e) {
					throw new BusinessException(e);
				}
			} else
				throw new NotFoundException("El estimulo no se encuentra en la BD");

		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public Map<String, Integer> estdisticasPorRol() throws BusinessException {
		Map<String, Integer> response = new HashMap<>();
		List<Object[]> results = documentoDAO.estdisticasPorRol();
		List<EstadisticaDTO> dtos = results.stream()
			    .map(result -> new EstadisticaDTO((String) result[0],((Number) result[1]).intValue()))
			    .collect(Collectors.toList());
		if (dtos != null) {
			for (EstadisticaDTO d : dtos) {
				response.put(d.getLabel(), d.getValue());
			}
		}
		return response;
	}

	@Override
	public Map<String, Integer> estadisticasPorMinisterio() throws BusinessException {
		Map<String, Integer> response = new HashMap<>();
		List<Object[]> results = documentoDAO.estadisticasPorMinisterio();
		List<EstadisticaDTO> dtos = results.stream()
			    .map(result -> new EstadisticaDTO((String) result[0],((Number) result[1]).intValue()))
			    .collect(Collectors.toList());
		if (dtos != null) {
			for (EstadisticaDTO d : dtos) {
				response.put(d.getLabel(), d.getValue());
			}
		}
		return response;
	}

}
