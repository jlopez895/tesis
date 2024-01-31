package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Chofer;
import ar.edu.iua.model.persistence.ChoferRepository;

@Service
public class ChoferBusiness implements IChoferBusiness {

	@Autowired
	private ChoferRepository choferDAO;
	
	
	@Override
	public Chofer load(Long id) throws BusinessException, NotFoundException {
		Optional<Chofer> op;
		try {
			op = choferDAO.findById(id);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!op.isPresent())
			throw new NotFoundException("No se encuentra el chofer con Id =" + id);
		return op.get();
	}

	@Override
	public Chofer load(long id, long dni) throws NotFoundException, BusinessException {
		Optional<Chofer> chofer = null;
		try {
			if (id != 0 && dni == 0)
				chofer = choferDAO.findById(id);
			if (id == 0 && dni != 0)
				chofer = choferDAO.findByDni(dni);
			if (id != 0 && dni != 0)
				chofer = choferDAO.findByDniAndId(dni, id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!chofer.isPresent())
			throw new NotFoundException("El Chofer no se encuentra en la BD");

		return chofer.get();
	}

	@Override
	public List<Chofer> list() throws BusinessException {
		try {
			return choferDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public Chofer add(Chofer chofer) throws BusinessException {
		try {

			if (chofer.checkBasicData() == null)
				return choferDAO.save(chofer);
			else
				throw new BusinessException();
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public Chofer update(Chofer chofer, long id) throws NotFoundException, BusinessException {
		Chofer choferNuevo = new Chofer();
		Chofer choferViejo = load(id, 0);

		choferNuevo.setId(id);

		if (chofer.getApellido() == null || chofer.getApellido().trim().length() == 0)
			choferNuevo.setApellido(choferViejo.getApellido());
		else
			choferNuevo.setApellido(chofer.getApellido());

		if (chofer.getDni() == 0)
			choferNuevo.setDni(choferViejo.getDni());
		else
			choferNuevo.setDni(chofer.getDni());

		if (chofer.getNombre() == null || chofer.getNombre().trim().length() == 0)
			choferNuevo.setNombre(choferViejo.getNombre());
		else
			choferNuevo.setNombre(chofer.getNombre());

		// FALTA VER LA LISTA DE ORDENES

		return add(choferNuevo);

	}

	@Override
	public void delete(long id) throws NotFoundException, BusinessException {
		try {
			choferDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("No se encuentra el chofer con id=" + id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public Chofer load(String codigoExterno) throws NotFoundException, BusinessException {
		Optional<Chofer> camion;
		try {
			camion = choferDAO.findFirstByCodigoexterno(codigoExterno);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!camion.isPresent())
			throw new NotFoundException("El chofer con codigo externo " + codigoExterno + " no se encuentra en la BD");
		return camion.get();
	}

	@Override
	public Chofer asegurarChofer(Chofer chofer) throws BusinessException {
		Chofer c = null;

		try {
			c = load(chofer.getCodigoexterno());
			c.setApellido(chofer.getApellido());
			c.setNombre(chofer.getNombre());
			c.setDni(chofer.getDni());

		} catch (NotFoundException e) {
			c = new Chofer(chofer);
		}
		return choferDAO.save(c);
		
	}
	
	@Override
	public Chofer findByCodigoExterno(String c) throws NotFoundException, BusinessException {
		
		Optional<Chofer> op=choferDAO.findByCodigoexterno(c);
		
		if(!op.isPresent()) {
			throw new NotFoundException("No se encuentra el chofer con el codigo externo =" + c);
		}
		
		return op.get();
	}

}