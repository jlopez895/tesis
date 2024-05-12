package ar.edu.iua.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.IMinisterioBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.Ministerio;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_MINISTERIOS)

@Api(value = "Ministerio", description = "Operaciones relacionadas con los ministerios", tags = { "Ministerios" })

public class MinisterioRestController {
	
	@Autowired
	private IMinisterioBusiness ministerioBusiness;
	@ApiOperation(value = "Obtener listado de estimulos", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ministerio>> list() {
		try {

			return new ResponseEntity<List<Ministerio>>(ministerioBusiness.list(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Ministerio>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
