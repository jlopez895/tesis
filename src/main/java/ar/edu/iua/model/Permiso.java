package ar.edu.iua.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permiso {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String permiso;

    @Column(length = 250, nullable = true)
    private String descripcion;
    
    @Column()
    private Integer idRolAsoc;
    
    @Column()
    private boolean hasMinisterios;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPermiso() {
		return permiso;
	}

	public void setPermiso(String permiso) {
		this.permiso = permiso;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Permiso(Integer id, String permiso, String descripcion) {
		super();
		this.id = id;
		this.permiso = permiso;
		this.descripcion = descripcion;
	}
    
	   // Constructors
    public Permiso() {
        super();
    }

	public Integer getIdRolAsoc() {
		return idRolAsoc;
	}

	public void setIdRolAsoc(Integer idRolAsoc) {
		this.idRolAsoc = idRolAsoc;
	}

	public boolean isHasMinisterios() {
		return hasMinisterios;
	}

	public void setHasMinisterios(boolean hasMinisterios) {
		this.hasMinisterios = hasMinisterios;
	}

    
}
