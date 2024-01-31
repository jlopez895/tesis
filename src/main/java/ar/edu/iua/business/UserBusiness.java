package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.User;
import ar.edu.iua.model.persistence.UserRepository;

@Service
public class UserBusiness implements IUserBusiness {

	@Autowired
	private UserRepository userDAO;

	@Override
	public User load(Integer id) throws NotFoundException, BusinessException {
		Optional<User> op;
		try {
			op = userDAO.findById(id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!op.isPresent()) {
			throw new NotFoundException("El user con id " + id + " no se encuentra en la BD");
		}
		return op.get();
	}

	@Override
	public List<User> list() throws BusinessException {

		try {
			return userDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public User add(User user) throws BusinessException {
		try {
			return userDAO.save(user);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public User update(User user) throws NotFoundException, BusinessException {
		load(user.getId());
		return add(user);
	}

	 @Override
	    public User load(String dniOrEmail) throws NotFoundException, BusinessException {
	        try {
	            User user = userDAO.findByDniOrEmail(dniOrEmail, dniOrEmail);

	            if (user == null) {
	                throw new NotFoundException("No se encuentra el usuario con DNI o email = " + dniOrEmail);
	            }

	            return user;
	        } catch (Exception e) {
	            throw new BusinessException(e);
	        }
	    }

}
