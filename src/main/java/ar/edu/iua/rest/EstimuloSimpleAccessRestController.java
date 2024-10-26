package ar.edu.iua.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.IEstimuloBusiness;
import ar.edu.iua.business.IEstimuloSimpleAccessBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.EstimuloSimpleAccess;
import ar.edu.iua.model.dto.MensajeRespuesta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_ESTIMULOS_SIMPLE_ACCESS)

@Api(value = "Estimulos Simple Access", description = "Operaciones relacionadas con los estimulos", tags = { "Estimulos" })

public class EstimuloSimpleAccessRestController {
	
	@Autowired
	private IEstimuloSimpleAccessBusiness business;
	
	@ApiOperation(value = "Nuevo estumulo", response = Estimulo.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "estimulo creado"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EstimuloSimpleAccess> load() {

		try {

			return new ResponseEntity<EstimuloSimpleAccess>(business.load(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<EstimuloSimpleAccess>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
