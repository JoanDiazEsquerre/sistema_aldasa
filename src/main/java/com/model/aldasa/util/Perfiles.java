package com.model.aldasa.util;

public enum Perfiles {
	
	ADMINISTRADOR("Administrador"),SUPERVISOR("Supervisor"),ASESOR("Asesor");
	
	private String name;
	
	private Perfiles(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

}
