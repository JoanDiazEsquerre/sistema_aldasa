package com.model.aldasa.util;

public enum Perfiles {
	
	ADMINISTRADOR("Administrador",1),SUPERVISOR("Supervisor",2),ASESOR("Asesor",3);
	
	private String name;
	private int id;
	
	private Perfiles(String name,int id) {
		this.name=name;
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

}
