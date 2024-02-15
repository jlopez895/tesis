package ar.edu.iua.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.model.Estimulo;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.EstimuloRepository;

@Service
public class EstimuloBusiness implements IEstimuloBusiness{
	
	@Autowired
	private IEstimuloBusiness estimuloService;
	
	@Autowired
	private EstimuloRepository estimuloDAO;

	@Override
	public RespuestaGenerica<Estimulo> nuevoEstimulo(Estimulo estimulo) throws BusinessException {
		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<Estimulo> rg = new RespuestaGenerica<Estimulo>(estimulo, m);

		String mensajeCheck = estimulo.checkBasicInfo();

		if (mensajeCheck != "OK") {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return rg;
		}

		try {
			estimulo.setDescripcion(estimulo.getDescripcion());
			estimulo.setEstado(1);
			estimulo.setTiempoEstmado(estimulo.getTiempoEstmado());
			estimulo.setTitulo(estimulo.getTitulo());
			
			estimuloDAO.save(estimulo);
		} catch (Exception e) {
			throw new BusinessException(e);
		}

		return rg;
	}
	
	@Override
	public Estimulo load(int nro) throws BusinessException, NotFoundException {
		Optional<Estimulo> estimulo = null;
		try {

			estimulo = estimuloDAO.findById(nro);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!estimulo.isPresent())
			throw new NotFoundException("El estimulo no se encuentra en la BD");

		return estimulo.get();
	}

	@Override
	public List<Estimulo> list() throws BusinessException {
		List<Estimulo> list=null;
		try {

			list=estimuloDAO.findByEstado();

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		
		if(list!=null&&list.size()>0)
			return list;
		else
			return null;
	}

	

}
