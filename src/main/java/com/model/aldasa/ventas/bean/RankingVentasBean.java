package com.model.aldasa.ventas.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.util.EstadoLote;

@ManagedBean
@ViewScoped
public class RankingVentasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
		
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
		
	private LazyDataModel<Empleado> lstEmpleadoLazy;
	
	private Empleado empleadoSelected;
	
	private boolean estado = true;
	private String anio;
	
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfY = new SimpleDateFormat("yy");
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");

	
	@PostConstruct
	public void init() {
	anio=sdfY.format(new Date());
	iniciarLazy();
	}
	
	public int lotesVendidoMes (String mes,String anioselected, Empleado empleado) {
		String codigo = mes+anioselected;
		Comision comision = comisionService.findByEstadoAndCodigo(true, codigo);
		if (comision != null) {
			List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(),empleado.getPerson().getDni() , comision.getFechaIni(), comision.getFechaCierre());
			if(!lstLotesVendidos.isEmpty()) {
				return lstLotesVendidos.size();
			}
		}
		return 0;
	}
	
	public int totalLotesVendido(Empleado empleado) {
		int lot = 0;
		List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(),empleado.getPerson().getDni() ,empleado.getFechaIngreso(), new Date());
		if(!lstLotesVendidos.isEmpty()) {
			return lstLotesVendidos.size();
		}
		return lot;
	}
	
	public int mesesTrabajados(Empleado empleado) {
		int meses = 0;
		int anioIngreso = empleado.getFechaIngreso().getYear();
		int anioActual= new Date().getYear(); 
		int aniosDiferencia = anioActual-anioIngreso;
		
		int mesIngreso=empleado.getFechaIngreso().getMonth();
		int mesActual=new Date().getMonth();
		int mesDiferencia=0;
		if(mesIngreso> mesActual) {
			mesDiferencia = mesIngreso-mesActual;
		}else if (mesActual>mesIngreso) {
			mesDiferencia = mesActual-mesIngreso;
		}
		
		
		if(aniosDiferencia != 0) {
				meses = (aniosDiferencia*12)-mesDiferencia;
			 if (mesActual>mesIngreso) {
				meses = (aniosDiferencia*12)+mesDiferencia;
			}
		}else  {
			meses = mesDiferencia;
		}
		return meses;
	}
	
	public double promedioMensualVentas(Empleado empleado) {
		double prom = 0.0;
		if(totalLotesVendido(empleado)>0) {
			double tlv = totalLotesVendido(empleado) ;
			double mt = mesesTrabajados(empleado);
			return Math.round(tlv/mt * 100.0) / 100.0;
		}
		
		return prom;
	}
	
	public double promedio3UltimosMenses(Empleado empleado) {
		double prom = 0.0;
		
		Date fechaActual= new Date();
		fechaActual.setDate(1);
		
		String mesActual=sdfM.format(fechaActual);
		String anioActual=sdfY.format(fechaActual);
		int ventaMesActual=lotesVendidoMes(mesActual, anioActual, empleado);

		Date mesAnterior = sumaRestaMeses(fechaActual,-1);
		
		String mesPasado=sdfM.format(mesAnterior);
		String anioMesPasado=sdfY.format(mesAnterior);
		
		int ventaMesAnterior= lotesVendidoMes(mesPasado, anioMesPasado, empleado);
		
		Date mesAnterior2 = sumaRestaMeses(fechaActual,-2);
		
		String mesAntePasado=sdfM.format(mesAnterior2);
		String anioMesAntePasado=sdfY.format(mesAnterior2);
		
		int ventaMesAnteAnterior= lotesVendidoMes(mesAntePasado, anioMesAntePasado, empleado);
		
		double suma3UltimosMeses = ventaMesActual + ventaMesAnterior + ventaMesAnteAnterior;
		if(suma3UltimosMeses != 0) {
			return Math.round(suma3UltimosMeses/3 * 100.0) / 100.0;
		}
		
		return prom;
	}
	
	public Date sumaRestaMeses(Date fecha, int mes) {
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setLenient(false);

		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		
		
		calendar.add(calendar.MONTH, mes);  
		return calendar.getTime();
	}
	
	public int mesesQueVendio(Empleado empleado) {
		int noVendio = 0;
		
//		int siVendio = lotesVendidoMes(mes, anioselected, empleado);
		
		
		
		return noVendio;
	}
	
	
	
	public void iniciarLazy() {

		lstEmpleadoLazy = new LazyDataModel<Empleado>() {
			private List<Empleado> datasource;
            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Empleado getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Empleado empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Empleado empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<Empleado> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

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
               
                Page<Empleado> pageEmpleado=null;
               
                
                pageEmpleado= empleadoService.findByPersonSurnamesLikeAndEstado(names, estado, pageable);
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	
	
	
	
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public LazyDataModel<Empleado> getLstEmpleadoLazy() {
		return lstEmpleadoLazy;
	}
	public void setLstEmpleadoLazy(LazyDataModel<Empleado> lstEmpleadoLazy) {
		this.lstEmpleadoLazy = lstEmpleadoLazy;
	}
	public Empleado getEmpleadoSelected() {
		return empleadoSelected;
	}
	public void setEmpleadoSelected(Empleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

}
