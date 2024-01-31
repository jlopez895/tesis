package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Cliente;
import ar.edu.iua.model.persistence.ClienteRepository;

@Service
public class ClienteBusiness implements IClienteBusiness {

	@Autowired
	private ClienteRepository clienteDAO;

	@Override
	public Cliente load(long id, String razonSocial) throws NotFoundException, BusinessException {
		Optional<Cliente> cliente = null;
		try {
			if (id != 0 && razonSocial.equals("*"))
				cliente = clienteDAO.findById(id);
			if (id == 0 && !razonSocial.equals("*"))
				cliente = clienteDAO.findByRazonSocial(razonSocial);
			if (id != 0 && (!razonSocial.equals("*")))
				cliente = clienteDAO.findByRazonSocialAndId(razonSocial, id);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!cliente.isPresent())
			throw new NotFoundException("El Cliente no se encuentra en la BD");

		return cliente.get();
	}

	@Override
	public List<Cliente> list() throws BusinessException {
		try {
			return clienteDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public Cliente add(Cliente cliente) throws BusinessException {
		try {

			if (cliente.checkBasicData() == null)
				return clienteDAO.save(cliente);
			else
				throw new BusinessException();
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public Cliente update(Cliente cliente, long id) throws NotFoundException, BusinessException {
		Cliente clienteNuevo = new Cliente();
		Cliente clienteViejo = load(id, "*");

		clienteNuevo.setId(id);

		if (cliente.getRazonSocial().equals(null) || cliente.getRazonSocial().trim().length() == 0)
			clienteNuevo.setRazonSocial((clienteViejo.getRazonSocial()));
		else
			clienteNuevo.setRazonSocial(cliente.getRazonSocial());

		if (cliente.getContacto() == 0)
			clienteNuevo.setContacto(clienteViejo.getContacto());
		else
			clienteNuevo.setContacto(cliente.getContacto());

		// lista

		return add(clienteNuevo);

	}

	@Override
	public void delete(long id) throws NotFoundException, BusinessException {
		try {
			clienteDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("No se encuentra el cliente con id=" + id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

	}

	@Override
	public Cliente load(String codigoExterno) throws NotFoundException, BusinessException {
		Optional<Cliente> cliente;
		try {
			cliente = clienteDAO.findFirstByCodigoexterno(codigoExterno);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!cliente.isPresent())
			throw new NotFoundException(
					"El producto con codigo externo " + codigoExterno + " no se encuentra en la BD");
		return cliente.get();
	}

	@Override
	public Cliente asegurarCliente(Cliente cliente) throws BusinessException {
		Cliente c = null;
		try {
			c = load(cliente.getCodigoexterno());
			c.setRazonSocial(cliente.getRazonSocial());
			c.setContacto(cliente.getContacto());
		} catch (NotFoundException e) {
			c = new Cliente(cliente);
		}
		return clienteDAO.save(c);
	}

}