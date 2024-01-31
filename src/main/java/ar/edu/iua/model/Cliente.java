package ar.edu.iua.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value="Cliente", description="Modelo de cliente")
@Entity
@Table(name = "clientes")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")

public class Cliente implements Serializable {

	private static final long serialVersionUID = 6736832429914967093L;

	@ApiModelProperty(notes="Identificador del cliente, clave autogenerada", required=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ApiModelProperty(notes="Razon social del cliente",required=true)
	@Column(nullable = false)
	private String razonSocial;

	@ApiModelProperty(notes="Telefono de contacto del cliente", required=true)
	@Column(nullable = false)
	private long contacto;

	@ApiModelProperty(notes="Lista de ordenes del cliente", required=false)
	@OneToMany(targetEntity = Orden.class, mappedBy = "cliente", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Orden> ordenList;

	@ApiModelProperty(notes="Codigo externo de integracion. Clave candidata", required=true)
	@Column(length = 50, nullable = true, unique = true)
	private String codigoexterno;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public long getContacto() {
		return contacto;
	}

	public void setContacto(long contacto) {
		this.contacto = contacto;
	}

	public List<Orden> getOrdenList() {
		return ordenList;
	}

	public void setOrdenList(List<Orden> ordenList) {
		this.ordenList = ordenList;
	}

	public String checkBasicData() {
		if (getRazonSocial().length() == 0)
			return "La razon social es un atributo obligatorio";

		if (getContacto() == 0)
			return "El contacto es un atributo obligatorio";

		return null;

	}

	public String getCodigoexterno() {
		return codigoexterno;
	}

	public void setCodigoexterno(String codigoexterno) {
		this.codigoexterno = codigoexterno;
	}

	public Cliente(Cliente cliente) {
		this.codigoexterno = cliente.getCodigoexterno();
		this.razonSocial = cliente.getRazonSocial();
		this.contacto = cliente.getContacto();
	}

	public Cliente() {

	}

}