package ar.edu.iua.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@Column(length = 250, nullable = true)
	private String descripcion;
	
	@Column()
	private Date fecha;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "notificaciones_roles", joinColumns = {
			@JoinColumn(name = "id_notificacion", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "id_rol", referencedColumnName = "id") })
	private Set<Rol> roles;
	
	@Column(length = 250, nullable = true)
	private int leida;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public int getLeida() {
		return leida;
	}

	public void setLeida(int leida) {
		this.leida = leida;
	}

	
	

}
