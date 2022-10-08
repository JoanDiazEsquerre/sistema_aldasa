package com.model.aldasa.general.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.prospeccion.bean.AgendaBean;
import com.model.aldasa.prospeccion.bean.ProspeccionBean;
import com.model.aldasa.prospeccion.bean.ProspectoBean;
import com.model.aldasa.prospeccion.bean.ReporteProspeccionBean;
import com.model.aldasa.service.UsuarioService;

@Named
@Component
@ManagedBean
@SessionScoped
public class NavegacionBean { 
	
	@Inject
	private ReporteProspeccionBean reporteProspeccionBean;
	
	@Inject
	private ProspeccionBean prospeccionBean;
	
	@Inject
	private AgendaBean agendaBean;
	
	@Inject
	private ProspectoBean prospectoBean;
	
	@Inject
	private PersonBean personBean;
	
	@Inject
	private UserBean userBean;
	
	@Inject
	private ProfileBean profileBean;
	
	@Inject
	private ProjectBean projectBean;
	
	@Inject
	private TeamBean teamBean;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private String ruta;
	private String username;
	private Usuario usuarioLogin = new Usuario();

	@PostConstruct
	public void init() {
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
		
	}
	
	public void getProcesoProspeccionPage() {
       ruta="modulos/prospeccion/procesos/prospeccion.xhtml";
       prospeccionBean.init();
    }
	
	public void getProcesoAgendaPage() {
       ruta="modulos/prospeccion/procesos/agenda.xhtml";
       agendaBean.init();
    }
	
	public void getProspectosPage() {
       ruta="modulos/prospeccion/mantenimientos/prospecto.xhtml";
       prospectoBean.init();
    }
	
	public void getProcesoReporteProspeccionPage() {
       ruta="modulos/prospeccion/procesos/reporteProspeccion.xhtml";
       reporteProspeccionBean.init();
    }
	
	public void getMantenimientoPersonasPage() {
        ruta = "modulos/general/mantenimientos/personas.xhtml";
        personBean.init();
    }
	
	public void getMantenimientoUsersPage() {
        ruta = "modulos/general/mantenimientos/users.xhtml";
        userBean.init();
    }

	public void getMantenimientoProfilePage() {
        ruta = "modulos/general/mantenimientos/profile.xhtml";
        profileBean.init();
    }
	public void getMantenimientoProjectPage() {
        ruta = "modulos/general/mantenimientos/project.xhtml";
        projectBean.init();
    }
	
	public void getMantenimientoTeamPage() {
        ruta = "modulos/general/mantenimientos/team.xhtml";
        teamBean.init();
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

	public ReporteProspeccionBean getReporteProspeccionBean() {
		return reporteProspeccionBean;
	}

	public void setReporteProspeccionBean(ReporteProspeccionBean reporteProspeccionBean) {
		this.reporteProspeccionBean = reporteProspeccionBean;
	}

	public ProspeccionBean getProspeccionBean() {
		return prospeccionBean;
	}

	public void setProspeccionBean(ProspeccionBean prospeccionBean) {
		this.prospeccionBean = prospeccionBean;
	}

	
	

}
