package ar.edu.iua.model.dto;

public class ConciliacionDTO {
	
	private double pesajeInicial; 
	private double pesajeFinal; 
	private double productoCargado;
	private double netoPorBalanza; 
	private double diferenciaBalCau; 
	private double promedioTemperatura; 
	private double promedioCaudal; 
	private double promedioDensidad;
	public double getPesajeInicial() {
		return pesajeInicial;
	}
	public void setPesajeInicial(double pesajeInicial) {
		this.pesajeInicial = pesajeInicial;
	}
	public double getPesajeFinal() {
		return pesajeFinal;
	}
	public void setPesajeFinal(double pesajeFinal) {
		this.pesajeFinal = pesajeFinal;
	}
	public double getProductoCargado() {
		return productoCargado;
	}
	public void setProductoCargado(double productoCargado) {
		this.productoCargado = productoCargado;
	}
	public double getNetoPorBalanza() {
		return netoPorBalanza;
	}
	public void setNetoPorBalanza(double netoPorBalanza) {
		this.netoPorBalanza = netoPorBalanza;
	}
	public double getDiferenciaBalCau() {
		return diferenciaBalCau;
	}
	public void setDiferenciaBalCau(double diferenciaBalCau) {
		this.diferenciaBalCau = diferenciaBalCau;
	}
	public double getPromedioTemperatura() {
		return promedioTemperatura;
	}
	public void setPromedioTemperatura(double promedioTemperatura) {
		this.promedioTemperatura = promedioTemperatura;
	}
	public double getPromedioCaudal() {
		return promedioCaudal;
	}
	public void setPromedioCaudal(double promedioCaudal) {
		this.promedioCaudal = promedioCaudal;
	}
	public double getPromedioDensidad() {
		return promedioDensidad;
	}
	public void setPromedioDensidad(double promedioDensidad) {
		this.promedioDensidad = promedioDensidad;
	}
	public ConciliacionDTO(double pesajeInicial, double pesajeFinal, double productoCargado, double netoPorBalanza,
			double diferenciaBalCau, double promedioTemperatura, double promedioCaudal, double promedioDensidad) {
		super();
		this.pesajeInicial = pesajeInicial;
		this.pesajeFinal = pesajeFinal;
		this.productoCargado = productoCargado;
		this.netoPorBalanza = netoPorBalanza;
		this.diferenciaBalCau = diferenciaBalCau;
		this.promedioTemperatura = promedioTemperatura;
		this.promedioCaudal = promedioCaudal;
		this.promedioDensidad = promedioDensidad;
	} 
	
	public ConciliacionDTO() {
		
	}
	@Override
	public String toString() {
		return "Pesaje Inicial:" + pesajeInicial + ", Pesaje Final:" + pesajeFinal + ", Producto Cargado:"
				+ productoCargado + ", Neto Por Balanza: " + netoPorBalanza + ", Diferencia entre balanza y caudalimetro: " + diferenciaBalCau
				+ ", Promedio Temperatura: " + promedioTemperatura + ", Promedio Caudal: " + promedioCaudal
				+ ", Promedio Densidad: " + promedioDensidad ;
	}
	
	

}
