package ar.edu.iua.model.dto;

public class MensajeRespuesta {
	
	private int codigo;
	private String mensaje = "OK";
	
	public MensajeRespuesta(int codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
	}
	
	public MensajeRespuesta() {
	
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	} 
	
	
	
	
	
	

}
