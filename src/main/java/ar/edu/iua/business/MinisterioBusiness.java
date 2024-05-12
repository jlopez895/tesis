package ar.edu.iua.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.model.Ministerio;
import ar.edu.iua.model.persistence.MinisterioRepository;

@Service
public class MinisterioBusiness implements IMinisterioBusiness {
	@Autowired
	private IMinisterioBusiness ministerioService;

	@Autowired
	private MinisterioRepository ministerioDao;

	@Override
	public List<Ministerio> list() throws BusinessException {
		try {
			return ministerioDao.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}
