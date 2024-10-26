package ar.edu.iua.business;

import java.util.List;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.User;

public interface IUserBusiness {

	public User load(Integer id) throws NotFoundException, BusinessException;

	public List<User> list() throws BusinessException;
	
	public User add(User user) throws BusinessException;
	
	public User update(User user) throws NotFoundException, BusinessException;

	public User load(String legajo) throws NotFoundException, BusinessException;
	
	public User loadUserName(String userName) throws NotFoundException, BusinessException;
	
	public List<User> findByRol(int idRol,String idMinisterio) throws BusinessException;
	
	public List<User> getAdmins() throws BusinessException;



}