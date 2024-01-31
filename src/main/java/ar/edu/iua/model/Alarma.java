package ar.edu.iua.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "alarmas")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@ApiModel(value = "Alarma", description = "Modelo de la alarma emitida ante un valor critico")

public class Alarma implements Serializable{

		private static final long serialVersionUID = -6185539901934211315L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@ApiModelProperty(notes = "Identificador de la alarma, generado automáticamente", required = true)
		private long idAlarma;
		
		@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
		@JoinColumn(name = "usuario_id")
		@ApiModelProperty(notes = "Usuario que aceptó la alarma", required = true)
		private User usuario;
		
		@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
		@JoinColumn(name = "orden_id")
		@ApiModelProperty(notes = "Orden en la que se generó la alarma", required = true)
		private Orden orden;
		
		@ApiModelProperty(notes = "Motivo por el que se generó la alarma", required = true)
		private String motivoAlarma;
		
		@ApiModelProperty(notes = "Fecha en la que se aceptó la alarma", required = true)
		private Date fechaAceptacion;

		

		public Alarma(long idAlarma, User usuario, Orden orden, String motivoAlarma, Date fechaAceptacion) {
			super();
			this.idAlarma = idAlarma;
			this.usuario = usuario;
			this.orden = orden;
			this.motivoAlarma = motivoAlarma;
			this.fechaAceptacion = fechaAceptacion;
		}

		public Alarma() {
			super();
		}

		public long getIdAlarma() {
			return idAlarma;
		}

		public void setIdAlarma(long idAlarma) {
			this.idAlarma = idAlarma;
		}

		public User getUsuarioQueAcepto() {
			return usuario;
		}

		public void setUsuarioQueAcepto(User usuario) {
			this.usuario = usuario;
		}

		public Date getFechaAceptacion() {
			return fechaAceptacion;
		}

		public void setFechaAceptacion(Date fechaAceptacion) {
			this.fechaAceptacion = fechaAceptacion;
		}

		public Orden getOrden() {
			return orden;
		}

		public void setOrden(Orden orden) {
			this.orden = orden;
		}

		public String getMotivoAlarma() {
			return motivoAlarma;
		}

		public void setMotivoAlarma(String motivoAlarma) {
			this.motivoAlarma = motivoAlarma;
		}
		
		

}
