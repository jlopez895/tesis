package ar.edu.iua.eventos;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.iua.model.Orden;
import ar.edu.iua.rest.Constantes;


@Component
public class OrdenEventListener2 implements ApplicationListener<OrdenEvent>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SimpMessagingTemplate wSock;
	
	@Override
	public void onApplicationEvent(OrdenEvent event) {

		if (event.getSource() instanceof Orden) {
			
			if(event.getTipo().equals(OrdenEvent.Tipo.TEMPERATURA_MAXIMA))
				manejaEventoExcesoTemperatura((Orden) event.getSource());
			
		}
	}
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${mail.alertas.to:jessilopez895@gmail.com}")
	private String to; 
	
	private void manejaEventoExcesoTemperatura(Orden orden) {
		String mensaje = String.format("La temperatura de carga de la orden " + orden.getNumeroOrden()+ " ha excedido el limite, su temperatura actual es de " + orden.getTemperaturaMaxima()+ "°C" + "TYPE=excesoTemp");
		
		wSock.convertAndSend(Constantes.TOPIC_SEND_WEBSOCKET_GRAPH,	mensaje);
		log.info(mensaje);

		String subject = "Exceso de temperatura en la orden " + orden.getNumeroOrden();
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("jlopez375@alumnos.iua.edu.ar");
			message.setTo(to);
			message.setSubject(subject);
			message.setText(mensaje.split("TYPE")[0]);
			emailSender.send(message);
			log.trace("Email enviado a " + to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
