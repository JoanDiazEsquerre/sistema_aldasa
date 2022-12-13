package com.model.aldasa.general.bean;

import java.io.Serializable;
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	
	@PostConstruct
	public void init() {
		anio=sdfY.format(new Date());
		iniciarLazy();
	}
	
	public void saveComision() {
		if(comisionSelected.getFechaIni()==null || comisionSelected.getFechaCierre()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos generales."));
			return ;
		}
		if(comisionSelected.getComisionContado()==null || comisionSelected.getComisionCredito()==null || comisionSelected.getBasicoJunior()==null || comisionSelected.getBonoJunior()==null || comisionSelected.getBasicoSenior()==null || comisionSelected.getBonoSenior()==null || comisionSelected.getBasicoMaster()==null || comisionSelected.getBonoMaster()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del asesor."));
			return ;
		}
		if(comisionSelected.getComisionSupervisor()==null || comisionSelected.getMeta()==null || comisionSelected.getComisionMetaSupervisor()==null || comisionSelected.getBonoMetaSupervisor()==null ) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del supervisor."));
			return ;
		}
		if(comisionSelected.getMetaOnline()==null || comisionSelected.getPrimeraVentaContadoOnline()==null || comisionSelected.getPrimeraVentaCreditoOnline()==null || comisionSelected.getBonoContadoOnline()==null || comisionSelected.getBonoCreditoOnline()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos de Online."));
			return ;
		}
		if(comisionSelected.getSubgerente()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar los datos del subgerente."));
			return ;
		}
		comisionService.save(comisionSelected);
		if (tituloDialog.equals("NUEVA COMISIÓN")) {
			newComision();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Se guardó correctamente."));
		
	}

	
	public void newComision() {
		tituloDialog="NUEVA COMISIÓN";
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
	}
	
	public void modifyComision( ) {
		tituloDialog="MODIFICAR COMISIÓN";
		
	}
	
	public void setfechaInicioFin() {
		try {
			Calendar calendar = Calendar.getInstance(); 
			System.out.println("Fecha Actual:" + sdf.format(calendar.getTime()));

			//A la fecha actual le pongo el día 1
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
                int anioConver=Integer.parseInt(anio);
                pageComision= comisionService.findByEstadoAndFechaIniYear(true, anioConver, pageable);
                
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
	
	

}
