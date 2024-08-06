package ar.edu.iua.model.dto;

import javax.persistence.Tuple;

public class Estadistica2DTO {

	private int label;
	private int value;

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Estadistica2DTO(int i, int value) {
		super();
		this.label = i;
		this.value = value;
	}


	public Estadistica2DTO() {
	}

}
