package ar.edu.iua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@ApiModel(value="Orden", description="Modelo de orden de carga ")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

public class Orden implements Serializable {

	private static final long serialVersionUID = 451621105748580924L;

	@ApiModelProperty(notes="Identificador de la orden, clave autogenerada", required=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ApiModelProperty(notes="Numero de la orden. Clave candidata", required=true)
	@Column(unique = true)
	private int numeroOrden;

	@ApiModelProperty(notes="Cantidad de kg que se debe cargar en el camion", required=true)
	@Column()
	private double preset;
	@ApiModelProperty(notes="Peso del camion vacio", required=true)
	@Column()
	private double pesajeInicial;
	@ApiModelProperty(notes="Peso del camion cargado", required=false)
	@Column()
	private double pesajeFinal;
	@ApiModelProperty(notes="Frecuencia de almacenamiento", required=false)
	@Column()
	private int frecuenciaAlmacenamiento;

	@ApiModelProperty(notes="Camion de la orden", required=true)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "camion_id")
	private Camion camion;
	@ApiModelProperty(notes="Cliente de la orden", required=true)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	@ApiModelProperty(notes="Chofer del camion de la orden", required=true)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "chofer_id")
	private Chofer chofer;
	@ApiModelProperty(notes="Producto de la orden", required=true)
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "producto_id")
	private Producto producto;

	// VA DEL 1 AL 4
	@ApiModelProperty(notes="Estado de la orden", required=false)
	@Column()
	private int estado;

	// DATOS QUE SE VAN ACTUALIZANDO SEGUN LA ORDEN DETALLE QUE SE RECIBE

	@ApiModelProperty(notes="Turno de carga", required=true)
	@Column(columnDefinition = "DATETIME")
	private Date turno;

	@ApiModelProperty(notes="Fecha/hora de realizacion del pesaje inicial", required=false)
	@Column(columnDefinition = "DATETIME")
	private Date fechaHoraPesajeInicial;

	@ApiModelProperty(notes="Fecha/hora del inicio de carga del camion", required=false)
	@Column(columnDefinition = "DATETIME")
	private Date fechaHoraInicioCarga;

	@ApiModelProperty(notes="Fecha/hora de finalizacion de carga del camion", required=false)
	@Column(columnDefinition = "DATETIME")
	private Date fechaHoraFinCarga;

	@ApiModelProperty(notes="Fecha/hora de realizacion del pesaje final", required=false)
	@Column(columnDefinition = "DATETIME")
	private Date fechaHoraPesajeFinal;

	@ApiModelProperty(notes="Password de 5 digitos. Generada aleatoriamente", required=false)
	@Column(length = 5)
	private int password;
	
	@OneToMany(targetEntity=Alarma.class, mappedBy="orden", fetch = FetchType.LAZY)
	@JsonBackReference
	@ApiModelProperty(notes = "Lista de todas las alarmas que se activaron durante esta orden", required = false)
	private List<Alarma> alarmasList;
	
	@ApiModelProperty(notes = "Bandera que indica si ya hay una alarma encendida, para no enviar multiples alarmas", required = false)
	private boolean tieneAlarmaEncendida = false;

	
	@Column()
	private double ultimaMasaAcumulada;
	@Column()
	private double ultimaDensidad;
	@Column()
	private double ultimaTemperatura;
	@Column()
	private double ultimoCaudal;

	@Column(columnDefinition = "DATETIME")
	private Date fechaHoraUltimoAlmacenamiento;

	@Column()
	private double promedioDensidad;
	@Column()
	private double promedioTemperatura;
	@Column()
	private double promedioCaudal;
	
	private float temperaturaMaxima = 80;

	public int getId() {
		return id;
	}

	public int getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(int numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public double getPreset() {
		return preset;
	}

	public void setPreset(double preset) {
		this.preset = preset;
	}

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

	public Camion getCamion() {
		return camion;
	}

	public void setCamion(Camion camion) {
		this.camion = camion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Chofer getChofer() {
		return chofer;
	}

	public void setChofer(Chofer chofer) {
		this.chofer = chofer;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public Date getTurno() {
		return turno;
	}

	public void setTurno(Date turno) {
		this.turno = turno;
	}
	
	

	public int getFrecuenciaAlmacenamiento() {
		return frecuenciaAlmacenamiento;
	}

	public void setFrecuenciaAlmacenamiento(int frecuenciaAlmacenamiento) {
		this.frecuenciaAlmacenamiento = frecuenciaAlmacenamiento;
	}

	public Date getFechaHoraPesajeInicial() {
		return fechaHoraPesajeInicial;
	}

	public void setFechaHoraPesajeInicial(Date fechaHoraPesajeInicial) {
		this.fechaHoraPesajeInicial = fechaHoraPesajeInicial;
	}

	public Date getFechaHoraInicioCarga() {
		return fechaHoraInicioCarga;
	}

	public void setFechaHoraInicioCarga(Date fechaHoraInicioCarga) {
		this.fechaHoraInicioCarga = fechaHoraInicioCarga;
	}

	public Date getFechaHoraFinCarga() {
		return fechaHoraFinCarga;
	}

	public void setFechaHoraFinCarga(Date fechaHoraFinCarga) {
		this.fechaHoraFinCarga = fechaHoraFinCarga;
	}

	public Date getFechaHoraPesajeFinal() {
		return fechaHoraPesajeFinal;
	}

	public void setFechaHoraPesajeFinal(Date fechaHoraPesajeFinal) {
		this.fechaHoraPesajeFinal = fechaHoraPesajeFinal;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public double getUltimaMasaAcumulada() {
		return ultimaMasaAcumulada;
	}

	public void setUltimaMasaAcumulada(double ultimaMasaAcumulada) {
		this.ultimaMasaAcumulada = ultimaMasaAcumulada;
	}

	public double getUltimaDensidad() {
		return ultimaDensidad;
	}

	public void setUltimaDensidad(double ultimaDensidad) {
		this.ultimaDensidad = ultimaDensidad;
	}

	public double getUltimaTemperatura() {
		return ultimaTemperatura;
	}

	public void setUltimaTemperatura(double ultimaTemperatura) {
		this.ultimaTemperatura = ultimaTemperatura;
	}

	public double getUltimoCaudal() {
		return ultimoCaudal;
	}

	public void setUltimoCaudal(double ultimoCaudal) {
		this.ultimoCaudal = ultimoCaudal;
	}

	public Date getFechaHoraUltimoAlmacenamiento() {
		return fechaHoraUltimoAlmacenamiento;
	}

	public void setFechaHoraUltimoAlmacenamiento(Date fechaHoraUltimoAlmacenamiento) {
		this.fechaHoraUltimoAlmacenamiento = fechaHoraUltimoAlmacenamiento;
	}

	public double getPromedioDensidad() {
		return promedioDensidad;
	}

	public void setPromedioDensidad(double promedioDensidad) {
		this.promedioDensidad = promedioDensidad;
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
	
	public boolean isTieneAlarmaEncendida() {
		return tieneAlarmaEncendida;
	}

	public void setTieneAlarmaEncendida(boolean tieneAlarmaEncendida) {
		this.tieneAlarmaEncendida = tieneAlarmaEncendida;
	}


	public String checkBasicDataStatusOne() {

		
		if ((getNumeroOrden() <= 0))
			return "El numero de orden es obligatorio";
		if (getTurno() == null)
			return "El atributo turno es obligatorio";
		if (getChofer() == null)
			return "El atributo chofer es obligatorio";
		if (getChofer().getCodigoexterno()==null || getChofer().getCodigoexterno().trim().length() == 0)
			return "El atributo chofer.codigoexterno es obligatorio";
		if (getChofer().getDni() == 0)
			return "El atributo chofer.dni es obligatorio";
		if (getCamion() == null)
			return "El atributo camion es obligatorio";
		if (getCamion().getCodigoexterno()==null || getCamion().getCodigoexterno().trim().length() == 0)
			return "El atributo camion.codigoexterno es obligatorio";
		if (getCamion().getPatente() == null || getCamion().getPatente().trim().length() == 0)
			return "El atributo camion.patente es obligatorio";
		if(getCamion().getCisternado() == null|| getCamion().getCisternado().length==0)
			return "El atributo camion.cisternado es obligatorio"; 
		if (getCliente() == null)
			return "El atributo cliente es obligatorio";
		if (getCliente().getCodigoexterno()==null || getCliente().getCodigoexterno().trim().length() == 0)
			return "El atributo cliente.codigoexterno es obligatorio";
		if (getCliente().getRazonSocial() == null || getCliente().getRazonSocial().trim().length() == 0)
			return "El atributo cliente.razonSocial es obligatorio";
		if (getProducto() == null)
			return "El atributo producto es obligatorio";
		if (getProducto().getCodigoexterno()==null || getProducto().getCodigoexterno().trim().length() == 0)
			return "El atributo producto.codigoexterno es obligatorio";
		if (getProducto().getNombre() == null || getProducto().getNombre().trim().length() == 0)
			return "El atributo producto.nombre es obligatorio";
		if ((getPreset() <= 0.0))
			return "El preset es obligatorio";

		return "Ok para estado 1";
	}

	public String checkBasicDataStatusTwo(int estado, Date turno) {

		String fechaActual = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String horaActual = new SimpleDateFormat("HH-00-00").format(new Date());
		String fechaTurno = new SimpleDateFormat("dd-MM-yyyy").format(turno);
		String horaTurno = new SimpleDateFormat("HH-00-00").format(turno);

		if (!fechaActual.equals(fechaTurno))
			return "El dia de carga no corresponde a la fecha del turno";
		if (!horaActual.equals(horaTurno))
			return "La hora de carga no corresponde a la hora del turno";
		if (estado != 1)
			return "Para realizar el pesaje inicial la orden debe estar en estado 1";
		if (getPesajeInicial() <= 0)
			return "El atributo pesaje es obligatorio";
		if(getPassword()==0 || String.valueOf(getPassword()).length()!=5)
			return "El atributo password es obligatorio";
		
		return "Ok para estado 2";

	}

	public String checkBasicDataStatusFour(Orden orden, int nroOrden) {

		if (getPesajeFinal() == 0)
			return "El atributo pesaje final es obligatorio";
		if (getPesajeFinal() < orden.getPesajeInicial()
				|| getPesajeFinal() < (orden.getPesajeInicial() + orden.getPreset()))
			return "Pesaje final invalido";
		if (orden.getEstado() != 3)
			return "Para realizar el pesaje final, la orden debe estar en estado 3";
		return "Ok para pesaje final";

	}
	
	public String checkBasicDataStatusThree(Orden orden) {
		
		if(orden.getPreset()!= orden.getUltimaMasaAcumulada())
			return "El camion aun no esta lleno"; 
		if(orden.getEstado()!=2)
			return "No se pueden cerrar ordenes con estado 1";
		return "Ok para cerrar orden";
	}

	public float getTemperaturaMaxima() {
		return temperaturaMaxima;
	}

	public void setTemperaturaMaxima(float temperaturaMaxima) {
		this.temperaturaMaxima = temperaturaMaxima;
	}
	

}