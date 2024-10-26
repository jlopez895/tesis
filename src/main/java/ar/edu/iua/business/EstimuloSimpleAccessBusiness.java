package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.EstimuloSimpleAccess;
import ar.edu.iua.model.persistence.EstimuloRepository;
import ar.edu.iua.model.persistence.EstimuloSimpleAccessRepository;

@Service
public class EstimuloSimpleAccessBusiness implements IEstimuloSimpleAccessBusiness{
	
	@Autowired
	private IEstimuloSimpleAccessBusiness estimuloService;

	@Autowired
	private EstimuloSimpleAccessRepository estimuloDAO;

	@Override
	public void nuevoEstimulo(Estimulo estimulo) throws BusinessException {
		try {
			List<EstimuloSimpleAccess> l=estimuloDAO.findAll();
			if(l.isEmpty()==true)
				add(estimulo);
			else
				update(estimulo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
	}
	
	private void add(Estimulo registro) throws BusinessException {
		try {
			
	
			EstimuloSimpleAccess r=new EstimuloSimpleAccess();
			r.setIdEstimulo(registro.getId());
			r.setDescripcion(registro.getDescripcion());
			r.setEstado(registro.getEstado());
			r.setFechaFin(registro.getFechaFin());
			r.setFechaInicio(registro.getFechaInicio());
			r.setTiempoEstmado(registro.getTiempoEstmado());
			r.setTitulo(registro.getTitulo());
			r.setUsuarioCreador(registro.getUsuarioCreador());
			r.setUsuarioFinalizador(registro.getUsuarioFinalizador());
			
			estimuloDAO.save(r);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	private void update(Estimulo registro) throws NotFoundException, BusinessException{
		
		EstimuloSimpleAccess registroNuevo = load();
		
		registroNuevo.setIdEstimulo(registro.getId());
		registroNuevo.setDescripcion(registro.getDescripcion());
		registroNuevo.setEstado(registro.getEstado());
		registroNuevo.setFechaFin(registro.getFechaFin());
		registroNuevo.setFechaInicio(registro.getFechaInicio());
		registroNuevo.setTiempoEstmado(registro.getTiempoEstmado());
		registroNuevo.setTitulo(registro.getTitulo());
		registroNuevo.setUsuarioCreador(registro.getUsuarioCreador());
		registroNuevo.setUsuarioFinalizador(registro.getUsuarioFinalizador());
		
		
		estimuloDAO.save(registroNuevo);
	}

	@Override
	public EstimuloSimpleAccess load() throws BusinessException {
		EstimuloSimpleAccess registro = null;
		try {
				registro = estimuloDAO.findAll().get(0);
			
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return registro;
	}

	@Override
	public void delete() throws BusinessException {
		try {
			estimuloDAO.deleteAll();
		
	} catch (Exception e) {
		throw new BusinessException(e);
	}
	
		
	}

}
