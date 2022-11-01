package com.model.aldasa.general.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProfileService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.Perfiles;

import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;

@Component
@ManagedBean
@SessionScoped
public class UserBean{
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private PersonService personService; 
	
	@Autowired
	private ProfileService profileService; 
	
	@Autowired
	private TeamService teamService; 
	
	@Autowired
	private ProspectService prospectService;
	
	@Autowired
	private ProspectionService prospectionService;
	
	private LazyDataModel<Usuario> lstUsuarioLazy;
	
	private List<Usuario> lstUsers;
	private List<Person> lstPerson;
	private List<Profile> lstProfile;
	private List<Team> lstTeam;
	
	private Usuario newUsuario;
	private Usuario userSelected;
	
	private boolean estado=true;
	private boolean validaUsuario;
	
	private String tituloDialog="";
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		listarPersonas();
		listarPerfiles();
		listarTeam();
	}
	
	public void onPageLoad(){
		if (!FacesContext.getCurrentInstance().isProcessingEvents()) {
			System.out.println("ok");
			
		}
		if (!FacesContext.getCurrentInstance().isPostback()) {
//			listarUsuarios();
			listarPersonas();
			listarPerfiles();
			listarTeam();
		}

	}
	
	
	public void iniciarLazy() {

		lstUsuarioLazy = new LazyDataModel<Usuario>() {
			private List<Usuario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Usuario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Usuario usuario : datasource) {
                    if (usuario.getId() == intRowKey) {
                        return usuario;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Usuario usuario) {
                return String.valueOf(usuario.getId());
            }

			@Override
			public List<Usuario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                //Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
                //Si debageas aqui te vas a dar cuenta como lo captura
                
                String username="%"+ (filterBy.get("username")!=null?filterBy.get("username").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String password="%"+ (filterBy.get("password")!=null?filterBy.get("password").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String personSurnames="%"+ (filterBy.get("person.surnames")!=null?filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
                String profileName="%"+ (filterBy.get("profile.name")!=null?filterBy.get("profile.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
//                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize);
                //Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE,
                //Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
                //Tu deberias preparar el metodo para cada filtro que tengas en la tabla
                Page<Usuario> pageUsuario=null;
                
                
                pageUsuario= usuarioService.findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus(profileName, personSurnames, password, username, estado, pageable);
                
                setRowCount((int) pageUsuario.getTotalElements());
                return datasource = pageUsuario.getContent();
            }
		};
	}
	
//	public void listarUsuarios() {
//		lstUsers=usuarioService.findByStatus(estado);
//	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void listarPerfiles() {
		lstProfile=profileService.findByStatus(true);
	}
	
	public void listarTeam() {
		lstTeam=teamService.findByStatus(true);
	}
	
	public void newUser() {
		tituloDialog = "NUEVO USUARIO";
		userSelected = new Usuario();
		userSelected.setStatus(true);
		
		listarPersonas();
	}
	
	public void updateUser() {
		tituloDialog = "MODIFICAR USUARIO";
		listarPersonas();
	}
	
	public boolean validarDatosUsuario(Usuario user) {
		boolean valor = true;
		
		if(user.getUsername().equals("") || user.getUsername()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombre de usuario."));
			return false ;
		}else {
			if(tituloDialog.equals("NUEVO USUARIO")) {
				Usuario buscaUsername = usuarioService.findByUsername(user.getUsername());
				if(buscaUsername!=null ) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe el nombre de usuario."));
					return false ;
				}
			}else {
				Usuario buscaUsername = usuarioService.findByUsernameException(user.getUsername(), user.getId());
				if(buscaUsername!=null ) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe el nombre de usuario."));
					return false ;
				}
			}
			
		}
		
		if(user.getPassword().equals("") || user.getPassword()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar contraseña."));
			return false ;
		}
		
		if(user.getPerson()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta asignar una persona."));
			return false ;
		}else {
			if(tituloDialog.equals("NUEVO USUARIO")) {
				Usuario buscarPorPersona =usuarioService.findByPerson(user.getPerson());
				if(buscarPorPersona!=null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La persona esta asignada en otro Usuario."));
					return false;
				}
			}else {
				Usuario buscaUsername = usuarioService.findByPersonException(user.getPerson().getId(), user.getId());
				if(buscaUsername!=null ) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La persona esta asignada en otro Usuario.."));
					return false ;
				}
			}
			
			
		}
		
		if(user.getProfile()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta asignar Perfil."));
			return false ;
		}
		
		return valor;
		
	}
	
	public void saveUpdateUser() {
		validaUsuario = validarDatosUsuario(userSelected);
		if(validaUsuario) {
			Usuario usu = usuarioService.save(userSelected); 
			if(usu==null) {
				validaUsuario=false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede guardar."));
			}else {
				
				
				List<Prospect> lstProspectAsesor = prospectService.findByPersonAssessor(usu.getPerson());		
				if(!lstProspectAsesor.isEmpty()) {
					for (Prospect prospect:lstProspectAsesor) {
						prospect.setPersonSupervisor(usu.getTeam().getPersonSupervisor());
						prospectService.save(prospect);
						
						List<Prospection> lstProspection = prospectionService.findByProspect(prospect);
						for (Prospection prospection:lstProspection) {
							if(prospection.getStatus().equals(EstadoProspeccion.EN_SEGUIMIENTO.getName())) {
								prospection.setPersonSupervisor(usu.getTeam().getPersonSupervisor());
								prospectionService.save(prospection);
							}
						}
						
					}
				}
				
				
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
				if(tituloDialog.equals("NUEVO USUARIO")) {
					newUser();
				}
			}
		}
		
	}
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
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
	
	public Converter getConversorProfile() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Profile c = null;
                    for (Profile si : lstProfile) {
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
                    return ((Profile) value).getId() + "";
                }
            }
        };
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

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public List<Usuario> getLstUsers() {
		return lstUsers;
	}
	public void setLstUsers(List<Usuario> lstUsers) {
		this.lstUsers = lstUsers;
	}
	public boolean getEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Usuario getUserSelected() {
		return userSelected;
	}
	public void setUserSelected(Usuario userSelected) {
		this.userSelected = userSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
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
	public ProfileService getProfileService() {
		return profileService;
	}
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	public List<Profile> getLstProfile() {
		return lstProfile;
	}
	public void setLstProfile(List<Profile> lstProfile) {
		this.lstProfile = lstProfile;
	}
	public boolean isValidaUsuario() {
		return validaUsuario;
	}
	public void setValidaUsuario(boolean validaUsuario) {
		this.validaUsuario = validaUsuario;
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

	public ProspectService getProspectService() {
		return prospectService;
	}

	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}

	public ProspectionService getProspectionService() {
		return prospectionService;
	}

	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}

	public LazyDataModel<Usuario> getLstUsuarioLazy() {
		return lstUsuarioLazy;
	}

	public void setLstUsuarioLazy(LazyDataModel<Usuario> lstUsuarioLazy) {
		this.lstUsuarioLazy = lstUsuarioLazy;
	}

	public Usuario getNewUsuario() {
		return newUsuario;
	}

	public void setNewUsuario(Usuario newUsuario) {
		this.newUsuario = newUsuario;
	}
	
	
	
}
