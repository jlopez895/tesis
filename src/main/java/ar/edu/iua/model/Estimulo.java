package ar.edu.iua.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@ApiModel(value="Estimulo", description="Modelo de estimulo")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

public class Estimulo implements Serializable {

	private static final long serialVersionUID = 451621105748580924L;

	@ApiModelProperty(notes="Identificador del estimulo, clave autogenerada", required=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ApiModelProperty(notes="titulo del estimulo", required=true)
	@Column()
	private String titulo;

	@ApiModelProperty(notes="descripcion del estimulo", required=true)
	@Column()
	private String descripcion;
	
	@ApiModelProperty(notes="Tiempo estimado de resolucion", required=true)
	@Column()
	private int tiempoEstmado;
	
	@ApiModelProperty(notes="Fecha de inicio", required=false)
	@Column()
	private Date fechaInicio;
	
	@ApiModelProperty(notes="Fecha fin", required=false)
	@Column()
	private Date fechaFin;
	
	@ApiModelProperty(notes="Fecha fin", required=false)
	@Column()
	private int estado;
	
	@ApiModelProperty(notes="Usuario creador", required=false)
	@Column()
	private int usuarioCreador;
	
	@ApiModelProperty(notes="Usuario finalizador", required=false)
	@Column()
	private int usuarioFinalizador;
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getTiempoEstmado() {
		return tiempoEstmado;
	}


	public void setTiempoEstmado(int tiempoEstmado) {
		this.tiempoEstmado = tiempoEstmado;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	

	public int getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(int usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	public int getUsuarioFinalizador() {
		return usuarioFinalizador;
	}

	public void setUsuarioFinalizador(int usuarioFinalizador) {
		this.usuarioFinalizador = usuarioFinalizador;
	}

	public Estimulo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Estimulo(int id, String titulo, String descripcion, int tiempoEstmado, Date fechaInicio, Date fechaFin,
			int estado, int usuarioCreador, int usuarioFinalizador) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.tiempoEstmado = tiempoEstmado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
		this.usuarioCreador = usuarioCreador;
		this.usuarioFinalizador = usuarioFinalizador;
	}

	public String checkBasicInfo() {
		if ((getTitulo() ==null))
			return "Debe ingresar el titulo del estímulo";
		if ((getDescripcion() ==null))
			return "Debe ingresar la descripción del estímulo";
		if (getTiempoEstmado() < 0)
			return "Debe ingresar el tiempo estimado de resolución";

		
		return "OK";
	}
	

	

}