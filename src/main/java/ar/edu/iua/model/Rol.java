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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String rol;

    @Column(length = 250, nullable = true)
    private String descripcion;
    
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permisos", joinColumns = {
            @JoinColumn(name = "id_rol", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "id_permiso", referencedColumnName = "id") })
    private Set<Permiso> permisos;

    // Constructors
    public Rol() {
        super();
    }





	public Rol(Integer id, String rol, String descripcion, Set<Permiso> permisos) {
		super();
		this.id = id;
		this.rol = rol;
		this.descripcion = descripcion;
		this.permisos = permisos;

	}



	// Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

	public Set<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(Set<Permiso> permisos) {
		this.permisos = permisos;
	}
    
    
}
