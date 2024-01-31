package ar.edu.iua.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.business.IProductoBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Cliente;
import ar.edu.iua.model.Producto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = Constantes.URL_PRODUCTOS)

@Api(value = "Productos", description = "Operaciones relacionadas con los productos", tags = { "Productos" })
public class ProductoRestController {

	@Autowired
	private IProductoBusiness productoBusiness;

	@ApiOperation(value="Obtener un producto mediante el ID o el nombre", response = Producto.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 404, message = "Producto no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Producto> load(
			@ApiParam(value = "Nombre del producto")@RequestParam(name = "nombre", required = false, defaultValue = "*") String nombre,
			@ApiParam(value = "Id del producto")@RequestParam(name = "id", required = false, defaultValue = "0") long id) {
		try {
			System.out.println(nombre + id);
			return new ResponseEntity<Producto>(productoBusiness.load(id, nombre), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<Producto>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value="Obtener listado de productos", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operación exitosa"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Producto>> list() {
		try {
			return new ResponseEntity<List<Producto>>(productoBusiness.list(), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<List<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value="Añadir un producto", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Producto creado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> add(@RequestBody Producto producto) {
		try {
			productoBusiness.add(producto);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("location", Constantes.URL_CHOFERES + "/" + producto.getId());
			return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value="Actualizacion de un producto", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Producto no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> update(
			@ApiParam(value = "ID del producto")@PathVariable("id") long id, @RequestBody Producto producto) {
		try {

			productoBusiness.update(producto, id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value="Eliminacion de un producto", response = Cliente.class)

	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Operacion exitosa"),
			@ApiResponse(code = 404, message = "Chofer no encontrado"),
			@ApiResponse(code = 500, message = "Error interno del servidor") 
	})
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(
			@ApiParam(value = "ID del producto")@PathVariable(name = "id") long id) {
		try {
			productoBusiness.delete(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Producto> load(@PathVariable("id") Long id) {
		
		try {
			return new ResponseEntity<Producto>(productoBusiness.findById(id),HttpStatus.OK); 
		}catch (NotFoundException e) {
			return new ResponseEntity<Producto>(HttpStatus.NOT_FOUND);
		}catch (BusinessException e) {
			return new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping(value = "/ce/{codigoExterno}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Producto> findByCodigoExterno(@PathVariable("codigoExterno") String codigoExterno) {
		
		try {
			return new ResponseEntity<Producto>(productoBusiness.findByCodigoExterno(codigoExterno),HttpStatus.OK); 
		}catch (NotFoundException e) {
			return new ResponseEntity<Producto>(HttpStatus.NOT_FOUND);
		}catch (BusinessException e) {
			return new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
