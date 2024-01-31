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

import ar.edu.iua.business.IChoferBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Chofer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_CHOFERES)

@Api(value = "Choferes", description = "Operaciones relacionadas con los choferes", tags = { "Choferes" })

public class ChoferRestController {

	@Autowired
	private IChoferBusiness choferBusiness;

	@ApiOperation(value="Obtener un chofer mediante el ID o el dni", response = Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Chofer> load(
			@ApiParam(value = "DNI del chofer")@RequestParam(name = "dni", required = false, defaultValue = "0") long dni,
			@ApiParam(value = "ID del chofer")@RequestParam(name = "id", required = false, defaultValue = "0") long id) {
		try {
			return new ResponseEntity<Chofer>(choferBusiness.load(id, dni), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Chofer>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Chofer>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value="Obtener listado de choferes", response = Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Chofer>> list() {
		try {
			return new ResponseEntity<List<Chofer>>(choferBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Chofer>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@ApiOperation(value="Añadir un chofer", response =Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Chofer creado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})

	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody Chofer chofer) {
		try {
			choferBusiness.add(chofer);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constantes.URL_CHOFERES + "/" + chofer.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@ApiOperation(value="Actualizacion de un chofer", response = Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(
			@ApiParam(value = "ID del chofer")@PathVariable("id") long id, @RequestBody Chofer chofer) {
		try {

			choferBusiness.update(chofer, id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Eliminacion de un chofer", response = Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(
			@ApiParam(value = "ID del chofer")@PathVariable(name = "id") long id) {
		try {
			choferBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Integracion de un chofer", response = Chofer.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/integracion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Chofer> integracion(@RequestBody Chofer chofer) {
		try {
			return new ResponseEntity<Chofer>(choferBusiness.asegurarChofer(chofer), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Chofer>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
