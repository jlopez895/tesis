package ar.edu.iua.business;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import ar.edu.iua.business.exception.BusinessException;
import ar.edu.iua.business.exception.NotFoundException;
import ar.edu.iua.eventos.OrdenEvent;
import ar.edu.iua.model.DetalleOrden;
import ar.edu.iua.model.Orden;
import ar.edu.iua.model.dto.MensajeRespuesta;
import ar.edu.iua.model.dto.RespuestaGenerica;
import ar.edu.iua.model.persistence.DetalleOrdenRepository;
import ar.edu.iua.model.persistence.OrdenRepository;

@Service
public class DetalleOrdenBusiness implements IDetalleOrdenBusiness {

	@Autowired
	private IOrdenBusiness ordenService;

	@Autowired
	private IDetalleOrdenBusiness detalleOrdenService;

	@Autowired
	private DetalleOrdenRepository detalleOrdenDAO;
	@Autowired
	private OrdenRepository ordenDAO;
	
	
	
	
	@Override
	public DetalleOrden load(long id) throws BusinessException, NotFoundException {
		Optional<DetalleOrden> detalleOrden = null;
		try {

			detalleOrden = detalleOrdenDAO.findById(id);

		} catch (Exception e) {
			throw new BusinessException(e);
		}
		if (!detalleOrden.isPresent())
			throw new NotFoundException("El orden no se encuentra en la BD");

		return detalleOrden.get();
	}

	@Override
	public RespuestaGenerica<DetalleOrden> cargarCamion(DetalleOrden detalleOrden, int nroOrden)
			throws BusinessException, NotFoundException {

		MensajeRespuesta m = new MensajeRespuesta();
		RespuestaGenerica<DetalleOrden> rg = new RespuestaGenerica<DetalleOrden>(detalleOrden, m);

		Orden orden = ordenService.load(nroOrden);

		String mensajeCheck = detalleOrden.checkBasicData(orden);

		if ((mensajeCheck != "Cargando camion") && (mensajeCheck != "Camion cargado")) {
			m.setCodigo(-1);
			m.setMensaje(mensajeCheck);
			return rg;
		}

		try {
			detalleOrden.setOrden(orden);
			detalleOrden.setMasaAcumulada(detalleOrden.getMasaAcumulada());
			detalleOrden.setDensidad(detalleOrden.getDensidad());
			detalleOrden.setTemperatura(detalleOrden.getTemperatura());
			detalleOrden.setCaudal(detalleOrden.getCaudal());
			detalleOrden.setFechaHoraMedicion(new Date());

			// SE COMPARA LA FECHAHORAMEDICION DEL DETALLE ORDEN CON LA FECHAHORAMEDICION
			// DEL
			// ULTIMO DETALLE ORDEN PARA VER SI HAY QUE ALMACENARLO O SOLO ACTUALIZAR LA
			// ORDEN

			// Se obtiene la fecha hora del ultimo detalle
			if (detalleOrdenDAO.findByFechaHoraMedicionDesc(orden.getId()) != null) {
				Date ultimoDetalleOrden = detalleOrdenDAO.findByFechaHoraMedicionDesc(orden.getId())
						.getFechaHoraMedicion();

				// Como se sabe que si o si la medicion se realiza el dia del turno, se compara
				// solo el tiempo

				Time timeNuevoDetalleOrden = Time.valueOf(detalleOrden.getFechaHoraMedicion().getHours() + ":"
						+ detalleOrden.getFechaHoraMedicion().getMinutes() + ":"
						+ detalleOrden.getFechaHoraMedicion().getSeconds());

				Time timeUltimoAlmacenamiento = Time.valueOf(orden.getFechaHoraUltimoAlmacenamiento().getHours() + ":"
						+ orden.getFechaHoraUltimoAlmacenamiento().getMinutes() + ":"
						+ orden.getFechaHoraUltimoAlmacenamiento().getSeconds());

				System.out.println("HORA DEL NUEVO DETALLE " + timeNuevoDetalleOrden);
				System.out.println("HORA DEL ULTIMO almacenamiento " + timeUltimoAlmacenamiento);

				long difference = timeNuevoDetalleOrden.getTime() - timeUltimoAlmacenamiento.getTime();
				long hours = TimeUnit.MILLISECONDS.toHours(difference);
				long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);

				System.out.println("Time in seconds: " + seconds + " seconds.");
				System.out.println("Time in minutes: " + minutes + " minutes.");
				System.out.println("Time in hours: " + hours + " hours.");

				if (seconds < orden.getFrecuenciaAlmacenamiento()) // Si pasaron menos de 60 segundos solo actualizamos
																	// la orden
				{

					orden.setUltimaMasaAcumulada(detalleOrden.getMasaAcumulada());
					orden.setUltimaDensidad(detalleOrden.getDensidad());
					orden.setUltimaTemperatura(detalleOrden.getTemperatura());
					orden.setUltimoCaudal(detalleOrden.getCaudal());
					orden.setFechaHoraInicioCarga(
							detalleOrdenDAO.findByFechaHoraMedicionAsc(orden.getId()).getFechaHoraMedicion());
					orden.setFechaHoraFinCarga(detalleOrden.getFechaHoraMedicion());
					
					//Si no tiene la alarma encendida, verifico si hay que generar alguna alarma
					//Si hay una alarma encendida no entro ya que no se pueden generar dos alarmas en simultaneo
					if(!orden.isTieneAlarmaEncendida()) {
						if (orden.getUltimaTemperatura()>orden.getTemperaturaMaxima()) {
							generaEvento(orden, OrdenEvent.Tipo.TEMPERATURA_MAXIMA);
							orden.setTieneAlarmaEncendida(true);
						}
					}
					
					
				}

				else // Guardamos el detalle orden y actualizamos la orden
				{
					detalleOrdenDAO.save(detalleOrden);
					orden.setUltimaMasaAcumulada(detalleOrden.getMasaAcumulada());
					orden.setUltimaDensidad(detalleOrden.getDensidad());
					orden.setUltimaTemperatura(detalleOrden.getTemperatura());
					orden.setUltimoCaudal(detalleOrden.getCaudal());
					orden.setFechaHoraInicioCarga(
							detalleOrdenDAO.findByFechaHoraMedicionAsc(orden.getId()).getFechaHoraMedicion());
					orden.setFechaHoraFinCarga(detalleOrden.getFechaHoraMedicion());
					orden.setFechaHoraUltimoAlmacenamiento(new Date());
					
					if(!orden.isTieneAlarmaEncendida()) {
						if (orden.getUltimaTemperatura()>orden.getTemperaturaMaxima()){
							generaEvento(orden, OrdenEvent.Tipo.TEMPERATURA_MAXIMA);
							orden.setTieneAlarmaEncendida(true);
						}
					}
				}

			} else {
				orden.setUltimaMasaAcumulada(detalleOrden.getMasaAcumulada());
				orden.setUltimaDensidad(detalleOrden.getDensidad());
				orden.setUltimaTemperatura(detalleOrden.getTemperatura());
				orden.setUltimoCaudal(detalleOrden.getCaudal());
				orden.setFechaHoraInicioCarga(detalleOrden.getFechaHoraMedicion());
				orden.setFechaHoraFinCarga(detalleOrden.getFechaHoraMedicion());
				orden.setFechaHoraUltimoAlmacenamiento(detalleOrden.getFechaHoraMedicion());
				detalleOrdenDAO.save(detalleOrden);
				
				//Si no tiene la alarma encendida, verifico si hay que generar alguna alarma
				//Si hay una alarma encendida no entro ya que no se pueden generar dos alarmas en simultaneo
				if(!orden.isTieneAlarmaEncendida()) {
					if (orden.getUltimaTemperatura()> orden.getTemperaturaMaxima()) {
						generaEvento(orden, OrdenEvent.Tipo.TEMPERATURA_MAXIMA);
						orden.setTieneAlarmaEncendida(true);
					}
				}
				
			}

			ordenDAO.save(orden);
		} catch (Exception e) {
			throw new BusinessException();
		}

		return rg;
	}
	
	
	@Autowired
	private ApplicationEventPublisher appEventPublisher;
	private void generaEvento(Orden orden, OrdenEvent.Tipo tipo) {
		appEventPublisher.publishEvent(new OrdenEvent(orden, tipo));
	}
	

	

}
