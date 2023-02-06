package com.model.aldasa.util;

public enum Perfiles {
	
	ADMINISTRADOR("Administrador",1),
	SUPERVISOR("Supervisor",3),
	ASESOR("Asesor",2), 
	CONTABILIDAD("Contabilidad", 7),
	ASISTENTE_ADMINISTRATIVO("Asistente Administrativo",4),
	ASISTENCIA("Asistencia",11),
	RECURSOS_HUMANOS("Recursos Humanos",6);
	
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
