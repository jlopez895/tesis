package ar.edu.iua.rest;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.IAlarmaBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Alarma;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value = Constantes.URL_ALARMAS)
@Api(value = "Alarmas", description = "Operaciones relacionadas con las alarmas", tags = { "Alarmas" })
public class AlarmaRestController {
	

private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IAlarmaBusiness alarmaBusiness;
	
	@ApiOperation(value="Obtener una alarma mediante su ID", response = Alarma.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "No se encuentra la alarma"), 
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ar.edu.iua.model.Alarma> load(@PathVariable("id") Long id) {
		try {
			return new ResponseEntity<Alarma>(alarmaBusiness.load(id),HttpStatus.OK);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<Alarma>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Alarma>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value="Obtener todas los alarmas almacenadas en la base de datos", response = Alarma.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Alarma>> list() {
		try {
			return new ResponseEntity<List<Alarma>>(alarmaBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<List<Alarma>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value="Guardar una nueva alarma en la base de datos", response = Alarma.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Alarma creada exitosamente"),
			@ApiResponse(code = 400, message = "Algun valor ingresado es incorrecto"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody Alarma alarma) {
		try {
			alarmaBusiness.save(alarma);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constantes.URL_ALARMAS + "/" + alarma.getIdAlarma());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value="Actualizar los datos de una alarma", response = Alarma.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "No se encuentra la alarma"), 
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(@RequestBody Alarma alarma) {
		try {
			alarmaBusiness.save(alarma);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value="Borrar una alarma indicando su ID", response = Alarma.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "No se encuentra la alarma"), 
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	
	@DeleteMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@PathVariable("id") Long id) {
		try {
			alarmaBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
