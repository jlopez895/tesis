package ar.edu.iua.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Producto", description="Modelo de producto a cargar")
@Entity
@Table(name = "productos")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(notes="Identificador del producto, clave autogenerada", required=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ApiModelProperty(notes="Nombre del producto", required=true)
	@Column(nullable = false)
	private String nombre;

	@ApiModelProperty(notes="Descripcion del producto", required=false)
	@Column()
	private String descripcion;

	@ApiModelProperty(notes="Codigo externo de integracion. Clave candidata", required=true)
	@Column(length = 50, nullable = true, unique = true)
	private String codigoexterno;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigoexterno() {
		return codigoexterno;
	}

	public void setCodigoexterno(String codigoexterno) {
		this.codigoexterno = codigoexterno;
	}

	public String checkBasicData() {
		if (getNombre().equals(null) || getNombre().trim().length() == 0)
			return "El nombre del producto es obligatorio";

		return null;

	}

	public Producto(Producto producto) {
		this.codigoexterno = producto.getCodigoexterno();
		this.nombre = producto.getNombre();
		this.descripcion = producto.getDescripcion();
	}

	public Producto() {

	}

}