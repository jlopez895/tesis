package ar.edu.iua.model;

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
@Table(name="ministerio")
public class Ministerio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 80, nullable = false)
	private String nombre;

	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ministerios_roles", joinColumns = {
            @JoinColumn(name = "id_ministerio", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "id_rol", referencedColumnName = "id") })
    private Set<Rol> roles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public Ministerio(Integer id, String nombre, Set<Rol> roles) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.roles = roles;
	}
	   public Ministerio() {
	        // constructor sin argumentos
	    }

	

}
