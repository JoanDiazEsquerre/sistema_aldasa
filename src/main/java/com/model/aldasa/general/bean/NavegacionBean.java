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
	private int idAsistentenAdmin = Perfiles.ASISTENTE_ADMINISTRATIVO.getId();
	
	private boolean menuProspeccion, menuProyecto, menuMantenimiento,menuReporte;
	private boolean subMenuReporteLotes, subMenuEmpleado, subMenuComision, subMenuComisiones, subMenuManzanas, subMenuLotes, subMenuProspectos, subMenuProspeccion,subMenuAgenda, subMenuSimulador, subMenuPersonas,subMenuUsuarios,subMenuPerfiles, 
					subMenuProyectos,subMenuEquipos,subMenuCambiarContrasenia, subMenuReporteAcciones;
	
	private int[] permisoProspectos= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoProspeccion= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoAgenda= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoSimulador= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoPersonas= {idAdministrador,idAsistentenAdmin};
	private int[] permisoUsuarios= {idAdministrador};
	private int[] permisoPerfiles= {idAdministrador};
	private int[] permisoProyectos= {idAdministrador,idAsistentenAdmin};
	private int[] permisoEquipos= {idAdministrador};
	private int[] permisoCambiarConstrasenia= {idAdministrador,idSupervisor,idAsesor, idAsistentenAdmin};
	private int[] permisoReporteAcciones= {idAdministrador,idSupervisor,idAsesor,idAsistentenAdmin};
	private int[] permisoManzanas= {idAdministrador,idAsistentenAdmin};
	private int[] permisoLotes= {idAdministrador,idSupervisor,idAsesor,idAsistentenAdmin};
	private int[] permisoComisiones= {idAdministrador};
	private int[] permisoComision= {idAdministrador};
	private int[] permisoEmpleado= {idAdministrador};
	private int[] permisoReporteLotes= {idAdministrador};


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
		subMenuProyectos= validaPermiso(permisoProyectos);
		subMenuManzanas = validaPermiso(permisoManzanas);
		subMenuLotes= validaPermiso(permisoLotes);
		subMenuComisiones= validaPermiso(permisoComisiones);
				
		if(subMenuManzanas || subMenuLotes || subMenuProyectos || subMenuComisiones){
			menuProyecto=true;
		}
		
		//******************************************************************************
		subMenuPersonas = validaPermiso(permisoPersonas);
		subMenuUsuarios= validaPermiso(permisoUsuarios);
		subMenuPerfiles= validaPermiso(permisoPerfiles);
		subMenuEquipos= validaPermiso(permisoEquipos);
		subMenuEmpleado= validaPermiso(permisoEmpleado);
		subMenuComision= validaPermiso(permisoComision);
		subMenuCambiarContrasenia= validaPermiso(permisoCambiarConstrasenia);
		
		if(subMenuPersonas || subMenuUsuarios || subMenuPerfiles || subMenuEquipos || subMenuEmpleado || subMenuComision || subMenuCambiarContrasenia) {
			menuMantenimiento=true;
		}
		
		//*******************************************************************************
		subMenuReporteAcciones = validaPermiso(permisoReporteAcciones);
		subMenuReporteLotes = validaPermiso(permisoReporteLotes);
		
		if(subMenuReporteAcciones || subMenuReporteLotes) {
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
		public void getMantenimientoProjectPage() {
	        ruta = "modulos/proyecto/mantenimientos/project.xhtml";
	    }
		public void getProyectoManzanasPage() {
	        ruta = "modulos/proyecto/mantenimientos/manzana.xhtml";
	    }
		public void getProyectoLotesPage() {
	        ruta = "modulos/proyecto/mantenimientos/lote.xhtml";
	    }
		public void getProyectoComisionesPage() {
	        ruta = "modulos/proyecto/procesos/comisiones.xhtml";
	    }
		public void getProcesoReporteLotesPage() {
	        ruta = "modulos/proyecto/procesos/reporteLotes.xhtml";
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
		public void getMantenimientoTeamPage() {
	        ruta = "modulos/general/mantenimientos/team.xhtml";
	    }
		public void getMantenimientoEmpleadoPage() {
	        ruta = "modulos/general/mantenimientos/empleado.xhtml";
	    }
		public void getMantenimientoComisionPage() {
	        ruta = "modulos/general/mantenimientos/comision.xhtml";
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
	public int getIdAsistentenAdmin() {
		return idAsistentenAdmin;
	}
	public void setIdAsistentenAdmin(int idAsistentenAdmin) {
		this.idAsistentenAdmin = idAsistentenAdmin;
	}
	public boolean isSubMenuComisiones() {
		return subMenuComisiones;
	}
	public void setSubMenuComisiones(boolean subMenuComisiones) {
		this.subMenuComisiones = subMenuComisiones;
	}
	public int[] getPermisoComisiones() {
		return permisoComisiones;
	}
	public void setPermisoComisiones(int[] permisoComisiones) {
		this.permisoComisiones = permisoComisiones;
	}
	public boolean isSubMenuComision() {
		return subMenuComision;
	}
	public void setSubMenuComision(boolean subMenuComision) {
		this.subMenuComision = subMenuComision;
	}
	public int[] getPermisoComision() {
		return permisoComision;
	}
	public void setPermisoComision(int[] permisoComision) {
		this.permisoComision = permisoComision;
	}
	public boolean isSubMenuEmpleado() {
		return subMenuEmpleado;
	}
	public void setSubMenuEmpleado(boolean subMenuEmpleado) {
		this.subMenuEmpleado = subMenuEmpleado;
	}
	public int[] getPermisoEmpleado() {
		return permisoEmpleado;
	}
	public void setPermisoEmpleado(int[] permisoEmpleado) {
		this.permisoEmpleado = permisoEmpleado;
	}
	public boolean isSubMenuReporteLotes() {
		return subMenuReporteLotes;
	}
	public void setSubMenuReporteLotes(boolean subMenuReporteLotes) {
		this.subMenuReporteLotes = subMenuReporteLotes;
	}
	public int[] getPermisoReporteLotes() {
		return permisoReporteLotes;
	}
	public void setPermisoReporteLotes(int[] permisoReporteLotes) {
		this.permisoReporteLotes = permisoReporteLotes;
	}
	
	

}
