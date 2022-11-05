package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Province;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProvinceService;

@ManagedBean
@ViewScoped
public class PersonBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;
	
	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;
	
	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;

	private LazyDataModel<Person> lstPersonsLazy;

	private Person personSelected,personNew;
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;

	private List<Country> lstCountry;
	private List<Department> lstDepartment;
	private List<Province> lstProvince;
	private List<District> lstDistrict;

	
	private Boolean estado = true;
	private String tituloDialog;

	@PostConstruct
	public void init() {
		listarCliente();
		listarPais();
	}
	
	public void listarPais() {
		lstCountry = countryService.findAll();
	}
	
	public void listarProvincias() {
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(departmentSelected !=null) {
			lstProvince = provinceService.findByDepartment(departmentSelected); 
		}
	}
	
	public void listarDepartamentos() {
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(countrySelected !=null) {
			lstDepartment = departmentService.findByCountry(countrySelected);
		}
	}
	public void listarDistritos() {
		lstDistrict = new ArrayList<>();
		
		if(provinceSelected!=null) {
			lstDistrict = districtService.findByProvince(provinceSelected); 
		}
	}
	
	public void limpiarDatosCuidades() {
		countrySelected=null;
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
	}

	public void listarCliente() {
//		listarPersonas();
		iniciarLazy();
	}
	
	public void iniciarLazy() {
		lstPersonsLazy = new LazyDataModel<Person>() {
			private List<Person> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Person getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Person person : datasource) {
                    if (person.getId() == intRowKey) {
                        return person;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Person person) {
                return String.valueOf(person.getId());
            }

			@Override
			public List<Person> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String dni="%"+ (filterBy.get("dni")!=null?filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String names="%"+ (filterBy.get("surnames")!=null?filterBy.get("surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				Pageable pageable = PageRequest.of(first/pageSize, pageSize);
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<Person> pagePerson= personService.findAllByDniLikeAndSurnamesLikeAndStatus(dni, names,estado,pageable);
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}
	
//	public void listarPersonas() {
//		lstPersons = personService.findByStatus(estado);
//	}

	public void newPerson() {
		tituloDialog = "NUEVA PERSONA";
		
		personNew = new Person();
		personNew.setStatus(true);
		
		limpiarDatosCuidades();
	}
	
	public void updatePerson() {
		tituloDialog = "MODIFICAR PERSONA";
		limpiarDatosCuidades();
		
		personNew = personSelected;
		
		if(personSelected.getDistrict()!=null) {
			countrySelected = personSelected.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = personSelected.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = personSelected.getDistrict().getProvince();
			listarDistritos();
			districtSelected = personSelected.getDistrict();
			
		}
		
		
	}
	

	public boolean validarDatosPersona(Person person) {
		boolean valor = true;
		
		if(person.getDni().equals("") || person.getDni()==null) {
			personNew.setDni("");
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar DNI."));
//			listarPersonas();
//			return false ;
		}else {
			if(tituloDialog.equals("NUEVA PERSONA")) {
				Person buscarPorDni = personService.findByDni(person.getDni());
				if(buscarPorDni!=null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El DNI ya existe."));
//					listarPersonas();
					return false;
				}
			}else {
				Person buscarPorDni = personService.findByDniException(person.getDni(),person.getId());
				if(buscarPorDni!=null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El DNI ya existe."));
//					listarPersonas();
					return false;
				}
			}
			
		}
		if(person.getSurnames().equals("") || person.getSurnames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Apellidos."));
//			listarPersonas();
			return false ;
		}
		if(person.getNames().equals("") || person.getNames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombres."));
//			listarPersonas();
			return false ;
		}
		if(person.getAddress().equals("") || person.getAddress()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar dirección."));
//			listarPersonas();
			return false ;
		}
		if(person.getPhone().equals("") || person.getPhone()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Teléfono."));
//			listarPersonas();
			return false ;
		}
		
		if(districtSelected==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta seleccionar un distrito."));
//			listarPersonas();
			return false ;
		} else {
			person.setDistrict(districtSelected);
		}
		
		return valor;
		
	}
	
	public void savePerson() {
		boolean valida = validarDatosPersona(personNew);
		
		if(valida) {
			Person per = personService.save(personNew); 
			if(per==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede guardar."));
			}else {
				
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
//				listarPersonas();
				if(tituloDialog.equals("NUEVA PERSONA")) {
					newPerson();
					countrySelected=null;
					lstDepartment.clear();
					lstProvince.clear();
					lstDistrict.clear();
				}
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

	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
//	public List<Person> getLstPersons() {
//		return lstPersons;
//	}
//	public void setLstPersons(List<Person> lstPersons) {
//		this.lstPersons = lstPersons;
//	}
	public Person getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<Person> getLstPersonsLazy() {
		return lstPersonsLazy;
	}
	public void setLstPersonsLazy(LazyDataModel<Person> lstPersonsLazy) {
		this.lstPersonsLazy = lstPersonsLazy;
	}
	


	public Person getPersonNew() {
		return personNew;
	}

	public void setPersonNew(Person personNew) {
		this.personNew = personNew;
	}

	/**/
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

	public Country getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public List<Country> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<Country> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
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

	public Department getDepartmentSelected() {
		return departmentSelected;
	}

	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}

	public ProvinceService getProvinceService() {
		return provinceService;
	}

	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
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

	public DistrictService getDistrictService() {
		return districtService;
	}

	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
	
	
}
