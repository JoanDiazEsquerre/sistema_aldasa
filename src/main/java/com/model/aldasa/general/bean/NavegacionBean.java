package com.model.aldasa.general.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@ManagedBean
@SessionScoped
public class NavegacionBean { 
	
	private String ruta;
	private String username;

	@PostConstruct
	public void init() {

		
		
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
	}
	
	public void getProcesoProspeccionPage() {
       ruta="modulos/prospeccion/procesos/prospeccion.xhtml";
    }
	
	public void getProcesoReporteProspeccionPage() {
	       ruta="modulos/prospeccion/procesos/reporteProspeccion.xhtml";
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
	public void getMantenimientoProjectPage() {
        ruta = "modulos/general/mantenimientos/project.xhtml";
    }

	public String getRuta() {
		return ruta;
	}


	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getUsername() {
		if(SecurityContextHolder.getContext().getAuthentication()!=null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    username = ((UserDetails)principal).getUsername();
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	

}
