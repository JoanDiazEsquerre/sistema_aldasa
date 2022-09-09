package com.model.aldasa.general.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Component;

@Component
@ManagedBean
@SessionScoped
public class NavegacionBean { 
	
	private String ruta;
	

	@PostConstruct
	public void init() {

		ruta = "modulos/prospeccion/mantenimientos/personas.xhtml";
	}
	
	public void getProcesoProspeccionPage() {
       ruta="modulos/prospeccion/procesos/prospeccion.xhtml";
    }
	
	public void getMantenimientoPersonasPage() {
        ruta = "modulos/prospeccion/mantenimientos/personas.xhtml";
    }
	
	public void getMantenimientoUsersPage() {
        ruta = "modulos/general/mantenimientos/users.xhtml";
    }

	public void getMantenimientoProfilePage() {
        ruta = "modulos/general/mantenimientos/profile.xhtml";
    }

	public String getRuta() {
		return ruta;
	}


	public void setRuta(String ruta) {
		this.ruta = ruta;
	}


	

}
