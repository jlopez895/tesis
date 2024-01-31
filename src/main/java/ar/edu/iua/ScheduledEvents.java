package ar.edu.iua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import ar.edu.iua.business.IOrdenBusiness;


@Configuration
@EnableScheduling
public class ScheduledEvents {

	@Autowired
	private IOrdenBusiness ordenBusiness;
	
	@Scheduled(fixedDelay = 5000, initialDelay = 1000)
	public void enviarDatos() {
		ordenBusiness.pushOrderData();
	}
	
}
