package ar.edu.iua.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Rol;
import ar.edu.iua.model.persistence.RolRepository;

@Service
public class RolBusiness implements IRolBusiness{

	@Autowired
	private RolRepository rolDAO;
	@Override
	public List<Rol> list() throws BusinessException {
		try {
			return rolDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	@Override
	public List<Rol> findByMinisterio(int ministerio) throws BusinessException {
		try {
			return rolDAO.findByMinisterio(ministerio);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
