package ar.edu.iua.model.dto;

import javax.persistence.Tuple;

public class EstadisticaDTO {

	private String label;
	private int value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public EstadisticaDTO(String i, int value) {
		super();
		this.label = i;
		this.value = value;
	}


	public EstadisticaDTO() {
	}

}
