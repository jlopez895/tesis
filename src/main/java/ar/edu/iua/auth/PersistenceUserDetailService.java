package ar.edu.iua.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.IUserBusiness;
import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.User;

@Service
public class PersistenceUserDetailService implements UserDetailsService {

	@Autowired
	private IUserBusiness userBusiness;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User r;

		try {
			r=userBusiness.load(username);
		} catch (NotFoundException e) {
			throw new UsernameNotFoundException("No se encuentra "+ username);
		} catch (BusinessException e) {
			throw new RuntimeException(e);
		}
		
		return r;
	}

}