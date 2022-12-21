package com.model.aldasa.proyecto.bean;


import java.io.Serializable;
import java.util.ArrayList;
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

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class LoteBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	private List<Lote> lstLotes;
	private List<Person> lstPerson;
	
	private StreamedContent file;
	
	private Person personAsesorSelected;
	private Lote loteSelected;
	private Team teamSelected;
	private Usuario usuarioLogin;
	
	private Project projectFilter;
	private Manzana manzanaFilter;
	private Manzana manzanaFilterMapeo;
	private Person personVenta;
	
	private String status = "";
	private String tituloDialog;
	private String nombreLoteSelected="";
	private boolean modificar = false;
	private int cantidadLotes=0;
	private Date fechaSeparacion, fechaVencimiento,fechaVendido;
	
	private LazyDataModel<Lote> lstLoteLazy;
	private List<Manzana> lstManzana = new ArrayList<>();
	private List<Project> lstProject = new ArrayList<>();
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor = new ArrayList<>();

	
	@PostConstruct
	public void init() {
		usuarioLogin = navegacionBean.getUsuarioLogin();
		if(usuarioLogin.getProfile().getId()==Perfiles.ADMINISTRADOR.getId() || usuarioLogin.getProfile().getId()==Perfiles.ASISTENTE_ADMINISTRATIVO.getId()) {
			modificar=true;
		}
		listarProject();
		listarManzanas();
		listarPersonas();
		iniciarLazy();
		cargarAsesorPorEquipo();
		lstTeam=teamService.findByStatus(true);

	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}

	public void newLote() {
		nombreLoteSelected="";
		tituloDialog="NUEVO LOTE";
		
		loteSelected=new Lote();
		loteSelected.setStatus("Disponible");
		
		listarManzanas();
		listarProject();
		listarPersonas();
	}
	
	public void modifyLote( ) {
		tituloDialog="MODIFICAR LOTE";
		nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+" / Lote: "+loteSelected.getNumberLote();
				
		fechaSeparacion = loteSelected.getFechaSeparacion();
		fechaVencimiento = loteSelected.getFechaVencimiento();
		fechaVendido = loteSelected.getFechaVendido();
		personVenta = loteSelected.getPersonVenta();
		
		
		Usuario usuarioAsesor = usuarioService.findByPerson(loteSelected.getPersonAssessor()); 
		if(usuarioAsesor != null) {
			teamSelected = usuarioAsesor.getTeam();
		}else {
			teamSelected = null;
		}
		
		
		cargarAsesorPorEquipo();
		personAsesorSelected = loteSelected.getPersonAssessor();

				
		listarManzanas();
		listarProject();
		listarPersonas();
	}
	
	public void changeCmbEstado() {
		if(loteSelected.getStatus().equals("Separado")) {
			if(fechaSeparacion == null) {
				fechaSeparacion = new Date(); 
				fechaVencimiento=sumarDiasAFecha(new Date(), 7);
			}else {
				
			}
		}
		
		if(loteSelected.getStatus().equals("Vendido")) {
			loteSelected.setTipoPago("Contado");
			if(fechaVendido == null) {
				fechaVendido=new Date();
			}
		}
		

	}
	
	public void calcularFechaVencimiento() {
		if(fechaSeparacion != null) {
			fechaVencimiento = sumarDiasAFecha(fechaSeparacion, 7);
		}
	}
	
	public Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	
	public void listarManzanas (){
		if(projectFilter == null) {
			lstManzana = manzanaService.findByStatusOrderByNameAsc(true);
		}else {
			lstManzana= manzanaService.findByProject(projectFilter.getId());
		}
		
		manzanaFilterMapeo = null;
		if(!lstManzana.isEmpty() && lstManzana != null) {
			manzanaFilterMapeo = lstManzana.get(0);
		}
		
	}
	
	public void listarProject(){
		lstProject= projectService.findByStatus(true);
	}
	
	public void listarLotes(){		
		lstLotes= loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter,manzanaFilterMapeo, "%%");
		cantidadLotes=0;
		if(!lstLotes.isEmpty()) {
			cantidadLotes = lstLotes.size();
		}
	}
	
	public void iniciarLazy() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
				String numberLote="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String manzana = "";
				if(manzanaFilter != null) {
					manzana = manzanaFilter.getName();
				}
				
				String proyecto = "%%";
				if(projectFilter != null) {
					proyecto = projectFilter.getName();
				}
				
                                
                Sort sort=Sort.by("numberLote").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
				Page<Lote> pageLote;
//				if(projectFilter.equals("")) {
//					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(numberLote,"%"+manzana+"%","%"+status+"%", pageable);
//				}else {
					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(numberLote, "%"+manzana+"%",proyecto,"%"+status+"%", pageable);
				
//				}
				setRowCount((int) pageLote.getTotalElements());
				return datasource = pageLote.getContent();
			}
		};
	}
	
	public void saveLote() {
		if(loteSelected.getNumberLote().equals("") || loteSelected.getNumberLote()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar número de lote."));
			return ;
		}
		
		if(loteSelected.getManzana()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una manzana."));
			return ;
		} 
		
		if(loteSelected.getProject()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar un proyecto."));
			return ;
		} 
		
		loteSelected.setFechaSeparacion(fechaSeparacion);
		loteSelected.setFechaVencimiento(fechaVencimiento);
		loteSelected.setPersonSupervisor(null);
		loteSelected.setPersonAssessor(null);
		loteSelected.setPersonVenta(null);
		loteSelected.setFechaVendido(null);


		if(loteSelected.getStatus().equals(EstadoLote.SEPARADO.getName())) {
			if(fechaSeparacion == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una fecha separación."));
				return ;
			}
			
			if(fechaVencimiento == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una fecha vencimiento."));
				return ;
			}
			
			if(teamSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar equipo."));
				return ;
			}else if (teamSelected != null) {
				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
			}
			
			if (personAsesorSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar asesor."));
				return ;
			}else if (personAsesorSelected != null) {
				loteSelected.setPersonAssessor(personAsesorSelected);
			}
		}

		if(loteSelected.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			if(personVenta == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (personVenta != null) {
				loteSelected.setPersonVenta(personVenta);
			}
			
			if(teamSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (teamSelected != null) {
				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
			}
			
			if (personAsesorSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (personAsesorSelected != null) {
				loteSelected.setPersonAssessor(personAsesorSelected);
			}
			
			if(fechaVendido == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (fechaVendido !=null) {
				loteSelected.setFechaVendido(fechaVendido);
			}
			
			if(loteSelected.getTipoPago().equals("Contado")) {
				loteSelected.setMontoInicial(null);
				loteSelected.setNumeroCuota(null);
				if (loteSelected.getMontoVenta() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
					return ;
				}
			}
			
			if(loteSelected.getTipoPago().equals("Crédito")) {
				if (loteSelected.getMontoVenta() == null || loteSelected.getMontoInicial() == null || loteSelected.getNumeroCuota() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
					return ;
				}
			}
		}
		
		//*********************************
		
		if (tituloDialog.equals("NUEVO LOTE")) {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProject(loteSelected.getNumberLote(), loteSelected.getManzana(), loteSelected.getProject());
			if (validarExistencia == null) {
				loteService.save(loteSelected);
				newLote();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				fechaSeparacion = null;
				fechaVencimiento = null;
				fechaVendido=null;
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
			}
		} else {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProjectException(loteSelected.getNumberLote(), loteSelected.getManzana().getId(), loteSelected.getProject().getId(), loteSelected.getId());
			if (validarExistencia == null) {
				loteService.save(loteSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+"/ lote: "+loteSelected.getNumberLote();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
			}
		}
		
	}
	
	public void calcularAreaPerimetro() {
		if(loteSelected.getMedidaFrontal() != null && loteSelected.getMedidaFrontal() >0) {
			if(loteSelected.getMedidaDerecha() != null && loteSelected.getMedidaDerecha() >0) {
				if(loteSelected.getMedidaIzquierda() != null && loteSelected.getMedidaIzquierda() >0) {
					if(loteSelected.getMedidaFondo() != null && loteSelected.getMedidaFondo() >0) {
						double area1 = (loteSelected.getMedidaFrontal()*loteSelected.getMedidaDerecha())/2;
						double area2 = (loteSelected.getMedidaIzquierda()*loteSelected.getMedidaFondo())/2;
						
						loteSelected.setArea(area1+area2);
						loteSelected.setPerimetro(loteSelected.getMedidaFrontal()+loteSelected.getMedidaDerecha()+loteSelected.getMedidaIzquierda()+loteSelected.getMedidaFondo());
					}
				}
			}
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
	
	public Converter getConversorManzana() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzana) {
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
                    return ((Manzana) value).getId() + "";
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
	
	public List<Manzana> completeManzana(String query) {
        List<Manzana> lista = new ArrayList<>();
        for (Manzana c : lstManzana) {
            if (c.getName().toUpperCase().contains(query.toUpperCase()) ) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public void fileDownloadView() {
        file = DefaultStreamedContent.builder()
                .name("proyecto"+projectFilter.getId()+".jpg")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/recursos/images/proyectos/proyecto"+projectFilter.getId()+".jpg"))
                .build();
    }
	
	public void cargarAsesorPorEquipo() {
		lstPersonAsesor = new ArrayList<>();
		List<Usuario> lstUsuarios = new ArrayList<>();
		
    	personAsesorSelected = null;
		if(teamSelected!= null) {
			lstUsuarios = usuarioService.findByTeam(teamSelected);
		}
		
		if(!lstUsuarios.isEmpty()){
			for(Usuario user : lstUsuarios) {
				lstPersonAsesor.add(user.getPerson());
			}
		}
	}
	
	public Converter getConversorPersonAsesor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Person c = null;
                    for (Person si : lstPersonAsesor) {
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
	
	public List<Person> completePersonAsesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAsesor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public boolean isModificar() {
		return modificar;
	}
	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}
	public String getNombreLoteSelected() {
		return nombreLoteSelected;
	}
	public void setNombreLoteSelected(String nombreLoteSelected) {
		this.nombreLoteSelected = nombreLoteSelected;
	}
	public Manzana getManzanaFilter() {
		return manzanaFilter;
	}
	public void setManzanaFilter(Manzana manzanaFilter) {
		this.manzanaFilter = manzanaFilter;
	}
	public List<Lote> getLstLotes() {
		return lstLotes;
	}
	public void setLstLotes(List<Lote> lstLotes) {
		this.lstLotes = lstLotes;
	}
	public StreamedContent getFile() {
		return file;
	}
	public void setFile(StreamedContent file) {
		this.file = file;
	}
	public Manzana getManzanaFilterMapeo() {
		return manzanaFilterMapeo;
	}
	public void setManzanaFilterMapeo(Manzana manzanaFilterMapeo) {
		this.manzanaFilterMapeo = manzanaFilterMapeo;
	}
	public int getCantidadLotes() {
		return cantidadLotes;
	}
	public void setCantidadLotes(int cantidadLotes) {
		this.cantidadLotes = cantidadLotes;
	}
	public Date getFechaSeparacion() {
		return fechaSeparacion;
	}
	public void setFechaSeparacion(Date fechaSeparacion) {
		this.fechaSeparacion = fechaSeparacion;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Date getFechaVendido() {
		return fechaVendido;
	}
	public void setFechaVendido(Date fechaVendido) {
		this.fechaVendido = fechaVendido;
	}

	public List<Person> getLstPerson() {
		return lstPerson;
	}

	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public Person getPersonVenta() {
		return personVenta;
	}

	public void setPersonVenta(Person personVenta) {
		this.personVenta = personVenta;
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

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Person getPersonAsesorSelected() {
		return personAsesorSelected;
	}

	public void setPersonAsesorSelected(Person personAsesorSelected) {
		this.personAsesorSelected = personAsesorSelected;
	}

	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}

	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
	}

	public TeamService getTeamService() {
		return teamService;
	}

	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	
	
}
