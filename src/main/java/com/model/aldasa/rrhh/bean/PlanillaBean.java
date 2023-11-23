package com.model.aldasa.rrhh.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.service.DetallePlanillaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.PlanillaService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class PlanillaBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private PlanillaService planillaService;
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{detallePlanillaService}")
	private DetallePlanillaService detallePlanillaService;
	
	private LazyDataModel<Planilla> lstPlanillaLazy;
	
	private Planilla planillaSelected;
	private Sucursal sucursal;
	private Planilla planillaNew;
	private Empleado empleado;
	
	private List<Sucursal> lstSucursal;
	private List<Empleado> lstEmpleadoCombo;
	private List<DetallePlanilla> lstDetallePlanillaTemp;
	private List<Empleado> lstEmpleadoTemp;
	
	private SelectItem[] cboMes;
    private SelectItem[] cboTipoPlanilla;
    
    private String tipoPlanilla , periodo, mes;
	private boolean estado = true;
	private boolean bloqueo;
	
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfDay = new SimpleDateFormat("MM");
	
	@PostConstruct
	public void init() {
		lstEmpleadoCombo = empleadoService.findByEstado(true);
		lstSucursal =sucursalService.findByEstado(true);
		crearFiltroMes();
		crearFiltroTipoPlanilla();
		
		bloquearPantalla();
		
		iniciarLazy();
	}
	
	public void bloquearPantalla() {
		bloqueo=false;
		planillaNew = planillaService.findByEstadoAndTemporal(true, true);
		
		if(planillaNew != null) {
			periodo= planillaNew.getPeriodo();
			mes = planillaNew.getMes();
			tipoPlanilla = planillaNew.getTipoPlanilla();
			sucursal = planillaNew.getSucursal();
			
			lstDetallePlanillaTemp = detallePlanillaService.findByEstadoAndPlanilla(estado, planillaNew);
			
			bloqueo=true;
		}else {
			periodo= sdfYear.format(new Date());
			mes = sdfDay.format(new Date());
			tipoPlanilla = "DEPENDIENTE";
			sucursal=null;
			
			listarDetallePlantillaTemporal();
			
			
		}
	}
	
	public void listarDetallePlantillaTemporal() {
		lstDetallePlanillaTemp = new ArrayList<>();
		lstEmpleadoTemp = new ArrayList<>();
		
		if(sucursal ==null) {
			lstEmpleadoTemp = empleadoService.findByEstadoAndPlanilla(true, tipoPlanilla == "DEPENDIENTE"? true: false);
			
		}else {
			lstEmpleadoTemp = empleadoService.findByEstadoAndPlanillaAndSucursal(true, tipoPlanilla == "DEPENDIENTE"? true: false, sucursal);
		}
		
		for(Empleado e : lstEmpleadoTemp) {
			DetallePlanilla dt  = new DetallePlanilla();
			dt.setEmpleado(e);
			dt.setTardanza(BigDecimal.ZERO);
			dt.setVacaciones(BigDecimal.ZERO);
			dt.setComisiones(BigDecimal.ZERO);
			dt.setBono(BigDecimal.ZERO);
			dt.setTotal(e.getSueldoBasico().subtract(dt.getTardanza()).add(dt.getVacaciones()).add(dt.getComisiones()).add(dt.getBono()));
			
			dt.setOnp(BigDecimal.ZERO);
			dt.setAporteObligatorio(BigDecimal.ZERO);
			dt.setPrimaSeguros(BigDecimal.ZERO);
			dt.setComisionVariable(BigDecimal.ZERO);
			
			
			if(tipoPlanilla == "DEPENDIENTE") {
				if(e.getFondoPension()!=null) {
					
					if(e.getFondoPension().getNombre().equals("ONP")) {
						
						dt.setOnp(dt.getTotal().multiply(e.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 2 , RoundingMode.HALF_UP))); 
					}else {
						dt.setAporteObligatorio(dt.getTotal().multiply(e.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 2 , RoundingMode.HALF_UP)));
						dt.setPrimaSeguros(dt.getTotal().multiply(e.getFondoPension().getPrimaSeguro()));
						dt.setComisionVariable(BigDecimal.ZERO);
						
					}
				}
			}
			
			dt.setRentaQuinta(BigDecimal.ZERO);
			dt.setDescMesAnterior(BigDecimal.ZERO);
			dt.setPagoVacTrunca(BigDecimal.ZERO);
			dt.setAdelanto(BigDecimal.ZERO);
			dt.setPrestamo(BigDecimal.ZERO);
			dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
			
			dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
			dt.setEsSalud(dt.getTotal().compareTo(new BigDecimal(1025)) == -1? new BigDecimal(92.25) : dt.getTotal().multiply( new BigDecimal(9).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))); 
			
			dt.setAbonado30(BigDecimal.ZERO);
			dt.setAbonado4(BigDecimal.ZERO); 

			
			lstDetallePlanillaTemp.add(dt);
			
			
		}
		
	}
	
	public void verDetalles() {
		System.out.println("aaaaaaaaaaaaaa");
	}
	
	private void crearFiltroMes() {
        cboMes = new SelectItem[13];
        cboMes[0] = new SelectItem("", "Todos");
        cboMes[1] = new SelectItem("1", "Enero");
        cboMes[2] = new SelectItem("2", "Febrero");
        cboMes[3] = new SelectItem("3", "Marzo");
        cboMes[4] = new SelectItem("4", "Abril");
        cboMes[5] = new SelectItem("5", "Mayo");
        cboMes[6] = new SelectItem("6", "Junio");
        cboMes[7] = new SelectItem("7", "Julio");
        cboMes[8] = new SelectItem("8", "Agosto");
        cboMes[9] = new SelectItem("9", "Septiembre");
        cboMes[10] = new SelectItem("10", "Octubre");
        cboMes[11] = new SelectItem("11", "Noviembre");
        cboMes[12] = new SelectItem("12", "Diciembre");
    }

    private void crearFiltroTipoPlanilla() {
        cboTipoPlanilla = new SelectItem[3];
        cboTipoPlanilla[0] = new SelectItem("", "[-Todos-]");
        cboTipoPlanilla[1] = new SelectItem("DEPENDIENTE", "DEPENDIENTE");
        cboTipoPlanilla[2] = new SelectItem("INDEPENDIENTE", "INDEPENDIENTE");
    }

	
	public void iniciarLazy() {

		lstPlanillaLazy = new LazyDataModel<Planilla>() {
			private List<Planilla> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Planilla getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Planilla empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Planilla empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<Planilla> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

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
               
                Page<Planilla> pagePlanilla=null;
               
                
                pagePlanilla= planillaService.findByEstado(estado, pageable);
                
                setRowCount((int) pagePlanilla.getTotalElements());
                return datasource = pagePlanilla.getContent();
            }
		};
	}
	
	public List<Empleado> completeEmpleado(String query) {
        List<Empleado> lista = new ArrayList<>();
        for (Empleado c : getLstEmpleadoCombo()) {
            if (c.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getPerson().getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }	

	public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
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
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorEmpleado() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Empleado c = null;
                    for (Empleado si : lstEmpleadoCombo) {
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
                    return ((Empleado) value).getId() + "";
                }
            }
        };
    }
	

	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public PlanillaService getPlanillaService() {
		return planillaService;
	}
	public void setPlanillaService(PlanillaService planillaService) {
		this.planillaService = planillaService;
	}
	public LazyDataModel<Planilla> getLstPlanillaLazy() {
		return lstPlanillaLazy;
	}
	public void setLstPlanillaLazy(LazyDataModel<Planilla> lstPlanillaLazy) {
		this.lstPlanillaLazy = lstPlanillaLazy;
	}
	public Planilla getPlanillaSelected() {
		return planillaSelected;
	}
	public void setPlanillaSelected(Planilla planillaSelected) {
		this.planillaSelected = planillaSelected;
	}
	public SelectItem[] getCboMes() {
		return cboMes;
	}
	public void setCboMes(SelectItem[] cboMes) {
		this.cboMes = cboMes;
	}
	public SelectItem[] getCboTipoPlanilla() {
		return cboTipoPlanilla;
	}
	public void setCboTipoPlanilla(SelectItem[] cboTipoPlanilla) {
		this.cboTipoPlanilla = cboTipoPlanilla;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public String getTipoPlanilla() {
		return tipoPlanilla;
	}
	public void setTipoPlanilla(String tipoPlanilla) {
		this.tipoPlanilla = tipoPlanilla;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public SimpleDateFormat getSdfYear() {
		return sdfYear;
	}
	public void setSdfYear(SimpleDateFormat sdfYear) {
		this.sdfYear = sdfYear;
	}
	public SimpleDateFormat getSdfDay() {
		return sdfDay;
	}
	public void setSdfDay(SimpleDateFormat sdfDay) {
		this.sdfDay = sdfDay;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Planilla getPlanillaNew() {
		return planillaNew;
	}
	public void setPlanillaNew(Planilla planillaNew) {
		this.planillaNew = planillaNew;
	}
	public boolean isBloqueo() {
		return bloqueo;
	}
	public void setBloqueo(boolean bloqueo) {
		this.bloqueo = bloqueo;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public List<Empleado> getLstEmpleadoCombo() {
		return lstEmpleadoCombo;
	}
	public void setLstEmpleadoCombo(List<Empleado> lstEmpleadoCombo) {
		this.lstEmpleadoCombo = lstEmpleadoCombo;
	}
	public List<DetallePlanilla> getLstDetallePlanillaTemp() {
		return lstDetallePlanillaTemp;
	}
	public void setLstDetallePlanillaTemp(List<DetallePlanilla> lstDetallePlanillaTemp) {
		this.lstDetallePlanillaTemp = lstDetallePlanillaTemp;
	}
	public DetallePlanillaService getDetallePlanillaService() {
		return detallePlanillaService;
	}
	public void setDetallePlanillaService(DetallePlanillaService detallePlanillaService) {
		this.detallePlanillaService = detallePlanillaService;
	}
	
	
	
}
