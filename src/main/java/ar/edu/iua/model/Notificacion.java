package ar.edu.iua.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column
	    private Integer tipo;

	    @Column
	    private Integer idAsoc;

	    @Column(length = 250, nullable = true)
	    private String descripcion;

	    @Column
	    private Date fecha;


		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getTipo() {
			return tipo;
		}

		public void setTipo(Integer tipo) {
			this.tipo = tipo;
		}

		public Integer getIdAsoc() {
			return idAsoc;
		}

		public void setIdAsoc(Integer idAsoc) {
			this.idAsoc = idAsoc;
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

}
