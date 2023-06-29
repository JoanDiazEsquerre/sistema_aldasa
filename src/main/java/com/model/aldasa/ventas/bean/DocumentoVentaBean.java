package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.TipoProductoType;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVenta;

@ManagedBean
@ViewScoped
public class DocumentoVentaBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{serieDocumentoService}")
	private SerieDocumentoService serieDocumentoService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;
	
	@ManagedProperty(value = "#{voucherService}")
	private VoucherService voucherService;
	
	@ManagedProperty(value = "#{productoService}")
	private ProductoService productoService;
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService;
	
	@ManagedProperty(value = "#{reportGenBo}")
	private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{loadImageDocumentoBean}")
	private LoadImageDocumentoBean loadImageDocumentoBean;
	
	private boolean estado = true;

	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	private LazyDataModel<Cuota> lstCuotaLazy;
	private LazyDataModel<Voucher> lstVoucherLazy;
	private LazyDataModel<Cuota> lstPrepagoLazy;
	private LazyDataModel<Contrato> lstContratosPendientesLazy;

	
	private List<SerieDocumento> lstSerieDocumento;
	private List<Cuota> lstCuota;    
	private List<Voucher> lstVoucher;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = new ArrayList<>();
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = new ArrayList<>(); 
	private List<DocumentoVenta> lstDocumentoVenta = new ArrayList<>();
	private List<Cuota> lstCuotaPagadas = new ArrayList<>();
	private List<Cuota> lstCuotaVista = new ArrayList<>();
	private List<Cuota> lstCuotaPendientes = new ArrayList<>();
	private List<Cuota> lstCuotaPendientesTemporal = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumento = new ArrayList<>();


	
	private DocumentoVenta documentoVentaSelected ;
	private SerieDocumento serieDocumentoSelected ;
	private Cuota cuotaSelected ;
	private Voucher voucherSelected ;
	private Cuota prepagoSelected ;
	private Cliente clienteSelected;
	private Contrato contratoPendienteSelected;
	private Cuota cuotaPendienteContratoSelected;
	private TipoDocumento tipoDocumentoSelected;


	private DocumentoVenta documentoVentaNew;
	private DetalleDocumentoVenta detalleDocumentoVentaSelected;
	private DetalleDocumentoVenta detalleDocumentoVenta;
	private Producto productoCuota;
	private Producto productoInteres;
	private Producto productoVoucher;
	private Producto productoAmortizacion;
	private Producto productoAdelanto;
	private Person persona;

	
	private Date fechaEmision = new Date() ;
	private Date fechaImag1, fechaImag2, fechaImag3, fechaImag4, fechaImag5, fechaImag6, fechaImag7, fechaImag8, fechaImag9, fechaImag10 ;
	private BigDecimal montoImag1, montoImag2, montoImag3, montoImag4, montoImag5, montoImag6, montoImag7, montoImag8, montoImag9, montoImag10;
	private String nroOperImag1, nroOperImag2, nroOperImag3, nroOperImag4, nroOperImag5, nroOperImag6, nroOperImag7, nroOperImag8, nroOperImag9, nroOperImag10;
	private String fechaTextoVista, montoLetra;
	private String  ruc, nombreRazonSocial, direccion, observacion, numero ; 
	private String tipoPago = "Contado";
	private String tipoPrepago ="PC";
	private String incluirUltimaCuota = "No";
	private Integer nuevoNroCuotas;

	private boolean pagoTotalPrepago = false;
	private boolean habilitarBoton = true;
	private boolean habilitarMontoPrepago = false;


	private String moneda = "S";
	private BigDecimal anticipos = BigDecimal.ZERO;
	private BigDecimal opGravada = BigDecimal.ZERO;
	private BigDecimal opExonerada = BigDecimal.ZERO;
	private BigDecimal opInafecta = BigDecimal.ZERO;
	private BigDecimal opGratuita = BigDecimal.ZERO;
	private BigDecimal descuentos = BigDecimal.ZERO;
	private BigDecimal ISC = BigDecimal.ZERO;
	private BigDecimal IGV = BigDecimal.ZERO;
	private BigDecimal otrosCargos = BigDecimal.ZERO;
	private BigDecimal otrosTributos = BigDecimal.ZERO;
	private BigDecimal importeTotal = BigDecimal.ZERO;
	
	private BigDecimal deudaActualSinInteres = BigDecimal.ZERO;
	private BigDecimal deudaActualConInteres = BigDecimal.ZERO;
	private BigDecimal montoPrepago = BigDecimal.ZERO;
	private BigDecimal nuevoInteres;
	
	private String tituloDialog;
	private String imagen1, imagen2, imagen3, imagen4, imagen5;


	
	private NumeroALetra numeroALetra = new  NumeroALetra();
	
	private Map<String, Object> parametros;
    private DataSourceDocumentoVenta dt; 
    
    private UploadedFile file1;
    private UploadedFile file2;
    private UploadedFile file3;
    private UploadedFile file4;
    private UploadedFile file5;
    private UploadedFile file6;
    private UploadedFile file7;
    private UploadedFile file8;
    private UploadedFile file9;
    private UploadedFile file10;

	SimpleDateFormat sdf = new SimpleDateFormat("dd 'de'  MMMMM 'del' yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");

	
	@PostConstruct
	public void init() {
		lstTipoDocumento = tipoDocumentoService.findByEstado(true);
		tipoDocumentoSelected = lstTipoDocumento.get(0);
		
		iniciarLazy();
		iniciarDatosDocVenta();	
		listarSerie();
		iniciarLazyCuota(); 
		iniciarLazyVoucher();
		iniciarLazyPrepago();
		iniciarLazyContratosPendientes();
		productoCuota = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.CUOTA.getTipo());
		productoInteres = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.INTERES.getTipo());
		productoVoucher = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.SEPARACION.getTipo());
		productoAmortizacion = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.AMORTIZACION.getTipo());
		productoAdelanto = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.ADELANTO.getTipo());
		

	} 
	
	public void verVoucher() {
		
		loadImageDocumentoBean.setNombreArchivo("0.png");
		imagen1 = "";
		imagen2 = "";
		imagen3 = "";
		imagen4 = "";
		imagen5 = "";
		
		String nombreBusqueda = "%"+documentoVentaSelected.getId() +"_%";
		
		List<Imagen> lstImagen = imagenService.findByNombreLikeAndEstado(nombreBusqueda, true);
		int contador = 1;
		for(Imagen i:lstImagen) {
			if(contador==1) {
				imagen1 = i.getNombre();
			}
			if(contador==2) {
				imagen2 = i.getNombre();
			}
			if(contador==3) {
				imagen3 = i.getNombre();
			}
			if(contador==4) {
				imagen4 = i.getNombre();
			}
			if(contador==5) {
				imagen5 = i.getNombre();
			}
			contador ++;
		}
		PrimeFaces.current().executeScript("PF('voucherDocumentoDialog').show();");
	}
	
	
	public void simularPrepago() {
		
		habilitarBoton=true;
		habilitarMontoPrepago=false;
		
		if(contratoPendienteSelected == null) {
			addErrorMessage("Debes importar un contrato.");
			return;
		}
		
		lstCuotaPendientesTemporal = new ArrayList<>();
		
//		---------------------------------------------------------------------------------
		
		if(tipoPrepago.equals("PC")) {
			
			if(montoPrepago==null) {
				addErrorMessage("Ingresar monto.");
				return;
			}
			if(montoPrepago.compareTo(BigDecimal.ZERO)<=0) {
				addErrorMessage("Monto tiene que ser mayor a 0.");
				return;
			}
			
			if(montoPrepago.compareTo(deudaActualConInteres)==0) {
				pagoTotalPrepago = true;
			}else if(montoPrepago.compareTo(deudaActualConInteres)==1) {
				pagoTotalPrepago = false;
				addErrorMessage("El monto prepago no debe ser mayor a la deuda actual.");
				return;
			}else {
				pagoTotalPrepago = false;
			}
			
			int primeraCuotaPendiente = lstCuotaPendientes.get(0).getNroCuota();
			
			if(primeraCuotaPendiente<=6 && montoPrepago.compareTo(deudaActualSinInteres)>0) {
				addErrorMessage("Por estar dentro de los 6 primeros meses, el monto a prepagar debe ser " + deudaActualSinInteres);
				return;
			}
						
			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
			}
			saldo = saldo.subtract(montoPrepago);			
			
			Cuota cuota = new Cuota();
			cuota.setCuotaSI(montoPrepago);
			cuota.setCuotaTotal(montoPrepago);
			
			lstCuotaVista.clear();

			if(incluirUltimaCuota.equals("Si")) {
				cuota.setCuotaRef(lstCuotaPendientes.get(0));
				lstCuotaPendientes.get(0).setPagoTotal("S");
				lstCuotaPagadas.add(lstCuotaPendientes.get(0));
				cuota.setCuotaSI(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				cuota.setCuotaTotal(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				saldo = saldo.add(lstCuotaPendientes.get(0).getInteres());
				lstCuotaPendientes.remove(0);
			}
			
			lstCuotaVista.addAll(lstCuotaPagadas);
		
			cuota.setNroCuota(0);
			cuota.setFechaPago(new Date());
			cuota.setInteres(BigDecimal.ZERO);
			cuota.setAdelanto(BigDecimal.ZERO);
			cuota.setPagoTotal("N");
			cuota.setContrato(contratoPendienteSelected);
			cuota.setEstado(true);
			cuota.setOriginal(false);
			cuota.setPrepago(true);
			lstCuotaVista.add(cuota);
			lstCuotaPendientesTemporal.add(cuota);
			
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(lstCuotaPendientes.size()), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteres = nuevaCuotaSI.multiply(contratoPendienteSelected.getInteres().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

			for (Cuota c:lstCuotaPendientes) {

				Cuota nuevaCuota = new Cuota(c);
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteres);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteres));
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
			} 
			
//			---------------------------------------------------------------------------------
			
		}else if(tipoPrepago.equals("AR")) {
			if(nuevoNroCuotas == null) {
				addErrorMessage("Ingresar el nuevo número de cuotas.");
				return;
			}
			
			if(nuevoInteres == null) {
				addErrorMessage("Ingresar el nuevo interés.");
				return;
			}
			

			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
				if(nuevoInteres.compareTo(BigDecimal.ZERO)==0) {
					saldo=saldo.subtract(c.getInteres());
				}
			}
			
			lstCuotaVista.clear();

			lstCuotaVista.addAll(lstCuotaPagadas);
			
			Integer nuevoNroCuotasPendientes = nuevoNroCuotas - lstCuotaPagadas.size()+1;
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(nuevoNroCuotasPendientes), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteresRedAmpl = nuevaCuotaSI.multiply(nuevoInteres.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			Integer nroCuota = lstCuotaPagadas.size();
			Date fecha = lstCuotaPagadas.get(1).getFechaPago();

			
			for(int i=0; i<nuevoNroCuotasPendientes;i++) {
				
				Cuota nuevaCuota = new Cuota();
				nuevaCuota.setNroCuota(nroCuota);
				nuevaCuota.setFechaPago(sumarRestarMeses(fecha, lstCuotaPagadas.size()-1+i));
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteresRedAmpl);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteresRedAmpl));
				nuevaCuota.setAdelanto(BigDecimal.ZERO);
				nuevaCuota.setPagoTotal("N");
				nuevaCuota.setContrato(contratoPendienteSelected);
				nuevaCuota.setEstado(true);
				nuevaCuota.setOriginal(false);
				nuevaCuota.setPrepago(false);
				nuevaCuota.setCuotaRef(null);
				
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
				nroCuota++;
				
			}
			
//			---------------------------------------------------------------------------------
			
		}else {
			if(montoPrepago==null) {
				addErrorMessage("Ingresar monto.");
				return;
			}
			if(montoPrepago.compareTo(BigDecimal.ZERO)<=0) {
				addErrorMessage("Monto tiene que ser mayor a 0.");
				return;
			}
			
			if(montoPrepago.compareTo(deudaActualConInteres)==0) {
				pagoTotalPrepago = true;
			}else if(montoPrepago.compareTo(deudaActualConInteres)==1) {
				pagoTotalPrepago = false;
				addErrorMessage("El monto prepago no debe ser mayor a la deuda actual.");
				return;
			}else {
				pagoTotalPrepago = false;
			}
			
			int primeraCuotaPendiente = lstCuotaPendientes.get(0).getNroCuota();
			
			if(primeraCuotaPendiente<=6 && montoPrepago.compareTo(deudaActualSinInteres)>0) {
				addErrorMessage("Por estar dentro de los 6 primeros meses, el monto a prepagar debe ser " + deudaActualSinInteres);
				return;
			}
			if(nuevoNroCuotas == null) {
				addErrorMessage("Ingresar el nuevo número de cuotas.");
				return;
			}
			
			if(nuevoInteres == null) {
				addErrorMessage("Ingresar el nuevo interés.");
				return;
			}
			

			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
				if(nuevoInteres.compareTo(BigDecimal.ZERO)==0) {
					saldo=saldo.subtract(c.getInteres());
				}
			}
			
			saldo = saldo.subtract(montoPrepago);	
			
			Cuota cuota = new Cuota();
			cuota.setCuotaSI(montoPrepago);
			cuota.setCuotaTotal(montoPrepago);
			
			lstCuotaVista.clear();

			if(incluirUltimaCuota.equals("Si")) {
				cuota.setCuotaRef(lstCuotaPendientes.get(0));
				lstCuotaPendientes.get(0).setPagoTotal("S");
				lstCuotaPagadas.add(lstCuotaPendientes.get(0));
				cuota.setCuotaSI(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				cuota.setCuotaTotal(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				saldo = saldo.add(lstCuotaPendientes.get(0).getInteres());
				lstCuotaPendientes.remove(0);
			}
			
			lstCuotaVista.addAll(lstCuotaPagadas);
		
			cuota.setNroCuota(0);
			cuota.setFechaPago(new Date());
			cuota.setInteres(BigDecimal.ZERO);
			cuota.setAdelanto(BigDecimal.ZERO);
			cuota.setPagoTotal("N");
			cuota.setContrato(contratoPendienteSelected);
			cuota.setEstado(true);
			cuota.setOriginal(false);
			cuota.setPrepago(true);
			lstCuotaVista.add(cuota);
			lstCuotaPendientesTemporal.add(cuota);
			
			
			
			Integer nuevoNroCuotasPendientes = nuevoNroCuotas - lstCuotaPagadas.size()+1;
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(nuevoNroCuotasPendientes), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteresRedAmpl = nuevaCuotaSI.multiply(nuevoInteres.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			Integer nroCuota = lstCuotaPagadas.size();
			Date fecha = lstCuotaPagadas.get(1).getFechaPago();

			
			for(int i=0; i<nuevoNroCuotasPendientes;i++) {
				
				Cuota nuevaCuota = new Cuota();
				nuevaCuota.setNroCuota(nroCuota);
				nuevaCuota.setFechaPago(sumarRestarMeses(fecha, lstCuotaPagadas.size()-1+i));
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteresRedAmpl);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteresRedAmpl));
				nuevaCuota.setAdelanto(BigDecimal.ZERO);
				nuevaCuota.setPagoTotal("N");
				nuevaCuota.setContrato(contratoPendienteSelected);
				nuevaCuota.setEstado(true);
				nuevaCuota.setOriginal(false);
				nuevaCuota.setPrepago(false);
				nuevaCuota.setCuotaRef(null);
				
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
				nroCuota++;
				
			}
			
			
		}
	
		habilitarBoton=false;
		habilitarMontoPrepago=true;
	
	}
	
	public Date sumarRestarMeses(Date fecha, int meses) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, meses);
		return calendar.getTime();
	}
	
	public void savePrepago() {
	
		if(contratoPendienteSelected==null) {
			addErrorMessage("Primero debes importar contrato.");
			return;
		}
		
		if(pagoTotalPrepago) {
			for(Cuota c:lstCuotaPendientes) {
				c.setPagoTotal("S");
				cuotaService.save(c);
			}
			contratoPendienteSelected.setCancelacionTotal(true);
			contratoService.save(contratoPendienteSelected);
			

		}else {
			if(!lstCuotaPendientesTemporal.isEmpty()) {
				for(Cuota c:lstCuotaPendientes) {
					c.setEstado(false);
					cuotaService.save(c);
				}
			
				for(Cuota c:lstCuotaPendientesTemporal) {
					c.setEstado(true);
					cuotaService.save(c);
				}
				
			}else {
				addErrorMessage("Primero debes simular un prepago.");
				return;
			}
		}
		
		
		
		Cliente cliente = clienteService.findByPersonAndEstado(contratoPendienteSelected.getPersonVenta(), true);
		if(cliente == null){
			 cliente = new Cliente();
			 cliente.setPerson(contratoPendienteSelected.getPersonVenta());
			 cliente.setRazonSocial(contratoPendienteSelected.getPersonVenta().getSurnames()+" "+contratoPendienteSelected.getPersonVenta().getNames());
			 cliente.setNombreComercial(contratoPendienteSelected.getPersonVenta().getSurnames()+" "+contratoPendienteSelected.getPersonVenta().getNames());
			 cliente.setRuc(contratoPendienteSelected.getPersonVenta().getDni());
			 cliente.setDireccion(contratoPendienteSelected.getPersonVenta().getAddress());
			 cliente.setPersonaNatural(true);
			 cliente.setEstado(true);
			 cliente.setFechaRegistro(new Date());
			 cliente.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
			 cliente = clienteService.save(cliente);
			 
		}		
		
		addInfoMessage("Se guardo el prepago correctamente.");
		lstCuotaPendientes.clear();
		cancelarContrato();
	}	
	
	public void importarContrato() {
		lstCuotaVista = new ArrayList<>();
		lstCuotaPagadas=cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("S", true, contratoPendienteSelected);
		lstCuotaPendientes = cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("N", true, contratoPendienteSelected);
		lstCuotaVista.addAll(lstCuotaPagadas);
		lstCuotaVista.addAll(lstCuotaPendientes);
		deudaActualSinInteres= BigDecimal.ZERO;
		deudaActualConInteres= BigDecimal.ZERO;

		for(Cuota c:lstCuotaPendientes) {
			if(c.getNroCuota()!=0) {
				BigDecimal a = c.getCuotaSI().subtract(c.getAdelanto());
				BigDecimal b = c.getCuotaTotal().subtract(c.getAdelanto());
				deudaActualSinInteres = deudaActualSinInteres.add(a);	
				deudaActualConInteres = deudaActualConInteres.add(b);	
			}
		}
		
		deudaActualSinInteres= BigDecimal.ZERO;
		for(Cuota c:lstCuotaPagadas) {
			deudaActualSinInteres = deudaActualSinInteres.add(c.getCuotaTotal());
		}
		deudaActualSinInteres = contratoPendienteSelected.getMontoVenta().subtract(deudaActualSinInteres);
		
	}
	
	
	public void anularDocumento() {
		documentoVentaSelected.setEstado(false);
		documentoVentaService.save(documentoVentaSelected);
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVentaSelected, true);
	
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaSelected) {
			d.setEstado(false); 
			detalleDocumentoVentaService.save(d);
			
			if(d.getVoucher()!=null) {
				d.getVoucher().setGeneraDocumento(false);
				voucherService.save(d.getVoucher());
			}
			if(d.getCuota()!=null) {
				d.getCuota().setPagoTotal("N");
				cuotaService.save(d.getCuota());
			}
			
		}
		
		addInfoMessage("Documento de venta anulado.");		
		
		
	}
	
	public boolean esImpar(int iNumero) {// mandas el numero, y retorna falso si es impar, 
		if (iNumero % 2 != 0)// esta linea devuelve el residuo de una division,ejemplo, cuanto es 1/2? 0.5
			return true;
		else
			return false;
	}
	public void updateAdelanto(DetalleDocumentoVenta detalle) {
		if(detalle.getAdelanto()==null) {
			detalle.setAdelanto(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}else if (detalle.getAdelanto().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setAdelanto(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}else {
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}
		
		calcularTotales();
	}
	
	public void updateInteres(DetalleDocumentoVenta detalle) {
		if(detalle.getInteres()==null) {
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else if (detalle.getInteres().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else {
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}
		
		calcularTotales();
		
		
	}
	
	public void updateAmortizacion(DetalleDocumentoVenta detalle) {
		if(detalle.getTotalTemp()==null) {
			detalle.setTotalTemp(detalle.getImporteVenta());
		}
		if(detalle.getAmortizacion()==null) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else if (detalle.getAmortizacion().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else {
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
			detalle.setProducto(productoAdelanto);
		}
		
		calcularTotales();
		
		
	}
	
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	
	public void modifyDocumentoVenta() {
		tituloDialog="MODIFICAR DETALLE DE DOCUMENTO DE VENTA";
	}
	
	public void BoletaDocumentoVenta( ) {
		tituloDialog="DOCUMENTO DE VENTA";
		montoLetra = numeroALetra.Convertir(documentoVentaSelected.getTotal()+"", true, "SOLES");
		lstDetalleDocumentoVentaSelected = new ArrayList<>();
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVentaSelected, true);
	}
	
	
	public void listarNombreRazonSocial() {
		DocumentoVenta documento = new DocumentoVenta();
		documento.getRazonSocial();
	}
	
	public String extension(String filename) {
		String valor = "" ;
		int index = filename.lastIndexOf('.');
		if (index == -1) {
			return valor;
		} else {
			valor = filename.substring(index + 1);
		}
		return valor;
	}
	
	public boolean validarDatosImagen() {
		boolean valida=false;
		if(file2!=null){
			if(fechaImag2==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag2==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag2.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file3!=null){
			if(fechaImag3==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag3==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag3.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file4!=null){
			if(fechaImag4==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag4==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag4.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file5!=null){
			if(fechaImag5==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag5==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag5.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file6!=null){
			if(fechaImag6==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag6==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag6.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file7!=null){
			if(fechaImag7==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag7==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag7.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file8!=null){
			if(fechaImag8==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag8==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag8.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file9!=null){
			if(fechaImag9==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag9==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag9.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		if(file10!=null){
			if(fechaImag10==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}
			if(montoImag10==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}
			if(nroOperImag10.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
		}
		
		return valida;
	}
	
	public void saveDocumentoVenta() {
		
		if(lstDetalleDocumentoVenta.isEmpty()) { 
			addErrorMessage("Debes importar al menos un documento.");
			return;
		}
		
		if(ruc.equals("")) {
			if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
				addErrorMessage("Ingresar DNI.");
			}else {
				addErrorMessage("Ingresar RUC.");
			}
			return;
		}
		
		if(nombreRazonSocial.equals("")) {
			if(ruc.equals("")) {
				if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
					addErrorMessage("Ingresar nombre.");
				}else {
					addErrorMessage("Ingresar razón social.");
				}
				return;
			}
		}
		
		if(direccion.equals("")) {
			addErrorMessage("Ingresar dirección.");
			return;
		}
		
		if(file1 == null) {
			addErrorMessage("Seleccione una imagen (voucher)");
			return;
		}else {
			if(fechaImag1==null) {
				addErrorMessage("Ingresar fecha del primer voucher");
				return;
			}
			if(montoImag1==null) {
				addErrorMessage("Ingresar monto del primer voucher");
				return;
			}
			if(nroOperImag1.equals("")) {
				addErrorMessage("Ingresar número de operación del primer voucher");
				return;
			}
		}
		
		boolean validaImagenes = validarDatosImagen();
		
		if(validaImagenes) {
			return;
		}
		
		if(clienteSelected==null) {
			Cliente cliente = new Cliente();
			
			cliente.setPerson(persona);
			cliente.setRazonSocial(nombreRazonSocial);
			cliente.setNombreComercial("");   
			cliente.setRuc(ruc);
			cliente.setDireccion(direccion);
			cliente.setPersonaNatural(tipoDocumentoSelected.getAbreviatura().equals("B")?true:false);
			cliente.setEstado(true);
			cliente.setFechaRegistro(new Date());
			cliente.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
			clienteSelected=clienteService.save(cliente);
			
			if(clienteSelected==null) {
				addErrorMessage("No se puede registrar el cliente.");
				return;
			}
			
		}else {
//			aqui actualiza los datos del cliente y guarda run razon doreccion
			clienteSelected.setPerson(persona);
			clienteSelected.setRuc(ruc);
			clienteSelected.setNombreComercial(nombreRazonSocial);
			clienteSelected.setDireccion(direccion);
			clienteService.save(clienteSelected);
		}
		
		DocumentoVenta documentoVenta = new DocumentoVenta();
		documentoVenta.setCliente(clienteSelected);
		documentoVenta.setDocumentoVentaRef(null);
		documentoVenta.setSucursal(navegacionBean.getSucursalLogin());
		documentoVenta.setTipoDocumento(tipoDocumentoSelected);
		documentoVenta.setSerie(serieDocumentoSelected.getSerie());
		documentoVenta.setNumero(""); // vamos a setear el numero despues de haber guardado el documento
		documentoVenta.setRuc(ruc);
		documentoVenta.setRazonSocial(nombreRazonSocial);
		documentoVenta.setNombreComercial(clienteSelected.getNombreComercial());
		documentoVenta.setDireccion(direccion);
		documentoVenta.setFechaEmision(fechaEmision);
		documentoVenta.setTipoMoneda(moneda);
		documentoVenta.setObservacion(observacion);
		documentoVenta.setTipoPago(tipoPago);
		documentoVenta.setSubTotal(importeTotal);
		documentoVenta.setIgv(IGV);
		documentoVenta.setTotal(importeTotal);
		documentoVenta.setFechaRegistro(new Date());
		documentoVenta.setUsuarioRegistro(navegacionBean.getUsuarioLogin());
		documentoVenta.setEstado(true);
		documentoVenta.setAnticipos(anticipos);
		documentoVenta.setOpGravada(opGravada);
		documentoVenta.setOpExonerada(opExonerada);
		documentoVenta.setOpInafecta(opInafecta);
		documentoVenta.setOpGratuita(opGratuita);
		documentoVenta.setDescuentos(descuentos);
		documentoVenta.setIsc(ISC);
		documentoVenta.setOtrosCargos(otrosCargos);
		documentoVenta.setOtrosTributos(otrosTributos);
		
		DocumentoVenta documento = documentoVentaService.save(documentoVenta); 
		if(documento != null) {
			SerieDocumento serie = serieDocumentoService.findById(serieDocumentoSelected.getId()).get();
			String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));
			
			Integer aumento = Integer.parseInt(serie.getNumero())+1;
			  			
			serie.setNumero(aumento+"");
			serieDocumentoService.save(serie);
			
			documento.setNumero(numeroActual); 
			documentoVentaService.save(documento);
			
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				d.setDocumentoVenta(documento);
				d.setEstado(true);
				detalleDocumentoVentaService.save(d);
				
				if(d.getCuota()!=null) {
					if(!d.getProducto().getTipoProducto().equals(TipoProductoType.INTERES.getTipo())) {
						BigDecimal cuota = d.getCuota().getCuotaTotal().subtract(d.getCuota().getAdelanto());
						BigDecimal cuota2 = d.getImporteVenta().add(d.getCuota().getInteres());
						if(cuota.compareTo(cuota2)==0 ) {
							d.getCuota().setPagoTotal("S");
						}else {
							d.getCuota().setAdelanto(d.getImporteVenta());
						}
						
						cuotaService.save(d.getCuota()); 
					}
					if(d.getCuota().getNroCuota()==0 && d.getCuota().getContrato().getTipoPago().equals("Contado") && d.getCuota().getPagoTotal().equals("S")) {
						d.getCuota().getContrato().setCancelacionTotal(true);							
						contratoService.save(d.getCuota().getContrato());
					}
					
				}
				
				if(d.getVoucher()!=null) {
					d.getVoucher().setGeneraDocumento(true); 
					voucherService.save(d.getVoucher());
				}
				if(d.getCuotaPrepago()!=null) {
					d.getCuotaPrepago().setPagoTotal("S");
					cuotaService.save(d.getCuotaPrepago());
				}
					
			}  
			
			lstDetalleDocumentoVenta.clear();// claer es limpiar en ingles prueba
			ruc = "";
			nombreRazonSocial = "";
			direccion = "";
			calcularTotales();
			
			addInfoMessage("Se guardó el documento correctamente.");
			
			subirImagenes(documento.getId() + "");
			setearInfoVoucher();
			
		}else {
			addErrorMessage("No se puede guardar el documento."); 
			return;
		}
		
	}
	
	public void setearInfoVoucher() {
		fechaImag1=null;fechaImag2=null;fechaImag3=null;fechaImag4=null;fechaImag5=null;fechaImag6=null;fechaImag7=null;fechaImag8=null;fechaImag9=null;fechaImag10=null;
		
		montoImag1=null;montoImag2=null;montoImag3=null;montoImag4=null;montoImag5=null;montoImag6=null;montoImag7=null;montoImag8=null;montoImag9=null;montoImag10=null;
		
		nroOperImag1="";nroOperImag2="";nroOperImag3="";nroOperImag4="";nroOperImag5="";nroOperImag6="";nroOperImag7="";nroOperImag8="";nroOperImag9="";nroOperImag10="";
	}
	
	public void subirImagenes(String idDocumento) {
		
		if(file1 != null) {
			String rename = idDocumento +"_1" + "." + getExtension(file1.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag1);
			registroImagen.setMonto(montoImag1);
			registroImagen.setNumeroOperacion(nroOperImag1);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file1);
		}
		if(file2 != null) {
			String rename = idDocumento +"_2" + "." + getExtension(file2.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag2);
			registroImagen.setMonto(montoImag2);
			registroImagen.setNumeroOperacion(nroOperImag2);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file2);
		}
		if(file3 != null) {
			String rename = idDocumento +"_3" + "." + getExtension(file3.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag3);
			registroImagen.setMonto(montoImag3);
			registroImagen.setNumeroOperacion(nroOperImag3);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file3);
		}
		if(file4 != null) {
			String rename = idDocumento +"_4" + "." + getExtension(file4.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag4);
			registroImagen.setMonto(montoImag4);
			registroImagen.setNumeroOperacion(nroOperImag4);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file4);
		}
		if(file5 != null) {
			String rename = idDocumento +"_5" + "." + getExtension(file5.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag5);
			registroImagen.setMonto(montoImag5);
			registroImagen.setNumeroOperacion(nroOperImag5);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file5);
		}
		if(file6 != null) {
			String rename = idDocumento +"_6" + "." + getExtension(file6.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag6);
			registroImagen.setMonto(montoImag6);
			registroImagen.setNumeroOperacion(nroOperImag6);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file6);
		}
		if(file7 != null) {
			String rename = idDocumento +"_7" + "." + getExtension(file7.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag7);
			registroImagen.setMonto(montoImag7);
			registroImagen.setNumeroOperacion(nroOperImag7);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file7);
		}
		if(file8 != null) {
			String rename = idDocumento +"_8" + "." + getExtension(file8.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag8);
			registroImagen.setMonto(montoImag8);
			registroImagen.setNumeroOperacion(nroOperImag8);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file8);
		}
		if(file9 != null) {
			String rename = idDocumento +"_9" + "." + getExtension(file9.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag9);
			registroImagen.setMonto(montoImag9);
			registroImagen.setNumeroOperacion(nroOperImag9);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file9);
		}
		if(file10 != null) {
			String rename = idDocumento +"_10" + "." + getExtension(file10.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag10);
			registroImagen.setMonto(montoImag10);
			registroImagen.setNumeroOperacion(nroOperImag10);
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file10);
		}
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
//      File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
//      File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
      File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaDocumentoVenta() + nombre);

      try {

          FileOutputStream fileOutputStream = new FileOutputStream(result);

          byte[] buffer = new byte[1024];

          int bulk;

          // Here you get uploaded picture bytes, while debugging you can see that 34818
          InputStream inputStream = file.getInputStream();

          while (true) {

              bulk = inputStream.read(buffer);

              if (bulk < 0) {

                  break;

              } //end of if

              fileOutputStream.write(buffer, 0, bulk);
              fileOutputStream.flush();

          } //end fo while(true)

          fileOutputStream.close();
          inputStream.close();

          FacesMessage msg = new FacesMessage("El archivo subió correctamente.");
          FacesContext.getCurrentInstance().addMessage(null, msg);

      } catch (IOException e) {
          e.printStackTrace();
          FacesMessage error = new FacesMessage("The files were not uploaded!");
          FacesContext.getCurrentInstance().addMessage(null, error);
      }
  }
	     
	public void cancelarDocumentoVenta() {
		ruc="";
		nombreRazonSocial="";
		direccion="";
		observacion="";
		clienteSelected=null;
		lstDetalleDocumentoVenta.clear();
		calcularTotales();
	}
	
	public void cancelarContrato() {
		contratoPendienteSelected=null;
		lstCuotaVista = new ArrayList<>();
		lstCuotaPendientes = new ArrayList<>();
		lstCuotaPendientesTemporal = new ArrayList<>();
		lstCuotaPagadas=new ArrayList<>();
		deudaActualSinInteres = BigDecimal.ZERO;
		deudaActualConInteres = BigDecimal.ZERO;
		montoPrepago = BigDecimal.ZERO;
		tipoPrepago = "PC";
		habilitarBoton=true;
		habilitarMontoPrepago=false;
		pagoTotalPrepago=false;

	}
	
	public void validacionFecha() {
		if(fechaEmision==null) {
			fechaEmision = new Date();
		}
	}
	
	public void listarVoucher() {
		lstVoucher = new ArrayList<>();
		lstVoucher = voucherService.findByEstado(true);
		
	}
	
	public void listarCuota() {
		lstCuota = new ArrayList<>();
		lstCuota = cuotaService.findByPagoTotalAndEstado("N", true);
		
	}
	
	public void importarCuota() {
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getCuota() != null) {
					if(cuotaSelected.getId()==d.getCuota().getId()) {
						addErrorMessage("Ya seleccionó la cuota");			
						return;
					}
					
					if(cuotaSelected.getContrato().getPersonVenta().getId() != d.getCuota().getContrato().getPersonVenta().getId()) {
						addErrorMessage("La cuota debe ser de la misma persona.");
						return;
					}
				}
			}

		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(cuotaSelected.getContrato().getPersonVenta(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(cuotaSelected.getContrato().getPersonVenta(), true, false);
		} 
		
		if(clienteSelected != null) {
			ruc = clienteSelected.getRuc();
			nombreRazonSocial = clienteSelected.getRazonSocial();
			direccion = clienteSelected.getDireccion();
		}else {
			ruc = cuotaSelected.getContrato().getPersonVenta().getDni();
			nombreRazonSocial = cuotaSelected.getContrato().getPersonVenta().getSurnames() + " " +cuotaSelected.getContrato().getPersonVenta().getNames();
			direccion = cuotaSelected.getContrato().getPersonVenta().getAddress(); 
		}
		
		
		if(cuotaSelected.getNroCuota() ==0) {
			DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
			//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
			detalle.setDocumentoVenta(null);
			detalle.setProducto(productoCuota);
			detalle.setDescripcion("PAGO DE INICIAL POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
			detalle.setAmortizacion(cuotaSelected.getCuotaSI());
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setAdelanto(cuotaSelected.getAdelanto());
			detalle.setImporteVenta(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
			detalle.setCuota(cuotaSelected);
			detalle.setVoucher(null);
			detalle.setCuotaPrepago(null);
			lstDetalleDocumentoVenta.add(detalle);
		}else {
			DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
			//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
			detalle.setDocumentoVenta(null);
			detalle.setProducto(productoCuota);
			detalle.setDescripcion("PAGO DE LA CUOTA N° "+ cuotaSelected.getNroCuota() +" POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
			detalle.setAmortizacion(cuotaSelected.getCuotaSI());
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setAdelanto(cuotaSelected.getAdelanto());		
			detalle.setImporteVenta(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
			detalle.setCuota(cuotaSelected);
			detalle.setVoucher(null);
			detalle.setCuotaPrepago(null);
			lstDetalleDocumentoVenta.add(detalle);
			
			
			DetalleDocumentoVenta detalleInteres = new DetalleDocumentoVenta();
			//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
			detalleInteres.setDocumentoVenta(null);
			detalleInteres.setProducto(productoInteres);
			detalleInteres.setDescripcion("POR EL INTERES CORRESPONDIENTE A LA CUOTA N°" + cuotaSelected.getNroCuota());
			detalleInteres.setAmortizacion(BigDecimal.ZERO);
			detalleInteres.setInteres(cuotaSelected.getInteres());
			detalleInteres.setAdelanto(BigDecimal.ZERO);
			detalleInteres.setImporteVenta(cuotaSelected.getInteres());
			detalleInteres.setCuota(cuotaSelected);
			detalleInteres.setVoucher(null);
			detalleInteres.setCuotaPrepago(null);
			lstDetalleDocumentoVenta.add(detalleInteres);
		}
		
		
		
		
		
		calcularTotales();
		persona = cuotaSelected.getContrato().getPersonVenta();
		addInfoMessage("Cuota importada correctamente."); 
		
	
	}
	
	public void importarVoucher() {
		
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getVoucher() != null) {
					if(voucherSelected.getId()==d.getVoucher().getId()) {
						addErrorMessage("Ya seleccionó el voucher");			
						return;
					}
					
					if(voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson().getId() != d.getVoucher().getRequerimientoSeparacion().getProspection().getProspect().getPerson().getId()) {
						addErrorMessage("El voucher debe ser de la misma persona.");
						return;
					}
					
					
				}
			}

		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson(), true, false);
		} 
		
		if(clienteSelected != null) {
			ruc = clienteSelected.getRuc();
			nombreRazonSocial = clienteSelected.getRazonSocial();
			direccion = clienteSelected.getDireccion();
		}else {
			ruc = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson().getDni();
			nombreRazonSocial = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson().getSurnames() + " " +voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson().getNames();
			direccion = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson().getAddress(); 
		}
		
		
		
		DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
		//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
		detalle.setDocumentoVenta(null);
		detalle.setProducto(productoVoucher);
		detalle.setDescripcion("PAGO DE SEPARACIÓN POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ voucherSelected.getRequerimientoSeparacion().getLote().getNumberLote() +" MZ - "+ voucherSelected.getRequerimientoSeparacion().getLote().getManzana().getName() +" , UBICADO EN " + voucherSelected.getRequerimientoSeparacion().getLote().getProject().getName());
		detalle.setAmortizacion(voucherSelected.getMonto());
		detalle.setInteres(BigDecimal.ZERO);
		detalle.setAdelanto(BigDecimal.ZERO);
		detalle.setImporteVenta(voucherSelected.getMonto());
		detalle.setCuota(null);
		detalle.setVoucher(voucherSelected);
		detalle.setCuotaPrepago(null);
		detalle.setEstado(true);
		lstDetalleDocumentoVenta.add(detalle);
		
		calcularTotales();
		persona = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson(); 
		addInfoMessage("Voucher importado correctamente."); 
	}
	
	public void importarPrepago() {
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getCuotaPrepago() != null) {
					if(prepagoSelected.getId()==d.getCuotaPrepago().getId()) {
						addErrorMessage("Ya seleccionó el prepago");			
						return;
					}
					
					if(prepagoSelected.getContrato().getPersonVenta().getId() != d.getCuotaPrepago().getContrato().getPersonVenta().getId()) {
						addErrorMessage("El prepago debe ser de la misma persona.");
						return;
					}
				}
			}

		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(prepagoSelected.getContrato().getPersonVenta(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(prepagoSelected.getContrato().getPersonVenta(), true, false);
		} 
		
		if(clienteSelected != null) {
			ruc = clienteSelected.getRuc();
			nombreRazonSocial = clienteSelected.getRazonSocial();
			direccion = clienteSelected.getDireccion();
		}else {
			ruc = prepagoSelected.getContrato().getPersonVenta().getDni();
			nombreRazonSocial = prepagoSelected.getContrato().getPersonVenta().getSurnames() + " " +prepagoSelected.getContrato().getPersonVenta().getNames();
			direccion = prepagoSelected.getContrato().getPersonVenta().getAddress(); 
		}
		
		if(prepagoSelected.getCuotaRef()!=null) {
			cuotaSelected=prepagoSelected.getCuotaRef();
			importarCuota();
		}
		
		DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
		//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
		detalle.setDocumentoVenta(null);
		detalle.setProducto(productoAmortizacion);
		detalle.setDescripcion(detalle.getProducto().getDescripcion().toUpperCase() + " POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ prepagoSelected.getContrato().getLote().getNumberLote() +" MZ - "+ prepagoSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + prepagoSelected.getContrato().getLote().getProject().getName());
																												
		detalle.setAmortizacion(prepagoSelected.getCuotaTotal());
		detalle.setInteres(BigDecimal.ZERO);
		detalle.setAdelanto(BigDecimal.ZERO);
		detalle.setImporteVenta(prepagoSelected.getCuotaTotal());
		detalle.setCuota(null);
		detalle.setVoucher(null);
		detalle.setCuotaPrepago(prepagoSelected);
		detalle.setEstado(true);
		lstDetalleDocumentoVenta.add(detalle);
		
		calcularTotales();
		persona = prepagoSelected.getContrato().getPersonVenta(); 
		addInfoMessage("Prepago importado correctamente."); 
		
		
		
	}
	
	public void calcularTotales() {
		anticipos = BigDecimal.ZERO;
		opInafecta=BigDecimal.ZERO;
		importeTotal=BigDecimal.ZERO;
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				opInafecta= opInafecta.add(d.getImporteVenta());
				importeTotal= importeTotal.add(d.getImporteVenta());
				if(d.getTotalTemp()!=null) {
					anticipos = anticipos.add(d.getAmortizacion());
				}
			}
		}
	} 
	
	public void eliminarDetalleVenta(int index) {
		lstDetalleDocumentoVenta.remove(index);
		calcularTotales();
		if(lstDetalleDocumentoVenta.isEmpty()) {
			clienteSelected = null;
			ruc = "";
			nombreRazonSocial = "";
			direccion = "";
			cuotaSelected = null;
			
		}
		addInfoMessage("Detalle eliminado");
		
	}
	
	public void iniciarDatosDocVenta() {
		
		documentoVentaNew= new DocumentoVenta(); 
		documentoVentaNew.setFechaEmision(new Date());
		
	}
	
	public void listarSerie() {
		lstSerieDocumento = serieDocumentoService.findByTipoDocumentoAndSucursal(tipoDocumentoSelected, navegacionBean.getSucursalLogin());
		serieDocumentoSelected=lstSerieDocumento.get(0);

		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
		
	}
	
	public void cambiarSerie() {
		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
	}
	
	public void iniciarLazy() {

		lstDocumentoVentaLazy = new LazyDataModel<DocumentoVenta>() {
			private List<DocumentoVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public DocumentoVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (DocumentoVenta documentoVenta : datasource) {
                    if (documentoVenta.getId() == intRowKey) {
                        return documentoVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(DocumentoVenta documentoVenta) {
                return String.valueOf(documentoVenta.getId());
            }

			@Override
			public List<DocumentoVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String razonSocial = "%" + (filterBy.get("razonSocial") != null ? filterBy.get("razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numero = "%" + (filterBy.get("numero") != null ? filterBy.get("numero").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String ruc = "%" + (filterBy.get("ruc") != null ? filterBy.get("ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("fechaEmision").descending();
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
               
                Page<DocumentoVenta> pageDocumentoVenta=null;
               
                
                pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, pageable);
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
            }
		};
	}
	
	public void iniciarLazyCuota() {

		lstCuotaLazy = new LazyDataModel<Cuota>() {
			private List<Cuota> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Cuota getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Cuota cuota : datasource) {
                    if (cuota.getId() == intRowKey) {
                        return cuota;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Cuota cuota) {
                return String.valueOf(cuota.getId());
            }

			@Override
			public List<Cuota> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("contrato.personVenta.surnames") != null ? filterBy.get("contrato.personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("contrato.personVenta.dni") != null ? filterBy.get("contrato.personVenta.dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
                Sort sort=Sort.by("fechaPago").ascending();
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
               
                Page<Cuota> pageCuota=null;
               
                
                pageCuota= cuotaService.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike("N", true, names, dni, pageable);
                
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
            }
		};
	}
		
	public void iniciarLazyVoucher() {

		lstVoucherLazy = new LazyDataModel<Voucher>() {
			private List<Voucher> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Voucher getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Voucher voucher : datasource) {
                    if (voucher.getId() == intRowKey) {
                        return voucher;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Voucher voucher) {
                return String.valueOf(voucher.getId());
            }

			@Override
			public List<Voucher> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("estado").ascending();
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
               
                Page<Voucher> pageVoucher=null;
               
                
                pageVoucher= voucherService.findByEstadoAndGeneraDocumento(true, false, pageable);
                
                setRowCount((int) pageVoucher.getTotalElements());
                return datasource = pageVoucher.getContent();
            }
		};
	}
	
	public void iniciarLazyPrepago() {

		lstPrepagoLazy = new LazyDataModel<Cuota>() {
			private List<Cuota> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Cuota getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Cuota cuota : datasource) {
                    if (cuota.getId() == intRowKey) {
                        return cuota;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Cuota cuota) {
                return String.valueOf(cuota.getId());
            }

			@Override
			public List<Cuota> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               

                Sort sort=Sort.by("fechaPago").ascending();
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
               
                Page<Cuota>pageCuota= cuotaService.findByPagoTotalAndEstadoAndPrepago("N", true, true, pageable);
                
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
            }
		};
	}
	
	public void iniciarLazyContratosPendientes() {

		lstContratosPendientesLazy = new LazyDataModel<Contrato>() {
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
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				String names = "%" + (filterBy.get("personVenta.surnames") != null ? filterBy.get("personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("personVenta.dni") != null ? filterBy.get("personVenta.dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				
                Sort sort=Sort.by("lote.manzana.name").ascending();
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
               
                Page<Contrato> pageContrato= contratoService.findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotal(names, dni, true, false, pageable);
                
                setRowCount((int) pageContrato.getTotalElements());
                return datasource = pageContrato.getContent();
            }
		};
	}

	public Converter getConversorSerie() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	SerieDocumento c = null;
                    for (SerieDocumento si : lstSerieDocumento) {
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
                    return ((SerieDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public void pdfDocumentoElectronico() {

        if (lstDetalleDocumentoVentaSelected == null || lstDetalleDocumentoVentaSelected.isEmpty()) {
            addInfoMessage("No hay datos para mostrar");
        } else {
        	
        	dt = new DataSourceDocumentoVenta();
            for (int i = 0; i < lstDetalleDocumentoVentaSelected.size(); i++) {
               
            	lstDetalleDocumentoVentaSelected.get(i).setDocumentoVenta(documentoVentaSelected);
                dt.addResumenDetalle(lstDetalleDocumentoVentaSelected.get(i));
            }
        	
        	
            parametros = new HashMap<String, Object>();
            parametros.put("TOTALSTRING", montoLetra);
            if (documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")) {
            	 parametros.put("TIPOCOMPROBANTE", "BOLETA DE VENTA");
            }else {
            	parametros.put("TIPOCOMPROBANTE", "FACTURA");
            }   
            
            parametros.put("NOMBRECOMERCIAL", documentoVentaSelected.getRazonSocial());
            parametros.put("RUC", documentoVentaSelected.getRuc());
            parametros.put("DIRECCION", documentoVentaSelected.getDireccion());
            parametros.put("NOMBRECOMERCIAL", documentoVentaSelected.getRazonSocial());
            parametros.put("FECHAEMISION", sdf2.format(documentoVentaSelected.getFechaEmision()));
            parametros.put("TIPOMONEDA", documentoVentaSelected.getTipoMoneda().equals("S")?"Soles":"Dólares");
            parametros.put("OBSERVACION", documentoVentaSelected.getObservacion());
            parametros.put("CONDICIONPAGO", documentoVentaSelected.getTipoPago());
            
            if(lstDetalleDocumentoVentaSelected.get(0).getProducto().getId()==productoVoucher.getId()) {
            	parametros.put("ANTICIPOS", documentoVentaSelected.getOpInafecta());
            }else {
                parametros.put("ANTICIPOS", documentoVentaSelected.getAnticipos());

            }
            String fecha = sdf2.format(documentoVentaSelected.getFechaEmision());
            parametros.put("OPGRAVADA", documentoVentaSelected.getOpGravada());
            parametros.put("OPEXONERADA", documentoVentaSelected.getOpExonerada());
            parametros.put("OPINAFECTA", documentoVentaSelected.getOpInafecta());
            parametros.put("OPGRATUITA", documentoVentaSelected.getOpGratuita());
            parametros.put("DESCUENTOS", documentoVentaSelected.getDescuentos());
            parametros.put("ISC", documentoVentaSelected.getIsc());
            parametros.put("IGV", documentoVentaSelected.getIgv());
            parametros.put("OTROSCARGOS", documentoVentaSelected.getOtrosCargos());
            parametros.put("OTROSTRIBUTOS", documentoVentaSelected.getOtrosTributos());
            parametros.put("IMPORTETOTAL", documentoVentaSelected.getTotal());
            parametros.put("QR", navegacionBean.getSucursalLogin().getRuc() + "|" + documentoVentaSelected.getTipoDocumento().getCodigo() + "|" + 
	    		documentoVentaSelected.getSerie() + "|" + documentoVentaSelected.getNumero() + "|" + "0" + "|" + documentoVentaSelected.getTotal() + "|" + 
	    		fecha + "|" + (documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")?"1":"6") + "|" + documentoVentaSelected.getRuc() + "|");



            String bar = navegacionBean.getSucursalLogin().getRuc().trim() + "|" + documentoVentaSelected.getSerie()+"-"+documentoVentaSelected.getNumero() + "|" + documentoVentaSelected.getFechaEmision();
            parametros.put("BARCODESTRING", bar);
            parametros.put("RUCEMPRESA", navegacionBean.getSucursalLogin().getRuc());
            
            parametros.put("RUTAIMAGEN", getRutaGrafico("/recursos/images/LOGO.png"));
            
            String path = "secured/view/modulos/ventas/reportes/jasper/repDocumentoFacturaElectronica.jasper"; 
            reportGenBo.exportByFormatNotConnectDb(dt, path, "pdf", parametros, "DOCUMENTO " , "hh");
            dt = null;
            parametros = null;
       

        }
    }

	public Converter getConversorTipoDocumento() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumento) {
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
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
	
	
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public LazyDataModel<DocumentoVenta> getLstDocumentoVentaLazy() {
		return lstDocumentoVentaLazy;
	}
	public void setLstDocumentoVentaLazy(LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy) {
		this.lstDocumentoVentaLazy = lstDocumentoVentaLazy;
	}
	public DocumentoVenta getDocumentoVentaSelected() {
		return documentoVentaSelected;
	}
	public void setDocumentoVentaSelected(DocumentoVenta documentoVentaSelected) {
		this.documentoVentaSelected = documentoVentaSelected;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getFechaTextoVista() {
		return fechaTextoVista;
	}
	public void setFechaTextoVista(String fechaTextoVista) {
		this.fechaTextoVista = fechaTextoVista;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public DocumentoVenta getDocumentoVentaNew() {
		return documentoVentaNew;
	}
	public void setDocumentoVentaNew(DocumentoVenta documentoVentaNew) {
		this.documentoVentaNew = documentoVentaNew;
	}
	public List<SerieDocumento> getLstSerieDocumento() {
		return lstSerieDocumento;
	}
	public void setLstSerieDocumento(List<SerieDocumento> lstSerieDocumento) {
		this.lstSerieDocumento = lstSerieDocumento;
	}
	public SerieDocumentoService getSerieDocumentoService() {
		return serieDocumentoService;
	}
	public void setSerieDocumentoService(SerieDocumentoService serieDocumentoService) {
		this.serieDocumentoService = serieDocumentoService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public SerieDocumento getSerieDocumentoSelected() {
		return serieDocumentoSelected;
	}
	public void setSerieDocumentoSelected(SerieDocumento serieDocumentoSelected) {
		this.serieDocumentoSelected = serieDocumentoSelected;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	public List<Cuota> getLstCuota() {
		return lstCuota;
	}
	public void setLstCuota(List<Cuota> lstCuota) {
		this.lstCuota = lstCuota;
	}
	public Cuota getCuotaSelected() {
		return cuotaSelected;
	}
	public void setCuotaSelected(Cuota cuotaSelected) {
		this.cuotaSelected = cuotaSelected;
	}
	public List<Voucher> getLstVoucher() {
		return lstVoucher;
	}
	public void setLstVoucher(List<Voucher> lstVoucher) {
		this.lstVoucher = lstVoucher;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public Voucher getVoucherSelected() {
		return voucherSelected;
	}
	public void setVoucherSelected(Voucher voucherSelected) {
		this.voucherSelected = voucherSelected;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}
	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getAnticipos() {
		return anticipos;
	}
	public void setAnticipos(BigDecimal anticipos) {
		this.anticipos = anticipos;
	}
	public BigDecimal getOpGravada() {
		return opGravada;
	}
	public void setOpGravada(BigDecimal opGravada) {
		this.opGravada = opGravada;
	}
	public BigDecimal getOpExonerada() {
		return opExonerada;
	}
	public void setOpExonerada(BigDecimal opExonerada) {
		this.opExonerada = opExonerada;
	}
	public BigDecimal getOpInafecta() {
		return opInafecta;
	}
	public void setOpInafecta(BigDecimal opInafecta) {
		this.opInafecta = opInafecta;
	}
	public BigDecimal getOpGratuita() {
		return opGratuita;
	}
	public void setOpGratuita(BigDecimal opGratuita) {
		this.opGratuita = opGratuita;
	}
	public BigDecimal getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(BigDecimal descuentos) {
		this.descuentos = descuentos;
	}
	public BigDecimal getISC() {
		return ISC;
	}
	public void setISC(BigDecimal iSC) {
		ISC = iSC;
	}
	public BigDecimal getIGV() {
		return IGV;
	}
	public void setIGV(BigDecimal iGV) {
		IGV = iGV;
	}
	public BigDecimal getOtrosCargos() {
		return otrosCargos;
	}
	public void setOtrosCargos(BigDecimal otrosCargos) {
		this.otrosCargos = otrosCargos;
	}
	public BigDecimal getOtrosTributos() {
		return otrosTributos;
	}
	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVentaSelected() {
		return detalleDocumentoVentaSelected;
	}
	public void setDetalleDocumentoVentaSelected(DetalleDocumentoVenta detalleDocumentoVentaSelected) {
		this.detalleDocumentoVentaSelected = detalleDocumentoVentaSelected;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVenta() {
		return lstDetalleDocumentoVenta;
	}
	public void setLstDetalleDocumentoVenta(List<DetalleDocumentoVenta> lstDetalleDocumentoVenta) {
		this.lstDetalleDocumentoVenta = lstDetalleDocumentoVenta;
	}
	public ProductoService getProductoService() {
		return productoService;
	}
	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}
	public Producto getProductoCuota() {
		return productoCuota;
	}
	public void setProductoCuota(Producto productoCuota) {
		this.productoCuota = productoCuota;
	}
	public Producto getProductoInteres() {
		return productoInteres;
	}
	public void setProductoInteres(Producto productoInteres) {
		this.productoInteres = productoInteres;
	}
	public List<DocumentoVenta> getLstDocumentoVenta() {
		return lstDocumentoVenta;
	}
	public void setLstDocumentoVenta(List<DocumentoVenta> lstDocumentoVenta) {
		this.lstDocumentoVenta = lstDocumentoVenta;
	}
	public LazyDataModel<Cuota> getLstCuotaLazy() {
		return lstCuotaLazy;
	}
	public void setLstCuotaLazy(LazyDataModel<Cuota> lstCuotaLazy) {
		this.lstCuotaLazy = lstCuotaLazy;
	}
	public LazyDataModel<Voucher> getLstVoucherLazy() {
		return lstVoucherLazy;
	}
	public void setLstVoucherLazy(LazyDataModel<Voucher> lstVoucherLazy) {
		this.lstVoucherLazy = lstVoucherLazy;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public Cliente getClienteSelected() {
		return clienteSelected;
	}
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public String getMontoLetra() {
		return montoLetra;
	}
	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}
	public NumeroALetra getNumeroALetra() {
		return numeroALetra;
	}
	public void setNumeroALetra(NumeroALetra numeroALetra) {
		this.numeroALetra = numeroALetra;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVentaSelected() {
		return lstDetalleDocumentoVentaSelected;
	}
	public void setLstDetalleDocumentoVentaSelected(List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected) {
		this.lstDetalleDocumentoVentaSelected = lstDetalleDocumentoVentaSelected;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVenta() {
		return detalleDocumentoVenta;
	}
	public void setDetalleDocumentoVenta(DetalleDocumentoVenta detalleDocumentoVenta) {
		this.detalleDocumentoVenta = detalleDocumentoVenta;
	}
	public Producto getProductoVoucher() {
		return productoVoucher;
	}
	public void setProductoVoucher(Producto productoVoucher) {
		this.productoVoucher = productoVoucher;
	}
	public Person getPersona() {
		return persona;
	}
	public void setPersona(Person persona) {
		this.persona = persona;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public DataSourceDocumentoVenta getDt() {
		return dt;
	}
	public void setDt(DataSourceDocumentoVenta dt) {
		this.dt = dt;
	}
	public LazyDataModel<Contrato> getLstContratosPendientesLazy() {
		return lstContratosPendientesLazy;
	}
	public void setLstContratosPendientesLazy(LazyDataModel<Contrato> lstContratosPendientesLazy) {
		this.lstContratosPendientesLazy = lstContratosPendientesLazy;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public Contrato getContratoPendienteSelected() {
		return contratoPendienteSelected;
	}
	public void setContratoPendienteSelected(Contrato contratoPendienteSelected) {
		this.contratoPendienteSelected = contratoPendienteSelected;
	}
	public List<Cuota> getLstCuotaPagadas() {
		return lstCuotaPagadas;
	}
	public void setLstCuotaPagadas(List<Cuota> lstCuotaPagadas) {
		this.lstCuotaPagadas = lstCuotaPagadas;
	}
	public Cuota getCuotaPendienteContratoSelected() {
		return cuotaPendienteContratoSelected;
	}
	public void setCuotaPendienteContratoSelected(Cuota cuotaPendienteContratoSelected) {
		this.cuotaPendienteContratoSelected = cuotaPendienteContratoSelected;
	}
	public BigDecimal getMontoPrepago() {
		return montoPrepago;
	}
	public void setMontoPrepago(BigDecimal montoPrepago) {
		this.montoPrepago = montoPrepago;
	}
	public String getTipoPrepago() {
		return tipoPrepago;
	}
	public void setTipoPrepago(String tipoPrepago) {
		this.tipoPrepago = tipoPrepago;
	}
	public List<Cuota> getLstCuotaVista() {
		return lstCuotaVista;
	}
	public void setLstCuotaVista(List<Cuota> lstCuotaVista) {
		this.lstCuotaVista = lstCuotaVista;
	}
	public List<Cuota> getLstCuotaPendientes() {
		return lstCuotaPendientes;
	}
	public void setLstCuotaPendientes(List<Cuota> lstCuotaPendientes) {
		this.lstCuotaPendientes = lstCuotaPendientes;
	}
	public List<Cuota> getLstCuotaPendientesTemporal() {
		return lstCuotaPendientesTemporal;
	}
	public void setLstCuotaPendientesTemporal(List<Cuota> lstCuotaPendientesTemporal) {
		this.lstCuotaPendientesTemporal = lstCuotaPendientesTemporal;
	}
	public BigDecimal getDeudaActualSinInteres() {
		return deudaActualSinInteres;
	}
	public void setDeudaActualSinInteres(BigDecimal deudaActualSinInteres) {
		this.deudaActualSinInteres = deudaActualSinInteres;
	}
	public BigDecimal getDeudaActualConInteres() {
		return deudaActualConInteres;
	}
	public void setDeudaActualConInteres(BigDecimal deudaActualConInteres) {
		this.deudaActualConInteres = deudaActualConInteres;
	}
	public boolean isPagoTotalPrepago() {
		return pagoTotalPrepago;
	}
	public void setPagoTotalPrepago(boolean pagoTotalPrepago) {
		this.pagoTotalPrepago = pagoTotalPrepago;
	}
	public boolean isHabilitarBoton() {
		return habilitarBoton;
	}
	public void setHabilitarBoton(boolean habilitarBoton) {
		this.habilitarBoton = habilitarBoton;
	}
	public LazyDataModel<Cuota> getLstPrepagoLazy() {
		return lstPrepagoLazy;
	}
	public void setLstPrepagoLazy(LazyDataModel<Cuota> lstPrepagoLazy) {
		this.lstPrepagoLazy = lstPrepagoLazy;
	}
	public Producto getProductoAmortizacion() {
		return productoAmortizacion;
	}
	public void setProductoAmortizacion(Producto productoAmortizacion) {
		this.productoAmortizacion = productoAmortizacion;
	}
	public void setPrepagoSelected(Cuota prepagoSelected) {
		this.prepagoSelected = prepagoSelected;
	}
	public boolean isHabilitarMontoPrepago() {
		return habilitarMontoPrepago;
	}
	public void setHabilitarMontoPrepago(boolean habilitarMontoPrepago) {
		this.habilitarMontoPrepago = habilitarMontoPrepago;
	}
	public List<TipoDocumento> getLstTipoDocumento() {
		return lstTipoDocumento;
	}
	public void setLstTipoDocumento(List<TipoDocumento> lstTipoDocumento) {
		this.lstTipoDocumento = lstTipoDocumento;
	}
	public TipoDocumento getTipoDocumentoSelected() {
		return tipoDocumentoSelected;
	}
	public void setTipoDocumentoSelected(TipoDocumento tipoDocumentoSelected) {
		this.tipoDocumentoSelected = tipoDocumentoSelected;
	}
	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public Producto getProductoAdelanto() {
		return productoAdelanto;
	}
	public void setProductoAdelanto(Producto productoAdelanto) {
		this.productoAdelanto = productoAdelanto;
	}
	public UploadedFile getFile1() {
		return file1;
	}
	public void setFile1(UploadedFile file1) {
		this.file1 = file1;
	}
	public UploadedFile getFile2() {
		return file2;
	}
	public void setFile2(UploadedFile file2) {
		this.file2 = file2;
	}
	public UploadedFile getFile3() {
		return file3;
	}
	public void setFile3(UploadedFile file3) {
		this.file3 = file3;
	}
	public UploadedFile getFile4() {
		return file4;
	}
	public void setFile4(UploadedFile file4) {
		this.file4 = file4;
	}
	public UploadedFile getFile5() {
		return file5;
	}
	public void setFile5(UploadedFile file5) {
		this.file5 = file5;
	}
	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	public String getImagen1() {
		return imagen1;
	}
	public void setImagen1(String imagen1) {
		this.imagen1 = imagen1;
	}
	public String getImagen2() {
		return imagen2;
	}
	public void setImagen2(String imagen2) {
		this.imagen2 = imagen2;
	}
	public String getImagen3() {
		return imagen3;
	}
	public void setImagen3(String imagen3) {
		this.imagen3 = imagen3;
	}
	public String getImagen4() {
		return imagen4;
	}
	public void setImagen4(String imagen4) {
		this.imagen4 = imagen4;
	}
	public String getImagen5() {
		return imagen5;
	}
	public void setImagen5(String imagen5) {
		this.imagen5 = imagen5;
	}
	public LoadImageDocumentoBean getLoadImageDocumentoBean() {
		return loadImageDocumentoBean;
	}
	public void setLoadImageDocumentoBean(LoadImageDocumentoBean loadImageDocumentoBean) {
		this.loadImageDocumentoBean = loadImageDocumentoBean;
	}
	public String getIncluirUltimaCuota() {
		return incluirUltimaCuota;
	}
	public void setIncluirUltimaCuota(String incluirUltimaCuota) {
		this.incluirUltimaCuota = incluirUltimaCuota;
	}
	public Cuota getPrepagoSelected() {
		return prepagoSelected;
	}
	public Integer getNuevoNroCuotas() {
		return nuevoNroCuotas;
	}
	public void setNuevoNroCuotas(Integer nuevoNroCuotas) {
		this.nuevoNroCuotas = nuevoNroCuotas;
	}
	public BigDecimal getNuevoInteres() {
		return nuevoInteres;
	}
	public void setNuevoInteres(BigDecimal nuevoInteres) {
		this.nuevoInteres = nuevoInteres;
	}
	public Date getFechaImag1() {
		return fechaImag1;
	}
	public void setFechaImag1(Date fechaImag1) {
		this.fechaImag1 = fechaImag1;
	}
	public Date getFechaImag2() {
		return fechaImag2;
	}
	public void setFechaImag2(Date fechaImag2) {
		this.fechaImag2 = fechaImag2;
	}
	public Date getFechaImag3() {
		return fechaImag3;
	}
	public void setFechaImag3(Date fechaImag3) {
		this.fechaImag3 = fechaImag3;
	}
	public Date getFechaImag4() {
		return fechaImag4;
	}
	public void setFechaImag4(Date fechaImag4) {
		this.fechaImag4 = fechaImag4;
	}
	public Date getFechaImag5() {
		return fechaImag5;
	}
	public void setFechaImag5(Date fechaImag5) {
		this.fechaImag5 = fechaImag5;
	}
	public Date getFechaImag6() {
		return fechaImag6;
	}
	public void setFechaImag6(Date fechaImag6) {
		this.fechaImag6 = fechaImag6;
	}
	public Date getFechaImag7() {
		return fechaImag7;
	}
	public void setFechaImag7(Date fechaImag7) {
		this.fechaImag7 = fechaImag7;
	}
	public Date getFechaImag8() {
		return fechaImag8;
	}
	public void setFechaImag8(Date fechaImag8) {
		this.fechaImag8 = fechaImag8;
	}
	public Date getFechaImag9() {
		return fechaImag9;
	}
	public void setFechaImag9(Date fechaImag9) {
		this.fechaImag9 = fechaImag9;
	}
	public Date getFechaImag10() {
		return fechaImag10;
	}
	public void setFechaImag10(Date fechaImag10) {
		this.fechaImag10 = fechaImag10;
	}
	public BigDecimal getMontoImag1() {
		return montoImag1;
	}
	public void setMontoImag1(BigDecimal montoImag1) {
		this.montoImag1 = montoImag1;
	}
	public BigDecimal getMontoImag2() {
		return montoImag2;
	}
	public void setMontoImag2(BigDecimal montoImag2) {
		this.montoImag2 = montoImag2;
	}
	public BigDecimal getMontoImag3() {
		return montoImag3;
	}
	public void setMontoImag3(BigDecimal montoImag3) {
		this.montoImag3 = montoImag3;
	}
	public BigDecimal getMontoImag4() {
		return montoImag4;
	}
	public void setMontoImag4(BigDecimal montoImag4) {
		this.montoImag4 = montoImag4;
	}
	public BigDecimal getMontoImag5() {
		return montoImag5;
	}
	public void setMontoImag5(BigDecimal montoImag5) {
		this.montoImag5 = montoImag5;
	}
	public BigDecimal getMontoImag6() {
		return montoImag6;
	}
	public void setMontoImag6(BigDecimal montoImag6) {
		this.montoImag6 = montoImag6;
	}
	public BigDecimal getMontoImag7() {
		return montoImag7;
	}
	public void setMontoImag7(BigDecimal montoImag7) {
		this.montoImag7 = montoImag7;
	}
	public BigDecimal getMontoImag8() {
		return montoImag8;
	}
	public void setMontoImag8(BigDecimal montoImag8) {
		this.montoImag8 = montoImag8;
	}
	public BigDecimal getMontoImag9() {
		return montoImag9;
	}
	public void setMontoImag9(BigDecimal montoImag9) {
		this.montoImag9 = montoImag9;
	}
	public BigDecimal getMontoImag10() {
		return montoImag10;
	}
	public void setMontoImag10(BigDecimal montoImag10) {
		this.montoImag10 = montoImag10;
	}
	public String getNroOperImag1() {
		return nroOperImag1;
	}
	public void setNroOperImag1(String nroOperImag1) {
		this.nroOperImag1 = nroOperImag1;
	}
	public String getNroOperImag2() {
		return nroOperImag2;
	}
	public void setNroOperImag2(String nroOperImag2) {
		this.nroOperImag2 = nroOperImag2;
	}
	public String getNroOperImag3() {
		return nroOperImag3;
	}
	public void setNroOperImag3(String nroOperImag3) {
		this.nroOperImag3 = nroOperImag3;
	}
	public String getNroOperImag4() {
		return nroOperImag4;
	}
	public void setNroOperImag4(String nroOperImag4) {
		this.nroOperImag4 = nroOperImag4;
	}
	public String getNroOperImag5() {
		return nroOperImag5;
	}
	public void setNroOperImag5(String nroOperImag5) {
		this.nroOperImag5 = nroOperImag5;
	}
	public String getNroOperImag6() {
		return nroOperImag6;
	}
	public void setNroOperImag6(String nroOperImag6) {
		this.nroOperImag6 = nroOperImag6;
	}
	public String getNroOperImag7() {
		return nroOperImag7;
	}
	public void setNroOperImag7(String nroOperImag7) {
		this.nroOperImag7 = nroOperImag7;
	}
	public String getNroOperImag8() {
		return nroOperImag8;
	}
	public void setNroOperImag8(String nroOperImag8) {
		this.nroOperImag8 = nroOperImag8;
	}
	public String getNroOperImag9() {
		return nroOperImag9;
	}
	public void setNroOperImag9(String nroOperImag9) {
		this.nroOperImag9 = nroOperImag9;
	}
	public String getNroOperImag10() {
		return nroOperImag10;
	}
	public void setNroOperImag10(String nroOperImag10) {
		this.nroOperImag10 = nroOperImag10;
	}
	public UploadedFile getFile6() {
		return file6;
	}
	public void setFile6(UploadedFile file6) {
		this.file6 = file6;
	}
	public UploadedFile getFile7() {
		return file7;
	}
	public void setFile7(UploadedFile file7) {
		this.file7 = file7;
	}
	public UploadedFile getFile8() {
		return file8;
	}
	public void setFile8(UploadedFile file8) {
		this.file8 = file8;
	}
	public UploadedFile getFile9() {
		return file9;
	}
	public void setFile9(UploadedFile file9) {
		this.file9 = file9;
	}
	public UploadedFile getFile10() {
		return file10;
	}
	public void setFile10(UploadedFile file10) {
		this.file10 = file10;
	}
	
	

}
