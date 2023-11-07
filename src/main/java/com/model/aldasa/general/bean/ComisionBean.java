package com.model.aldasa.general.bean;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.ComisionProyectoService;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ComisionBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{comisionProyectoService}")
	private ComisionProyectoService comisionProyectoService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	private LazyDataModel<Comision> lstComisionLazy;
	
	private List<Team> lstTeam;
	private List<MetaSupervisor> lstMetaSupervisor;
	private List<ComisionProyecto> lstComisionProyecto;
	private List<Project> lstProyecto;
	
	private Comision comisionSelected;
	private Team teamSelected;
	private MetaSupervisor metaSupervisorSelected;
	private ComisionProyecto comisionProyectoNew, comisionProyectoSelected;
	
	private String tituloDialog, anio;
	
	private Integer meta = null;
	
	private Date fechaIniFilter, fechaFinFilter;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy");
	
	@PostConstruct
	public void init() {
		iniciarComisionProyecto();
		anio=sdfY.format(new Date());
		getListaLazyComision();
		lstProyecto = projectService.findByStatusAndSucursal(true, navegacionBean.getSucursalLogin());
	}
	
	public void eliminarComisionProyecto(ComisionProyecto cp) {
		cp.setEstado(false);
		comisionProyectoService.save(cp);
		listarComisionProyecto();
		addInfoMessage("Se eliminó correctamente.");
	}
	
	public void saveComisionProyecto() {
		if(comisionProyectoNew.getProyecto()==null) {
			addErrorMessage("Seleccionar un Proyecto");
			return;
		}else {
			ComisionProyecto cp = comisionProyectoService.findByComisionAndProyectoAndEstado(comisionSelected, comisionProyectoNew.getProyecto(), true);
			if(cp!=null) {
				addErrorMessage("Ya se agregó el proyecto.");
				return;
			}
		}
		
		if(comisionProyectoNew.getInteresContado()==null || comisionProyectoNew.getInteresContado().compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("El interés al contado debe ser mayor que 0.");
			return;
		}
		
		if(comisionProyectoNew.getInteresCredito()==null || comisionProyectoNew.getInteresCredito().compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("El interés al crédito debe ser mayor que 0.");
			return;
		}
		
		ComisionProyecto save = comisionProyectoService.save(comisionProyectoNew);
		if(save!=null) {
			addInfoMessage("Se guardó correctamente.");
			iniciarComisionProyecto();
			listarComisionProyecto();
		}
	}
	
	public void iniciarComisionProyecto() {
		comisionProyectoNew = new ComisionProyecto();
		comisionProyectoNew.setEstado(true);
		comisionProyectoNew.setComision(comisionSelected); 
	}
	
	public void listarComisionProyecto() {
		iniciarComisionProyecto();
		lstComisionProyecto = comisionProyectoService.findByComisionAndEstado(comisionSelected, true);
	}
	
	public void getListaLazyComision() {
		try {
			fechaIniFilter = sdf2.parse(anio+"-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fechaFinFilter = sdf2.parse(anio+"-12-31");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iniciarLazy();
	}
	
	public void saveComision() {
		if(comisionSelected.getFechaIni()==null || comisionSelected.getFechaCierre()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos generales."));
			return ;
		}
		
		comisionSelected.setCodigo(sdfM.format(comisionSelected.getFechaIni())+""+sdfY2.format(comisionSelected.getFechaIni())); 
		
		if(comisionSelected.getComisionContado()==null || comisionSelected.getComisionCredito()==null || comisionSelected.getBasicoJunior()==null || comisionSelected.getBonoJunior()==null || comisionSelected.getBasicoSenior()==null || comisionSelected.getBonoSenior()==null || comisionSelected.getBasicoMaster()==null || comisionSelected.getBonoMaster()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del asesor."));
			return ;
		}
		if(comisionSelected.getComisionSupervisor()==null || comisionSelected.getComisionMetaSupervisor()==null || comisionSelected.getBonoMetaSupervisor()==null ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del supervisor."));
			return ;
		}
		if(comisionSelected.getMetaOnline()==null || comisionSelected.getPrimeraVentaContadoOnline()==null || comisionSelected.getPrimeraVentaCreditoOnline()==null || comisionSelected.getBonoContadoOnline()==null || comisionSelected.getBonoCreditoOnline()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos Online."));
			return ;
		}
		
		if(comisionSelected.getMetaAsesorExterno()==null || comisionSelected.getBasicoAsesorExterno()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos Externos."));
			return ;
		}
		
		if(comisionSelected.getSubgerente()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del subgerente."));
			return ;
		}
		
		if (tituloDialog.equals("NUEVA COMISIÓN")) {
			Comision validarExistencia = comisionService.findByEstadoAndCodigo(true, comisionSelected.getCodigo());
			if (validarExistencia == null) {
				comisionService.save(comisionSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				newComision();
			}else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya se programado una comision con el mismo rango de fechas."));
			}
		} else {
			Comision validarExistencia = comisionService.findByCodigoAndIdException(comisionSelected.getCodigo(), comisionSelected.getId());
			if (validarExistencia == null) {
				comisionService.save(comisionSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya se programado una comision con el mismo rango de fechas."));
			}
		}
		
	}

	public void saveMetaSupervisor() {
		if (teamSelected == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar equipo."));
			return ;
		}else if(teamSelected.getPersonSupervisor() == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El equipo no tiene supervisor."));
			return;
		}else {
			MetaSupervisor supervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comisionSelected, true, teamSelected.getPersonSupervisor());
			if(supervisor != null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El supervisor ya esta registrado."));
				return;
			}
		}
		if(meta == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar meta."));
			return ;
		}
		
		MetaSupervisor guardaMeta = new MetaSupervisor();
		guardaMeta.setComision(comisionSelected);
		guardaMeta.setPersonSupervisor(teamSelected.getPersonSupervisor());
		guardaMeta.setMeta(meta);
		guardaMeta.setEstado(true);
		
		MetaSupervisor guardar = metaSupervisorService.save(guardaMeta);
		 if (guardar != null) {
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				cargarTeam();
		 }else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar."));
		 }
		
	
		
	}
	
	public void newComision() {
		tituloDialog="NUEVA COMISIÓN";
		comisionSelected=new Comision();
		setfechaInicioFin();
		comisionSelected.setComisionContado(8);
		comisionSelected.setComisionCredito(4);
		comisionSelected.setBasicoJunior(new BigDecimal(1025.00));
		comisionSelected.setBonoJunior(new BigDecimal(200.00));
		comisionSelected.setBasicoSenior(new BigDecimal(1200.00));
		comisionSelected.setBonoSenior(new BigDecimal(300.00));
		comisionSelected.setBasicoMaster(new BigDecimal(1500.00));
		comisionSelected.setBonoMaster(new BigDecimal(500.00));
		comisionSelected.setComisionSupervisor(1);
		comisionSelected.setComisionMetaSupervisor(2); 
		comisionSelected.setBonoMetaSupervisor(new BigDecimal(500.00));
		comisionSelected.setSubgerente(1);
		comisionSelected.setMetaOnline(10);
		comisionSelected.setPrimeraVentaCreditoOnline(new BigDecimal(300.00));
		comisionSelected.setPrimeraVentaContadoOnline(new BigDecimal(500.00));
		comisionSelected.setBonoCreditoOnline(new BigDecimal(400.00)); 
		comisionSelected.setBonoContadoOnline(new BigDecimal(400.00));
		comisionSelected.setEstado(true);
		comisionSelected.setMetaAsesorExterno(5); 
		comisionSelected.setBasicoAsesorExterno(new BigDecimal(1025.00)); 
		comisionSelected.setComisionSupervisorOnline(new BigDecimal(0.5));
	}
	
	public void modifyComision( ) {
		tituloDialog="MODIFICAR COMISIÓN";
		
	}
	
	public void setfechaInicioFin() {
		try {
			Calendar calendar = Calendar.getInstance(); 

			//A la fecha actual le pongo el día 1
			calendar.set(Calendar.DAY_OF_MONTH,1);
			comisionSelected.setFechaIni(calendar.getTime());

			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			
			int ultimoDiaMes=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int mesActual = Integer.parseInt(sdfM.format(new Date())) ;
			int anioActual = Integer.parseInt(sdfY.format(new Date())) ;
			String fechaFinn=ultimoDiaMes + "/"+mesActual+"/"+anioActual;
			comisionSelected.setFechaCierre(sdf.parse(fechaFinn)); 
			
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}

	public void iniciarLazy() {

		lstComisionLazy = new LazyDataModel<Comision>() {
			private List<Comision> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Comision getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Comision comision : datasource) {
                    if (comision.getId() == intRowKey) {
                        return comision;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comision comision) {
                return String.valueOf(comision.getId());
            }

			@Override
			public List<Comision> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fechaIni").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }          
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<Comision> pageComision=null;
               
                
                pageComision= comisionService.findByEstadoAndFechaIniBetween(true, fechaIniFilter,fechaFinFilter, pageable);
                
                setRowCount((int) pageComision.getTotalElements());
                return datasource = pageComision.getContent();
            }
		};
	}
	
	public void cargarTeam(){
		lstTeam = teamService.findByStatus(true);
		teamSelected = null;
		meta = null;
		lstMetaSupervisor = metaSupervisorService.findByComisionAndEstado(comisionSelected, true);
	}
	
	public void eliminarMetaSupervisor() {
		metaSupervisorSelected.setEstado(false);
		MetaSupervisor meta = metaSupervisorService.save(metaSupervisorSelected);
		if(meta != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se eliminó correctamente."));
			cargarTeam();
		}
	
	}
	
	public Converter getConversorTeam() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Team c = null;
                    for (Team si : lstTeam) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Team) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProyecto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProyecto) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Project) value).getId() + "";
                }
            }
        };
    }
	

	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public Comision getComisionSelected() {
		return comisionSelected;
	}
	public void setComisionSelected(Comision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public LazyDataModel<Comision> getLstComisionLazy() {
		return lstComisionLazy;
	}
	public void setLstComisionLazy(LazyDataModel<Comision> lstComisionLazy) {
		this.lstComisionLazy = lstComisionLazy;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public Date getFechaIniFilter() {
		return fechaIniFilter;
	}
	public void setFechaIniFilter(Date fechaIniFilter) {
		this.fechaIniFilter = fechaIniFilter;
	}
	public Date getFechaFinFilter() {
		return fechaFinFilter;
	}
	public void setFechaFinFilter(Date fechaFinFilter) {
		this.fechaFinFilter = fechaFinFilter;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public Integer getMeta() {
		return meta;
	}
	public void setMeta(Integer meta) {
		this.meta = meta;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public List<MetaSupervisor> getLstMetaSupervisor() {
		return lstMetaSupervisor;
	}
	public void setLstMetaSupervisor(List<MetaSupervisor> lstMetaSupervisor) {
		this.lstMetaSupervisor = lstMetaSupervisor;
	}
	public MetaSupervisor getMetaSupervisorSelected() {
		return metaSupervisorSelected;
	}
	public void setMetaSupervisorSelected(MetaSupervisor metaSupervisorSelected) {
		this.metaSupervisorSelected = metaSupervisorSelected;
	}
	public ComisionProyectoService getComisionProyectoService() {
		return comisionProyectoService;
	}
	public void setComisionProyectoService(ComisionProyectoService comisionProyectoService) {
		this.comisionProyectoService = comisionProyectoService;
	}
	public List<ComisionProyecto> getLstComisionProyecto() {
		return lstComisionProyecto;
	}
	public void setLstComisionProyecto(List<ComisionProyecto> lstComisionProyecto) {
		this.lstComisionProyecto = lstComisionProyecto;
	}
	public List<Project> getLstProyecto() {
		return lstProyecto;
	}
	public void setLstProyecto(List<Project> lstProyecto) {
		this.lstProyecto = lstProyecto;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public ComisionProyecto getComisionProyectoNew() {
		return comisionProyectoNew;
	}
	public void setComisionProyectoNew(ComisionProyecto comisionProyectoNew) {
		this.comisionProyectoNew = comisionProyectoNew;
	}
	public ComisionProyecto getComisionProyectoSelected() {
		return comisionProyectoSelected;
	}
	public void setComisionProyectoSelected(ComisionProyecto comisionProyectoSelected) {
		this.comisionProyectoSelected = comisionProyectoSelected;
	}
	
}
