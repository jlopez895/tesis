package ar.edu.iua.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notificaciones_usuarios")
public class NotificacionUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_notificacion", nullable = false)
    private int idNotificacion;

    
    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;

    @Column(nullable = false)
    private boolean leida;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

    public int getIdNotificacion() {
		return idNotificacion;
	}

	public void setIdNotificacion(int idNotificacion) {
		this.idNotificacion = idNotificacion;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}

	public boolean isLeida() {
        return leida;
    }

    public void setLeido(boolean leida) {
        this.leida = leida;
    }
}