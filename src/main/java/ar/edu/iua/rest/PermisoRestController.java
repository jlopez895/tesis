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

import ar.edu.iua.business.IDocumentoBusiness;
import ar.edu.iua.business.IPermisoBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Permiso;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_PERMISOS)

@Api(value = "Permisos", description = "Operaciones relacionadas con los permisos", tags = {
		"Permisos" })
public class PermisoRestController {
	@Autowired
	private IPermisoBusiness permisoBusiness;
	@ApiOperation(value = "Obtener listado de permisos del usuario", response = Permiso.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list/{idUsuario}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Permiso>> list(@ApiParam(value = "El id del usuario") @PathVariable("idUsuario") int idUsuario) {
		try {

			return new ResponseEntity<List<Permiso>>(permisoBusiness.list(idUsuario), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Permiso>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@ApiOperation(value = "Obtener un permiso por id rol", response = Permiso.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Permiso> get(@ApiParam(value = "El id del rol") @PathVariable("id") int id) {
		try {

			return new ResponseEntity<Permiso>(permisoBusiness.get(id), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<Permiso>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
