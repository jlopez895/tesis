package ar.edu.iua.rest;

import java.io.Console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.authtoken.IAuthTokenBusiness;
import ar.edu.iua.business.UserBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.User;



@RestController
public class CoreRestController extends BaseRestController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserBusiness userBusiness;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@PostMapping(value = "/login-token", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loginToken(@RequestParam(value = "dni") String dni,
                                             @RequestParam(value = "password") String password) {
        try {
            User u = userBusiness.load(dni);  // Utiliza el método load que busca por DNI o email
            String msg = u.checkAccount(passwordEncoder, password);
            if (msg != null) {
                return new ResponseEntity<String>(msg, HttpStatus.UNAUTHORIZED);
            } else {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
                        u.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                return new ResponseEntity<String>(userToJson(getUserLogged()).get("authtoken").toString(),
                        HttpStatus.OK);
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>("BAD_ACCOUNT_DNI", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/login-user", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loginUser(@RequestParam(value = "dni") String dni,
                                            @RequestParam(value = "password") String password) {
        try {
            User u = userBusiness.load(dni);  // Utiliza el método load que busca por DNI o email
            String msg = u.checkAccount(passwordEncoder, password);
            if (msg != null) {
                return new ResponseEntity<String>(msg, HttpStatus.UNAUTHORIZED);
            } else {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(u, null,
                        u.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                return new ResponseEntity<String>(userToJson(getUserLogged()).toString(),
                        HttpStatus.OK);
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>("BAD_ACCOUNT_DNI", HttpStatus.UNAUTHORIZED);
        }
    }


	@GetMapping(value = Constantes.URL_AUTH_INFO)
	public ResponseEntity<String> authInfo() {
		return new ResponseEntity<String>(userToJson(getUserLogged()).toString(), HttpStatus.OK);
	}

	@Autowired
	private IAuthTokenBusiness authTokenBusiness;

	@GetMapping(value = Constantes.URL_LOGOUT)
	public ResponseEntity<String> logout() {
		try {
			User u = getUserLogged();
			if (u != null) {
				authTokenBusiness.delete(u.getSessionToken());
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}