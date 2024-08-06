package ar.edu.iua.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import ar.edu.iua.business.IDocumentoBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.MensajeRespuesta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_DOCUMENTOS)

@Api(value = "Documentos", description = "Operaciones relacionadas con los Documentos", tags = {
		"Documentos" })
public class DocumentoRestController {
	
	@Autowired
	private IDocumentoBusiness documentoBusiness;

	@ApiOperation(value = "Nuevo documento", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	
	@PostMapping(value = "/nuevoDocumento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> cargarDocumento(@RequestBody Documento documento)
			throws NotFoundException {

		try {
			MensajeRespuesta m = documentoBusiness.nuevoDocumento(documento).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value = "Obtener listado de documentos por estimulo", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list/{idEstimulo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Documento>> list(@ApiParam(value = "El numero del estimulo") @PathVariable("idEstimulo") int idEstimulo) {
		try {

			return new ResponseEntity<List<Documento>>(documentoBusiness.list(idEstimulo), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Documento>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtener listado de documentos por estimulo", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/estadisticasPorRol", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> estadisticasPorRol() {
	    try {
	       
	        return new ResponseEntity<Map<String, Integer>>(documentoBusiness.estdisticasPorRol(), HttpStatus.OK);
	    } catch (BusinessException e) {
	        return new ResponseEntity<Map<String, Integer>>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@ApiOperation(value = "Obtener listado de documentos por estimulo", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/estadisticasPorMinisterio", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> estadisticasPorMinisterio() {
	    try {
	       
	        return new ResponseEntity<Map<String, Integer>>(documentoBusiness.estadisticasPorMinisterio(), HttpStatus.OK);
	    } catch (BusinessException e) {
	        return new ResponseEntity<Map<String, Integer>>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@ApiOperation(value = "Obtener un documento en particular", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Documento no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/obtenerDoc/{idDocumento}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Documento> load(
			@ApiParam(value = "El numero del documento que se desea obtener") @PathVariable("idDocumento") int idDocumento) {

		try {
			return new ResponseEntity<Documento>(documentoBusiness.load(idDocumento), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Documento>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Documento>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "cambiar estado", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "estimulo cerrado"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PutMapping(value = "/cambiarEstado/{idDocumento}/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Documento> cambiarEstado(@ApiParam(value = "El numero del documento que se desea cerrar") @PathVariable("idDocumento") int idDocumento, @PathVariable("estado") int estado) {

		try {
			return new ResponseEntity<Documento>(documentoBusiness.cambiarEstado(idDocumento,estado), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Documento>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Documento>(HttpStatus.NOT_FOUND);
		}
	}
}
