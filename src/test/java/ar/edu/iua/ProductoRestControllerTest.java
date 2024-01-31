package ar.edu.iua;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import ar.edu.iua.authtoken.AuthToken;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Producto;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;
import ar.edu.iua.rest.ProductoRestController;

public class ProductoRestControllerTest {
	

   	@Autowired
	private MockMvc mvc;
   
	@MockBean
	private ProductoRestController productoMock;

   
    private static Producto producto;
    
    @BeforeClass
    public static void setup() throws NotFoundException, BusinessException {
    	
    	producto= new Producto();
    	producto.setCodigoexterno("142536");
    	producto.setId(new Long(1));
    	producto.setNombre("Gas Butano");
    	producto.setDescripcion("Gas Butano para llenado de tanques a domicilio.");
    	
    }
  


    @Test
    public void testLoadByidSuccess() throws BusinessException, Exception {
    	String token = getToken();
    	
    	Producto productoDescContains= producto;
    	Long id = (long) 1;
    	
    	when(productoMock.load(id)).thenReturn(ResponseEntity.ok(productoDescContains));
    	
    	mvc.perform(get("/api/final/productos/"+id)
    			.param("xauthtoken", token)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath(("$.id"),  is(producto.getId()), Long.class));
    	
    }
    

    @Test
    public void testLoadByCodigoExternoSuccess() throws BusinessException, Exception {
    	
    	String token = getToken();
    	
    	Producto productoDescContains= producto;
    	String codigoExterno = "c3svZYkLJF";
    	
    	when(productoMock.findByCodigoExterno(codigoExterno)).thenReturn(ResponseEntity.ok(productoDescContains));
    	
    	mvc.perform(get("/api/final/productos/ce/"+codigoExterno)
    			.param("xauthtoken", token)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    	
    	
    }
    
    private String getToken() {
    	Rol admin = new Rol(1, "Admin", "Testing del sistema");
    	User u = new User(2, "Jessica", "Lopez", "jlopez375@alumnos.iua.edu.ar", "123", "jlopez", admin, true);
    	u.setRoles(new HashSet<Rol>());
    	u.getRoles().add(admin);
    	
    	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
				u.getAuthorities());
    	
    	System.out.println("Autoridades = "+u.getAuthorities());
    	
    	SecurityContextHolder.getContext().setAuthentication(auth);
    	
    	Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();
		u = (User) auth1.getPrincipal();
    
		AuthToken newToken = new AuthToken(u.getSessionTimeout(), u.getUsername());
		
		String token = newToken.encodeCookieValue();
    	token = token.replace("[", "").replace("]", "");
    	
    	System.out.println("Token = "+token);
    	
    	return token;
    }



}
