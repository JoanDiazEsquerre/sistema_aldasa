package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Profile;
import com.model.aldasa.service.ProfileService;

@ManagedBean
@ViewScoped
public class ProfileBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{profileService}")
	private ProfileService profileService;
	
	private LazyDataModel<Profile> lstProfileLazy;
	
	private Profile profileSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
	}
	
	public void newProfile() {
		tituloDialog="NUEVO PERFIL";
		profileSelected=new Profile();
		profileSelected.setStatus(true);
		profileSelected.setName("");
		
	}
	
	public void modifyProfile( ) {
		tituloDialog="MODIFICAR PERFIL";
		
	}
	
	
	public void saveProfile() {
		if(profileSelected.getName().equals("") || profileSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre del perfil."));
			
			return ;
		} 
		if (tituloDialog.equals("NUEVO PERFIL")) {
			Profile validarExistencia = profileService.findByName(profileSelected.getName());
			if (validarExistencia == null) {
				profileService.save(profileSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				newProfile();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El perfil ya existe."));
			}
		} else {
			Profile validarExistencia = profileService.findByNameException(profileSelected.getName(), profileSelected.getId());
			if (validarExistencia == null) {
				profileService.save(profileSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El perfil ya existe."));
			}
		}
		
	}
	
	public void iniciarLazy() {

		lstProfileLazy = new LazyDataModel<Profile>() {
			private List<Profile> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Profile getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Profile profile : datasource) {
                    if (profile.getId() == intRowKey) {
                        return profile;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Profile profile) {
                return String.valueOf(profile.getId());
            }

			@Override
			public List<Profile> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("name") != null ? filterBy.get("name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("name").ascending();
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
               
                Page<Profile> pageProfile=null;
               
                
                pageProfile= profileService.findByNameLikeAndStatus(names, estado, pageable);
                
                setRowCount((int) pageProfile.getTotalElements());
                return datasource = pageProfile.getContent();
            }
		};
	}

	
	public ProfileService getProfileService() {
		return profileService;
	}
	
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	public Profile getProfileSelected() {
		return profileSelected;
	}
	public void setProfileSelected(Profile profileSelected) {
		this.profileSelected = profileSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public LazyDataModel<Profile> getLstProfileLazy() {
		return lstProfileLazy;
	}
	public void setLstProfileLazy(LazyDataModel<Profile> lstProfileLazy) {
		this.lstProfileLazy = lstProfileLazy;
	}
	
	
	
}
