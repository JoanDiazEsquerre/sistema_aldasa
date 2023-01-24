package com.model.aldasa.util;

public enum EstadoRequerimientoSeparacionType {

	PENDIENTE("Pendiente"), APROBADO("Aprobado"), RECHAZADO("Rechazado");
	
	private String descripcion;

	private EstadoRequerimientoSeparacionType (String descripcion) {
		this.descripcion=descripcion;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	
	
}
