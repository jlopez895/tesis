package ar.edu.iua.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Permiso;
import ar.edu.iua.model.persistence.PermisoRepository;
import ar.edu.iua.model.persistence.UserRepository;

@Service
public class PermisoBusiness implements IPermisoBusiness{
	@Autowired
	private PermisoRepository permisoDao;
	@Override
	public List<Permiso> list(int idUser) throws BusinessException {
		try {
			return permisoDao.findByUser(idUser);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
