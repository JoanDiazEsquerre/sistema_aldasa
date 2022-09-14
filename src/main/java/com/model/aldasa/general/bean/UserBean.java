package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProfileService;
import com.model.aldasa.service.UsuarioService;

import javax.faces.convert.Converter;

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
	
	private List<Usuario> lstUsers;
	private List<Person> lstPerson;
	private List<Profile> lstProfile;
	
	private Usuario userSelected;
	
	private boolean estado=true;
	private boolean validaUsuario;
	
	private String tituloDialog="";
	
	@PostConstruct
	public void init() {
		listarUsuarios();
		listarPersonas();
		listarPerfiles();
	}
	
	public void listarUsuarios() {
		lstUsers=usuarioService.findByStatus(estado);
	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void listarPerfiles() {
		lstProfile=profileService.findByStatus(true);
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
			Usuario buscaUsername = usuarioService.findByUsername(user.getUsername());
			if(buscaUsername!=null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya existe el nombre de usuario."));
				return false ;
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
			Usuario buscarPorPersona =usuarioService.findByPerson(userSelected.getPerson());
			if(buscarPorPersona!=null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La persona esta asignada en otro Usuario."));
				return false;
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
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
				listarUsuarios();
				if(tituloDialog.equals("NUEVO USUARIO")) {
					userSelected=new Usuario();
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
	
	
}
