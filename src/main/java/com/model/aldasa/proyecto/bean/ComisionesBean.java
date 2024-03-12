package com.model.aldasa.proyecto.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Person;
//import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.service.CargoService;
import com.model.aldasa.service.ComisionSupervisorService;
import com.model.aldasa.service.ComisionesService;
import com.model.aldasa.service.ConfiguracionComisionService;
import com.model.aldasa.service.DetalleComisionesService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ComisionesBean extends BaseBean implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{configuracionComisionService}")
	private ConfiguracionComisionService configuracionComisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{detalleComisionesService}")
	private DetalleComisionesService detalleComisionesService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{cargoService}")
	private CargoService cargoService;
	
	@ManagedProperty(value = "#{comisionSupervisorService}")
	private ComisionSupervisorService comisionSupervisorService; 
	
	private LazyDataModel<ComisionSupervisor> lstComisionSupervisorLazy;
	private LazyDataModel<Comisiones> lstComisionesLazy;
	
	private ConfiguracionComision configuracionComisionSelected;
	private ComisionSupervisor comisionSupervisorSelected;
	private Comisiones comisionesSelected;
	
	private BigDecimal porcentajeGeneral;
	
	private List<ConfiguracionComision> lstConfiguracionComision;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	@PostConstruct
	public void init() {
		cargarListaConfiguracion();
		
		iniciarLazyComisionSupervisor();
		iniciarComisionesLazy();
	}
	
	public void cargarListaConfiguracion() {
		lstConfiguracionComision = configuracionComisionService.findByEstadoOrderByCodigoDesc(true);
		configuracionComisionSelected = lstConfiguracionComision.get(0);
		
		calcularPorcentajeGeneral();
	}
	
	public BigDecimal calcularPorcentaje(ComisionSupervisor comisionSupervisor) {
		BigDecimal calculo = BigDecimal.ZERO;
		int num = comisionSupervisor.getNumVendido();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			calculo = multiplicado.divide(new BigDecimal(comisionSupervisor.getMeta()), 2, RoundingMode.HALF_UP);
		}
		return calculo;
	}
	
	public void calcularPorcentajeGeneral() {
		porcentajeGeneral = BigDecimal.ZERO;
		int num = configuracionComisionSelected.getNumVendidojv();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			porcentajeGeneral = multiplicado.divide(new BigDecimal(configuracionComisionSelected.getMetajv()), 2, RoundingMode.HALF_UP);
		}
	}
	
	public List<DetalleComisiones> getDetalleComisiones(Comisiones comisiones) {
		return detalleComisionesService.findByEstadoAndComisiones(true, comisiones);
	}
	
	public void iniciarLazyComisionSupervisor() {
		lstComisionSupervisorLazy = new LazyDataModel<ComisionSupervisor>() {
			private List<ComisionSupervisor> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public ComisionSupervisor getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (ComisionSupervisor comisiones : datasource) {
                    if (comisiones.getId() == intRowKey) {
                        return comisiones;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(ComisionSupervisor comisiones) {
                return String.valueOf(comisiones.getId());
            }

			@Override
			public List<ComisionSupervisor> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {				
				
                Sort sort=Sort.by("id").ascending();
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
                
				Page<ComisionSupervisor> pageComisiones = comisionSupervisorService.findByEstadoAndConfiguracionComision(true, configuracionComisionSelected, pageable);
			
					
				setRowCount((int) pageComisiones.getTotalElements());
				return datasource = pageComisiones.getContent();
			}
		};
	}
	
	public void iniciarComisionesLazy() {
		lstComisionesLazy = new LazyDataModel<Comisiones>() {
			private List<Comisiones> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Comisiones getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Comisiones plantillaVenta : datasource) {
                    if (plantillaVenta.getId() == intRowKey) {
                        return plantillaVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comisiones plantillaVenta) {
                return String.valueOf(plantillaVenta.getId());
            }

			@Override
			public List<Comisiones> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura

				
				 Sort sort=Sort.by("id").descending();
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

				Page<Comisiones> pagePlantillaVenta= comisionesService.findByEstadoAndComisionSupervisor(true, comisionSupervisorSelected, pageable);		
				
				setRowCount((int) pagePlantillaVenta.getTotalElements());
				return datasource = pagePlantillaVenta.getContent();
			}
		};
	}
	
	public void cambiarComision() {
		calcularPorcentajeGeneral();
//		lstComisiones = comisionesService.findByEstadoAndConfiguracionComision(true, comisionSelected);
//		lstPersonSupervisor = new ArrayList<>();
//		personSupervisorSelected=null;
//		
//		if(!lstComisiones.isEmpty()) {
//			for(Comisiones c : lstComisiones) {
//				// para listar supervisores
//				if(lstPersonSupervisor.isEmpty()) {
//					lstPersonSupervisor.add(c.getPersonSupervisor());
//				}else {
//					boolean encuentra=false;
//					for(Person p:lstPersonSupervisor) {
//						
//						if(p.getId().equals(c.getPersonSupervisor().getId())) { 
//							encuentra=true;
//						}
//					}
//					if(!encuentra) {
//						lstPersonSupervisor.add(c.getPersonSupervisor());
//					}
//				}
//			}
//			
//		}
//		
//		
//		for(Team equipo :lstTeam) {
//
//			if(lstPersonSupervisor.isEmpty()) { 
//				lstPersonSupervisor.add(equipo.getPersonSupervisor());
//			}else {
//				boolean encuentra=false;
//				for(Person p:lstPersonSupervisor) {
//					
//					if(p.getId().equals(equipo.getPersonSupervisor().getId())) { 
//						encuentra=true;
//					}
//				}
//				if(!encuentra) {
//					lstPersonSupervisor.add(equipo.getPersonSupervisor());
//				}
//			}
//			
//			
//		}
			
		
		
		
	}
	
	
	public Converter getConversorConfiguracionComision() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	ConfiguracionComision c = null;
                    for (ConfiguracionComision si : lstConfiguracionComision) {
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
                    return ((ConfiguracionComision) value).getId() + "";
                }
            }
        };
    }
	
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public ConfiguracionComisionService getConfiguracionComisionService() {
		return configuracionComisionService;
	}
	public void setConfiguracionComisionService(ConfiguracionComisionService configuracionComisionService) {
		this.configuracionComisionService = configuracionComisionService;
	}
//	public DetalleComisionesService getComisionesService() {
//		return comisionesService;
//	}
//	public void setComisionesService(DetalleComisionesService comisionesService) {
//		this.comisionesService = comisionesService;
//	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public CargoService getCargoService() {
		return cargoService;
	}
	public void setCargoService(CargoService cargoService) {
		this.cargoService = cargoService;
	}
	public ConfiguracionComision getConfiguracionComisionSelected() {
		return configuracionComisionSelected;
	}
	public void setConfiguracionComisionSelected(ConfiguracionComision configuracionComisionSelected) {
		this.configuracionComisionSelected = configuracionComisionSelected;
	}
	public List<ConfiguracionComision> getLstConfiguracionComision() {
		return lstConfiguracionComision;
	}
	public void setLstConfiguracionComision(List<ConfiguracionComision> lstConfiguracionComision) {
		this.lstConfiguracionComision = lstConfiguracionComision;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public ComisionSupervisorService getComisionSupervisorService() {
		return comisionSupervisorService;
	}
	public void setComisionSupervisorService(ComisionSupervisorService comisionSupervisorService) {
		this.comisionSupervisorService = comisionSupervisorService;
	}
	public LazyDataModel<ComisionSupervisor> getLstComisionSupervisorLazy() {
		return lstComisionSupervisorLazy;
	}
	public void setLstComisionSupervisorLazy(LazyDataModel<ComisionSupervisor> lstComisionSupervisorLazy) {
		this.lstComisionSupervisorLazy = lstComisionSupervisorLazy;
	}
	public ComisionSupervisor getComisionSupervisorSelected() {
		return comisionSupervisorSelected;
	}
	public void setComisionSupervisorSelected(ComisionSupervisor comisionSupervisorSelected) {
		this.comisionSupervisorSelected = comisionSupervisorSelected;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public LazyDataModel<Comisiones> getLstComisionesLazy() {
		return lstComisionesLazy;
	}
	public void setLstComisionesLazy(LazyDataModel<Comisiones> lstComisionesLazy) {
		this.lstComisionesLazy = lstComisionesLazy;
	}
	public DetalleComisionesService getDetalleComisionesService() {
		return detalleComisionesService;
	}
	public void setDetalleComisionesService(DetalleComisionesService detalleComisionesService) {
		this.detalleComisionesService = detalleComisionesService;
	}
	public Comisiones getComisionesSelected() {
		return comisionesSelected;
	}
	public void setComisionesSelected(Comisiones comisionesSelected) {
		this.comisionesSelected = comisionesSelected;
	}
	public BigDecimal getPorcentajeGeneral() {
		return porcentajeGeneral;
	}
	public void setPorcentajeGeneral(BigDecimal porcentajeGeneral) {
		this.porcentajeGeneral = porcentajeGeneral;
	}
	
	
		
}
