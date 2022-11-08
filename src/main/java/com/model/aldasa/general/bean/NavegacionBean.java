package com.model.aldasa.general.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.context.SecurityContextHolder;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.Perfiles;

@ManagedBean(name = "navegacionBean")
@SessionScoped
public class NavegacionBean implements Serializable  { 
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	private String ruta;
	private String username;
	private Usuario usuarioLogin = new Usuario();
	
	private int idAdministrador = Perfiles.ADMINISTRADOR.getId();
	private int idSupervisor = Perfiles.SUPERVISOR.getId();
	private int idAsesor = Perfiles.ASESOR.getId();
	
	private boolean menuProspeccion, menuProyecto, menuMantenimiento,menuReporte;
	private boolean subMenuManzanas, subMenuLotes, subMenuProspectos, subMenuProspeccion,subMenuAgenda, subMenuSimulador, subMenuPersonas,subMenuUsuarios,subMenuPerfiles, 
					subMenuProyectos,subMenuEquipos,subMenuCambiarContrasenia, subMenuReporteAcciones;
	
	private int[] permisoProspectos= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoProspeccion= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoAgenda= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoSimulador= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoPersonas= {idAdministrador};
	private int[] permisoUsuarios= {idAdministrador};
	private int[] permisoPerfiles= {idAdministrador};
	private int[] permisoProyectos= {idAdministrador};
	private int[] permisoEquipos= {idAdministrador};
	private int[] permisoCambiarConstrasenia= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoReporteAcciones= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoManzanas= {idAdministrador};
	private int[] permisoLotes= {idAdministrador};

	@PostConstruct
	public void init() {
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
		username = SecurityContextHolder.getContext().getAuthentication().getName();
		usuarioLogin = usuarioService.findByUsername(username);
		permisoPantallas();
	}
	

	
	public void permisoPantallas() {
		menuProspeccion=false; menuProyecto=false; menuMantenimiento=false; menuReporte=false;
		
		//*******************************************************************************
		subMenuProspectos = validaPermiso(permisoProspectos);
		subMenuProspeccion= validaPermiso(permisoProspeccion);
		subMenuAgenda= validaPermiso(permisoAgenda);
		subMenuSimulador= validaPermiso(permisoSimulador);
		
		if(subMenuProspectos || subMenuProspeccion || subMenuAgenda || subMenuSimulador) {
			menuProspeccion=true;
		}
		//*******************************************************************************
		subMenuManzanas = validaPermiso(permisoManzanas);
		subMenuLotes= validaPermiso(permisoLotes);
				
		if(subMenuManzanas || subMenuLotes ){
			menuProyecto=true;
		}
		
		//******************************************************************************
		subMenuPersonas = validaPermiso(permisoPersonas);
		subMenuUsuarios= validaPermiso(permisoUsuarios);
		subMenuPerfiles= validaPermiso(permisoPerfiles);
		subMenuProyectos= validaPermiso(permisoProyectos);
		subMenuEquipos= validaPermiso(permisoEquipos);
		subMenuCambiarContrasenia= validaPermiso(permisoCambiarConstrasenia);
		
		if(subMenuPersonas || subMenuUsuarios || subMenuPerfiles || subMenuProyectos || subMenuEquipos || subMenuCambiarContrasenia) {
			menuMantenimiento=true;
		}
		
		//*******************************************************************************
		subMenuReporteAcciones = validaPermiso(permisoReporteAcciones);
		
		if(subMenuReporteAcciones) {
			menuReporte=true;
		}
		//*******************************************************************************
				
	}
	
	public boolean validaPermiso(int[] permiso) {
		boolean valida = false;
		for(int per:permiso) {
			if(per == usuarioLogin.getProfile().getId()) {
				valida= true;
			}
		}
		
		return valida;
	}
	
	
	public void getProcesoProspeccionPage() {
	       ruta="modulos/prospeccion/procesos/prospeccion.xhtml";
	    }
		
		public void getProcesoSimuladorPage() {
		       ruta="modulos/prospeccion/procesos/simulador.xhtml";
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
		
		public void getProyectoManzanasPage() {
	        ruta = "modulos/proyecto/mantenimientos/manzana.xhtml";
	    }
		
		public void getProyectoLotesPage() {
	        ruta = "modulos/proyecto/mantenimientos/lote.xhtml";
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
		
		public void getMantenimientoTeamPage() {
	        ruta = "modulos/general/mantenimientos/team.xhtml";
	    }
		
		public void getMantenimientoPasswordchangePage() {
	        ruta = "modulos/general/mantenimientos/passwordchange.xhtml";
	    }
	
	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
	

	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getUsername() {
		return this.username;
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

	public int getIdAdministrador() {
		return idAdministrador;
	}

	public void setIdAdministrador(int idAdministrador) {
		this.idAdministrador = idAdministrador;
	}

	public int getIdSupervisor() {
		return idSupervisor;
	}

	public void setIdSupervisor(int idSupervisor) {
		this.idSupervisor = idSupervisor;
	}

	public int getIdAsesor() {
		return idAsesor;
	}

	public void setIdAsesor(int idAsesor) {
		this.idAsesor = idAsesor;
	}

	public boolean isMenuProspeccion() {
		return menuProspeccion;
	}

	public void setMenuProspeccion(boolean menuProspeccion) {
		this.menuProspeccion = menuProspeccion;
	}

	public boolean isMenuMantenimiento() {
		return menuMantenimiento;
	}

	public void setMenuMantenimiento(boolean menuMantenimiento) {
		this.menuMantenimiento = menuMantenimiento;
	}

	public boolean isMenuReporte() {
		return menuReporte;
	}

	public void setMenuReporte(boolean menuReporte) {
		this.menuReporte = menuReporte;
	}

	public boolean isSubMenuProspectos() {
		return subMenuProspectos;
	}

	public void setSubMenuProspectos(boolean subMenuProspectos) {
		this.subMenuProspectos = subMenuProspectos;
	}

	public boolean isSubMenuProspeccion() {
		return subMenuProspeccion;
	}

	public void setSubMenuProspeccion(boolean subMenuProspeccion) {
		this.subMenuProspeccion = subMenuProspeccion;
	}

	public boolean isSubMenuAgenda() {
		return subMenuAgenda;
	}

	public void setSubMenuAgenda(boolean subMenuAgenda) {
		this.subMenuAgenda = subMenuAgenda;
	}

	public boolean isSubMenuPersonas() {
		return subMenuPersonas;
	}

	public void setSubMenuPersonas(boolean subMenuPersonas) {
		this.subMenuPersonas = subMenuPersonas;
	}

	public boolean isSubMenuUsuarios() {
		return subMenuUsuarios;
	}

	public void setSubMenuUsuarios(boolean subMenuUsuarios) {
		this.subMenuUsuarios = subMenuUsuarios;
	}

	public boolean isSubMenuPerfiles() {
		return subMenuPerfiles;
	}

	public void setSubMenuPerfiles(boolean subMenuPerfiles) {
		this.subMenuPerfiles = subMenuPerfiles;
	}

	public boolean isSubMenuProyectos() {
		return subMenuProyectos;
	}

	public void setSubMenuProyectos(boolean subMenuProyectos) {
		this.subMenuProyectos = subMenuProyectos;
	}

	public boolean isSubMenuEquipos() {
		return subMenuEquipos;
	}

	public void setSubMenuEquipos(boolean subMenuEquipos) {
		this.subMenuEquipos = subMenuEquipos;
	}

	public boolean isSubMenuCambiarContrasenia() {
		return subMenuCambiarContrasenia;
	}

	public void setSubMenuCambiarContrasenia(boolean subMenuCambiarContrasenia) {
		this.subMenuCambiarContrasenia = subMenuCambiarContrasenia;
	}

	public boolean isSubMenuReporteAcciones() {
		return subMenuReporteAcciones;
	}

	public void setSubMenuReporteAcciones(boolean subMenuReporteAcciones) {
		this.subMenuReporteAcciones = subMenuReporteAcciones;
	}

	public int[] getPermisoProspectos() {
		return permisoProspectos;
	}

	public void setPermisoProspectos(int[] permisoProspectos) {
		this.permisoProspectos = permisoProspectos;
	}

	public int[] getPermisoProspeccion() {
		return permisoProspeccion;
	}

	public void setPermisoProspeccion(int[] permisoProspeccion) {
		this.permisoProspeccion = permisoProspeccion;
	}

	public int[] getPermisoAgenda() {
		return permisoAgenda;
	}

	public void setPermisoAgenda(int[] permisoAgenda) {
		this.permisoAgenda = permisoAgenda;
	}

	public int[] getPermisoPersonas() {
		return permisoPersonas;
	}

	public void setPermisoPersonas(int[] permisoPersonas) {
		this.permisoPersonas = permisoPersonas;
	}

	public int[] getPermisoUsuarios() {
		return permisoUsuarios;
	}

	public void setPermisoUsuarios(int[] permisoUsuarios) {
		this.permisoUsuarios = permisoUsuarios;
	}

	public int[] getPermisoPerfiles() {
		return permisoPerfiles;
	}

	public void setPermisoPerfiles(int[] permisoPerfiles) {
		this.permisoPerfiles = permisoPerfiles;
	}

	public int[] getPermisoProyectos() {
		return permisoProyectos;
	}

	public void setPermisoProyectos(int[] permisoProyectos) {
		this.permisoProyectos = permisoProyectos;
	}

	public int[] getPermisoEquipos() {
		return permisoEquipos;
	}

	public void setPermisoEquipos(int[] permisoEquipos) {
		this.permisoEquipos = permisoEquipos;
	}

	public int[] getPermisoCambiarConstrasenia() {
		return permisoCambiarConstrasenia;
	}

	public void setPermisoCambiarConstrasenia(int[] permisoCambiarConstrasenia) {
		this.permisoCambiarConstrasenia = permisoCambiarConstrasenia;
	}

	public int[] getPermisoReporteAcciones() {
		return permisoReporteAcciones;
	}

	public void setPermisoReporteAcciones(int[] permisoReporteAcciones) {
		this.permisoReporteAcciones = permisoReporteAcciones;
	}

	public boolean isSubMenuSimulador() {
		return subMenuSimulador;
	}

	public void setSubMenuSimulador(boolean subMenuSimulador) {
		this.subMenuSimulador = subMenuSimulador;
	}

	public boolean isMenuProyecto() {
		return menuProyecto;
	}

	public void setMenuProyecto(boolean menuProyecto) {
		this.menuProyecto = menuProyecto;
	}

	public int[] getPermisoSimulador() {
		return permisoSimulador;
	}

	public void setPermisoSimulador(int[] permisoSimulador) {
		this.permisoSimulador = permisoSimulador;
	}

	public boolean isSubMenuManzanas() {
		return subMenuManzanas;
	}

	public void setSubMenuManzanas(boolean subMenuManzanas) {
		this.subMenuManzanas = subMenuManzanas;
	}

	public boolean isSubMenuLotes() {
		return subMenuLotes;
	}

	public void setSubMenuLotes(boolean subMenuLotes) {
		this.subMenuLotes = subMenuLotes;
	}

	public int[] getPermisoManzanas() {
		return permisoManzanas;
	}

	public void setPermisoManzanas(int[] permisoManzanas) {
		this.permisoManzanas = permisoManzanas;
	}

	public int[] getPermisoLotes() {
		return permisoLotes;
	}

	public void setPermisoLotes(int[] permisoLotes) {
		this.permisoLotes = permisoLotes;
	}

	

}
