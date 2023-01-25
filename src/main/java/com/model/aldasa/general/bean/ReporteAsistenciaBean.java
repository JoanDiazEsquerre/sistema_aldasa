package com.model.aldasa.general.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.AsistenciaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ReporteAsistenciaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{asistenciaService}")
	private AsistenciaService asistenciaService;
	
	private LazyDataModel<Asistencia> lstAsistenciaLazy;
	
	private List<Empleado> lstEmpleado;
	private Empleado empleadoSelected;
	private Asistencia asistenciaSelected;
	
	private String tipo;
	private String tituloDialog="";
	private String nombreArchivo = "Reporte de Asistencia.xlsx";
	private Date fechaIni,fechaFin;
	
	
	private StreamedContent fileDes;
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");

	
	@PostConstruct
	public void init() {
		lstEmpleado=empleadoService.findByEstadoOrderByPersonSurnamesAsc(true);
		fechaIni = new Date() ;
		fechaFin = new Date() ;
		tipo="";
		iniciarLazy();
	}
	
	public void updateAsistencia() {
		tituloDialog = "MODIFICAR ASISTENCIA";
	

	}
	
	public void newAsistencia() {
		tituloDialog = "NUEVA ASISTENCIA";
		asistenciaSelected = new Asistencia();
		asistenciaSelected.setEmpleado(null);
		asistenciaSelected.setTipo("");
		asistenciaSelected.setHora(null); 
	}
	
	public void saveAsistencia() {
		if(asistenciaSelected.getEmpleado()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar empleado."));
			return ;
		}
		if(asistenciaSelected.getTipo().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar tipo."));
			return ;
		}
		if(asistenciaSelected.getHora()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar fecha."));
			return ;
		}
		Asistencia asistencia = asistenciaService.save(asistenciaSelected);
		
		if(tituloDialog.equals("NUEVA ASISTENCIA")) {
			newAsistencia();
		}
		
		if (asistencia == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));

		}
		
		
	}
		
	 
	public void procesarExcel() {
		  XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Asistencia");

	        CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
	        CellStyle styleTitulo =UtilXls.styleCell(workbook,'A');
	        //CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//	        Row rowTituloHoja = sheet.createRow(0);
//	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//	        cellTituloHoja.setCellValue("Reporte de Acciones");
//	        cellTituloHoja.setCellStyle(styleBorder);
//	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

	        Row rowSubTitulo = sheet.createRow(0);
	        Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
	        Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
	        Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("ENTRADA");cellSubEntrada1.setCellStyle(styleTitulo);
	        Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("SALIDA");cellSubSalida1.setCellStyle(styleTitulo);
	        Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("ENTRADA");cellSubEntrada2.setCellStyle(styleTitulo);
	        Cell cellSubSalida2 = rowSubTitulo.createCell(5);cellSubSalida2.setCellValue("SALIDA");cellSubSalida2.setCellStyle(styleTitulo);
	        Cell cellSubArea = rowSubTitulo.createCell(6);cellSubArea.setCellValue("ÁREA");cellSubArea.setCellStyle(styleTitulo);
	        
	       
	        
	        if(fechaFin.before(fechaIni)) {
	        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La fecha fin debe ser mayor o igual que la fecha inicio");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
	        }else {
	        	Date fecha1=fechaIni;
	        	Date fecha2=fechaFin;
	        	
	        	int index = 1;
	        	while (fecha1.getTime() <= fecha2.getTime() ){
	        		
	        		List<Empleado>  lstempleados = new ArrayList<>();
	        	    if(empleadoSelected != null) {
	        	    	Empleado emp = empleadoService.findByPerson(empleadoSelected.getPerson());
	        	    	lstempleados.add(emp);
	        	    }else {
	        	    	lstempleados = empleadoService.findByEstadoOrderByPersonSurnamesAsc(true);
	        	    }
	        	    
	        	    
	        	    
	        	    
	        	    if(!lstempleados.isEmpty()) {
	        	    	for(Empleado empleado : lstempleados) {
	        	    		Row rowDetail = sheet.createRow(index);
	        	    		Cell cellfecha = rowDetail.createCell(0); cellfecha.setCellValue(sdf.format(fecha1)); cellfecha.setCellStyle(styleBorder);
	        	    		Cell cellEmpleado = rowDetail.createCell(1); cellEmpleado.setCellValue(empleado.getPerson().getSurnames()+" "+empleado.getPerson().getNames()); cellEmpleado.setCellStyle(styleBorder);
	        	    		Cell cellE1 = rowDetail.createCell(2); cellE1.setCellStyle(styleBorder);
	        	    		Cell cellS1 = rowDetail.createCell(3); cellS1.setCellStyle(styleBorder);
	        	    		Cell cellE2 = rowDetail.createCell(4); cellE2.setCellStyle(styleBorder);
	        	    		Cell cellS2 = rowDetail.createCell(5); cellS2.setCellStyle(styleBorder);
	        	    		Cell cellArea = rowDetail.createCell(6);cellArea.setCellValue(empleado.getArea().getNombre()); cellArea.setCellStyle(styleBorder);
	        	    		

	        	    		
	        	    		Date dia1 = fecha1;
	        	    		String day = sdf.format(dia1);
	        	    		try {
								dia1 = sdf.parse(day);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        	    	
	        	    		
	        	    		Date dia2 = fecha1;
	        	    		dia2.setHours(23);
	        	    		dia2.setMinutes(59);
	        	    		dia2.setSeconds(59);
	        	    		List<Asistencia> lstasistenciasEntrada = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetween(empleado.getPerson().getDni(), "E", dia1, dia2);
	        	    		
	        	    		if(!lstasistenciasEntrada.isEmpty()) {
	        	    			int a = 2;
	        	    			for(Asistencia asistencia : lstasistenciasEntrada) {
	        		        	    Cell cellHora = rowDetail.createCell(a);cellHora.setCellValue(sdfTime.format(asistencia.getHora()));cellHora.setCellStyle(styleBorder);
	        		        	    a = a+2;
	        	    			}
	        	    			
	        	    		}
	        	  
	        	    		
	        	    		List<Asistencia> lstasistenciasSalida = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetween(empleado.getPerson().getDni(), "S", dia1, dia2);
	        	    		
	        	    		if(!lstasistenciasSalida.isEmpty()) {
	        	    			int b = 3;
	        	    			for(Asistencia asistencia : lstasistenciasSalida) {
	        		        	    Cell cellHora = rowDetail.createCell(b);cellHora.setCellValue(sdfTime.format(asistencia.getHora()));cellHora.setCellStyle(styleBorder);
	        		        	    b = b+2;
	        	    			}
	        	    			
	        	    		}
	        	    		
	        	    		index++;
	        	    	}
	        	    }
	        	    
	        	    
	        	    
	        	    fecha1=sumarDiasAFecha(fecha1, 1);
	        	
	        		
	        	}
	        	
	        }


	        for (int j = 0; j <= 6; j++) {
	            sheet.autoSizeColumn(j);
	        }
	        try {
	            ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            workbook.write(out);
	            out.close();
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("aplication/xls")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}
	
	public  Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      Date date= calendar.getTime(); 
	      date.setHours(0);
	      date.setMinutes(0); 
	      date.setSeconds(0);
	      return date; 
	}
	
	public void iniciarLazy() {

		lstAsistenciaLazy = new LazyDataModel<Asistencia>() {
			private List<Asistencia> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Asistencia getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Asistencia asistencia : datasource) {
                    if (asistencia.getId() == intRowKey) {
                        return asistencia;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Asistencia asistencia) {
                return String.valueOf(asistencia.getId());
            }

			@Override
			public List<Asistencia> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                
				Sort sort=Sort.by("hora").ascending();
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
               
                Page<Asistencia> pageAsistencia=null;
                
                String dni="%%";
                 
                if(empleadoSelected!=null) {
                	dni= "%" + empleadoSelected.getPerson().getDni() + "%";
                }
                
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                pageAsistencia= asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(dni,"%"+tipo+"%", fechaIni, fechaFin, pageable);
                
                setRowCount((int) pageAsistencia.getTotalElements());
                return datasource = pageAsistencia.getContent();
            }
		};
	}
    
	public String convertirHora (Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}
	
	public String convertirTipo (String tipo) {
		String valor = "";
		if(tipo.equals("E")) {
			valor = "ENTRADA";
		}else {
			valor = "SALIDA";
		}

		return valor;
	}
	
	public Converter getConversorEmpleado() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Empleado c = null;
                    for (Empleado si : lstEmpleado) {
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
	
	public List<Empleado> completeEmpleado(String query) {
        List<Empleado> lista = new ArrayList<>();
        for (Empleado empleado : lstEmpleado) {
            if (empleado.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase()) || empleado.getPerson().getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(empleado);
            }
        }
        return lista;
    }
	
	
	
	public List<Empleado> getLstEmpleado() {
		return lstEmpleado;
	}
	public void setLstEmpleado(List<Empleado> lstEmpleado) {
		this.lstEmpleado = lstEmpleado;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public Empleado getEmpleadoSelected() {
		return empleadoSelected;
	}
	public void setEmpleadoSelected(Empleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}
	public Asistencia getAsistenciaSelected() {
		return asistenciaSelected;
	}
	public void setAsistenciaSelected(Asistencia asistenciaSelected) {
		this.asistenciaSelected = asistenciaSelected;
	}
	public LazyDataModel<Asistencia> getLstAsistenciaLazy() {
		return lstAsistenciaLazy;
	}
	public void setLstAsistenciaLazy(LazyDataModel<Asistencia> lstAsistenciaLazy) {
		this.lstAsistenciaLazy = lstAsistenciaLazy;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public AsistenciaService getAsistenciaService() {
		return asistenciaService;
	}
	public void setAsistenciaService(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	
	
	
	
	
	
}
