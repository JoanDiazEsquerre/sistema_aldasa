package com.model.aldasa.general.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.TeamService;

@ManagedBean
@ViewScoped
public class TeamBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService; 
	
	private List<Team> listTeam;
	private List<Person> lstPerson;
	private Team teamSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarTeam();
		listarPersonas();
	}
	
	public void listarTeam (){
		listTeam=teamService.findByStatus(estado);
	}
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	public void newTeam() {
		tituloDialog="NUEVO EQUIPO";
		teamSelected=new Team();
		teamSelected.setStatus(true);
		teamSelected.setName("");
		listarPersonas();
		
	}
	
	public void modifyTeam( ) {
		tituloDialog="MODIFICAR EQUIPO";
		listarPersonas();
		
	}
	
	
	public void saveTeam() {
		if(teamSelected.getName().equals("") || teamSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre del equipo."));
			listarTeam();
			return ;
		} 
		if (tituloDialog.equals("NUEVO EQUIPO")) {
			Team validarExistencia = teamService.findByName(teamSelected.getName());
			if (validarExistencia == null) {
				teamService.save(teamSelected);
				newTeam();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarTeam();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El equipo ya existe."));
				listarTeam();
			}
		} else {
			Team validarExistencia = teamService.findByNameException(teamSelected.getName(), teamSelected.getId());
			if (validarExistencia == null) {
				teamService.save(teamSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarTeam();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El equipo ya existe."));
				listarTeam();
			}
		}
		
	}
	public Converter getConversorPersonSupervisor() {
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
	public List<Person> completePersonSupervisor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
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
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	
	public List<Team> getListTeam() {
		return listTeam;
	}

	public void setListTeam(List<Team> listTeam) {
		this.listTeam = listTeam;
	}

	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	

	
	
}
