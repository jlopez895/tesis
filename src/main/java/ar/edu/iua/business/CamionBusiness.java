package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;	
//import ar.edu.iua.eventos.CamionEvent;
import ar.edu.iua.model.Camion;
import ar.edu.iua.model.persistence.CamionRepository;

@Service
public class CamionBusiness implements ICamionBusiness {

	@Autowired
	private CamionRepository camionDAO; 
	
	@Override
		public Camion load(String patente, long id) throws NotFoundException, BusinessException {
			Optional<Camion> camion = null; 
			try {
					if(!patente.equals("*") && id == 0)
						camion = camionDAO.findByPatente(patente);
					if(patente.equals("*")&&(id!=0))
						camion = camionDAO.findById(id);
					if(!patente.equals("*")&&(id!=0))
						camion = camionDAO.findByPatenteAndId(patente, id);
					
				} catch (Exception e) {
					throw new BusinessException(e);
				} 
			if(!camion.isPresent())
				throw new NotFoundException("El camion no se encuentra en la BD");
			
			return camion.get(); 
		}

	@Override
	public List<Camion> list() throws BusinessException {
		try {
			return camionDAO.findAll();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public Camion add(Camion camion) throws BusinessException {
		try {
			if(camion.checkBasicData()==null)
				return camionDAO.save(camion);
			else 
				throw new BusinessException();
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
	}

	@Override
	public Camion update(Camion camion, Long id) throws NotFoundException, BusinessException {
		
		Camion camionNuevo = new Camion();
		Camion camionViejo = load("*",id);
		
		camionNuevo.setId(id);
		if(camion.getPatente()==null || camion.getPatente().trim().length()==0)
				camionNuevo.setPatente(camionViejo.getPatente());
			else
				camionNuevo.setPatente(camion.getPatente());
			
			/*if(camion.getOrdenList().isEmpty())
				camionNuevo.setOrdenList(camionViejo.getOrdenList());
			else
				camionNuevo.setOrdenList(camion.getOrdenList());
			*/
			if(camion.getDescripcion()==null || camion.getDescripcion().trim().length()==0)
				camionNuevo.setDescripcion(camionViejo.getDescripcion());
			else 
				camionNuevo.setDescripcion(camion.getDescripcion());
			
			if(camion.getCodigoexterno()==null || camion.getCodigoexterno().trim().length()==0)
				camionNuevo.setCodigoexterno(camionViejo.getCodigoexterno());
			else
				camionNuevo.setCodigoexterno(camion.getCodigoexterno());
			
			/*
			if(camion.getCisternado().length==0)
				camionNuevo.setCisternado(camionViejo.getCisternado());
			else
				camionNuevo.setCisternado(camion.getCisternado());
			*/
			
		return add(camionNuevo);
	}
	

	@Override
	public void delete(Long id) throws NotFoundException, BusinessException {
		try {
			camionDAO.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("No se encuentra el camion con id=" + id);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
	}

	@Override
	public Camion load(String codigoExterno) throws NotFoundException, BusinessException {
		Optional<Camion> camion; 
		try {
			camion = camionDAO.findFirstByCodigoexterno(codigoExterno);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if(!camion.isPresent())
			throw new NotFoundException("El producto con codigo externo " + codigoExterno + " no se encuentra en la BD");
		return camion.get(); 
	}

	@Override
	public Camion asegurarCamion(Camion camion) throws BusinessException {
		Camion c = null;
		try {
			c = load(camion.getCodigoexterno());
			c.setPatente(camion.getPatente());
			c.setCisternado(camion.getCisternado());
			c.setDescripcion(camion.getDescripcion());
		} catch (NotFoundException e) {
			c = new Camion(camion);
		}
		return camionDAO.save(c);
	}
	
	

}
