package com.model.aldasa.general.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.fileupload.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.Perfiles;

import org.primefaces.PrimeFaces;

@ManagedBean(name = "navegacionBean")
@SessionScoped
public class NavegacionBean implements Serializable  { 
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	private String ruta;
	private String username;                              
	private Usuario usuarioLogin = new Usuario();
	private Sucursal sucursalLogin;
	
	private int idAdministrador = Perfiles.ADMINISTRADOR.getId();
	private int idSupervisor = Perfiles.SUPERVISOR.getId();
	private int idAsesor = Perfiles.ASESOR.getId();
	private int idAsistentenAdmin = Perfiles.ASISTENTE_ADMINISTRATIVO.getId();
	private int idAsistencia = Perfiles.ASISTENCIA.getId();
	private int idRecursosHumanos = Perfiles.RECURSOS_HUMANOS.getId();
	private int idContabilidad = Perfiles.CONTABILIDAD.getId();
	private int idAsistenteVenta = Perfiles.ASISTENTE_VENTA.getId();

	
	private boolean menuProspeccion, menuProyecto, menuMantenimiento,menuReporte, menuAsistencia, menuVentas;
	private boolean subMenuReporteLotes, subMenuEmpleado, subMenuComision, subMenuComisiones, subMenuManzanas, subMenuLotes, subMenuProspectos, subMenuProspeccion,subMenuAgenda, subMenuSimulador, subMenuPersonas,subMenuUsuarios,subMenuPerfiles, 
					subMenuProyectos,subMenuEquipos,subMenuCambiarContrasenia, subMenuReporteAcciones, subMenuAsistencia, subMenuReporteAsistencia, subMenuRequerimientoSeparacion, subMenuRankingVentas, subMenuContrato, subMenuDocumentoVenta;
	
	private int[] permisoProspectos= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoProspeccion= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoAgenda= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoSimulador= {idAdministrador,idSupervisor,idAsesor};
	private int[] permisoPersonas= {idAdministrador,idAsistentenAdmin, idAsistenteVenta};
	private int[] permisoUsuarios= {idAdministrador, idAsistentenAdmin, idAsistenteVenta};
	private int[] permisoPerfiles= {idAdministrador};
	private int[] permisoProyectos= {idAdministrador,idAsistentenAdmin,idAsistenteVenta};
	private int[] permisoEquipos= {idAdministrador};
	private int[] permisoCambiarConstrasenia= {idAdministrador,idSupervisor,idAsesor, idAsistentenAdmin,idAsistenteVenta,idRecursosHumanos, idContabilidad};
	private int[] permisoReporteAcciones= {idAdministrador,idSupervisor,idAsesor,idAsistentenAdmin, idAsistenteVenta};
	private int[] permisoManzanas= {idAdministrador,idAsistentenAdmin,idAsistenteVenta};
	private int[] permisoLotes= {idAdministrador,idSupervisor,idAsesor,idAsistentenAdmin,idAsistenteVenta};
	private int[] permisoComisiones= {idAdministrador};
	private int[] permisoComision= {idAdministrador};
	private int[] permisoEmpleado= {idAdministrador, idRecursosHumanos};
	private int[] permisoReporteLotes= {idAdministrador};
	private int[] permisoAsistencia= {idAdministrador, idAsistencia, idRecursosHumanos};
	private int[] permisoReporteAsistencia= {idAdministrador, idRecursosHumanos};
	private int[] permisoRequerimientoSeparacion= {idAdministrador,idAsistentenAdmin,idAsistenteVenta,idContabilidad};
	private int[] permisoRankingVentas= {idAdministrador};
	private int[] permisoContrato= {idAdministrador};
	private int[] permisoDocumentoVentas= {idAdministrador};

	@PostConstruct
	public void init() {
		sucursalLogin = (Sucursal)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sucursalLog"); 
		if(sucursalLogin==null) {
			cerrarSesion();
			return;
			
		}
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
		username = SecurityContextHolder.getContext().getAuthentication().getName();
		usuarioLogin = usuarioService.findByUsername(username);
		permisoPantallas();
	}
	

	
	public void permisoPantallas() {
		menuProspeccion=false; menuProyecto=false; menuMantenimiento=false; menuReporte=false; menuAsistencia=false;
		
		//*******************************************************************************
		subMenuProspectos = validaPermiso(permisoProspectos);
		subMenuProspeccion= validaPermiso(permisoProspeccion);
		subMenuAgenda= validaPermiso(permisoAgenda);
		subMenuSimulador= validaPermiso(permisoSimulador);
		subMenuRequerimientoSeparacion= validaPermiso(permisoRequerimientoSeparacion);
	
		if(subMenuProspectos || subMenuProspeccion || subMenuAgenda || subMenuSimulador || subMenuRequerimientoSeparacion) {
			menuProspeccion=true;
		}
		//*******************************************************************************
		subMenuProyectos= validaPermiso(permisoProyectos);
		subMenuManzanas = validaPermiso(permisoManzanas);
		subMenuLotes= validaPermiso(permisoLotes);
		subMenuComisiones= validaPermiso(permisoComisiones);
		subMenuContrato = validaPermiso(permisoContrato);
				
		if(subMenuManzanas || subMenuLotes || subMenuProyectos || subMenuComisiones || subMenuContrato){
			menuProyecto=true;
		}
		
		//******************************************************************************
		subMenuRankingVentas= validaPermiso(permisoRankingVentas);
		subMenuDocumentoVenta=validaPermiso(permisoDocumentoVentas);
				
		if(subMenuRankingVentas || subMenuDocumentoVenta){
			menuVentas=true;
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
		subMenuReporteAsistencia = validaPermiso(permisoReporteAsistencia);
		
		if(subMenuReporteAcciones || subMenuReporteLotes || subMenuReporteAsistencia) {
			menuReporte=true;
		}
		//*******************************************************************************
		
		subMenuAsistencia = validaPermiso(permisoAsistencia);

		if(subMenuAsistencia) {
			menuAsistencia=true;
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
		ruta = "modulos/prospeccion/procesos/prospeccion.xhtml";
	}

	public void getProcesoSimuladorPage() {
		ruta = "modulos/prospeccion/procesos/simulador.xhtml";
	}

	public void getProcesoAgendaPage() {
		ruta = "modulos/prospeccion/procesos/agenda.xhtml";
	}

	public void getProcesoRequerimientoSeparacionPage() {
		ruta = "modulos/prospeccion/procesos/requerimientoSeparacion.xhtml";
	}

	public void getProspectosPage() {
		ruta = "modulos/prospeccion/mantenimientos/prospecto.xhtml";
	}

	public void getProcesoReporteProspeccionPage() {
		ruta = "modulos/prospeccion/procesos/reporteProspeccion.xhtml";
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

	public void getAsistenciaAsistenciaPage() {
		ruta = "modulos/asistencia/mantenimientos/asistencia.xhtml";
	}

	public void getAsistenciaReporteAsistenciaPage() {
		ruta = "modulos/asistencia/procesos/reporteAsistencia.xhtml";
	}

	public void getVentasRankingVentasPage() {
		ruta = "modulos/ventas/mantenimientos/rankingVentas.xhtml";
	}
	
	public void getVentasDocumentoVentaPage() {
		ruta = "modulos/ventas/mantenimientos/documentoVenta.xhtml";
	}

	public void getContratosPage() {
		ruta = "modulos/proyecto/mantenimientos/contrato.xhtml";
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
	public boolean isMenuAsistencia() {
		return menuAsistencia;
	}
	public void setMenuAsistencia(boolean menuAsistencia) {
		this.menuAsistencia = menuAsistencia;
	}
	public boolean isSubMenuAsistencia() {
		return subMenuAsistencia;
	}
	public void setSubMenuAsistencia(boolean subMenuAsistencia) {
		this.subMenuAsistencia = subMenuAsistencia;
	}
	public int[] getPermisoAsistencia() {
		return permisoAsistencia;
	}
	public void setPermisoAsistencia(int[] permisoAsistencia) {
		this.permisoAsistencia = permisoAsistencia;
	}
	public boolean isSubMenuReporteAsistencia() {
		return subMenuReporteAsistencia;
	}
	public void setSubMenuReporteAsistencia(boolean subMenuReporteAsistencia) {
		this.subMenuReporteAsistencia = subMenuReporteAsistencia;
	}
	public int[] getPermisoReporteAsistencia() {
		return permisoReporteAsistencia;
	}
	public void setPermisoReporteAsistencia(int[] permisoReporteAsistencia) {
		this.permisoReporteAsistencia = permisoReporteAsistencia;
	}
	public int getIdAsistencia() {
		return idAsistencia;
	}
	public void setIdAsistencia(int idAsistencia) {
		this.idAsistencia = idAsistencia;
	}
	public int getIdRecursosHumanos() {
		return idRecursosHumanos;
	}
	public void setIdRecursosHumanos(int idRecursosHumanos) {
		this.idRecursosHumanos = idRecursosHumanos;
	}
	public boolean isSubMenuRequerimientoSeparacion() {
		return subMenuRequerimientoSeparacion;
	}
	public void setSubMenuRequerimientoSeparacion(boolean subMenuRequerimientoSeparacion) {
		this.subMenuRequerimientoSeparacion = subMenuRequerimientoSeparacion;
	}
	public int[] getPermisoRequerimientoSeparacion() {
		return permisoRequerimientoSeparacion;
	}
	public void setPermisoRequerimientoSeparacion(int[] permisoRequerimientoSeparacion) {
		this.permisoRequerimientoSeparacion = permisoRequerimientoSeparacion;
	}
	public boolean isMenuVentas() {
		return menuVentas;
	}
	public void setMenuVentas(boolean menuVentas) {
		this.menuVentas = menuVentas;
	}
	public boolean isSubMenuRankingVentas() {
		return subMenuRankingVentas;
	}
	public void setSubMenuRankingVentas(boolean subMenuRankingVentas) {
		this.subMenuRankingVentas = subMenuRankingVentas;
	}
	public int[] getPermisoRankingVentas() {
		return permisoRankingVentas;
	}
	public void setPermisoRankingVentas(int[] permisoRankingVentas) {
		this.permisoRankingVentas = permisoRankingVentas;
	}
	public int getIdContabilidad() {
		return idContabilidad;
	}
	public void setIdContabilidad(int idContabilidad) {
		this.idContabilidad = idContabilidad;
	}
	public boolean isSubMenuContrato() {
		return subMenuContrato;
	}
	public void setSubMenuContrato(boolean subMenuContrato) {
		this.subMenuContrato = subMenuContrato;
	}
	public Sucursal getSucursalLogin() {
		return sucursalLogin;
	}
	public void setSucursalLogin(Sucursal sucursalLogin) {
		this.sucursalLogin = sucursalLogin;
	}
	public int getIdAsistenteVenta() {
		return idAsistenteVenta;
	}
	public void setIdAsistenteVenta(int idAsistenteVenta) {
		this.idAsistenteVenta = idAsistenteVenta;
	}
	public int[] getPermisoContrato() {
		return permisoContrato;
	}
	public void setPermisoContrato(int[] permisoContrato) {
		this.permisoContrato = permisoContrato;
	}
	public boolean isSubMenuDocumentoVenta() {
		return subMenuDocumentoVenta;
	}
	public void setSubMenuDocumentoVenta(boolean subMenuDocumentoVenta) {
		this.subMenuDocumentoVenta = subMenuDocumentoVenta;
	}
	public int[] getPermisoDocumentoVentas() {
		return permisoDocumentoVentas;
	}
	public void setPermisoDocumentoVentas(int[] permisoDocumentoVentas) {
		this.permisoDocumentoVentas = permisoDocumentoVentas;
	}
	
	
}
