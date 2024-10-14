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

import ar.edu.iua.business.INoticiaBusiness;
import ar.edu.iua.business.exception.BusinessException;

import ar.edu.iua.model.Documento;
import ar.edu.iua.model.Noticia;
import ar.edu.iua.model.Notificacion;
import ar.edu.iua.model.dto.MensajeRespuesta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_NOTICIAS)

@Api(value = "Noticias", description = "Operaciones relacionadas con las noticias", tags = {
		"Notificaciones" })
public class NoticiaRestController {

	
	@Autowired
	private INoticiaBusiness noticiaBusiness;
	@ApiOperation(value = "Obtener listado de noticias", response = Notificacion.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Noticia>> list() {
		try {

			return new ResponseEntity<List<Noticia>>(noticiaBusiness.list(), HttpStatus.OK);

		} catch (BusinessException e) {
			return new ResponseEntity<List<Noticia>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Nuevo documento", response = Documento.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Orden no encontrada"),
			@ApiResponse(code = 400, message = "El servidor no procesará la solicitud porque no puede o no debe debido  a un error del usuario "),
			@ApiResponse(code = 500, message = "Error interno del servidor") })
	
	@PostMapping(value = "/nuevaNoticia", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MensajeRespuesta> cargarDocumento(@RequestBody Noticia noticia)
			throws BusinessException {

		MensajeRespuesta m = noticiaBusiness.nuevaNoticia(noticia).getMensaje();
		if (m.getCodigo() == 0) {
			return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.OK);
		} else {
			return new ResponseEntity<MensajeRespuesta>(m, HttpStatus.BAD_REQUEST);

		}
	}

}
