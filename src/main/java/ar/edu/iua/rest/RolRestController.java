package ar.edu.iua.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.IRolBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Rol;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_ROLES)

@Api(value = "Roles", description = "Operaciones relacionadas con los roles", tags = { "Roles" })

public class RolRestController {
	
	@Autowired
	private IRolBusiness rolBusiness;
	@ApiOperation(value = "Obtener listado de estimulos", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Rol>> list() {
		try {

			return new ResponseEntity<List<Rol>>(rolBusiness.list(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Rol>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@ApiOperation(value = "Obtener listado de roles permitidos para comunicarse", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/listRol/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Rol>> listRol(@ApiParam(value = "El id del rol del usuario") @PathVariable("id") int id) {
		try {

			return new ResponseEntity<List<Rol>>(rolBusiness.listRol(id), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Rol>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
