package com.model.aldasa.prospeccion.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.UsuarioService;

@Component
@ManagedBean
@SessionScoped
public class ProspeccionBean {
	
	@Autowired
	private ProspectionService prospectionService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProspectionDetailService prospectionDetailService;
	
	@Autowired
	private ActionService actionService;
	
	private LazyDataModel<Prospection> lstProspectionLazy;
	
	private Prospection prospectionSelected;
	private Prospection prospectionNew;
	private ProspectionDetail prospectionDetailSelected;
	private ProspectionDetail prospectionDetailNew;
	
	private String status = "En seguimiento";
	private String titleDialog;
	
	private List<Person> lstPerson;
	private List<Person> lstPersonAssessor;
	private List<SelectItem> countriesGroup;
	private List<Project> lstProject;
	private List<ProspectionDetail> lstProspectionDetail = new ArrayList<>();
	private List<Action> lstActions;

	
	
	@PostConstruct
	public void init() {
		prospectionNew = new Prospection();
		
		iniciarLazy();
		listarPersonas();
		listarPersonasAssessor();
		listarProject();
		listarActions();
		
		countriesGroup = new ArrayList<>();
        SelectItemGroup europeCountries = new SelectItemGroup("Redes Sociales");
        europeCountries.setSelectItems(new SelectItem[]{
            new SelectItem("WhatsAap", "WhatsApp"),
            new SelectItem("Facebook", "Facebook"),
            new SelectItem("Instagram", "Instagram")
        });
        countriesGroup.add(europeCountries);
	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void listarProject() {
		lstProject=projectService.findByStatus(true);
	}
	
	public void listarActions() {
		lstActions=actionService.findByStatus(true);
	}
	
	public void listarPersonasAssessor() {
		List<Usuario> lstUsersAssesor = usuarioService.findByProfileIdAndStatus(2, true);
		lstPersonAssessor = new ArrayList<>();
		
		if(lstUsersAssesor!=null) {
			if(!lstUsersAssesor.isEmpty()) {
				for(Usuario ase :lstUsersAssesor ) {
					lstPersonAssessor.add(ase.getPerson());
				}
			}
		}
	}
	
	public void iniciarLazy() {
		lstProspectionLazy = new LazyDataModel<Prospection>() {
			private List<Prospection> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Prospection getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Prospection pros : datasource) {
                    if (pros.getId() == intRowKey) {
                        return pros;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Prospection prospection) {
                return String.valueOf(prospection.getId());
            }

			@Override
			public List<Prospection> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String originContact="%"+ (filterBy.get("originContact")!=null?filterBy.get("originContact").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String assessor="%"+ (filterBy.get("assessor.surnames")!=null?filterBy.get("assessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				Pageable pageable = PageRequest.of(first/pageSize, pageSize);
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<Prospection> pageProspection= prospectionService.findAllByOriginContactLikeAndAssessorSurnamesLikeAndStatus(originContact,assessor, status, pageable);
				setRowCount((int) pageProspection.getTotalElements());
				return datasource = pageProspection.getContent();
			}
		};
	}
	
	public void saveNewProspection() {
		if(prospectionNew.getPerson()==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione un prospecto."));
			return;
		}else {
			Prospection searchProspection = prospectionService.findByPersonIdAndStatus(prospectionNew.getPerson().getId(), "En seguimiento");
			if(searchProspection != null) {
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El prospecto esta en seguimiento por el asesor " + searchProspection.getAssessor().getNames()+" "+searchProspection.getAssessor().getSurnames()));
				return;
			}
		}
		
		if(prospectionNew.getAssessor()==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione un prospecto."));
			return;
		}
		
		if(prospectionNew.getDateStart()==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione un prospecto."));
			return;
		}
		
		prospectionNew.setDateRegister(new Date());
		prospectionNew.setStatus("En seguimiento");
		Prospection nuevo= prospectionService.save(prospectionNew);
		if(nuevo!=null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Se registro correctamente el prospecto "+ nuevo.getPerson().getSurnames()+" "+ nuevo.getPerson().getNames()));
			prospectionNew = new Prospection();
		}
	}
	
	public void modifyProspection() {
		titleDialog ="PROSPECTO: "+ prospectionSelected.getPerson().getSurnames()+" "+ prospectionSelected.getPerson().getNames();
		
		lstProspectionDetail = prospectionDetailService.findByProspection(prospectionSelected);
		
		
		
	}
	
	public void saveProspectionSelected() {
		if(prospectionSelected.getStatus().equals("En seguimiento")) {
			Prospection searchProspection = prospectionService.findByPersonIdAndStatus(prospectionSelected.getPerson().getId(), "En seguimiento");
			if(searchProspection != null) {
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El prospecto esta en seguimiento por el asesor " + searchProspection.getAssessor().getNames()+" "+searchProspection.getAssessor().getSurnames()));
				return;
			}
		}
		
		
		prospectionService.save(prospectionSelected);
		FacesContext.getCurrentInstance().addMessage("messages1", new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Se cambio correctamente el estado a: " + prospectionSelected.getStatus()));
		modifyProspection();
	}
	
	
	public Converter getConversorPerson() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPerson) {
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
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPerson) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
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
	
	
	
	
	public LazyDataModel<Prospection> getLstProspectionLazy() {
		return lstProspectionLazy;
	}

	public void setLstProspectionLazy(LazyDataModel<Prospection> lstProspectionLazy) {
		this.lstProspectionLazy = lstProspectionLazy;
	}

	public Prospection getProspectionSelected() {
		return prospectionSelected;
	}

	public void setProspectionSelected(Prospection prospectionSelected) {
		this.prospectionSelected = prospectionSelected;
	}

	public ProspectionService getProspectionService() {
		return prospectionService;
	}

	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public List<Person> getLstPerson() {
		return lstPerson;
	}

	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List<Person> getLstPersonAssessor() {
		return lstPersonAssessor;
	}

	public void setLstPersonAssessor(List<Person> lstPersonAssessor) {
		this.lstPersonAssessor = lstPersonAssessor;
	}

	public List<SelectItem> getCountriesGroup() {
		return countriesGroup;
	}

	public void setCountriesGroup(List<SelectItem> countriesGroup) {
		this.countriesGroup = countriesGroup;
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

	public Prospection getProspectionNew() {
		return prospectionNew;
	}

	public void setProspectionNew(Prospection prospectionNew) {
		this.prospectionNew = prospectionNew;
	}

	public String getTitleDialog() {
		return titleDialog;
	}

	public void setTitleDialog(String titleDialog) {
		this.titleDialog = titleDialog;
	}

	public List<ProspectionDetail> getLstProspectionDetail() {
		return lstProspectionDetail;
	}

	public void setLstProspectionDetail(List<ProspectionDetail> lstProspectionDetail) {
		this.lstProspectionDetail = lstProspectionDetail;
	}

	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}

	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}

	public ProspectionDetail getProspectionDetailSelected() {
		return prospectionDetailSelected;
	}

	public void setProspectionDetailSelected(ProspectionDetail prospectionDetailSelected) {
		this.prospectionDetailSelected = prospectionDetailSelected;
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

	public ProspectionDetail getProspectionDetailNew() {
		return prospectionDetailNew;
	}

	public void setProspectionDetailNew(ProspectionDetail prospectionDetailNew) {
		this.prospectionDetailNew = prospectionDetailNew;
	}

	
}
