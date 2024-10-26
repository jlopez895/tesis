package ar.edu.iua.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.INotificacionBusiness;
import ar.edu.iua.business.IPermisoBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.Permiso;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_NOTIFICACIONES)

@Api(value = "Permisos", description = "Operaciones relacionadas con las notificaciones", tags = {
		"Notificaciones" })
public class NotificacionRestController {
	
	@Autowired
	private INotificacionBusiness notificacionBusiness;
	@ApiOperation(value = "Obtener listado de notificaciones del usuario", response = Notificacion.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list/{idRol}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Notificacion>> list(@ApiParam(value = "El rol del usuario") @PathVariable("idRol") int idRol) {
		try {

			return new ResponseEntity<List<Notificacion>>(notificacionBusiness.list(idRol), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Notificacion>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtener una notificacion", response = Notificacion.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Notificacion> get(@ApiParam(value = "El rol del usuario") @PathVariable("id") int id) {
		try {

			return new ResponseEntity<Notificacion>(notificacionBusiness.get(id), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<Notificacion>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

}
