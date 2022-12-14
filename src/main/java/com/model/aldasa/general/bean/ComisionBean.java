package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.service.ComisionService;

@ManagedBean
@ViewScoped
public class ComisionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	private LazyDataModel<Comision> lstComisionLazy;
	
	private Comision comisionSelected;
	
	private String tituloDialog, anio;
	
	private Date fechaIniFilter, fechaFinFilter;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy");
	
	@PostConstruct
	public void init() {
		anio=sdfY.format(new Date());
		getListaLazyComision();
	}
	
	public void getListaLazyComision() {
		try {
			fechaIniFilter = sdf2.parse(anio+"-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fechaFinFilter = sdf2.parse(anio+"-12-31");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iniciarLazy();
	}
	
	public void saveComision() {
		if(comisionSelected.getFechaIni()==null || comisionSelected.getFechaCierre()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos generales."));
			return ;
		}
		
		comisionSelected.setCodigo(sdfM.format(comisionSelected.getFechaIni())+""+sdfY2.format(comisionSelected.getFechaIni())); 
		
		if(comisionSelected.getComisionContado()==null || comisionSelected.getComisionCredito()==null || comisionSelected.getBasicoJunior()==null || comisionSelected.getBonoJunior()==null || comisionSelected.getBasicoSenior()==null || comisionSelected.getBonoSenior()==null || comisionSelected.getBasicoMaster()==null || comisionSelected.getBonoMaster()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del asesor."));
			return ;
		}
		if(comisionSelected.getComisionSupervisor()==null || comisionSelected.getMeta()==null || comisionSelected.getComisionMetaSupervisor()==null || comisionSelected.getBonoMetaSupervisor()==null ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del supervisor."));
			return ;
		}
		if(comisionSelected.getMetaOnline()==null || comisionSelected.getPrimeraVentaContadoOnline()==null || comisionSelected.getPrimeraVentaCreditoOnline()==null || comisionSelected.getBonoContadoOnline()==null || comisionSelected.getBonoCreditoOnline()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos Online."));
			return ;
		}
		
		if(comisionSelected.getMetaAsesorExterno()==null || comisionSelected.getBasicoAsesorExterno()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos Externos."));
			return ;
		}
		
		if(comisionSelected.getSubgerente()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del subgerente."));
			return ;
		}
		
		if (tituloDialog.equals("NUEVA COMISI??N")) {
			Comision validarExistencia = comisionService.findByEstadoAndCodigo(true, comisionSelected.getCodigo());
			if (validarExistencia == null) {
				comisionService.save(comisionSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				newComision();
			}else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya se programado una comision con el mismo rango de fechas."));
			}
		} else {
			Comision validarExistencia = comisionService.findByCodigoAndIdException(comisionSelected.getCodigo(), comisionSelected.getId());
			if (validarExistencia == null) {
				comisionService.save(comisionSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ya se programado una comision con el mismo rango de fechas."));
			}
		}
		
	}

	
	public void newComision() {
		tituloDialog="NUEVA COMISI??N";
		comisionSelected=new Comision();
		setfechaInicioFin();
		comisionSelected.setComisionContado(8);
		comisionSelected.setComisionCredito(4);
		comisionSelected.setBasicoJunior(1025.00);
		comisionSelected.setBonoJunior(200.00);
		comisionSelected.setBasicoSenior(1200.00);
		comisionSelected.setBonoSenior(300.00);
		comisionSelected.setBasicoMaster(1500.00);
		comisionSelected.setBonoMaster(500.00);
		comisionSelected.setMeta(30);
		comisionSelected.setComisionSupervisor(1);
		comisionSelected.setComisionMetaSupervisor(2); 
		comisionSelected.setBonoMetaSupervisor(500.00);
		comisionSelected.setSubgerente(1);
		comisionSelected.setMetaOnline(10);
		comisionSelected.setPrimeraVentaCreditoOnline(300.00);
		comisionSelected.setPrimeraVentaContadoOnline(500.00);
		comisionSelected.setBonoCreditoOnline(400.00); 
		comisionSelected.setBonoContadoOnline(400.00);
		comisionSelected.setEstado(true);
		comisionSelected.setMetaAsesorExterno(5); 
		comisionSelected.setBasicoAsesorExterno(1025.00); 
		comisionSelected.setComisionSupervisorOnline(0.5);
	}
	
	public void modifyComision( ) {
		tituloDialog="MODIFICAR COMISI??N";
		
	}
	
	public void setfechaInicioFin() {
		try {
			Calendar calendar = Calendar.getInstance(); 

			//A la fecha actual le pongo el d??a 1
			calendar.set(Calendar.DAY_OF_MONTH,1);
			comisionSelected.setFechaIni(calendar.getTime());

			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			
			int ultimoDiaMes=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int mesActual = Integer.parseInt(sdfM.format(new Date())) ;
			int anioActual = Integer.parseInt(sdfY.format(new Date())) ;
			String fechaFinn=ultimoDiaMes + "/"+mesActual+"/"+anioActual;
			comisionSelected.setFechaCierre(sdf.parse(fechaFinn)); 
			
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}

	public void iniciarLazy() {

		lstComisionLazy = new LazyDataModel<Comision>() {
			private List<Comision> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Comision getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Comision comision : datasource) {
                    if (comision.getId() == intRowKey) {
                        return comision;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comision comision) {
                return String.valueOf(comision.getId());
            }

			@Override
			public List<Comision> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fechaIni").ascending();
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
               
                Page<Comision> pageComision=null;
               
                
                pageComision= comisionService.findByEstadoAndFechaIniBetween(true, fechaIniFilter,fechaFinFilter, pageable);
                
                setRowCount((int) pageComision.getTotalElements());
                return datasource = pageComision.getContent();
            }
		};
	}
	

	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public Comision getComisionSelected() {
		return comisionSelected;
	}
	public void setComisionSelected(Comision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public LazyDataModel<Comision> getLstComisionLazy() {
		return lstComisionLazy;
	}
	public void setLstComisionLazy(LazyDataModel<Comision> lstComisionLazy) {
		this.lstComisionLazy = lstComisionLazy;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public Date getFechaIniFilter() {
		return fechaIniFilter;
	}
	public void setFechaIniFilter(Date fechaIniFilter) {
		this.fechaIniFilter = fechaIniFilter;
	}
	public Date getFechaFinFilter() {
		return fechaFinFilter;
	}
	public void setFechaFinFilter(Date fechaFinFilter) {
		this.fechaFinFilter = fechaFinFilter;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
