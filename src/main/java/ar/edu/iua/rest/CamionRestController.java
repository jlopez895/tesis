package ar.edu.iua.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.ICamionBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Camion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_CAMIONES)

@Api(value = "Camiones", description = "Operaciones relacionadas con los camiones", tags = { "Camiones" })

public class CamionRestController {

	@Autowired
	private ICamionBusiness camionBusiness;

	@ApiOperation(value="Obtener un camion mediante el ID o la patente", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Camion no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Camion> load(
			@ApiParam(value = "Patente del camion") @RequestParam(name = "patente", required = false, defaultValue = "*") String patente,
			@ApiParam(value = "ID del camion") @RequestParam(name = "id", required = false, defaultValue = "0") long id) {
		try {
			return new ResponseEntity<Camion>(camionBusiness.load(patente, id), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Camion>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Camion>(HttpStatus.NOT_FOUND);
		}
	}

	
	@ApiOperation(value="Obtener listado de camiones", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Camion>> list() {
		try {
			return new ResponseEntity<List<Camion>>(camionBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Camion>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value="Añadir un camion", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Camion creado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody Camion camion) {
		try {
			camionBusiness.add(camion);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constantes.URL_CAMIONES + "/" + camion.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@ApiOperation(value="Actualizacion de un camion", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Camion no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@ApiParam(value = "ID del camion")@PathVariable("id") Long id, @RequestBody Camion camion) {
		try {

			camionBusiness.update(camion, id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Eliminacion de un camion", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Camion no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@ApiParam(value = "ID del camion")@PathVariable(name = "id") long id) {
		try {
			camionBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Integracion de un camion", response = Camion.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/integracion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Camion> integracion(@RequestBody Camion camion) {
		try {
			return new ResponseEntity<Camion>(camionBusiness.asegurarCamion(camion), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Camion>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
