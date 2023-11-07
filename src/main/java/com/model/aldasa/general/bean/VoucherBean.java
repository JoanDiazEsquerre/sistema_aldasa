package com.model.aldasa.general.bean;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.AddExports;
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

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class VoucherBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	private String nombreArchivo = "Reporte de Voucher.xlsx";
	
	private boolean estado;
	private String tituloDialog;
	
	private Sucursal sucursal, sucursalDialog;
	private Imagen imagenSelected;
	private CuentaBancaria ctaBanFilter;
	
	private List<Sucursal> lstSucursal;
	private List<CuentaBancaria> lstCuentaBancaria;
	private List<CuentaBancaria> lstCuentaBancariaFilter;
	
	private LazyDataModel<Imagen> lstImagenLazy;
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	
	private Date fechaIni, fechaFin;
	
	private StreamedContent fileDes;
	
	@PostConstruct
	public void init() {
		estado=true;
		lstSucursal =sucursalService.findByEstado(true);
		
		fechaIni = new Date();
		fechaFin = new Date();
		iniciarLazy();
	}
	
	public void procesarExcel() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Voucher");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("FECHA Y HORA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MONTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("NRO OPERACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("CUENTA BANCARIA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("TIPO TRANSACCION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("BOLETA / FACTURA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("COMENTARIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TIPO PRODUCTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("USUARIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("SUCURSAL");cellSub1.setCellStyle(styleTitulo);


		fechaIni.setHours(0);
        fechaIni.setMinutes(0);
        fechaIni.setSeconds(0);
        fechaFin.setHours(23);
        fechaFin.setMinutes(59);
        fechaFin.setSeconds(59);
		
        List<Imagen> lstVoucher = new ArrayList<>();
        
        String sucursalName="%%";
        if(sucursal!=null) {
        	sucursalName="%"+sucursal.getRazonSocial()+"%";
        }
        
        if(ctaBanFilter != null) {
        	lstVoucher =  imagenService.findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(estado, ctaBanFilter, fechaIni, fechaFin);

        }else {
        	lstVoucher =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(estado, sucursalName, fechaIni, fechaFin);
        }
        
		
		int index = 1;
		BigDecimal total = BigDecimal.ZERO;
		
		for (Imagen imagen : lstVoucher) {
//			List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(d, true);	
			
			rowSubTitulo = sheet.createRow(index);
			cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(convertirHora(imagen.getFecha()));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(imagen.getMonto().doubleValue());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(imagen.getNumeroOperacion());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(imagen.getCuentaBancaria().getBanco().getNombre()+ " : " +imagen.getCuentaBancaria().getNumero()+" ("+ (imagen.getCuentaBancaria().getMoneda().equals("S") ? "SOLES)": "DÓLARES)"));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(imagen.getTipoTransaccion());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": imagen.getDocumentoVenta().getSerie()+"-"+imagen.getDocumentoVenta().getNumero());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(imagen.getDocumentoVenta()==null? imagen.getComentario() : obtenerDetalleBoleta(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
			
			cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": obtenerDetalleBoletaTipoProducto(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);

			cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": obtenerDetalleBoletaProyecto(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": obtenerDetalleBoletaManzana(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": obtenerDetalleBoletaLote(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
			
			
			cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(imagen.getUsuario()==null?"": imagen.getUsuario().getUsername());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(imagen.getCuentaBancaria().getSucursal().getRazonSocial());cellSub1.setCellStyle(styleBorder);
			
			total = total.add(imagen.getMonto());
			
			index++;
		}
		
		
		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(total.doubleValue());cellSub1.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 12; j++) {
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
	
	public String obtenerDetalleBoleta(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			detalle = detalle + det.getDescripcion()+".";
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaProyecto(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstProyectos = new ArrayList<>();
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {			
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaManzana(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstManzana = new ArrayList<>();
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}			
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaLote(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstLote = new ArrayList<>();

		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaTipoProducto(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstProducto = new ArrayList<>();

		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			String nombre =  det.getProducto().getDescripcion();
			if(lstProducto.isEmpty()) {
				lstProducto.add(nombre);
				detalle = detalle + nombre;
			}else {
				boolean busqueda = buscarPalabra(lstProducto, nombre);
				if(!busqueda) {
					lstProducto.add(nombre);
					detalle = detalle + nombre;
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public boolean buscarPalabra(List<String> lstPalabra, String busqueda) {
		boolean encuentra = false;
		for(String nom : lstPalabra) {
			if(nom.equals(busqueda)) {
				encuentra= true;
			}
		}
		return encuentra;
	}
	
	public void saveVoucher() {
		if(imagenSelected.getFecha() == null) {
			addErrorMessage("Debe ingresar una fecha.");
			return;
		}
		if(imagenSelected.getMonto() == null) {
			addErrorMessage("Debe ingresar un monto.");
			return;
		}else {
			if(imagenSelected.getMonto().compareTo(BigDecimal.ZERO) < 1) {
				addErrorMessage("El monto debe ser mayor que 0.");
				return;
			}
		}
		if(imagenSelected.getNumeroOperacion().equals("")) {
			addErrorMessage("Debe ingresar número de operación.");
			return;
		}
		
		if(imagenSelected.getCuentaBancaria()==null) {
			addErrorMessage("Debe seleccionar una cuenta bancaria.");
			return;
		}
		
		
		imagenSelected.setUsuario(navegacionBean.getUsuarioLogin());
		imagenSelected.setFechaRegistro(new Date());
		
		List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(true, imagenSelected.getFecha(), imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria(), imagenSelected.getTipoTransaccion());
		if(!buscarImagen.isEmpty()) {
			addErrorMessage("Ya existe el voucher");
			return;
		}
		
		Imagen imagen = imagenService.save(imagenSelected);
		if(imagen != null) {
			addInfoMessage("Se registró correctamente el voucher.");
			PrimeFaces.current().executeScript("PF('imagenDialog').hide();"); 

		}else {
			addErrorMessage("No se pudo guardar el voucher"); 
		}
	}
	
	public void listarCuentaBancaria() {
		lstCuentaBancaria = new ArrayList<>();
		
		if(sucursalDialog!= null) {
			lstCuentaBancaria=cuentaBancariaService.findByEstadoAndSucursal(true, sucursalDialog);
			if(!lstCuentaBancaria.isEmpty()) {
				imagenSelected.setCuentaBancaria(lstCuentaBancaria.get(0));
			}
		}
		
	}
	
	public void listarCuentaBancariaFilter() {
		lstCuentaBancariaFilter = new ArrayList<>();
		
		if(sucursal != null) {
			lstCuentaBancariaFilter=cuentaBancariaService.findByEstadoAndSucursal(true, sucursal);
		}
		
	}
	
	public void anularVoucher() {
		imagenSelected.setEstado(false);
		imagenService.save(imagenSelected);
		addInfoMessage("Se anuló correctamente el voucher.");
	}
	
	public void newImagen() {
		tituloDialog="NUEVO VOUCHER";
		sucursalDialog=null;
		lstCuentaBancaria = new ArrayList<>();
		imagenSelected = new Imagen();
		imagenSelected.setNombre("-");
		imagenSelected.setCarpeta("IMG-DOCUMENTO-VENTA");
		imagenSelected.setEstado(true);
		imagenSelected.setTipoTransaccion("DEPOSITO EN EFECTIVO");
		
	}
	
	public String convertirHora(Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}
	
	public void iniciarLazy() {

		lstImagenLazy = new LazyDataModel<Imagen>() {
			private List<Imagen> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Imagen getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Imagen i : datasource) {
                    if (i.getId() == intRowKey) {
                        return i;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Imagen imagen) {
                return String.valueOf(imagen.getId());
            }

			@Override
			public List<Imagen> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String numeroOp = "%" + (filterBy.get("numeroOperacion") != null ? filterBy.get("numeroOperacion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String cuenta = "%" + (filterBy.get("cuentaBancaria.numero") != null ? filterBy.get("cuentaBancaria.numero").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String tipoTransaccion = "%" + (filterBy.get("tipoTransaccion") != null ? filterBy.get("tipoTransaccion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
 
				
				
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
               
                Page<Imagen> page=null;
                String sucursalName="%%";
                if(sucursal!=null) {
                	sucursalName="%"+sucursal.getRazonSocial()+"%";
                }
               
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                if(ctaBanFilter != null) {
                    page =  imagenService.findByEstadoAndCuentaBancariaAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeAndFechaBetweenOrderByIdDesc(estado,ctaBanFilter,numeroOp,cuenta,tipoTransaccion,fechaIni,fechaFin, pageable); 

                }else {
                    page =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeAndFechaBetweenOrderByIdDesc(estado,sucursalName,numeroOp,cuenta,tipoTransaccion,fechaIni,fechaFin, pageable); 
                }
                
                
                setRowCount((int) page.getTotalElements());
                return datasource = page.getContent();
            }
		};
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
	
	public Converter getConversorCuentaBancaria() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancaria) {
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
                    return ((CuentaBancaria) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorCuentaBancariaFilter() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancariaFilter) {
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
                    return ((CuentaBancaria) value).getId() + "";
                }
            }
        };
    }

	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
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
	public Imagen getImagenSelected() {
		return imagenSelected;
	}
	public void setImagenSelected(Imagen imagenSelected) {
		this.imagenSelected = imagenSelected;
	}
	public LazyDataModel<Imagen> getLstImagenLazy() {
		return lstImagenLazy;
	}
	public void setLstImagenLazy(LazyDataModel<Imagen> lstImagenLazy) {
		this.lstImagenLazy = lstImagenLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public Sucursal getSucursalDialog() {
		return sucursalDialog;
	}
	public void setSucursalDialog(Sucursal sucursalDialog) {
		this.sucursalDialog = sucursalDialog;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
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
	public CuentaBancaria getCtaBanFilter() {
		return ctaBanFilter;
	}
	public void setCtaBanFilter(CuentaBancaria ctaBanFilter) {
		this.ctaBanFilter = ctaBanFilter;
	}
	public List<CuentaBancaria> getLstCuentaBancariaFilter() {
		return lstCuentaBancariaFilter;
	}
	public void setLstCuentaBancariaFilter(List<CuentaBancaria> lstCuentaBancariaFilter) {
		this.lstCuentaBancariaFilter = lstCuentaBancariaFilter;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	
	
		
}
