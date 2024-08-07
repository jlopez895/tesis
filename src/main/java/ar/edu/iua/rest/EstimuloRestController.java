package ar.edu.iua.rest;

import java.io.Console;
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

import ar.edu.iua.business.IEstimuloBusiness;
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
@RequestMapping(value = Constantes.URL_ESTIMULOS)

@Api(value = "Estimulos", description = "Operaciones relacionadas con los estimulos", tags = { "Estimulos" })

public class EstimuloRestController {
	
	@Autowired
	private IEstimuloBusiness estimuloBusiness;

	@ApiOperation(value = "Nuevo estumulo", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "estimulo creado"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PostMapping(value = "/nuevoEstimulo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> load(@RequestBody Estimulo estimulo) {

		try {

			MensajeRespuesta m = estimuloBusiness.nuevoEstimulo(estimulo).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtener listado de estimulos", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Estimulo>> list() {
		try {

			return new ResponseEntity<List<Estimulo>>(estimuloBusiness.list(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Estimulo>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtener listado de estimulos", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list/old2", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Estimulo>> listOld() {
		try {

			return new ResponseEntity<List<Estimulo>>(estimuloBusiness.listOld(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Estimulo>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtener un estimulo por su id", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Estimulo no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/obtenerEstimulo/{idEstimulo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Estimulo> get(@ApiParam(value = "El numero del estimulo que se desea obtener") @PathVariable("idEstimulo") int idEstimulo) 
	{
		try {
			return new ResponseEntity<Estimulo>(estimuloBusiness.load(idEstimulo), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Estimulo>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Estimulo>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@ApiOperation(value = "cambiar estado", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "estimulo cerrado"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PutMapping(value = "/cambiarEstado/{idEstimulo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Estimulo> cambiarEstado(@ApiParam(value = "El numero del estimulo que se desea cerrar") @PathVariable("idEstimulo") int idEstimulo) {

		try {
			return new ResponseEntity<Estimulo>(estimuloBusiness.cerrarEstimulo(idEstimulo), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Estimulo>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Estimulo>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value = "Obtener listado de documentos por estimulo", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/estadisticasPorEstimulo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Integer>> estadisticasPorRol() {
	    try {
	       
	        return new ResponseEntity<Map<String, Integer>>(estimuloBusiness.estadisticasPorEstimulo(), HttpStatus.OK);
	    } catch (BusinessException e) {
	        return new ResponseEntity<Map<String, Integer>>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
