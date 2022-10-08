package com.model.aldasa.prospeccion.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.Perfiles;


@Component
@ManagedBean
@SessionScoped
public class ReporteProspeccionBean {
	
	@Inject
	private NavegacionBean navegacionBean;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProspectService prospectService;
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProspectionDetailService prospectionDetailService;
	
	private Usuario usuarioLogin = new Usuario();
	
	private List<SelectItem> countriesGroup;
	private List<Prospect> lstProspect;
	private List<Person> lstPersonAssessor;
	private List<Action> lstActions;
	private List<Project> lstProject;
	private List<ProspectionDetail> lstProspectionDetailReporte;
	
	private Date fechaIni,fechaFin;
	private String origenContactoSelected;
	
	private Prospect prospectSelected;
	private Person personAssessorSelected;
	private Action actionSelected;
	private Project projectSelected;
	
	@PostConstruct
	public void init() {
		fechaIni = new Date() ;
		fechaFin = new Date() ;

		countriesGroup = new ArrayList<>();
        SelectItemGroup europeCountries = new SelectItemGroup("Redes Sociales");
        europeCountries.setSelectItems(new SelectItem[]{
            new SelectItem("WhatsAap", "WhatsApp"),
            new SelectItem("Facebook", "Facebook"),
            new SelectItem("Instagram", "Instagram"),
            new SelectItem("Google/ADS", "Google/ADS"),
            new SelectItem("Google/SEO", "Google/SEO")
        });
        countriesGroup.add(europeCountries);
        
        listarActions();
        listarProject();
	}
	
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());
		listarProspect();
		listarPersonasAssessor();
	}
	
	public void buscarReporte() {
		String idPerson="%%";
		String idPersonAssessor="";
		String idAction="";
		String originContact=origenContactoSelected;
		String idProject="";
		
		if(prospectSelected!=null) {
			idPerson= "%"+prospectSelected.getPerson().getId()+"%";
		}
		
		if(personAssessorSelected!=null) {
			idPersonAssessor= personAssessorSelected.getId()+"";
		}
		
		if(actionSelected!=null) {
			idAction= actionSelected.getId()+"";
		}
		
		if(projectSelected!=null) {
			idAction= projectSelected.getId()+"";
		}
		
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
//			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndProspectionProspectPersonIdLikeAndProspectionPersonAssessorIdLikeAndActionIdLikeAndProspectionOriginContactLikeAndProspectionProjectIdLikeAndDateBetween(false, idPerson, idPersonAssessor, idAction, originContact, idProject, fechaIni, fechaFin);
			lstProspectionDetailReporte = prospectionDetailService.findByProspectionStatusAndScheduledAndDateBetween(EstadoProspeccion.EN_SEGUIMIENTO.getName(),false, fechaIni,fechaFin);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {	
			lstProspectionDetailReporte = prospectionDetailService.findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(usuarioLogin.getPerson(), EstadoProspeccion.EN_SEGUIMIENTO.getName(),false,fechaIni,fechaFin);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(usuarioLogin.getPerson(),EstadoProspeccion.EN_SEGUIMIENTO.getName(),false,fechaIni,fechaFin);
		}
	}
	
	public void listarProspect() {		
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstProspect = prospectService.findAll();
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {	
			lstProspect = prospectService.findByPersonAssessor(usuarioLogin.getPerson());
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstProspect = prospectService.findByPersonSupervisor(usuarioLogin.getPerson());
		}
	}
	
	public void listarActions() {
		lstActions=actionService.findByStatus(true);
	}
	
	public void listarProject() {
		lstProject=projectService.findByStatus(true);
	}
	
	public void mensajeERROR(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void mensajeINFO(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void listarPersonasAssessor() {
		List<Usuario> lstUsersAssesor = new ArrayList<>();
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstUsersAssesor = usuarioService.findByProfileIdAndStatus(2, true);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {	
			lstUsersAssesor.add(usuarioLogin);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstUsersAssesor = usuarioService.findByTeamPersonSupervisorAndStatus(usuarioLogin.getPerson(), true);
		}
		
		lstPersonAssessor = new ArrayList<>();
		
		if(lstUsersAssesor!=null) {
			if(!lstUsersAssesor.isEmpty()) {
				for(Usuario ase :lstUsersAssesor ) {
					lstPersonAssessor.add(ase.getPerson());
				}
			}
		}
	}
	
	public Converter getConversorProspect() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Prospect c = null;
                    for (Prospect si : lstProspect) {
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
                    return ((Prospect) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPersonAssessor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPersonAssessor) {
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
                    return ((Person) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorAction() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Action c = null;
                    for (Action si : lstActions) {
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
                    return ((Action) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProject() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProject) {
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
	
	public List<Prospect> completeProspect(String query) {
        List<Prospect> lista = new ArrayList<>();
        for (Prospect c : lstProspect) {
            if (c.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getPerson().getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public List<Person> completePersonAssesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAssessor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	
	
	
	
	
	

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}

	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
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

	public List<SelectItem> getCountriesGroup() {
		return countriesGroup;
	}

	public void setCountriesGroup(List<SelectItem> countriesGroup) {
		this.countriesGroup = countriesGroup;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public ProspectService getProspectService() {
		return prospectService;
	}

	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}

	public List<Prospect> getLstProspect() {
		return lstProspect;
	}

	public void setLstProspect(List<Prospect> lstProspect) {
		this.lstProspect = lstProspect;
	}

	public Prospect getProspectSelected() {
		return prospectSelected;
	}

	public void setProspectSelected(Prospect prospectSelected) {
		this.prospectSelected = prospectSelected;
	}

	public List<Person> getLstPersonAssessor() {
		return lstPersonAssessor;
	}

	public void setLstPersonAssessor(List<Person> lstPersonAssessor) {
		this.lstPersonAssessor = lstPersonAssessor;
	}

	public Person getPersonAssessorSelected() {
		return personAssessorSelected;
	}

	public void setPersonAssessorSelected(Person personAssessorSelected) {
		this.personAssessorSelected = personAssessorSelected;
	}

	public Action getActionSelected() {
		return actionSelected;
	}

	public void setActionSelected(Action actionSelected) {
		this.actionSelected = actionSelected;
	}

	public ActionService getActionService() {
		return actionService;
	}

	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}

	public List<Action> getLstActions() {
		return lstActions;
	}

	public void setLstActions(List<Action> lstActions) {
		this.lstActions = lstActions;
	}

	public String getOrigenContactoSelected() {
		return origenContactoSelected;
	}

	public void setOrigenContactoSelected(String origenContactoSelected) {
		this.origenContactoSelected = origenContactoSelected;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public List<Project> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}

	public Project getProjectSelected() {
		return projectSelected;
	}

	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}

	public List<ProspectionDetail> getLstProspectionDetailReporte() {
		return lstProspectionDetailReporte;
	}

	public void setLstProspectionDetailReporte(List<ProspectionDetail> lstProspectionDetailReporte) {
		this.lstProspectionDetailReporte = lstProspectionDetailReporte;
	}
	
	

}
