package ar.edu.iua.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@ApiModel(value="Documento", description="Modelo de documento")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

//ESTE ES NUESTRO SISTEMA EXTERNO
public class Documento implements Serializable {

	private static final long serialVersionUID = 4551249436451185765L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column()
	private int tipo;
	@Column()
	private Date fecha;
	@Column()
	private String titulo;
	@Column()
	private String descripcion;
	@Column()
	private boolean esFinal;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_estimulo")
	private Estimulo estimulo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_usuario_creador")
	private User usuario;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_rol_destinatario")
	private Rol rol;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public int getTipo() {
		return tipo;
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEsFinal() {
		return esFinal;
	}


	public void setEsFinal(boolean esFinal) {
		this.esFinal = esFinal;
	}

	public Estimulo getEstimulo() {
		return estimulo;
	}

	public void setEstimulo(Estimulo estimulo) {
		this.estimulo = estimulo;
	}


	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}


	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	

	public Documento(long id, int tipo, Date fecha, String titulo, String descripcion, boolean esFinal,
			Estimulo estimulo, User usuario, Rol rol) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.fecha = fecha;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.esFinal = esFinal;
		this.estimulo = estimulo;
		this.usuario = usuario;
		this.rol = rol;
	}
	
	

	public Documento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String checkBasicData(Estimulo estimulo) {
		//if (orden.getEstado() == 3)
			//return "El camion esta lleno y la orden se cerró";
		if (estimulo.getEstado() != 1)
			return "El estímulo se encuentra cerrado";
		if (getTitulo() == null)
			return "El título es obligatorio";
		if (getDescripcion() == null)
			return "La descripcón es obligatoria";
		if (getTipo() == 0)
			return "Debe ingresar el tipo de documento";
		
		
		return "OK";

	}

}