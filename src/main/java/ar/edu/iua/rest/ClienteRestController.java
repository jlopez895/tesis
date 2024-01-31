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

import ar.edu.iua.business.IClienteBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Cliente;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_CLIENTES)

@Api(value = "Clientes", description = "Operaciones relacionadas con los clientes", tags = { "Clientes" })

public class ClienteRestController {

	@Autowired
	private IClienteBusiness clienteBusiness;
	
	@ApiOperation(value="Obtener un cliente mediante el ID o la razon social", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})

	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cliente> load(
			@ApiParam(value = "Razon social del cliente")@RequestParam(name = "razonSocial", required = false, defaultValue = "*") String razonSocial,
			@ApiParam(value = "DNI del cliente")@RequestParam(name = "id", required = false, defaultValue = "0") long id) {
		try {
			return new ResponseEntity<Cliente>(clienteBusiness.load(id, razonSocial), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Cliente>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Cliente>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value="Obtener listado de clientes", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cliente>> list() {
		try {
			return new ResponseEntity<List<Cliente>>(clienteBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Cliente>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value="Añadir un cliente", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Chofer creado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody Cliente cliente) {
		try {
			clienteBusiness.add(cliente);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constantes.URL_CHOFERES + "/" + cliente.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@ApiOperation(value="Actualizacion de un cliente", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(
			@ApiParam(value = "ID del cliente")@PathVariable("id") long id, @RequestBody Cliente cliente) {
		try {

			clienteBusiness.update(cliente, id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Eliminacion de un cliente", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(
			@ApiParam(value = "ID del cliente")@PathVariable(name = "id") long id) {
		try {
			clienteBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
