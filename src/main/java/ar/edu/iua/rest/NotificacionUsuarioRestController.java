package ar.edu.iua.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.INotificacionBusiness;
import ar.edu.iua.business.INotificacionUsuarioBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.NotificacionUsuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_NOTIFICACIONES_USUARIO)

@Api(value = "Permisos", description = "Operaciones relacionadas con las notificaciones del usuario", tags = {
		"Notificaciones" })
public class NotificacionUsuarioRestController {
	

	@Autowired
	private INotificacionUsuarioBusiness business;
	
	@ApiOperation(value = "cambiar estado", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "estimulo cerrado"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PutMapping(value = "/leida/{idNot}/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NotificacionUsuario> leida(@ApiParam(value = "El id de la notificacion") @PathVariable("idNot") int idNot,
			@ApiParam(value = "El id del usuario") @PathVariable("idUser") int idUser) {

		try {
			return new ResponseEntity<NotificacionUsuario>(business.leida(idNot,idUser), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<NotificacionUsuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<NotificacionUsuario>(HttpStatus.NOT_FOUND);
		}
	}

}
