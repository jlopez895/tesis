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

import ar.edu.iua.rest.Constantes;


@Component
public class OrdenEventListener2 implements ApplicationListener<OrdenEvent>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SimpMessagingTemplate wSock;
	
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${mail.alertas.to:jessilopez895@gmail.com}")
	private String to;

	@Override
	public void onApplicationEvent(OrdenEvent event) {
		// TODO Auto-generated method stub
		
	} 
	
	
	
}
