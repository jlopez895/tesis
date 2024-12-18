package ar.edu.iua.rest;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.edu.iua.authtoken.AuthToken;
import ar.edu.iua.authtoken.IAuthTokenBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Permiso;
import ar.edu.iua.model.RolPrincipalHolder;
import ar.edu.iua.model.User;
import ar.edu.iua.model.persistence.PermisoRepository;

public class BaseRestController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app.session.token.timeout:360}") //Usa este valor por defecto si es que no esta en app.propeties
	private int sessionTimeout;

	@Autowired
	private IAuthTokenBusiness authTokenBusiness;
	@Autowired
	private HttpSession session;
	
	@Autowired
	private PermisoRepository perm;
	
	protected User getUserLogged() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		RolPrincipalHolder.getInstance().setIdRolPrincipal(user.getRolPrincipal().getId());
		return user;
	}

	protected JSONObject userToJson(User u) {
		//AuthToken token = new AuthToken(sessionTimeout, u.getUsername());
		AuthToken token = new AuthToken(u.getSessionTimeout(), u.getUsername());
		String tokenValue = null;
		try {
			authTokenBusiness.save(token);
			tokenValue = token.encodeCookieValue();
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);

		}
		JSONObject o = new JSONObject();

		o.put("username", u.getUsername());
		o.put("fullname", u.getNombreCompleto());
		o.put("idUser", u.getId());
		o.put("email", u.getEmail());
		o.put("rolPrinc",u.getRolPrincipal().getId());
		o.put("rolPrincDesc",u.getRolPrincipal().getDescripcion());
		o.put("ministerioPrinc",u.getMinisterioPrincipal()==null?"":u.getMinisterioPrincipal().getId());
		List<Permiso> permisos=perm.obtenerTodosRol(u.getRolPrincipal().getId());
		JSONArray pe = new JSONArray();
		if(permisos!=null)
		{
			for(Permiso p:permisos)
			{
				pe.put(p.getPermiso());
			}
			o.put("permisos", pe);
		}
		JSONArray r = new JSONArray();
		for (GrantedAuthority g : u.getAuthorities()) {
			r.put(g.getAuthority());
		}
		o.put("roles", r);
		o.put("authtoken", tokenValue);
		return o;
	}

}