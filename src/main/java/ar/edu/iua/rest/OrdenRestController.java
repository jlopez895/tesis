package ar.edu.iua.rest;

import ar.edu.iua.business.IOrdenBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.dto.MensajeRespuesta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constantes.URL_ORDENES)

@Api(value = "Ordenes", description = "Operaciones relacionadas con las ordenes", tags = { "Ordenes" })
public class OrdenRestController {

	@Autowired
	private IOrdenBusiness ordenBusiness;

	@ApiOperation(value = "Comienzo de carga de una orden con datos de sistemas externos", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 201, message = "Orden creada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PostMapping(value = "/ingresoOrden", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> load(@RequestBody Orden orden) {

		try {
			System.out.println(orden.getNumeroOrden());
			MensajeRespuesta m = ordenBusiness.recibirEstadoUno(orden).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Carga de datos del pesaje inicial del camion en la orden", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PostMapping(value = "/pesajeInicial/{nroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> pesajeInicial(@RequestBody Orden orden,
			@ApiParam(value = "Numero de orden en la que se desea cargar los datos") @PathVariable("nroOrden") int nroOrden)
			throws NotFoundException {

		try {
			MensajeRespuesta m = ordenBusiness.recibirEstadoDos(orden, nroOrden).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Carga de datos del pesaje final del camion en la orden", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PostMapping(value = "/pesajeFinal/{nroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> pesajeFinal(@RequestBody Orden orden,
			@ApiParam(value = "Numero de orden en la que se desea cargar los datos") @PathVariable("nroOrden") int nroOrden)
			throws NotFoundException {

		try {
			MensajeRespuesta m = ordenBusiness.pesajeFinal(orden, nroOrden).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Solicitud de conciliacion por numero de orden", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@GetMapping(value = "/pedirConciliacion/{nroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> pedirConciliacion(
			@ApiParam(value = "Numero de orden de la que se desea obtener la conciliacion") @PathVariable("nroOrden") int nroOrden)
			throws NotFoundException {

		try {
			MensajeRespuesta m = ordenBusiness.generarConciliacion(nroOrden).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Obtener una orden por numero de orden", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/{numeroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Orden> load(
			@ApiParam(value = "El numero de la Orden que se desea obtener") @PathVariable("numeroOrden") int numeroOrden) {

		try {
			return new ResponseEntity<Orden>(ordenBusiness.load(numeroOrden), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Orden>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Orden>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Obtener listado de ordenes", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Orden>> list() {
		try {

			return new ResponseEntity<List<Orden>>(ordenBusiness.list(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Orden>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@ApiOperation(value = "Solicitud para cerrar una orden", response = Orden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	@PutMapping(value = "/cerrarOrden/{nroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> cerrarOrden(
			@ApiParam(value = "Numero de orden de la que se desea obtener la conciliacion") @PathVariable("nroOrden") int nroOrden)
			throws NotFoundException {

		try {
			MensajeRespuesta m = ordenBusiness.cerrarOrden(nroOrden).getMensaje();
			if (m.getCodigo() == 0) {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
			} else {
				return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

			}
		} catch (BusinessException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<MensajeRespuesta>(HttpStatus.NOT_FOUND);
		}
	}

}
