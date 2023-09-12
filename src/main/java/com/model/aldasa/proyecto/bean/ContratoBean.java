package com.model.aldasa.proyecto.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.primefaces.component.column.Column;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.ObservacionContrato;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Simulador;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.proyecto.jrdatasource.DataSourceCronogramaPago;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ObservacionContratoService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.PlantillaVentaService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVenta;

@ManagedBean
@ViewScoped
public class ContratoBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{voucherService}")
	private VoucherService voucherService;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{reportGenBo}")
	private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{observacionContratoService}")
	private ObservacionContratoService observacionContratoService;
	
	@ManagedProperty(value = "#{plantillaVentaService}")
	private PlantillaVentaService plantillaVentaService;
	
	
	private String meses[]= {"ENERO","FEBRERO","MARZO","ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE","DICIEMBRE"};
	
	private LazyDataModel<Contrato> lstContratoLazy;
	
	private List<Lote> lstLotesSinContrato;
	private List<Person> lstPerson;
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	private List<Simulador> lstSimulador = new ArrayList<>();
	private List<Simulador> lstSimuladorPrevio = new ArrayList<>();
	private List<ObservacionContrato> lstObservacionContrato = new ArrayList<>();
	
	private List<Cuota> lstCuotaVista = new ArrayList<>();

	private Lote loteSelected;
	private Contrato contratoSelected;
	private ObservacionContrato obsSelected;
	
	private StreamedContent fileDes;
	private String nombreArchivo = "Contrato.docx";

	private String nombreLoteSelected;
	private Date fechaVenta, fechaPrimeraCuota; 
	private Person persona1, persona2, persona3, persona4, persona5;
	private BigDecimal montoVenta, montoInicial, interes; 
	private BigDecimal sumaCuotaSI, sumaInteres, sumaCuotaTotal;
	private String tipoPago ="";
	private String observacion ="";
	private int nroCuotas;
	private boolean estado;
	
	private NumeroALetra numeroAletra = new NumeroALetra();
	
	private Map<String, Object> parametros;

    private DataSourceCronogramaPago dt; 

	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd"); 
	
	@PostConstruct
	public void init() {
		estado = true;
		iniciarLazy();
		listarPersonas();
		lstCuentaBancaria = cuentaBancariaService.findByEstadoAndSucursal(true, navegacionBean.getSucursalLogin());
		verCronogramaPago();
		observacion="";
		
	}
	
	public void cambiarEstadoFirma(Contrato contrato) {
		if(contrato.isFirma()) {
			contrato.setFirma(false);
		}else {
			contrato.setFirma(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la firma del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar la firma del contrato.");
		}
	}
	
	public void cambiarPagoTotalCuota(Cuota cuota) {
		if(cuota.getPagoTotal().equals("S")) {
			cuota.setPagoTotal("N");
		}else {
			cuota.setPagoTotal("S");
		}
		Cuota cambioFirma = cuotaService.save(cuota);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la cuota correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar la cuota.");
		}
	}
	
	public void deleteObs() {
		
		obsSelected.setEstado(false);
		observacionContratoService.save(obsSelected);
		addInfoMessage("Observación eliminado.");
		cargarListaObservacion();
	}
	
	public void cargarListaObservacion() {
		lstObservacionContrato = observacionContratoService.findByEstado(true);
	}
	
	public void anadirObsContrato() {
		
		if(observacion.equals("")) {
			addErrorMessage("Agrege una observación.");
			return;
		}
		
		ObservacionContrato obs = new ObservacionContrato(); 
		obs.setContrato(contratoSelected);
		obs.setUsuario(navegacionBean.getUsuarioLogin());
		obs.setObservacion(observacion);
		obs.setFechaRegistro(new Date());
		obs.setEstado(true);
		ObservacionContrato observ =  observacionContratoService.save(obs);
			
		if(observ!=null) {
			cargarListaObservacion();
			addInfoMessage("Se guardó correctamente.");
			observacion="";
			
		}else {
			addErrorMessage("No se pudo guardar.");
		}
		
		
	}
	
	public void editarFecha(Cuota cuota) {
		if(cuota.getFechaPago()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió la fecha correctamente.");
		}
		verCronogramaPago();
	}
	
	public void editarInteres(Cuota cuota) {
		if(cuota.getInteres()!=null) {
			cuota.setCuotaTotal(cuota.getCuotaSI().add(cuota.getInteres()));
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el interés correctamente.");
		}
		verCronogramaPago();
	}
	
							public void editarTotalCuota(Cuota cuota) {
		if(cuota.getCuotaTotal()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el total correctamente.");
		}
		verCronogramaPago();
	}
	
	public void editarAdelanto(Cuota cuota) {
		if(cuota.getAdelanto()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el adelanto correctamente.");
		}
		verCronogramaPago();
	}
	
	public void onCellEdit(CellEditEvent event) throws ParseException {
		
		Cuota cuota = lstCuotaVista.get(event.getRowIndex());
		Cuota cuotasiguiente = null;
		if(event.getRowIndex()!= (lstCuotaVista.size()-1)) {
			cuotasiguiente = lstCuotaVista.get(event.getRowIndex()+1);
		}
		Column column = (Column) event.getColumn();
		
		if (column.getId().equals("idCuotaSI")) {
			BigDecimal montoOld = new BigDecimal(event.getOldValue().toString());
						
			if(event.getNewValue()==null) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("Ingresar monto.");
				return;
			}
			
			BigDecimal monto = new BigDecimal(event.getNewValue().toString());
			
			
			if(monto.compareTo(BigDecimal.ZERO)==-1) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("El monto tiene que se mayor a 0.");
				return;
			}
			if(monto.compareTo(BigDecimal.ZERO)==0) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("El monto tiene que se mayor a 0.");
				return;
			}
			
		
			BigDecimal diferencia = BigDecimal.ZERO;   
			if(monto.compareTo(montoOld)==-1) {
				diferencia = montoOld.subtract(monto);
				if(cuotasiguiente!=null)cuotasiguiente.setCuotaSI(cuotasiguiente.getCuotaSI().add(diferencia));
			}else if(monto.compareTo(montoOld)==1){
				diferencia = monto.subtract(montoOld);
				if(cuotasiguiente!=null)cuotasiguiente.setCuotaSI(cuotasiguiente.getCuotaSI().subtract(diferencia));
			}
			
			cuota.setCuotaTotal(cuota.getCuotaSI().add(cuota.getInteres()));
			if(cuotasiguiente!=null)cuotasiguiente.setCuotaTotal(cuotasiguiente.getCuotaSI().add(cuotasiguiente.getInteres()));
			
			cuotaService.save(cuota);
			if(cuotasiguiente!=null)cuotaService.save(cuotasiguiente);
			verCronogramaPago();
			addInfoMessage("Se cambió correctamente el monto");
		}
			
    }

	public void generarPdfCronograma() {
		 if (lstCuotaVista == null || lstCuotaVista.isEmpty()) {
	            addInfoMessage("No hay datos para mostrar");
	        } else {
	        	
	        	dt = new DataSourceCronogramaPago();
	            for (int i = 0; i < lstCuotaVista.size(); i++) {
	               
	            	lstCuotaVista.get(i).setContrato(contratoSelected);;
	                dt.addResumenDetalle(lstCuotaVista.get(i));
	            }
	        	
	        	
	            parametros = new HashMap<String, Object>();
	            parametros.put("MZ-LT", contratoSelected.getLote().getManzana().getName()+"-"+contratoSelected.getLote().getNumberLote());
	            parametros.put("PROYECTO", contratoSelected.getLote().getProject().getName());	            
	            parametros.put("RUTAIMAGEN", getRutaGrafico("/recursos/images/LOGO.png"));
	            
	            String path = "secured/view/modulos/proyecto/reportes/jasper/repCronogramaPago.jasper"; 
	            reportGenBo.exportByFormatNotConnectDb(dt, path, "pdf", parametros, "CRONOGRAMA DE PAGO " , contratoSelected.getLote().getManzana().getName()+"-"+contratoSelected.getLote().getNumberLote());
	            dt = null;
	            parametros = null;
	      
	        }
	}
	
	public void verCronogramaPago() {
		lstCuotaVista = new ArrayList<>();
		List<Cuota>lstCuotaPagadas=cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("S", true, contratoSelected);
		List<Cuota>lstCuotaPendientes = cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("N", true, contratoSelected);
		lstCuotaVista.addAll(lstCuotaPagadas);
		lstCuotaVista.addAll(lstCuotaPendientes);
		
		sumaCuotaSI = BigDecimal.ZERO;
		sumaInteres = BigDecimal.ZERO;
		sumaCuotaTotal = BigDecimal.ZERO;
		
		for(Cuota c:lstCuotaVista) {
			if(c.getNroCuota()!=0 && c.isPrepago()==false) {
				sumaCuotaSI = sumaCuotaSI.add(c.getCuotaSI());
				sumaInteres = sumaInteres.add(c.getInteres());
				sumaCuotaTotal = sumaCuotaTotal.add(c.getCuotaTotal());
			}
			
		}
		
	}
	
	public void cambiarTipoPago() {
		if(tipoPago.equals("Contado")) {
			montoInicial=null;
			nroCuotas=0;
			interes=null;

		}
	}
	
	public void botonVerCuota(boolean agregaBD, Contrato contrato) {
		PrimeRequestContext context = PrimeRequestContext.getCurrentInstance(); 
		if(fechaVenta==null) {
			addErrorMessage("Ingresar Fecha de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(montoVenta==null) {
			addErrorMessage("Ingresar Monto de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}else if(montoVenta==BigDecimal.ZERO) {
			addErrorMessage("El monto de venta debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(montoInicial==null) {
			addErrorMessage("Ingresar Monto Inicial.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}else if(montoInicial==BigDecimal.ZERO) {
			addErrorMessage("El monto inicial debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(nroCuotas==0) {
			addErrorMessage("El número de cuotas debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(interes==null) {
			addErrorMessage("Ingresar Interés.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(fechaPrimeraCuota==null) {
			addErrorMessage("Ingresar Fecha de Primera Cuota.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(persona1==null) {
			addErrorMessage("Ingresar Persona de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		
		lstSimuladorPrevio.clear();
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota("0");
		filaInicio.setFechaPago(fechaVenta);
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(BigDecimal.ZERO);
		filaInicio.setInteres(BigDecimal.ZERO);
		filaInicio.setCuotaTotal(BigDecimal.ZERO);
		lstSimuladorPrevio.add(filaInicio);
		
		if(agregaBD) {
			Cuota cuotaCero = new Cuota();
			cuotaCero.setNroCuota(0);
			cuotaCero.setFechaPago(fechaVenta);
			cuotaCero.setCuotaSI(montoInicial);
			cuotaCero.setInteres(BigDecimal.ZERO);
			cuotaCero.setCuotaTotal(montoInicial);
			cuotaCero.setAdelanto(BigDecimal.ZERO);

			RequerimientoSeparacion requerimiento = requerimientoSeparacionService.findAllByLoteAndEstado(contrato.getLote(), "Atendido");
			if(requerimiento!=null) {
				Voucher voucher = voucherService.findByRequerimientoSeparacion(requerimiento);
				if(voucher!=null) {
					List<DetalleDocumentoVenta> detalle = detalleDocumentoVentaService.findByVoucherIdAndEstado(voucher.getId(), true);
					if(!detalle.isEmpty()) {
						for(DetalleDocumentoVenta d: detalle) {
							if(d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("F") || d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")) {
								cuotaCero.setAdelanto(d.getImporteVenta());
							}
						}
					}
					
				}
			} 
			
			cuotaCero.setPagoTotal("N");
			cuotaCero.setContrato(contrato);
			cuotaCero.setEstado(true);
			cuotaCero.setOriginal(true);
			cuotaService.save(cuotaCero);
		}
		
		
		BigDecimal montoInteres=BigDecimal.ZERO;
		BigDecimal montoDeuda = montoVenta.subtract(montoInicial);
		BigDecimal sumaDecimales = BigDecimal.ZERO;
		BigDecimal sumaCuotaSI = BigDecimal.ZERO;
		BigDecimal cuota = montoDeuda.divide(new BigDecimal(nroCuotas), 0, RoundingMode.HALF_UP);
		
		if(interes.compareTo(BigDecimal.ZERO)==0) {	
			
			for(int i=0; i<nroCuotas;i++) {
				Simulador filaCouta = new Simulador();
				if(i==0) {
					filaCouta.setFechaPago(fechaPrimeraCuota);
				}else {
					filaCouta.setFechaPago(sumarRestarMeses(fechaPrimeraCuota, i));
				}
				filaCouta.setNroCuota((i+1)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(BigDecimal.ZERO);
				filaCouta.setCuotaTotal(cuota);
				
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI());
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				lstSimuladorPrevio.add(filaCouta);
			}
			for(Simulador s:lstSimuladorPrevio) {
				if(s.getNroCuota().equals("1")) {
					BigDecimal decimales = sumaCuotaSI.subtract(montoDeuda);
					s.setCuotaSI(s.getCuotaSI().subtract(decimales));
					s.setCuotaTotal(s.getCuotaSI());
					
				}
			}
			
			for(Simulador filaCuota:lstSimuladorPrevio) {
				if(agregaBD) {
					if(!filaCuota.getNroCuota().equals("0")) {
						Cuota cuotaBD = new Cuota();
						cuotaBD.setNroCuota(Integer.parseInt(filaCuota.getNroCuota()) );
						cuotaBD.setFechaPago(filaCuota.getFechaPago());
						cuotaBD.setCuotaSI(filaCuota.getCuotaSI());
						cuotaBD.setInteres(filaCuota.getInteres());
						cuotaBD.setCuotaTotal(filaCuota.getCuotaTotal());
						cuotaBD.setAdelanto(filaCuota.getInicial());
						cuotaBD.setPagoTotal("N");
						cuotaBD.setContrato(contrato);
						cuotaBD.setEstado(true);
						cuotaBD.setOriginal(true);
						cuotaService.save(cuotaBD);
					}
				}
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(montoDeuda);
			filaTotal.setInteres(BigDecimal.ZERO);
			filaTotal.setCuotaTotal(montoDeuda);
			lstSimuladorPrevio.add(filaTotal);
			
		}else {
			
			BigDecimal porc = interes;
			BigDecimal porcMin = (porc.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			BigDecimal totalInteres = montoDeuda.multiply(porcMin);
//			montoInteres = montoDeuda.multiply(porcMin);     
			montoInteres = totalInteres.divide(new BigDecimal(nroCuotas), 0, RoundingMode.HALF_UP); 
			
			

			for(int i=0; i<nroCuotas;i++) {
				Simulador filaCouta = new Simulador();
				if(i==0) {
					filaCouta.setFechaPago(fechaPrimeraCuota);
					filaCouta.setCuotaSI(montoDeuda.subtract(cuota.multiply(new BigDecimal(nroCuotas-1))));
					filaCouta.setInteres(totalInteres.subtract(montoInteres.multiply(new BigDecimal(nroCuotas-1))));
				}else {
					filaCouta.setFechaPago(sumarRestarMeses(fechaPrimeraCuota, i));
					filaCouta.setCuotaSI(cuota);
					filaCouta.setInteres(montoInteres);
				}
				filaCouta.setNroCuota((i+1)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				
				filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.add(montoInteres).subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				lstSimuladorPrevio.add(filaCouta);
				
			}
			
			for(Simulador s:lstSimuladorPrevio) {
				if(s.getNroCuota().equals("1")) {
					BigDecimal decimales = sumaCuotaSI.subtract(montoDeuda);
					s.setCuotaSI(s.getCuotaSI().subtract(decimales));
					s.setCuotaTotal(s.getCuotaSI().add(s.getInteres()));
					
				}
			}
			
			for(Simulador filaCouta:lstSimuladorPrevio) {
				if(agregaBD) {
					if(!filaCouta.getNroCuota().equals("0")) {
						Cuota cuotaBD = new Cuota();
						cuotaBD.setNroCuota(Integer.parseInt(filaCouta.getNroCuota()));
						cuotaBD.setFechaPago(filaCouta.getFechaPago());
						cuotaBD.setCuotaSI(filaCouta.getCuotaSI());
						cuotaBD.setInteres(filaCouta.getInteres());
						cuotaBD.setCuotaTotal(filaCouta.getCuotaTotal());
						cuotaBD.setAdelanto(filaCouta.getInicial());
						cuotaBD.setPagoTotal("N");
						cuotaBD.setContrato(contrato);
						cuotaBD.setEstado(true);
						cuotaBD.setOriginal(true);
						cuotaService.save(cuotaBD);
					}
				}
			}
			
			BigDecimal sumaInicial = BigDecimal.ZERO;
			BigDecimal sumaSI = BigDecimal.ZERO;
			BigDecimal sumaInteres = BigDecimal.ZERO;
			BigDecimal sumaTotal = BigDecimal.ZERO;
			
			for(Simulador sim:lstSimuladorPrevio) {
				sumaInicial=sumaInicial.add(sim.getInicial());
				sumaSI=sumaSI.add(sim.getCuotaSI());
				sumaInteres=sumaInteres.add(sim.getInteres());
				sumaTotal=sumaTotal.add(sim.getCuotaTotal());
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(sumaInicial);
			filaTotal.setCuotaSI(sumaSI);
			filaTotal.setInteres(sumaInteres);
			filaTotal.setCuotaTotal(sumaTotal);
			lstSimuladorPrevio.add(filaTotal);
			
		}
		
		context.getCallbackParams().put("noEsValido", true);
	}
	
	public void anularContrato() {
		contratoSelected.setEstado(false);
		contratoService.save(contratoSelected);
		contratoSelected.getLote().setRealizoContrato("N");
		loteService.save(contratoSelected.getLote());
		addInfoMessage("Contrato Anulado Correctamente.");

		
		if(contratoSelected.getTipoPago().equals("Crédito")) {
			List<Cuota> lstcuota = cuotaService.findByContratoAndEstado(contratoSelected, true);
			for(Cuota c:lstcuota) {
				c.setEstado(false);
				c.setOriginal(false);
				cuotaService.save(c);
			}
		}	
	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void generarExcel() throws IOException, XmlException {
		if(contratoSelected.getTipoPago().equals("Crédito")) {
			if(contratoSelected.getLote().getProject().getId() ==1) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==2) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==3) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==4) { 
				
			}
			if(contratoSelected.getLote().getProject().getId() ==5) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==6) { 
				formatoCredito();		
			}
			if(contratoSelected.getLote().getProject().getId() ==7) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==8) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==9) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==10) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==11) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==12) { 
				formatoCredito();		
			}
			if(contratoSelected.getLote().getProject().getId() ==13) { 
				formatoCredito();
			}
			
			
		}else {
			if(contratoSelected.getLote().getProject().getId() ==1) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==2) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==3) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==4) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==5) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==6) { 
				formatoContado();			
			}
			if(contratoSelected.getLote().getProject().getId() ==7) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==8) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==9) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==10) { 
				formatoContado_ASR_IV();
			}
			if(contratoSelected.getLote().getProject().getId() ==11) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==12) { 
				formatoContado();			
			}
			if(contratoSelected.getLote().getProject().getId() ==13) { 
				formatoContado();
			}
			
		}
	}
	
	public void formatoContado_ASR_IV() throws XmlException {
		// initialize a blank document
			XWPFDocument document = new XWPFDocument();
			// create a new file
			// create a new paragraph paragraph
			XWPFParagraph paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			
			XWPFRun runTitle = paragraph.createRun();
			runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
			runTitle.setBold(true);
			runTitle.setFontFamily("Century Gothic");
			runTitle.setFontSize(12);

			
			XWPFParagraph paragraph2 = document.createParagraph();
			paragraph2.setAlignment(ParagraphAlignment.BOTH);
			
			XWPFRun run = paragraph2.createRun();
			run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
			
			run = paragraph2.createRun();run.setText("ABARCA BIENES RAÍCES E.I.R.L.");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON RUC Nº 20609173093, REPRESENTADA POR SU ");estiloNormalTexto(run);
					
			run = paragraph2.createRun();run.setText("TITULAR GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11389528 "); estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FICAL EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
			
			List<Person> lstPersonas = new ArrayList<>();
			if(contratoSelected.getPersonVenta()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta());
			}
			if(contratoSelected.getPersonVenta2()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta2());
			}
			if(contratoSelected.getPersonVenta3()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta3());
			}
			if(contratoSelected.getPersonVenta4()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta4());
			}
			if(contratoSelected.getPersonVenta5()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta5());
			}
				
			int contador = 1;
			for(Person p: lstPersonas) {
				run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run); 
				run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
				
				
				
				
				if(lstPersonas.size()==contador){
					run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
				}else {
					run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
				}
				contador++;
			}
			run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("CON INTERVENCIÓN DE EL SR. ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. Nº 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; CON EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
			
//			run = paragraph2.createRun();run.setText("");estiloNormalTexto(run);
//			run = paragraph2.createRun();run.setText("");estiloNegritaTexto(run);
				
			XWPFParagraph paragraphPrimero = document.createParagraph();
			paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
			
			XWPFRun runPrimero = paragraphPrimero.createRun();
			runPrimero.addBreak();runPrimero.addBreak();
			runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
			runPrimero.setUnderline(UnderlinePatterns.SINGLE);
			
			String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
					+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
					+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "</w:abstractNum>";

			String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
					+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
					+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "</w:abstractNum>";

					
			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
			CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
			XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
			XWPFNumbering numbering = document.createNumbering();
			BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
			BigInteger numID = numbering.addNum(abstractNumID);

			XWPFParagraph paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

			paragrapha = document.createParagraph();
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("1. ");
			run.setBold(true);
			run = paragrapha.createRun();
			run.setText("1.	UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO YENCALA BOGGIANO– COD. PREDIO. 7_6159260_67034, ÁREA HA. "
					+ "4.1730 U.C. 67034, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, EN LO SUCESIVO DENOMINADO EL "
					+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N°11072174, ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N°II- SEDE CHICLAYO.");estiloNormalTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("2. ");
			run.setBold(true);
			run = paragrapha.createRun();
			run.setText("QUE CON FECHA 18 DE MARZO DE 2023 SE SUSCRIBIÓ UN CONTRATO DE COMPRA VENTA CON ARRAS CONFIRMATORIAS ENTRE ALAN CRUZADO "
					+ "BALCAZAR Y ABARCA BIENES RAÍCES EIRL, MEDIANTE EL CUAL SE ADQUIRIO LA PROPIEDAD DESCRTITA EN LA CLAUSULA 1.1 PARA EL DESARROLLO "
					+ "DEL PROYECTO DE LOTIZACION MATERIA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
					
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("3. ");
			run.setBold(true);
			run = paragrapha.createRun();
			run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE "
					+ "DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE IV Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
			
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SEGUNDO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBJETO");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
					+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 
			

			paragrapha = document.createParagraph();
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
					+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
					+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA:"+contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run);
			
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.setUnderline(UnderlinePatterns.SINGLE); 
			run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
			run.addBreak();
			run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
			run.addBreak();
			run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
			run.addBreak();
			run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
			run.addBreak();
			run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
			run.addBreak();
			run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
			run.addBreak();run.addBreak();

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
					+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("TERCERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("CUARTO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA, PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS "
					+ "ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
					+ "PARA LO CUAL ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
					+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
				
				
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
				
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
					+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
					+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
					
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
					+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO "
					+ "DEL PROYECTO.");estiloNormalTexto(run);	
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
					+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
					+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
				
					
						
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();	
			run.setText("QUINTO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODA DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
					+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
					+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
					+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
					+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
			
				
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS)LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
					+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
					+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS)LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
					+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
					+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
					+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS)LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
						
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SEXTO");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
				
			run.addBreak();
			run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("A) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
					+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE ASI COMO A REALIZAR EL MISMO PROCEDIMIENTO ANTE LA OFICINA DE REGISTROS PUBLICOS.");estiloNormalTexto(run); 

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("B) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
					+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("C) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("D) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("E) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
					+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
					+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
					
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("F) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
					+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
					+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
			run.setBold(true);
			run.setFontFamily("Century Gothic");
			run.setFontSize(9);
				
			run.addBreak();
			run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("A) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
				run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("B) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
					+ "PRINCIPALES, AVENIDAS Y AEAS VERDES.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("C) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("D) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("OCTAVO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun(); 
			run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
					+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
					+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
					+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("NOVENO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE HARÁN CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL "
					+ "ARTÍCULO N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE "
					+ "ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS "
					+ "PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA "
					+ "EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN "
					+ "QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO "
					+ "ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
					+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
					+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("DOMICILIO");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
					+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
					+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
					+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
					+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
					+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
					+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
			run.setText("___________________________________");estiloNormalTexto(run);
			run.addBreak();
			run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
			run.addBreak();
			run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		     
				
				
				try {			
					 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
			            File file = new File(filePath);
			            FileOutputStream out = new FileOutputStream(file);
			            document.write(out);
			            out.close();
			            fileDes=null;
			            fileDes = DefaultStreamedContent.builder()
			                    .name(nombreArchivo)
			                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
			                    .build();
		            
		    		 
		      
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		
	}
	
	public void formatoCredito() throws IOException, XmlException {

		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN, DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C.");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText(", CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
				
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN CAL. LOS AMARANTOS NRO. 245 URB. FEDERICO VILLARREAL, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run);
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);

		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+contratoSelected.getLote().getProject().getCodigoPredio().toUpperCase()+", "
						+ "ÁREA HA. "+contratoSelected.getLote().getProject().getAreaHectarea().toUpperCase()+" U.C. "+contratoSelected.getLote().getProject().getUnidadCatastral().toUpperCase()+", DISTRITO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getName()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName()+", DEPARTAMENTO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName()+", "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText(", DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getZonaRegistral()+".");estiloNegritaTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN "+contratoSelected.getLote().getProject().getName().toUpperCase()+" Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		
		
		
	

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA:"+contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE COMPROMETE A ENTREGAR LA UNIDAD INMOBILIARIA MATERIA DE LA PRESENTE COMPRA VENTA EN LAS "
				+ "CONDICIONES QUE SE ESTIPULAN EN EL PRESENTE CONTRATO Y QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACEPTAN EXPRESAMENTE.");estiloNormalTexto(run); 
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES)");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("EL MONTO DE  CON DEPÓSITO(S) ");estiloNormalTexto(run);
 		
 		
 		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
 		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		
		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
		
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO "
				+ "DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y "
				+ "DOCUMENTOS EMITIDOS POR ÉL, SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER "
				+ "CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE ESTA CLÁUSULA "
				+ "TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE EL (LOS) LOTE(S), "
				+ "SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, "
				+ "PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES "
				+ "EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL "
				+ "INMUEBLE, MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA "
				+ "O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LUEGO REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR "
				+ "LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA, AFIRMAMENTO DE CALLES PRINCIPALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTAS Y ESCRITURAS PÚBLICAS A LA CANCELACIÓN DE LOS SALDOS POR COMPRA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run); 
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN "
				+ "DE MEJORAS, QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA "
				+ "DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD "
				+ "CON EL ARTÍCULO N°1345 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, "
				+ "A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA "
				+ "Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, "
				+ "SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS "
				+ "RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, "
				+ "NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, "
				+ "SOMETERSE A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y "
				+ "SEÑALANDO COMO TALES, LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, LUGARES A "
				+ "LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE MEDIANTE CARTA CURSADA POR "
				+ "CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS "
				+ "EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO "
				+ "DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA PREPAGO DE CAPITAL; EN CASO, QUE "
				+ "EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL  DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA LO CUAL EL NOTIFICADOR "
				+ "SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL "
				+ "EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS NORMAS DEL "
				+ "CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  

	        
//	        XWPFTable table = document.createTable();
	        
        //Creating first Row
//        XWPFTableRow row1 = table.getRow(0);
//        XWPFTableCell cell = table.getRow(0).getCell(0);
//        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//        row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
//
//        //Creating second Row
//        XWPFTableRow row2 = table.createRow();
//        row2.getCell(0).setText("Second Row, First Column");
//        row2.getCell(1).setText("Second Row, Second Column");
//
//        //create third row
//        XWPFTableRow row3 = table.createRow();
//        row3.getCell(0).setText("Third Row, First Column");
//        row3.getCell(1).setText("Third Row, Second Column");
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	
	public void listarLotesSinContrato() {
		lstLotesSinContrato = new ArrayList<>();
		List<Lote> lstLotesVendidos = loteService.findByStatusAndProjectSucursal(EstadoLote.VENDIDO.getName(), navegacionBean.getSucursalLogin());
		if(!lstLotesVendidos.isEmpty()) {
			for(Lote lote:lstLotesVendidos) {
				if(lote.getRealizoContrato().equals("N")) {
					lstLotesSinContrato.add(lote);
				}
			}
		}
	}
	
	public void seleccionarLote() {
		nombreLoteSelected = loteSelected.getNumberLote()+" -  MZ "+loteSelected.getManzana().getName()+" / "+ loteSelected.getProject().getName();
		
		if(loteSelected.getTipoPago()!=null) {
			if(loteSelected.getTipoPago().equals("Crédito")) {
				montoInicial = loteSelected.getMontoInicial();
				nroCuotas = loteSelected.getNumeroCuota();
				interes = loteSelected.getInteres();
			}else {
				montoInicial = BigDecimal.ZERO;
				nroCuotas = 0 ;
				interes = BigDecimal.ZERO;
			}
		}
		
			
		fechaVenta = loteSelected.getFechaVendido();
		montoVenta = loteSelected.getMontoVenta();
		tipoPago = loteSelected.getTipoPago();
		persona1 = loteSelected.getPersonVenta();
	
		
		
		
		
		
	}
	
	public void eliminarLoteSelected() {
		loteSelected=null;
		
		fechaVenta=null;
		montoVenta = null;
		tipoPago = null;
		montoInicial = null;
		nroCuotas = 0;
		interes = null;
		persona1 = null;
		persona2=null;
		persona3 = null;
		persona4 = null; 
		persona5 = null;
		fechaPrimeraCuota=null;
	}
	
	public void saveContrato() {
	
		if(loteSelected == null) {
			addErrorMessage("Seleccionar un lote.");
			return ;  
		}
		if(fechaVenta==null) {
			addErrorMessage("Seleccionar fecha de venta.");
			return ;  
		}
		if(montoVenta==null) {
			addErrorMessage("Ingresar monto de venta.");
			return ;  
		}else if(montoVenta==BigDecimal.ZERO){
			addErrorMessage("Monto venta debe ser mayor a 0.");
			return ;
		}
		
		if(tipoPago.equals("")) {
			addErrorMessage("Seleccionar tipo de pago.");
			return ;  
		}else if(tipoPago.equals("Crédito")){
			if(montoInicial==null) {
				addErrorMessage("Ingresar monto inicial.");
				return ;  
			}else if (montoInicial==BigDecimal.ZERO){
				addErrorMessage("Monto inicial debe ser mayor a 0.");
				return ;
			}
			if(nroCuotas==0){
				addErrorMessage("Número de cuotas debe ser mayor a 0.");
				return ;
			}
			if(interes==null) {
				addErrorMessage("Ingresar interes.");
				return ;  
			}
			if(fechaPrimeraCuota==null) {
				addErrorMessage("Ingresar la fecha de primera cuota.");
				return ;  
			}
		}
		
		if(persona1==null && persona2==null && persona3==null && persona4==null && persona5==null ) {
			addErrorMessage("Seleccionar al menos una persona de venta.");
			return ;  
		}
		Contrato contrato = new Contrato();
		contrato.setLote(loteSelected);
		contrato.setEstado(true);
		contrato.setFechaVenta(fechaVenta);
		contrato.setMontoVenta(montoVenta);
		contrato.setTipoPago(tipoPago);
		
		if(contrato.getTipoPago().equals("Contado")) {
			contrato.setMontoInicial(null);
			contrato.setNumeroCuota(null); 
			contrato.setInteres(null);

		}else {
			contrato.setMontoInicial(montoInicial);
			contrato.setNumeroCuota(nroCuotas); 
			contrato.setInteres(interes);
			contrato.setFechaPrimeraCuota(fechaPrimeraCuota); 
		}
		
		contrato.setPersonVenta(persona1);
		contrato.setPersonVenta2(persona2);
		contrato.setPersonVenta3(persona3);
		contrato.setPersonVenta4(persona4);      
		contrato.setPersonVenta5(persona5);
		
		Contrato contratoSave = contratoService.save(contrato);
		
		if(contratoSave!= null) {
			if(contratoSave.getTipoPago().equals("Crédito")) {
				botonVerCuota(true, contratoSave); 
			}else {
				Cuota cuota = new Cuota();
				cuota.setNroCuota(0);
				cuota.setFechaPago(fechaVenta);
				cuota.setCuotaSI(montoVenta);
				cuota.setInteres(BigDecimal.ZERO);
				cuota.setCuotaTotal(montoVenta);
				cuota.setAdelanto(BigDecimal.ZERO);
				
				RequerimientoSeparacion requerimiento = requerimientoSeparacionService.findAllByLoteAndEstado(contrato.getLote(), "Atendido");
				if(requerimiento!=null) {
					Voucher voucher = voucherService.findByRequerimientoSeparacion(requerimiento);
					if(voucher!=null) {
						List<DetalleDocumentoVenta> detalle = detalleDocumentoVentaService.findByVoucherIdAndEstado(voucher.getId(), true); 
						if(!detalle.isEmpty()) {
							for(DetalleDocumentoVenta d: detalle) {
								if(d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("F") || d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")) {
									cuota.setAdelanto(d.getImporteVenta());
								}
							}
							
						}
						
					}
				} 
				
				cuota.setPagoTotal("N");
				cuota.setContrato(contrato);
				cuota.setEstado(true);
				cuota.setOriginal(true);
				cuotaService.save(cuota);
			}
			
			
			List<PlantillaVenta> lstPlantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSave.getLote());
			if(!lstPlantilla.isEmpty()) {
				PlantillaVenta p = lstPlantilla.get(lstPlantilla.size()-1);
				p.setRealizoContrato(true);
				plantillaVentaService.save(p);
			}
			
			
		}
		
		loteSelected.setRealizoContrato("S"); 
		loteService.save(loteSelected);
		eliminarLoteSelected();
		addInfoMessage("Se guardó correctamente el contrato"); 
		
	
		

	}
	
	public void iniciarLazy() {

		lstContratoLazy = new LazyDataModel<Contrato>() {
			private List<Contrato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Contrato getRowData(String rowKey) {     
                int intRowKey = Integer.parseInt(rowKey);
                for (Contrato contrato : datasource) {
                    if (contrato.getId() == intRowKey) {
                        return contrato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Contrato contrato) {
                return String.valueOf(contrato.getId());
            }

			@Override
			public List<Contrato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String proyecto = "%" + (filterBy.get("lote.project.name") != null ? filterBy.get("lote.project.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String manzana = "%" + (filterBy.get("lote.manzana.name") != null ? filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numLote = "%" + (filterBy.get("lote.numberLote") != null ? filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				
				
               Sort sort=Sort.by("fechaVenta").ascending();
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
               
                Page<Contrato> pageContrato=null;
               
                
                pageContrato= contratoService.findByEstadoAndLoteProjectSucursalAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, navegacionBean.getSucursalLogin(),proyecto, manzana, numLote, pageable);
                
                setRowCount((int) pageContrato.getTotalElements());
                return datasource = pageContrato.getContent();
            }
		};
	}
	
	public void formatoContado() throws IOException, XmlException {

		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN, DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C.");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText(", CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
				
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN CAL. LOS AMARANTOS NRO. 245 URB. FEDERICO VILLARREAL, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);

		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+contratoSelected.getLote().getProject().getCodigoPredio().toUpperCase()+", "
						+ "ÁREA HA. "+contratoSelected.getLote().getProject().getAreaHectarea().toUpperCase()+" U.C. "+contratoSelected.getLote().getProject().getUnidadCatastral().toUpperCase()+", DISTRITO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getName()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName()+", DEPARTAMENTO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName()+", "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText(", DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getZonaRegistral()+".");estiloNegritaTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN "+contratoSelected.getLote().getProject().getName().toUpperCase()+" Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		
		
		
	

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA:"+contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		

		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DESPÓSITOS ");estiloNormalTexto(run);
	
 		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
 		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
 		
		
	
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL "
				+ "INMUEBLE, MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA "
				+ "O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LUEGO REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
			
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
				
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA, AFIRMAMENTO DE CALLES PRINCIPALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTAS Y ESCRITURAS PÚBLICAS, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN "
				+ "DE MEJORAS, QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA "
				+ "DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS "
				+ "CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, LUGARES A LOS QUE SERÁN "
				+ "DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, "
				+ "DEBERÁ SER COMUNICADO A LA OTRA PARTE MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE 5 "
				+ "(CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS "
				+ "Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	public  void estiloCentrarCeldaTabla(XWPFTableCell cell) {  
        CTTc cttc = cell.getCTTc();  
        CTTcPr ctPr = cttc.addNewTcPr();  
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
    } 
	
	public void estiloNegritaTexto(XWPFRun run) {
		run.setBold(true); run.setFontFamily("Century Gothic");run.setFontSize(9);
	}
	public void estiloNormalTexto(XWPFRun run) {
		run.setFontFamily("Century Gothic");run.setFontSize(9);
	}

	public void setTableWidth(XWPFTable table,String width){  
        CTTbl ttbl = table.getCTTbl();  
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
        CTJc cTJc=tblPr.addNewJc();  
        cTJc.setVal(STJc.Enum.forString("center"));  
        tblWidth.setW(new BigInteger(width));  
        tblWidth.setType(STTblWidth.DXA);  
    } 
	
	public  void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {  
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {  
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);  
            if ( cellIndex == fromCell ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    }  
	
	public  void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {  
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {  
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);  
            if ( rowIndex == fromRow ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    } 
	 
	public  void fillTable(XWPFTable table, List<String> texto) {  
        for (int rowIndex = 0; rowIndex < table.getNumberOfRows(); rowIndex++) {  
            XWPFTableRow row = table.getRow(rowIndex);  
            row.setHeight(380);  
            for (int colIndex = 0; colIndex < row.getTableCells().size(); colIndex++) {  
                XWPFTableCell cell = row.getCell(colIndex);  
                
                cell.setText(texto.get(colIndex));
//                if(rowIndex%2==0){  
//                     setCellText(cell, " cell " + rowIndex + colIndex + " ", "D4DBED", 1000);  
//                }else{  
//                     setCellText(cell, " cell " + rowIndex + colIndex + " ", "AEDE72", 1000);  
//                }  
            }  
        }  
    }
	
	public Date sumarRestarMeses(Date fecha, int meses) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, meses);
		return calendar.getTime();
	}
	
	public void simularCuotas (Contrato contrato) {
		lstSimulador.clear();
		
//		Simulador filaInicio = new Simulador();
//		filaInicio.setNroCuota("0");
//		filaInicio.setFechaPago(contrato.getFechaVenta());
//		filaInicio.setInicial(contrato.getMontoInicial());
//		filaInicio.setCuotaSI(BigDecimal.ZERO);
//		filaInicio.setInteres(BigDecimal.ZERO);
//		filaInicio.setCuotaTotal(BigDecimal.ZERO);
//		lstSimulador.add(filaInicio);
//		
//		// una pista
		BigDecimal totalSI=BigDecimal.ZERO;
		BigDecimal totalInteres=BigDecimal.ZERO;
		BigDecimal totalCuotaTotal=BigDecimal.ZERO;
		
		List<Cuota> lstCuotaContrato = cuotaService.findByContratoAndOriginal(contrato, true);
		int contador = 0;
		for(Cuota c:lstCuotaContrato) {
			Simulador simulador = new Simulador();
			simulador.setNroCuota(c.getNroCuota()+"");
			simulador.setFechaPago(c.getFechaPago()); 
			if(contador==0) {
				simulador.setInicial(contrato.getMontoInicial());
				simulador.setCuotaSI(BigDecimal.ZERO);
			}else {
				simulador.setInicial(BigDecimal.ZERO);
				simulador.setCuotaSI(c.getCuotaSI());
			}
			
			simulador.setInteres(c.getInteres());
			simulador.setCuotaTotal(c.getCuotaTotal());
			lstSimulador.add(simulador);
			
			totalSI = totalSI.add(simulador.getCuotaSI()); //totalSI+simulador.getCuotaSI(); lo sbigdecimal se sunan de estam manera
			totalInteres = totalInteres.add(simulador.getInteres());
			totalCuotaTotal = totalCuotaTotal.add(simulador.getCuotaTotal());
			contador++;
		}
			
		Simulador filaTotal = new Simulador();
		filaTotal.setNroCuota("TOTAL");
		filaTotal.setInicial(contrato.getMontoInicial());
		filaTotal.setCuotaSI(totalSI);
		filaTotal.setInteres(totalInteres);
		filaTotal.setCuotaTotal(totalCuotaTotal); 
		lstSimulador.add(filaTotal);
			
		
	}
		

	public List<Person> completePersonSurnames(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }	
	
	
	public Converter getConversorPersonSurnames() {
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
	
	
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public List<Lote> getLstLotesSinContrato() {
		return lstLotesSinContrato;
	}
	public void setLstLotesSinContrato(List<Lote> lstLotesSinContrato) {
		this.lstLotesSinContrato = lstLotesSinContrato;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public String getNombreLoteSelected() {
		return nombreLoteSelected;
	}
	public void setNombreLoteSelected(String nombreLoteSelected) {
		this.nombreLoteSelected = nombreLoteSelected;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public Date getFechaPrimeraCuota() {
		return fechaPrimeraCuota;
	}
	public void setFechaPrimeraCuota(Date fechaPrimeraCuota) {
		this.fechaPrimeraCuota = fechaPrimeraCuota;
	}
	public Person getPersona1() {
		return persona1;
	}
	public void setPersona1(Person persona1) {
		this.persona1 = persona1;
	}
	public Person getPersona2() {
		return persona2;
	}
	public void setPersona2(Person persona2) {
		this.persona2 = persona2;
	}
	public Person getPersona3() {
		return persona3;
	}
	public void setPersona3(Person persona3) {
		this.persona3 = persona3;
	}
	public Person getPersona4() {
		return persona4;
	}
	public void setPersona4(Person persona4) {
		this.persona4 = persona4;
	}
	public Person getPersona5() {
		return persona5;
	}
	public void setPersona5(Person persona5) {
		this.persona5 = persona5;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public Integer getNroCuotas() {
		return nroCuotas;
	}
	public void setNroCuotas(Integer nroCuotas) {
		this.nroCuotas = nroCuotas;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public LazyDataModel<Contrato> getLstContratoLazy() {
		return lstContratoLazy;
	}
	public void setLstContratoLazy(LazyDataModel<Contrato> lstContratoLazy) {
		this.lstContratoLazy = lstContratoLazy;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Contrato getContratoSelected() {
		return contratoSelected;
	}
	public void setContratoSelected(Contrato contratoSelected) {
		this.contratoSelected = contratoSelected;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public String[] getMeses() {
		return meses;
	}
	public void setMeses(String[] meses) {
		this.meses = meses;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public List<Simulador> getLstSimulador() {
		return lstSimulador;
	}
	public void setLstSimulador(List<Simulador> lstSimulador) {
		this.lstSimulador = lstSimulador;
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
	public NumeroALetra getNumeroAletra() {
		return numeroAletra;
	}
	public void setNumeroAletra(NumeroALetra numeroAletra) {
		this.numeroAletra = numeroAletra;
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
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public SimpleDateFormat getSdfDay() {
		return sdfDay;
	}
	public void setSdfDay(SimpleDateFormat sdfDay) {
		this.sdfDay = sdfDay;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public List<Simulador> getLstSimuladorPrevio() {
		return lstSimuladorPrevio;
	}
	public void setLstSimuladorPrevio(List<Simulador> lstSimuladorPrevio) {
		this.lstSimuladorPrevio = lstSimuladorPrevio;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	public List<Cuota> getLstCuotaVista() {
		return lstCuotaVista;
	}
	public void setLstCuotaVista(List<Cuota> lstCuotaVista) {
		this.lstCuotaVista = lstCuotaVista;
	}
	public DataSourceCronogramaPago getDt() {
		return dt;
	}
	public void setDt(DataSourceCronogramaPago dt) {
		this.dt = dt;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public BigDecimal getSumaCuotaSI() {
		return sumaCuotaSI;
	}
	public void setSumaCuotaSI(BigDecimal sumaCuotaSI) {
		this.sumaCuotaSI = sumaCuotaSI;
	}
	public BigDecimal getSumaInteres() {
		return sumaInteres;
	}
	public void setSumaInteres(BigDecimal sumaInteres) {
		this.sumaInteres = sumaInteres;
	}
	public BigDecimal getSumaCuotaTotal() {
		return sumaCuotaTotal;
	}
	public void setSumaCuotaTotal(BigDecimal sumaCuotaTotal) {
		this.sumaCuotaTotal = sumaCuotaTotal;
	}
	public List<ObservacionContrato> getLstObservacionContrato() {
		return lstObservacionContrato;
	}
	public void setLstObservacionContrato(List<ObservacionContrato> lstObservacionContrato) {
		this.lstObservacionContrato = lstObservacionContrato;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ObservacionContratoService getObservacionContratoService() {
		return observacionContratoService;
	}
	public void setObservacionContratoService(ObservacionContratoService observacionContratoService) {
		this.observacionContratoService = observacionContratoService;
	}
	public ObservacionContrato getObsSelected() {
		return obsSelected;
	}
	public void setObsSelected(ObservacionContrato obsSelected) {
		this.obsSelected = obsSelected;
	}
	public PlantillaVentaService getPlantillaVentaService() {
		return plantillaVentaService;
	}
	public void setPlantillaVentaService(PlantillaVentaService plantillaVentaService) {
		this.plantillaVentaService = plantillaVentaService;
	}
		
	
}
