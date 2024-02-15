package ar.edu.iua.rest;

import java.util.Set;
import java.util.stream.Collectors;

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
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.User;

@RestController
public class CoreRestController extends BaseRestController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserBusiness userBusiness;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthTokenBusiness authTokenBusiness;

    @PostMapping(value = "/login-token", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loginToken(@RequestParam(value = "legajo") String legajo,
                                             @RequestParam(value = "password") String password) {
        try {
            User u = userBusiness.load(legajo);
            String msg = u.checkAccount(passwordEncoder, password);
            if (msg != null) {
                return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
            } else {
                authenticateUser(u);
                return new ResponseEntity<>(userToJson(getUserLogged()).get("authtoken").toString(),
                        HttpStatus.OK);
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("BAD_ACCOUNT_LEGAJO", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/login-user", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> loginUser(@RequestParam(value = "legajo") String legajo,
                                            @RequestParam(value = "password") String password) {
        try {
            User u = userBusiness.load(legajo);
            String msg = u.checkAccount(passwordEncoder, password);
            if (msg != null) {
                return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
            } else {
                authenticateUser(u);
                return new ResponseEntity<>(userToJson(getUserLogged()).toString(),
                        HttpStatus.OK);
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("BAD_ACCOUNT_LEGAJO", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = Constantes.URL_AUTH_INFO)
    public ResponseEntity<String> authInfo() {
        return new ResponseEntity<>(userToJson(getUserLogged()).toString(), HttpStatus.OK);
    }

    @GetMapping(value = Constantes.URL_LOGOUT)
    public ResponseEntity<String> logout() {
        try {
            User u = getUserLogged();
            if (u != null) {
                authTokenBusiness.delete(u.getSessionToken());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/user-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserRolesByLegajo(@RequestParam(value = "legajo") String legajo) {
        try {
            User user = userBusiness.load(legajo);
            if (user != null) {
                //Set<Rol> roles = user.getRoles();
                return new ResponseEntity<>("este usuario tiene este rol", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("esto no funciona");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
}
