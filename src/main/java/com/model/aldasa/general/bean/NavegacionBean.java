package com.model.aldasa.general.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;

@Named
@Component
@ManagedBean
@SessionScoped
public class NavegacionBean { 
	@Autowired
	private UsuarioService usuarioService;
	
	private String ruta;
	private String username;
	private Usuario usuarioLogin = new Usuario();

	@PostConstruct
	public void init() {
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
//		System.out.println("AAA"+getUsername());
//		usuarioLogin = usuarioService.findByUsername(getUsername());
	}
	
	public void getProcesoProspeccionPage() {
       ruta="modulos/prospeccion/procesos/prospeccion.xhtml";
    }
	
	public void getProcesoAgendaPage() {
	       ruta="modulos/prospeccion/procesos/agenda.xhtml";
	    }
	
	public void getProspectosPage() {
	       ruta="modulos/prospeccion/mantenimientos/prospecto.xhtml";
	    }
	
	public void getProcesoReporteProspeccionPage() {
       ruta="modulos/prospeccion/procesos/reporteProspeccion.xhtml";
    }
	
	public void getMantenimientoPersonasPage() {
        ruta = "modulos/general/mantenimientos/personas.xhtml";
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
	
	
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(getUsername());
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

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	
	

}
