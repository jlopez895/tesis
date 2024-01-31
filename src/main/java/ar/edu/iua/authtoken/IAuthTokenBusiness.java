package ar.edu.iua.authtoken;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;

public interface IAuthTokenBusiness {
	public AuthToken save(AuthToken at) throws BusinessException;

	public AuthToken load(String series) throws BusinessException, NotFoundException;

	public void delete(AuthToken at) throws BusinessException;

	public void purgeTokens() throws BusinessException;
	
	public void delete(String token) throws BusinessException ;

}