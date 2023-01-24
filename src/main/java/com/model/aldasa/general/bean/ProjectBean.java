package com.model.aldasa.general.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Province;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProvinceService;

@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;

	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;

	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;
	
	private List<Project> listProject;
	private Project projectSelected;
	private boolean estado = true;
	
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;
	
	private List<Country> lstCountry;
	private List<Department> lstDepartment;
	private List<Province> lstProvince;
	private List<District> lstDistrict;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarProject();
		listarPais();
	}
	
	public void listarPais() {
		lstCountry = countryService.findAll();
	}

	public void listarDepartamentos() {
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();

		if (countrySelected != null) {
			lstDepartment = departmentService.findByCountry(countrySelected);
		}
	}

	public void listarProvincias() {
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();

		if (departmentSelected != null) {
			lstProvince = provinceService.findByDepartment(departmentSelected);
		}
	}

	public void listarDistritos() {
		lstDistrict = new ArrayList<>();

		if (provinceSelected != null) {
			lstDistrict = districtService.findByProvince(provinceSelected);
		}
	}
	
	public void listarProject (){
		listProject= projectService.findByStatus(estado);
	}
	
	public void newProject() {
		tituloDialog="NUEVO PROYECTO";
		projectSelected=new Project();
		projectSelected.setStatus(true);
		projectSelected.setName("");
		setearPais();
		
	}
	
	public void modifyProject( ) {
		tituloDialog="MODIFICAR PROYECTO";
		setearPais();
		if(projectSelected.getDistrict()!=null) {
			countrySelected = projectSelected.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = projectSelected.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = projectSelected.getDistrict().getProvince();
			listarDistritos();
			districtSelected = projectSelected.getDistrict();
		}
	}
	
	public void setearPais() {
		countrySelected = null;
		departmentSelected = null;
		provinceSelected = null;
		districtSelected = null;
	}
	
	
	public void saveProject() {
		if(projectSelected.getName().equals("") || projectSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre del proyecto."));
			listarProject();
			return ;
		} 
		
		if(projectSelected.getUbicacion().equals("") || projectSelected.getUbicacion()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar ubicación."));
			return ;
		} 
		
		if(projectSelected.getSector().equals("") || projectSelected.getSector()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar sector."));
			return ;
		} 
		
		if(projectSelected.getPredio().equals("") || projectSelected.getPredio()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar predio."));
			return ;
		} 
		
		if(projectSelected.getCodigoPredio().equals("") || projectSelected.getCodigoPredio()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar código de predio."));
			return ;
		} 
		
		if(projectSelected.getAreaHectarea().equals("") || projectSelected.getAreaHectarea()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar área / hectárea."));
			return ;
		} 
		
		if(projectSelected.getUnidadCatastral().equals("") || projectSelected.getUnidadCatastral()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar unidad catastral."));
			return ;
		} 
		
		if(projectSelected.getNumPartidaElectronica().equals("") || projectSelected.getNumPartidaElectronica()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar N° de partida electrónica."));
			return ;
		} 
		
		if(projectSelected.getZonaRegistral().equals("") || projectSelected.getZonaRegistral()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar zona registral."));
			return ;
		} 
		
		if(districtSelected==null ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar distrito."));
			return ;
		} else {
			projectSelected.setDistrict(districtSelected);
			
			}
		
		if (tituloDialog.equals("NUEVO PROYECTO")) {
			Project validarExistencia = projectService.findByName(projectSelected.getName());
			if (validarExistencia == null) {
				projectService.save(projectSelected);
				newProject();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProject();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El proyecto ya existe."));
				listarProject();
			}
		} else {
			Project validarExistencia = projectService.findByNameException(projectSelected.getName(), projectSelected.getId());
			if (validarExistencia == null) {
				projectService.save(projectSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProject();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El proyecto ya existe."));
				listarProject();
			}
		}
		
	}
	
	public Converter getConversorCountry() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Country c = null;
					for (Country si : lstCountry) {
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
					return ((Country) value).getId() + "";
				}
			}
		};
	}

	public Converter getConversorDepartment() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Department c = null;
					for (Department si : lstDepartment) {
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
					return ((Department) value).getId() + "";
				}
			}
		};
	}
	
	public Converter getConversorProvince() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Province c = null;
					for (Province si : lstProvince) {
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
					return ((Province) value).getId() + "";
				}
			}
		};
	}
	
	public Converter getConversorDistrict() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					District c = null;
					for (District si : lstDistrict) {
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
					return ((District) value).getId() + "";
				}
			}
		};
	}
	
	
	
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public Project getProjectSelected() {
		return projectSelected;
	}
	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
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
	public void setListProject(List<Project> listProject) {
		this.listProject = listProject;
	}
	public List<Project> getListProject() {
		return listProject;
	}
	public Country getCountrySelected() {
		return countrySelected;
	}
	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}
	public Department getDepartmentSelected() {
		return departmentSelected;
	}
	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}
	public Province getProvinceSelected() {
		return provinceSelected;
	}
	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}
	public District getDistrictSelected() {
		return districtSelected;
	}
	public void setDistrictSelected(District districtSelected) {
		this.districtSelected = districtSelected;
	}
	public List<Country> getLstCountry() {
		return lstCountry;
	}
	public void setLstCountry(List<Country> lstCountry) {
		this.lstCountry = lstCountry;
	}
	public List<Department> getLstDepartment() {
		return lstDepartment;
	}
	public void setLstDepartment(List<Department> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}
	public List<Province> getLstProvince() {
		return lstProvince;
	}
	public void setLstProvince(List<Province> lstProvince) {
		this.lstProvince = lstProvince;
	}
	public List<District> getLstDistrict() {
		return lstDistrict;
	}
	public void setLstDistrict(List<District> lstDistrict) {
		this.lstDistrict = lstDistrict;
	}
	public CountryService getCountryService() {
		return countryService;
	}
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
	public DepartmentService getDepartmentService() {
		return departmentService;
	}
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	public ProvinceService getProvinceService() {
		return provinceService;
	}
	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
	}
	public DistrictService getDistrictService() {
		return districtService;
	}
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	

	
	
}
