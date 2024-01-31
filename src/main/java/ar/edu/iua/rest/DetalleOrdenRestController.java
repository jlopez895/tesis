package ar.edu.iua.rest;

import ar.edu.iua.business.IDetalleOrdenBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.DetalleOrden;
import ar.edu.iua.model.dto.MensajeRespuesta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constantes.URL_DETALLE_ORDEN)

@Api(value = "Detalles de ordenes", description = "Operaciones relacionadas con los detalles de orden", tags = {
		"Detalles de ordenes" })
public class DetalleOrdenRestController {

	@Autowired
	private IDetalleOrdenBusiness detalleOrdenBusiness;

	@ApiOperation(value = "Generacion de detalles de orden de la carga del camion", response = DetalleOrden.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	
	@PostMapping(value = "/cargarCamion/{nroOrden}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> cargarCamion(@RequestBody DetalleOrden detalleOrden,
			@ApiParam(value = "Numero de la orden") @PathVariable("nroOrden") int nroOrden)
			throws NotFoundException {

		try {
			MensajeRespuesta m = detalleOrdenBusiness.cargarCamion(detalleOrden, nroOrden).getMensaje();
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