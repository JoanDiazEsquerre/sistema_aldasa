package com.model.aldasa.proyecto.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Cargo;
import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Meta;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.ResumenComisionAsesor;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.AreaService;
import com.model.aldasa.service.CargoService;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.ComisionesService;
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
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{cargoService}")
	private CargoService cargoService; 
	
	@ManagedProperty(value = "#{areaService}")
	private AreaService areaService; 
	
	private LazyDataModel<Comisiones> lstComisionesLazy;
	private LazyDataModel<Usuario> lstUsuarioLazy;
	private LazyDataModel<Empleado> lstEmpleadoLazy;
//	private LazyDataModel<Lote> lstLoteVendidoLazy;
	
	private Team teamSelected;
	private Comision comisionSelected;
	private Comisiones comisionesSelected;
	private Lote loteSelected;
	private Person personSupervisorSelected, personSupervisorResumen;
	private Cargo cargoFilter;
	private Area areaFilter;
	private Team teamFilter;
	private Comision comision1, comision2, comision3;
	
	private String fecha1, fecha2, fecha3;
	private String nombreArchivo = "Reporte de Comisiones.xlsx";

	private Date fechaIni,fechaFin;
	private Integer comisionContado=8;
	private Integer comisionCredito=4;
	private int index=0;
		
	private BigDecimal totalSolesContado = BigDecimal.ZERO;
	private BigDecimal totalSolesInicial = BigDecimal.ZERO;
	private BigDecimal totalSolesPendiente = BigDecimal.ZERO;
	private BigDecimal totalComisionSupervisor;
	
//	private List<Person> lstPersonAsesor;
	private List<Comision> lstComision;
	private List<Comisiones> lstComisiones;
	private List<Lote> lstLotesVendidos;
	private List<Meta> lstMeta;
	private List<MetaSupervisor> lstMetaSupervisor;
	private List<Person> lstPersonSupervisor;
	private List<Cargo> lstCargo;
	private List<Area> lstArea;
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor;
	private List<ResumenComisionAsesor> lstResumenMuestra;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	SimpleDateFormat sdfMesAnio = new SimpleDateFormat("MMM/yyyy"); 
	
	private StreamedContent fileDes;
	
	@PostConstruct
	public void init() {
//		comisionSelected = comisionService.findByEstadoAndCodigo(true, sdfM.format(new Date())+sdfY2.format(new Date()));
		lstCargo=cargoService.findByEstadoOrderByDescripcionAsc(true);
		lstArea=areaService.findByEstadoOrderByNombreAsc(true);
		lstTeam=teamService.findByStatus(true);
		lstComision = comisionService.findByEstadoOrderByCodigoDesc(true);
		comisionSelected = lstComision.get(0);
		cambiarComision();
		iniciarLazyComisiones();
		iniciarLazyEmpleados();
		verMesSiguiente();
	}
	
	public void verResumen() {
		lstResumenMuestra = new ArrayList<>();
		totalComisionSupervisor = BigDecimal.ZERO;
		
		if(!lstComisiones.isEmpty()) {
			for(Person asesor : lstPersonAsesor) {
				int contadorVenta = 0;
				ResumenComisionAsesor resumen = new ResumenComisionAsesor();
				resumen.setPersonAsesor(asesor);
				resumen.setComision(BigDecimal.ZERO);
				resumen.setComisionSupervisor(BigDecimal.ZERO);
				resumen.setBono(BigDecimal.ZERO);
				resumen.setTotalLotesVendidos(0);
				
				for(Comisiones comision : lstComisiones) {
					if(personSupervisorResumen == null) {
						if(asesor.getId().equals(comision.getPersonAsesor().getId())) {
							resumen.setComision(resumen.getComision().add(comision.getComisionAsesor()));
							resumen.setComisionSupervisor(resumen.getComisionSupervisor().add(comision.getComisionSupervisor()));
							contadorVenta++;
						}
					}else {
						if(asesor.getId().equals(comision.getPersonAsesor().getId()) && comision.getPersonSupervisor().getId().equals(personSupervisorResumen.getId())) {
							resumen.setComision(resumen.getComision().add(comision.getComisionAsesor()));
							resumen.setComisionSupervisor(resumen.getComisionSupervisor().add(comision.getComisionSupervisor()));
							
							totalComisionSupervisor = totalComisionSupervisor.add(comision.getComisionSupervisor());
							
							contadorVenta++;
						}
					}
					
					
				}
				resumen.setTotalLotesVendidos(contadorVenta); 
				
				if(contadorVenta >= 2 && contadorVenta <= 4) {
					resumen.setBono(comisionSelected.getBonoJunior());
				}
				
				if(contadorVenta >= 5 && contadorVenta <= 8) {
					resumen.setBono(comisionSelected.getBonoSenior());
				}
				
				if(contadorVenta >= 9) {
					resumen.setBono(comisionSelected.getBonoMaster());
				}
				
				lstResumenMuestra.add(resumen);
				
			}
		}
	}

	
	
	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Comisiones");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
// 		CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//	        Row rowTituloHoja = sheet.createRow(0);
//	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//	        cellTituloHoja.setCellValue("Reporte de Acciones");
//	        cellTituloHoja.setCellStyle(styleBorder);
//	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("SUPERVISOR");cellSub1.setCellStyle(styleTitulo);
		Cell cellSub2 = rowSubTitulo.createCell(1);cellSub2.setCellValue("ASESOR");cellSub2.setCellStyle(styleTitulo);
		Cell cellSub3 = rowSubTitulo.createCell(2);cellSub3.setCellValue("CLIENTE");cellSub3.setCellStyle(styleTitulo);
		Cell cellSub4 = rowSubTitulo.createCell(3);cellSub4.setCellValue("LOTE");cellSub4.setCellStyle(styleTitulo);
		Cell cellSub5 = rowSubTitulo.createCell(4);cellSub5.setCellValue("MANZANA");cellSub5.setCellStyle(styleTitulo);
		Cell cellSub6 = rowSubTitulo.createCell(5);cellSub6.setCellValue("PROYECTO");cellSub6.setCellStyle(styleTitulo);
		Cell cellSub7 = rowSubTitulo.createCell(6);cellSub7.setCellValue("FECHA VENTA");cellSub7.setCellStyle(styleTitulo);
		Cell cellSub8 = rowSubTitulo.createCell(7);cellSub8.setCellValue("TIPO PAGO");cellSub8.setCellStyle(styleTitulo);
		Cell cellSub9 = rowSubTitulo.createCell(8);cellSub9.setCellValue("PRECIO");cellSub9.setCellStyle(styleTitulo);
		Cell cellSub10 = rowSubTitulo.createCell(9);cellSub10.setCellValue("COMISION ASESOR");cellSub10.setCellStyle(styleTitulo);
		Cell cellSub11 = rowSubTitulo.createCell(10);cellSub11.setCellValue("COMISION SUPERVISOR");cellSub11.setCellStyle(styleTitulo);
		Cell cellSub12 = rowSubTitulo.createCell(11);cellSub12.setCellValue("COMISION SUBGERENTE");cellSub12.setCellStyle(styleTitulo);

		int index = 1;
		if (!lstComisiones.isEmpty()) {
			for (Comisiones c : lstComisiones) {
				Row rowDetail = sheet.createRow(index);
				Cell cell1 = rowDetail.createCell(0);cell1.setCellValue(c.getPersonSupervisor().getSurnames()+" "+c.getPersonSupervisor().getNames());cell1.setCellStyle(styleBorder);
				Cell cell2 = rowDetail.createCell(1);cell2.setCellValue(c.getPersonAsesor().getSurnames()+" "+c.getPersonAsesor().getNames());cell2.setCellStyle(styleBorder);
				Cell cell3 = rowDetail.createCell(2);cell3.setCellValue(c.getContrato().getPersonVenta().getSurnames()+" "+c.getContrato().getPersonVenta().getNames());cell3.setCellStyle(styleBorder);
				Cell cell4 = rowDetail.createCell(3);cell4.setCellValue(c.getLote().getNumberLote());cell4.setCellStyle(styleBorder);
				Cell cell5 = rowDetail.createCell(4);cell5.setCellValue(c.getLote().getManzana().getName());cell5.setCellStyle(styleBorder);
				Cell cell6 = rowDetail.createCell(5);cell6.setCellValue(c.getLote().getProject().getName());cell6.setCellStyle(styleBorder); 
				Cell cell7 = rowDetail.createCell(6);cell7.setCellValue(c.getContrato().getFechaVenta()==null?"": sdf.format(c.getContrato().getFechaVenta()));cell7.setCellStyle(styleBorder);
				Cell cell8 = rowDetail.createCell(7);cell8.setCellValue(c.getContrato().getTipoPago());cell8.setCellStyle(styleBorder);
				Cell cell9 = rowDetail.createCell(8);cell9.setCellValue(c.getContrato().getMontoVenta()+"");cell9.setCellStyle(styleBorder);
				Cell cell10 = rowDetail.createCell(9);cell10.setCellValue(c.getComisionAsesor()+"");cell10.setCellStyle(styleBorder);
				Cell cell11 = rowDetail.createCell(10);cell11.setCellValue(c.getComisionSupervisor()+"");cell11.setCellStyle(styleBorder);
				Cell cell12 = rowDetail.createCell(11);cell12.setCellValue(c.getComisionSubgerente()+"");cell12.setCellStyle(styleBorder);
				
				index++;
			}
		}

		for (int j = 0; j <= 11; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public double promedio3UltimosMenses(Empleado empleado) {
		double promedio = 0.0;
		double total1=0;
		
		if(comision1!=null) {
			total1 = total1+ buscarLotesVendidos(empleado, comision1);
		}
		if(comision2!=null) {
			total1 = total1+ buscarLotesVendidos(empleado, comision2);
		}
		if(comision3!=null) {
			total1 = total1+ buscarLotesVendidos(empleado, comision3);
		}
		
		promedio = total1/3;
	
		promedio = Math.round(promedio * 100.0) / 100.0;	
		//**************************************
		if (promedio >= 0.0) {
			empleado.setNivel("BAJO");
		}
		if (promedio >= 1.5) {
			empleado.setNivel("MEDIO");
		}
		if (promedio >= 5.0) {
			empleado.setNivel("ALTO");
		}
		
		
		
		return promedio;
	}
	
	public int buscarLotesVendidos(Empleado empleado, Comision comision) {
		int total = 0;
		
		if(comision != null) {
			List<Comisiones> lstCom = comisionesService.findByEstadoAndComisionAndPersonAsesor(true, comision, empleado.getPerson());
			if(!lstCom.isEmpty()) {
				total = lstCom.size();
			}
		}
		
		return total;
	}
	
	public void verMesAnterior() {
		index++;
		comision1 = lstComision.get(index);
		comision2 = lstComision.get(index+1);
		comision3 = lstComision.get(index+2);
		
		fecha1 = "No Registrado";
		fecha2 = "No Registrado";
		fecha3 = "No Registrado";
		
		if(comision1!=null) {
			fecha1 = sdfMesAnio.format(comision1.getFechaIni());
		}
		if(comision2!=null) {
			fecha2 = sdfMesAnio.format(comision2.getFechaIni());
		}
		if(comision3!=null) {
			fecha3 = sdfMesAnio.format(comision3.getFechaIni());
		}
		
	}
	
	public void verMesSiguiente() {
		index--;
		if(index<=0) {
			index=0;
			comision1 = lstComision.get(index);
			comision2 = lstComision.get(index+1);
			comision3 = lstComision.get(index+2);
			
			fecha1 = "No Registrado";
			fecha2 = "No Registrado";
			fecha3 = "No Registrado";
			
			if(comision1!=null) {
				fecha1 = sdfMesAnio.format(comision1.getFechaIni());
			}
			if(comision2!=null) {
				fecha2 = sdfMesAnio.format(comision2.getFechaIni());
			}
			if(comision3!=null) {
				fecha3 = sdfMesAnio.format(comision3.getFechaIni());
			}
		}
		
		
		
	}
	
	public void mostrarTresMeses() {
		
	}
	
	public void iniciarLazyEmpleados() {

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
                
                String cargo = "%%";
                String area = "%%";
                
                if(cargoFilter!=null) {
                	cargo = "%"+cargoFilter.getDescripcion()+"%";
                }
                
                if(areaFilter!=null) {
                	area = "%"+areaFilter.getNombre()+"%";
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<Empleado> pageEmpleado=null;
               
                if(teamFilter==null) {
                    pageEmpleado= empleadoService.findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLike(names, true,cargo,area, pageable);
                }else {
                    pageEmpleado= empleadoService.findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLikeAndTeam(names, true,cargo,area,teamFilter, pageable);

                }
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	public void aplicarComisionMeta(MetaSupervisor metaSupervisor) {
		for(Comisiones c : lstComisiones) {
			if(c.getPersonSupervisor().getId().equals(metaSupervisor.getPersonSupervisor().getId())) {
				BigDecimal multiplicaSup = c.getLote().getMontoVenta().multiply(new BigDecimal(metaSupervisor.getComision().getComisionMetaSupervisor())); 
				c.setComisionSupervisor(multiplicaSup.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
				comisionesService.save(c);
			}	
		}
		addInfoMessage("Se aplicó el cambio de porcentaje correctamente."); 
	}
	
	public void listarLotesVendidosPorTeam() {
		Date fechaIni = comisionSelected.getFechaIni();
		fechaIni.setHours(0);
		fechaIni.setMinutes(0);
		fechaIni.setSeconds(0);
		
		Date fechaFin = comisionSelected.getFechaCierre();
		fechaFin.setHours(23);
		fechaFin.setMinutes(59);
		fechaFin.setSeconds(59);
		
		if(teamSelected.getName().equals("ONLINE")) {
			
		}else if(teamSelected.getName().equals("EXTERNOS")){
			
		}else if(teamSelected.getName().equals("INTERNOS")) {
			
		}else {
			if(teamSelected.getPersonSupervisor()!=null) {
				lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), "%%", fechaIni, fechaFin);
				
			}
		}
		
	}
	
//	public void iniciarLazyLotesVendidos() {
//		lstLoteVendidoLazy = new LazyDataModel<Lote>() {
//			private List<Lote> datasource;
//
//            @Override
//            public void setRowIndex(int rowIndex) {
//                if (rowIndex == -1 || getPageSize() == 0) {
//                    super.setRowIndex(-1);
//                } else {
//                    super.setRowIndex(rowIndex % getPageSize());
//                }
//            }
//
//            @Override
//            public Lote getRowData(String rowKey) {
//                int intRowKey = Integer.parseInt(rowKey);
//                for (Lote lote : datasource) {
//                    if (lote.getId() == intRowKey) {
//                        return lote;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public String getRowKey(Lote lote) {
//                return String.valueOf(lote.getId());
//            }
//
//			@Override
//			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
//				
//				
//                                
//                Sort sort=Sort.by("numberLote").ascending();
//                if(sortBy!=null) {
//                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
//                	    System.out.println(entry.getKey() + "/" + entry.getValue());
//                	   if(entry.getValue().getOrder().isAscending()) {
//                		   sort = Sort.by(entry.getKey()).descending();
//                	   }else {
//                		   sort = Sort.by(entry.getKey()).ascending();
//                	   }
//                	}
//                }
//                
//                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
//                
//				Page<Lote> pageLote;
//				
//				Date fechaIni = comisionSelected.getFechaIni();
//				fechaIni.setHours(0);
//				fechaIni.setMinutes(0);
//				fechaIni.setSeconds(0);
//				
//				Date fechaFin = comisionSelected.getFechaCierre();
//				fechaFin.setHours(23);
//				fechaFin.setMinutes(59);
//				fechaFin.setSeconds(59);
//
//				pageLote= loteService.findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween("%"+EstadoLote.VENDIDO.getName()+"%","%"+teamSelected.getPersonSupervisor().getDni()+"%", "%%", fechaIni, fechaFin, pageable);
//				
//				setRowCount((int) pageLote.getTotalElements());
//				return datasource = pageLote.getContent();
//			}
//		};
//	}
	
//	public void verDetalleAsesores() {
//		lstPersonAsesor = new ArrayList<>();
//		List<Lote> lstLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), "%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		int contador = 0;
//		if(!lstLotes.isEmpty()) {
//			for(Lote lote : lstLotes) {
//				Person personAsesor = lote.getPersonAssessor();
//				
//				if(contador == 0) {
//					lstPersonAsesor.add(personAsesor);
//				}else {
//					boolean encuentra = false;
//					for(Person asesor:lstPersonAsesor) {
//						if(asesor.equals(lote.getPersonAssessor())) {
//							encuentra=true;
//						}
//					}
//					
//					if(!encuentra) {
//						lstPersonAsesor.add(personAsesor);
//					}
//				}
//				contador++;
//			}
//			
//			for(Person asesor:lstPersonAsesor) {
//				int nroContado = 0;
//				int nroCredito = 0;
//				double comisionContado = 0.0;
//				double comisionCredito = 0.0;
//				double totalComision = 0.0;
//				
//				for(Lote lote : lstLotes) {
//					if(asesor.equals(lote.getPersonAssessor())) {
//						int nro = asesor.getLotesVendidos()+1;
//						if(lote.getTipoPago().equals("Contado")) {
//							nroContado = nroContado+1;
//							totalComision = totalComision + calcularComisionAsesor(lote);
//							comisionContado = comisionContado+ calcularComisionAsesor(lote);
//						}else {
//							nroCredito = nroCredito+1;
//							totalComision = totalComision + calcularComisionAsesor(lote);
//							comisionCredito = comisionCredito+ calcularComisionAsesor(lote);
//						}
//						
//						asesor.setLotesVendidos(nro); 
//						asesor.setLotesContado(nroContado);
//						asesor.setLotesCredito(nroCredito);
//						asesor.setTotalComision(totalComision); 
//						asesor.setComisionContado(comisionContado);
//						asesor.setComisionCredito(comisionCredito); 
//					}
//				}
//			}
//		}
//	}
	
	public String asignarTipoBono(Person asesor) {
		String tipoBono = "Ninguno";
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			tipoBono = "Bono Junior";
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			tipoBono = "Bono Senior";
		}
		if(asesor.getLotesVendidos()>=9) {
			tipoBono = "Bono Master";
		}
		return tipoBono;
		
	}
	
	public BigDecimal obtenerSueldoBasicoAsesor(Person asesor) {
		BigDecimal sueldo = BigDecimal.ZERO;
		Empleado empleado = empleadoService.findByPersonId(asesor.getId());
		if(empleado != null) {
			sueldo = empleado.getSueldoBasico();
		}
	
		return sueldo;
		
	}
	
	public BigDecimal obtenerSueldoBasicoAsesorBono(Person asesor) {
		BigDecimal sueldo = BigDecimal.ZERO;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			sueldo = comisionSelected.getBasicoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			sueldo = comisionSelected.getBasicoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			sueldo = comisionSelected.getBasicoMaster();
		}
	
		return sueldo;
	}
	
	public BigDecimal obtenerBonoAsesor(Person asesor) {
		BigDecimal bono = BigDecimal.ZERO;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			bono = comisionSelected.getBonoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			bono = comisionSelected.getBonoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			bono = comisionSelected.getBonoMaster();
		}
	
		return bono;
	}
	
	public BigDecimal pagoMesAsesor(Person person) {
		BigDecimal pagoMes = BigDecimal.ZERO;
		String tipoBono = asignarTipoBono(person);
		if(tipoBono.equals("Ninguno")) {
			pagoMes = obtenerSueldoBasicoAsesor(person).add(person.getTotalComision());
		}else {
			pagoMes = person.getTotalComision().add(obtenerBonoAsesor(person)).add(obtenerSueldoBasicoAsesorBono(person));
		}
		
		return pagoMes;
	}
	
	public BigDecimal calcularSueldoMesAsesor(Person asesor) {
		BigDecimal sueldoMes = BigDecimal.ZERO;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			sueldoMes = comisionSelected.getBonoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			sueldoMes = comisionSelected.getBonoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			sueldoMes = comisionSelected.getBonoMaster();
		}
	
		return sueldoMes;
	}
	
	public BigDecimal obtenerTotoalComisionesAsesor(Person asesor) {
		BigDecimal sueldo = BigDecimal.ZERO;
		Empleado empleado = empleadoService.findByPersonId(asesor.getId());
		if(empleado != null) {
			sueldo = empleado.getSueldoBasico();
		}
	
		return sueldo;
		
	}
	
	public void cambiarComision() {
		lstComisiones = comisionesService.findByEstadoAndComision(true, comisionSelected);
		lstPersonSupervisor = new ArrayList<>();
		lstPersonAsesor = new ArrayList<>();
		personSupervisorSelected=null;
		
		if(!lstComisiones.isEmpty()) {
			for(Comisiones c : lstComisiones) {
				// para listar supervisores
				if(lstPersonSupervisor.isEmpty()) {
					lstPersonSupervisor.add(c.getPersonSupervisor());
				}else {
					boolean encuentra=false;
					for(Person p:lstPersonSupervisor) {
						
						if(p.getId().equals(c.getPersonSupervisor().getId())) { 
							encuentra=true;
						}
					}
					if(!encuentra) {
						lstPersonSupervisor.add(c.getPersonSupervisor());
					}
				}
				
				//para listar asesores
				if(lstPersonAsesor.isEmpty()) {
					lstPersonAsesor.add(c.getPersonAsesor());
				}else {
					boolean encuentra=false;
					for(Person p:lstPersonAsesor) {
						
						if(p.getId().equals(c.getPersonAsesor().getId())) { 
							encuentra=true;
						}
					}
					if(!encuentra) {
						lstPersonAsesor.add(c.getPersonAsesor());						
					}
				}
			}
			
		}
		
//		lstPersonSupervisor = personService.getPersonSupervisor(EstadoLote.VENDIDO.getName(), comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		
	}

	public List<Lote> listaLotesVendidoPorAsesor(Person asesor){
		List<Lote> listaLotes = new ArrayList<>();
		listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), asesor.getDni(), comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		
		return listaLotes;
		
	}
	
	public BigDecimal calcularSolesContado(Team team) {
		BigDecimal solesContado = BigDecimal.ZERO;
		
		if(team.getName().equals("INTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado.add(lote.getMontoVenta());
					}
				}
			}
			totalSolesContado = totalSolesContado.add(solesContado);
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado.add(lote.getMontoVenta());
					}
				}
			}
			totalSolesContado = totalSolesContado.add(solesContado);
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado.add(lote.getMontoVenta());
					}
				}
			}
			totalSolesContado = totalSolesContado.add(solesContado);
			
		}
		
		
		return solesContado;
	}
	
	public BigDecimal calcularSolesInicial(Team team) {
		BigDecimal solesCredito = BigDecimal.ZERO;
		
		if(team.getName().equals("INTERNOS")) {
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito.add(lote.getMontoInicial());
					}
				}
			}
			totalSolesInicial = totalSolesInicial.add(solesCredito);
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito.add(lote.getMontoInicial());
					}
				}
			}
			totalSolesInicial = totalSolesInicial.add(solesCredito);
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito.add(lote.getMontoInicial());
					}
				}
			}
			totalSolesInicial = totalSolesInicial.add(solesCredito);
		}
		
		
		
		return solesCredito;
	}
	
	public BigDecimal calcularSolesPendiente(Team team) {
		BigDecimal solesPendiente = BigDecimal.ZERO;
		
		if(team.getName().equals("INTERNOS")) {
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						BigDecimal saldo = lote.getMontoVenta().subtract(lote.getMontoInicial());
						solesPendiente = solesPendiente.add(saldo);
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente.add(solesPendiente);
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						BigDecimal saldo = lote.getMontoVenta().subtract(lote.getMontoInicial());
						solesPendiente = solesPendiente.add(saldo);
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente.add(solesPendiente);
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Crédito")) {
						BigDecimal saldo = lote.getMontoVenta().subtract(lote.getMontoInicial());
						solesPendiente = solesPendiente.add(saldo);
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente.add(solesPendiente);
		}
		
		
		
		return solesPendiente;
	}
	
	public BigDecimal calcularPorcentaje(MetaSupervisor metaSupervisor) {
		BigDecimal calculo = BigDecimal.ZERO;
		int num = 0;
		for(Comisiones c : lstComisiones) {
			if(c.getPersonSupervisor().getId().equals(metaSupervisor.getPersonSupervisor().getId())) {
				num++;
			}	
		}
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			calculo = multiplicado.divide(new BigDecimal(metaSupervisor.getMeta()), 2, RoundingMode.HALF_UP);
		}
		return calculo;
	}
	
	public int calcularLotesVendidosSupervisor(MetaSupervisor metaSupervisor) {
		int num = 0;
		for(Comisiones c : lstComisiones) {
			if(c.getPersonSupervisor().getId().equals(metaSupervisor.getPersonSupervisor().getId())) {
				num++;
			}	
		}
		
		return num;
	}
	
	public void mostrarMeta() {
		totalSolesContado=BigDecimal.ZERO;
		totalSolesInicial=BigDecimal.ZERO;
		totalSolesPendiente=BigDecimal.ZERO;
		lstMetaSupervisor = metaSupervisorService.findByComisionAndEstado(comisionSelected, true);
//		List<Person> lstSupervisoresCampo = personService.getPersonSupervisorCampo(comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		if (!lstSupervisoresCampo.isEmpty()) {
//			for(Person persona : lstSupervisoresCampo) {
//				List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), persona, "%%", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//				
//				Meta meta = new Meta();
//				meta.setSupervisor(persona.getSurnames() + " " + persona.getNames());
//				meta.setLotesVendidos(lstLotesVendidos.size());
//				
//				MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comisionSelected, true, persona);
//				BigDecimal calculo = BigDecimal.ZERO;
//				if(metaSupervisor != null) {
//					BigDecimal multiplicado = new BigDecimal(lstLotesVendidos.size()).multiply(new BigDecimal(100));    
//					calculo = multiplicado.divide(new BigDecimal(metaSupervisor.getMeta()));
//				}
//				
//				meta.setPorcentajeMeta(calculo.intValue());
//				
//				BigDecimal contado = BigDecimal.ZERO ;
//				BigDecimal inicial = BigDecimal.ZERO ;
//				BigDecimal saldo = BigDecimal.ZERO ;
//				
//				for(Lote lote : lstLotesVendidos) {
//					if(lote.getTipoPago().equals("Contado")) {
//						contado = contado.add(lote.getMontoVenta());
//					}else {
//						inicial = inicial.add(lote.getMontoInicial());
//						BigDecimal calculaSaldo = lote.getMontoVenta().subtract(lote.getMontoInicial());
//						saldo =saldo.add(calculaSaldo) ;
//					}
//				}
//				totalSolesContado = totalSolesContado.add(contado);
//				totalSolesInicial = totalSolesInicial.add(inicial);
//				totalSolesPendiente = totalSolesPendiente.add(saldo);
//
//				meta.setMontoContado(contado);
//				meta.setMontoInicial(inicial);
//				meta.setSaldoPendiente(saldo);
//				lstMeta.add(meta);
//			}
//		}
//		
//		List<Comisiones> lstInternos = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "I", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		if(!lstInternos.isEmpty()) {
//		
//			Meta meta = new Meta() ;
//			meta.setSupervisor("Internos");
//			meta.setLotesVendidos(lstInternos.size());
//			
//			meta.setPorcentajeMeta(0);
//			
//			BigDecimal contado = BigDecimal.ZERO;
//			BigDecimal inicial = BigDecimal.ZERO ;
//			BigDecimal saldo = BigDecimal.ZERO ;
//			
//			for(Comisiones comisiones : lstInternos) {
//				if(comisiones.getLote().getTipoPago().equals("Contado")) {
//					contado = contado.add(comisiones.getLote().getMontoVenta());
//				}else {
//					inicial = inicial.add(comisiones.getLote().getMontoInicial());
//					BigDecimal calculoSaldo = comisiones.getLote().getMontoVenta().subtract(comisiones.getLote().getMontoInicial());
//					saldo = saldo.add(calculoSaldo);
//				}
//			}
//			totalSolesContado = totalSolesContado.add(contado);
//			totalSolesInicial = totalSolesInicial.add(inicial);
//			totalSolesPendiente = totalSolesPendiente.add(saldo) ;
//
//			
//			meta.setMontoContado(contado);
//			meta.setMontoInicial(inicial);
//			meta.setSaldoPendiente(saldo);
//			lstMeta.add(meta);
//		}
//		
//		List<Comisiones> lstExternos = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "E", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		if(!lstExternos.isEmpty()) {
//		
//			Meta meta = new Meta() ;
//			meta.setSupervisor("Externos");
//			meta.setLotesVendidos(lstExternos.size());
//			meta.setPorcentajeMeta(0);
//			
//			BigDecimal contado = BigDecimal.ZERO ;
//			BigDecimal inicial = BigDecimal.ZERO ;
//			BigDecimal saldo = BigDecimal.ZERO ;
//			
//			for(Comisiones comisiones : lstExternos) {
//				if(comisiones.getLote().getTipoPago().equals("Contado")) {
//					contado = contado.add(comisiones.getLote().getMontoVenta());
//				}else {
//					inicial = inicial.add(comisiones.getLote().getMontoInicial());
//					BigDecimal calculoSaldo = comisiones.getLote().getMontoVenta().subtract(comisiones.getLote().getMontoInicial());
//					saldo = saldo.add(calculoSaldo);
//				}
//			}
//			totalSolesContado = totalSolesContado.add(contado);
//			totalSolesInicial = totalSolesInicial.add(inicial);
//			totalSolesPendiente = totalSolesPendiente.add(saldo) ;
//			
//			meta.setMontoContado(contado);
//			meta.setMontoInicial(inicial);
//			meta.setSaldoPendiente(saldo);
//			lstMeta.add(meta);
//		}
//		
//		List<Comisiones> lstOnline = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "O", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		if(!lstOnline.isEmpty()) {
//		
//			Meta meta = new Meta() ;
//			meta.setSupervisor("Online/"+ lstOnline.get(0).getLote().getPersonSupervisor().getSurnames() + " " + lstOnline.get(0).getLote().getPersonSupervisor().getNames());
//			meta.setLotesVendidos(lstOnline.size());
//			
//			MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comisionSelected, true, lstOnline.get(0).getLote().getPersonSupervisor());
//			BigDecimal calculo = BigDecimal.ZERO;
//			if(metaSupervisor != null) {
//				BigDecimal multiplicacion = new BigDecimal(lstOnline.size()).multiply(new BigDecimal(100));
//				calculo = multiplicacion.divide(new BigDecimal(metaSupervisor.getMeta()));
//			}
//			
//			meta.setPorcentajeMeta(calculo.intValue());
//						
//			BigDecimal contado = BigDecimal.ZERO ;
//			BigDecimal inicial = BigDecimal.ZERO ;
//			BigDecimal saldo = BigDecimal.ZERO ;
//			
//			for(Comisiones comisiones : lstOnline) {
//				if(comisiones.getLote().getTipoPago().equals("Contado")) {
//					contado = contado.add(comisiones.getLote().getMontoVenta());
//				}else {
//					inicial = inicial.add(comisiones.getLote().getMontoInicial());
//					BigDecimal calculoSaldo = comisiones.getLote().getMontoVenta().subtract(comisiones.getLote().getMontoInicial());
//					saldo = saldo.add(calculoSaldo);
//				}
//			}
//			totalSolesContado = totalSolesContado.add(contado);
//			totalSolesInicial = totalSolesInicial.add(inicial);
//			totalSolesPendiente = totalSolesPendiente.add(saldo) ;
//
//			
//			meta.setMontoContado(contado);
//			meta.setMontoInicial(inicial);
//			meta.setSaldoPendiente(saldo);
//			lstMeta.add(meta);
//		}
		
		
	}
	
	public void iniciarLazyUsuarioAsesor() {
		lstUsuarioLazy = new LazyDataModel<Usuario>() {
			private List<Usuario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Usuario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Usuario usuario : datasource) {
                    if (usuario.getId() == intRowKey) {
                        return usuario;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Usuario usuario) {
                return String.valueOf(usuario.getId());
            }

			@Override
			public List<Usuario> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
//				String numberLote="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String status = "%Vendido%";
				String dniAsesor = "%%";
				String dniSupervisor = "%%";
				
				if(teamSelected!=null)dniSupervisor ="%"+ teamSelected.getPersonSupervisor().getDni()+"%";
            
                Sort sort=Sort.by("personAssessor.surnames").ascending();
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
                
				Page<Usuario> pageUsuario;
				pageUsuario= usuarioService.findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus("%%", "", "", "", true, pageable);
				
				setRowCount((int) pageUsuario.getTotalElements());
				return datasource = pageUsuario.getContent();
			}
		};
	}
	
	public void iniciarLazyComisiones() {
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
                for (Comisiones comisiones : datasource) {
                    if (comisiones.getId() == intRowKey) {
                        return comisiones;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comisiones comisiones) {
                return String.valueOf(comisiones.getId());
            }

			@Override
			public List<Comisiones> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {				
				
                Sort sort=Sort.by("lote.fechaVendido").ascending();
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
                
				Page<Comisiones> pageComisiones = null;
				
				if(personSupervisorSelected==null) {
					pageComisiones= comisionesService.findByEstadoAndComision(true, comisionSelected, pageable);
				}else {
					pageComisiones= comisionesService.findByEstadoAndComisionAndPersonSupervisor(true, comisionSelected, personSupervisorSelected,pageable);
				}
				
				
				
//				if(opcionAsesor.equals("")) {
//					pageComisiones= comisionesService.findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(true,EstadoLote.VENDIDO.getName(), "%%", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);
//
//				}else if(opcionAsesor.equals("I")) {
//					pageComisiones = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "I", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);
//				}else if(opcionAsesor.equals("E")) {
//					pageComisiones = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "E", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);
//
//				}else {
//					Person personaSupervisor = new Person();
//					int id = Integer.parseInt(opcionAsesor) ;
//					personaSupervisor.setId(id);
//					pageComisiones= comisionesService.findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(true,EstadoLote.VENDIDO.getName(), personaSupervisor , "%%" , comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);
//
//				}
				setRowCount((int) pageComisiones.getTotalElements());
				return datasource = pageComisiones.getContent();
			}
		};
	}
	
	public Converter getConversorComision() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Comision c = null;
                    for (Comision si : lstComision) {
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
                    return ((Comision) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPersonSupervisor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Person c = null;
                    for (Person si : lstPersonSupervisor) {
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
	
	public Converter getConversorCargo() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Cargo c = null;
                    for (Cargo si : lstCargo) {
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
                    return ((Cargo) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorArea() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Area c = null;
                    for (Area si : lstArea) {
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
                    return ((Area) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorTeam() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Team c = null;
                    for (Team si : lstTeam) {
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
                    return ((Team) value).getId() + "";
                }
            }
        };
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
	public Integer getComisionContado() {
		return comisionContado;
	}
	public void setComisionContado(Integer comisionContado) {
		this.comisionContado = comisionContado;
	}
	public Integer getComisionCredito() {
		return comisionCredito;
	}
	public void setComisionCredito(Integer comisionCredito) {
		this.comisionCredito = comisionCredito;
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
	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
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
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public List<Comision> getLstComision() {
		return lstComision;
	}
	public void setLstComision(List<Comision> lstComision) {
		this.lstComision = lstComision;
	}
	public Comision getComisionSelected() {
		return comisionSelected;
	}
	public void setComisionSelected(Comision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}

	public LazyDataModel<Comisiones> getLstComisionesLazy() {
		return lstComisionesLazy;
	}
	public void setLstComisionesLazy(LazyDataModel<Comisiones> lstComisionesLazy) {
		this.lstComisionesLazy = lstComisionesLazy;
	}

	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public LazyDataModel<Usuario> getLstUsuarioLazy() {
		return lstUsuarioLazy;
	}
	public void setLstUsuarioLazy(LazyDataModel<Usuario> lstUsuarioLazy) {
		this.lstUsuarioLazy = lstUsuarioLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BigDecimal getTotalSolesContado() {
		return totalSolesContado;
	}
	public void setTotalSolesContado(BigDecimal totalSolesContado) {
		this.totalSolesContado = totalSolesContado;
	}
	public BigDecimal getTotalSolesInicial() {
		return totalSolesInicial;
	}
	public void setTotalSolesInicial(BigDecimal totalSolesInicial) {
		this.totalSolesInicial = totalSolesInicial;
	}
	public BigDecimal getTotalSolesPendiente() {
		return totalSolesPendiente;
	}
	public void setTotalSolesPendiente(BigDecimal totalSolesPendiente) {
		this.totalSolesPendiente = totalSolesPendiente;
	}
	//	public LazyDataModel<Lote> getLstLoteVendidoLazy() {
//		return lstLoteVendidoLazy;
//	}
//	public void setLstLoteVendidoLazy(LazyDataModel<Lote> lstLoteVendidoLazy) {
//		this.lstLoteVendidoLazy = lstLoteVendidoLazy;
//	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public List<Lote> getLstLotesVendidos() {
		return lstLotesVendidos;
	}
	public void setLstLotesVendidos(List<Lote> lstLotesVendidos) {
		this.lstLotesVendidos = lstLotesVendidos;
	}
	public List<Comisiones> getLstComisiones() {
		return lstComisiones;
	}
	public void setLstComisiones(List<Comisiones> lstComisiones) {
		this.lstComisiones = lstComisiones;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public List<Meta> getLstMeta() {
		return lstMeta;
	}
	public void setLstMeta(List<Meta> lstMeta) {
		this.lstMeta = lstMeta;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public List<Person> getLstPersonSupervisor() {
		return lstPersonSupervisor;
	}
	public void setLstPersonSupervisor(List<Person> lstPersonSupervisor) {
		this.lstPersonSupervisor = lstPersonSupervisor;
	}
	public Comisiones getComisionesSelected() {
		return comisionesSelected;
	}
	public void setComisionesSelected(Comisiones comisionesSelected) {
		this.comisionesSelected = comisionesSelected;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public Person getPersonSupervisorSelected() {
		return personSupervisorSelected;
	}
	public void setPersonSupervisorSelected(Person personSupervisorSelected) {
		this.personSupervisorSelected = personSupervisorSelected;
	}
	public List<MetaSupervisor> getLstMetaSupervisor() {
		return lstMetaSupervisor;
	}
	public void setLstMetaSupervisor(List<MetaSupervisor> lstMetaSupervisor) {
		this.lstMetaSupervisor = lstMetaSupervisor;
	}
	public CargoService getCargoService() {
		return cargoService;
	}
	public void setCargoService(CargoService cargoService) {
		this.cargoService = cargoService;
	}
	public LazyDataModel<Empleado> getLstEmpleadoLazy() {
		return lstEmpleadoLazy;
	}
	public void setLstEmpleadoLazy(LazyDataModel<Empleado> lstEmpleadoLazy) {
		this.lstEmpleadoLazy = lstEmpleadoLazy;
	}
	public Cargo getCargoFilter() {
		return cargoFilter;
	}
	public void setCargoFilter(Cargo cargoFilter) {
		this.cargoFilter = cargoFilter;
	}
	public List<Cargo> getLstCargo() {
		return lstCargo;
	}
	public void setLstCargo(List<Cargo> lstCargo) {
		this.lstCargo = lstCargo;
	}
	public AreaService getAreaService() {
		return areaService;
	}
	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
	public Area getAreaFilter() {
		return areaFilter;
	}
	public void setAreaFilter(Area areaFilter) {
		this.areaFilter = areaFilter;
	}
	public List<Area> getLstArea() {
		return lstArea;
	}
	public void setLstArea(List<Area> lstArea) {
		this.lstArea = lstArea;
	}
	public Team getTeamFilter() {
		return teamFilter;
	}
	public void setTeamFilter(Team teamFilter) {
		this.teamFilter = teamFilter;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public Comision getComision1() {
		return comision1;
	}
	public void setComision1(Comision comision1) {
		this.comision1 = comision1;
	}
	public Comision getComision2() {
		return comision2;
	}
	public void setComision2(Comision comision2) {
		this.comision2 = comision2;
	}
	public Comision getComision3() {
		return comision3;
	}
	public void setComision3(Comision comision3) {
		this.comision3 = comision3;
	}
	public String getFecha1() {
		return fecha1;
	}
	public void setFecha1(String fecha1) {
		this.fecha1 = fecha1;
	}
	public String getFecha2() {
		return fecha2;
	}
	public void setFecha2(String fecha2) {
		this.fecha2 = fecha2;
	}
	public String getFecha3() {
		return fecha3;
	}
	public void setFecha3(String fecha3) {
		this.fecha3 = fecha3;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public SimpleDateFormat getSdfMesAnio() {
		return sdfMesAnio;
	}
	public void setSdfMesAnio(SimpleDateFormat sdfMesAnio) {
		this.sdfMesAnio = sdfMesAnio;
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

	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}

	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
	}

	public List<ResumenComisionAsesor> getLstResumenMuestra() {
		return lstResumenMuestra;
	}

	public void setLstResumenMuestra(List<ResumenComisionAsesor> lstResumenMuestra) {
		this.lstResumenMuestra = lstResumenMuestra;
	}

	public Person getPersonSupervisorResumen() {
		return personSupervisorResumen;
	}

	public void setPersonSupervisorResumen(Person personSupervisorResumen) {
		this.personSupervisorResumen = personSupervisorResumen;
	}

	public BigDecimal getTotalComisionSupervisor() {
		return totalComisionSupervisor;
	}

	public void setTotalComisionSupervisor(BigDecimal totalComisionSupervisor) {
		this.totalComisionSupervisor = totalComisionSupervisor;
	}
	
}
