package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ClienteBean extends BaseBean implements Serializable{
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService; 
	
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Cliente> lstClienteLazy;
	
	private List<Person> lstPerson;
	
	private boolean estado = true;
	private boolean personaNaturalCliente;
	private String tituloDialog;
	private String razonSocialCliente;
	private String nombreComercialCliente;
	private String rucDniCliente;
	private String direccionCliente;
	private String email1Cliente;
	private String email2Cliente;
	private String email3Cliente;

	
	private Cliente clienteSelected;
	private Person personSelected;

	
	
	@PostConstruct
	public void init() {
		
	}
	
	public void modifyCliente( ) {
		tituloDialog="MODIFICAR CLIENTE";
	}
	
	public void newCliente() {
		tituloDialog="NUEVO CLIENTE";
		iniciarDatosCliente();

	}
	
	
	public void onChangePerson() {
        if(personSelected!=null) {
            razonSocialCliente=personSelected.getSurnames()+" "+ personSelected.getNames();
            nombreComercialCliente=personSelected.getSurnames()+" "+ personSelected.getNames();
            rucDniCliente=personSelected.getDni();
            direccionCliente=personSelected.getAddress();
            email1Cliente=personSelected.getEmail();;
            
        }else {
            iniciarDatosCliente();
        }
    }
    
    public void iniciarDatosCliente() {
        personaNaturalCliente = true;
        personSelected=null;
        razonSocialCliente = "";
        nombreComercialCliente = "";
        rucDniCliente = "";
        direccionCliente = "";
        email1Cliente = "";
        email2Cliente = "";
        email3Cliente = "";
    }
    
    public void saveCliente() {
        if(personSelected == null) {
            addErrorMessage("Debes seleccionar una persona o representante.");
            return;
        }
        
        if(razonSocialCliente.equals("")) {
            addErrorMessage("Debes ingresar la Razon Social.");
            return;
        }
        if(nombreComercialCliente.equals("")) {
            addErrorMessage("Debes ingresar un nombre Comercial.");
            return;
        }
        if(rucDniCliente.equals("")) {
            addErrorMessage("Debes ingresar RUC o DNI.");
            return;
        }
        
        
    }
	
	public void iniciarLazy() {

		lstClienteLazy = new LazyDataModel<Cliente>() {
			private List<Cliente> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Cliente getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Cliente cliente : datasource) {
                    if (cliente.getId() == intRowKey) {
                        return cliente;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Cliente cliente) {
                return String.valueOf(cliente.getId());
            }

			@Override
			public List<Cliente> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String razonSocial = "%" + (filterBy.get("razonSocial") != null ? filterBy.get("razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String nombreComercial = "%" + (filterBy.get("nombreComercial") != null ? filterBy.get("nombreComercial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String ruc = "%" + (filterBy.get("ruc") != null ? filterBy.get("ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("dni") != null ? filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";


                Sort sort=Sort.by("person.surnames").ascending();
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
               
                Page<Cliente> pageCliente=null;
               
                
                pageCliente= clienteService.findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(razonSocial, nombreComercial, ruc, dni, estado, pageable);
                
                setRowCount((int) pageCliente.getTotalElements());
                return datasource = pageCliente.getContent();
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
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPerson) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	

	public LazyDataModel<Cliente> getLstClienteLazy() {
		return lstClienteLazy;
	}
	public void setLstClienteLazy(LazyDataModel<Cliente> lstClienteLazy) {
		this.lstClienteLazy = lstClienteLazy;
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
	public Cliente getClienteSelected() {
		return clienteSelected;
	}
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public boolean isPersonaNaturalCliente() {
		return personaNaturalCliente;
	}
	public void setPersonaNaturalCliente(boolean personaNaturalCliente) {
		this.personaNaturalCliente = personaNaturalCliente;
	}
	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}
	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}
	public String getNombreComercialCliente() {
		return nombreComercialCliente;
	}
	public void setNombreComercialCliente(String nombreComercialCliente) {
		this.nombreComercialCliente = nombreComercialCliente;
	}
	public String getRucDniCliente() {
		return rucDniCliente;
	}
	public void setRucDniCliente(String rucDniCliente) {
		this.rucDniCliente = rucDniCliente;
	}
	public Person getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public String getEmail1Cliente() {
		return email1Cliente;
	}
	public void setEmail1Cliente(String email1Cliente) {
		this.email1Cliente = email1Cliente;
	}
	public String getEmail2Cliente() {
		return email2Cliente;
	}
	public void setEmail2Cliente(String email2Cliente) {
		this.email2Cliente = email2Cliente;
	}
	public String getEmail3Cliente() {
		return email3Cliente;
	}
	public void setEmail3Cliente(String email3Cliente) {
		this.email3Cliente = email3Cliente;
	}
	
	
	

}
