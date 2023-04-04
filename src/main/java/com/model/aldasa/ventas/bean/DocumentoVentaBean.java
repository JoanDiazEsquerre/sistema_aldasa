package com.model.aldasa.ventas.bean;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
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

import org.primefaces.event.CellEditEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.TipoProductoType;

@ManagedBean
@ViewScoped
public class DocumentoVentaBean extends BaseBean implements Serializable{

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
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	private boolean estado = true;

	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	private LazyDataModel<Cuota> lstCuotaLazy;
	private LazyDataModel<Voucher> lstVoucherLazy;

	
	private List<SerieDocumento> lstSerieDocumento;
	private List<Cuota> lstCuota;    
	private List<Voucher> lstVoucher;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = new ArrayList<>();
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = new ArrayList<>(); 
	private List<DocumentoVenta> lstDocumentoVenta = new ArrayList<>();


	private DocumentoVenta documentoVentaSelected ;
	private SerieDocumento serieDocumentoSelected ;
	private Cuota cuotaSelected ;
	private Voucher voucherSelected ;
	private Cliente clienteSelected;

	private DocumentoVenta documentoVentaNew;
	private DetalleDocumentoVenta detalleDocumentoVentaSelected;
	private DetalleDocumentoVenta detalleDocumentoVenta;
	private Producto productoCuota;
	private Producto productoInteres;
	private Producto productoVoucher;
	private Person persona;

	
	private Date fechaEmision = new Date() ;
	private String fechaTextoVista, montoLetra;
	private String tipoComprobante, ruc, nombreRazonSocial, direccion, observacion, numero ; 
	private String tipoPago = "Contado";
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
	
	private String tituloDialog;
	
	private NumeroALetra numeroALetra = new  NumeroALetra();


	SimpleDateFormat sdf = new SimpleDateFormat("dd 'de'  MMMMM 'del' yyyy");
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		iniciarDatosDocVenta();	
		tipoComprobante="B";
		listarSerie();
		iniciarLazyCuota(); 
		iniciarLazyVoucher();
		
		productoCuota = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.CUOTA.getTipo());
		productoInteres = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.INTERES.getTipo());
		productoVoucher = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.SEPARACION.getTipo());
		
		
	} 
	
	public void aplicarPrePago() {
		if(lstDetalleDocumentoVenta.isEmpty()) {		
			addErrorMessage("Se han agregado detalles al documento.");
			return;
		}else {
			if(lstDetalleDocumentoVenta.size()<=2) {
				addErrorMessage("Debe importar más de dos cuotas para aplicar Pre-Pago"); 
				return;
			}
			
			// para validar que las cuotas sean consecutivas
//			int primerNumCuota = lstDetalleDocumentoVenta.get(0).getCuota().getNroCuota();
			int primerNumSgte = lstDetalleDocumentoVenta.get(0).getCuota().getNroCuota()+1;
			int contador = 1;
			for(DetalleDocumentoVenta detalle : lstDetalleDocumentoVenta) {
				if(esImpar(contador)) {
					//valida que todos los detalles sean importadas de cuotas, puedes?(valida si la cuota es null, mesnaje que todos los documentos importados deben ser cuotas )
					if(detalle.getCuota() == null) {
						addInfoMessage("Todos los documentos importados deben ser cuotas.");
					}
					
					
					if(contador!=1) {	
						if(detalle.getCuota().getNroCuota()== primerNumSgte) {
							primerNumSgte++;
						}else {
							addErrorMessage("No se puede aplicar Pre-Pago, debe importar cuotas consecutivas.");
							return; 
						}
					}
				}
				
				contador++;
			}
			
			
		}
		// toma nota, asi se obtiene el primer valor de una lista
//		una pregunta, supongamos qe estemos en el caso que ueremos validar el segundo detalle, como o haces? ok
//		DetalleDocumentoVenta primerDetalle = lstDetalleDocumentoVenta.get(0);
//		valida que detalle no sea nulo
		
		Cuota primeraCuota = lstDetalleDocumentoVenta.get(0).getCuota();
		
		if(primeraCuota!=null) {
			
		}else {
			addErrorMessage("");
		}
		
		addInfoMessage("se puede hacer la sigte validacion..."); 
		
		
			
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
		if(detalle.getAmortizacion()==null) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else if (detalle.getAmortizacion().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else {
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
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
	
	public void saveDocumentoVenta() {
		
		if(lstDetalleDocumentoVenta.isEmpty()) { 
			addErrorMessage("Debes importar al menos un documento.");
			return;
		}
		
		if(ruc.equals("")) {
			if(tipoComprobante.equals("B")) {
				addErrorMessage("Ingresar DNI.");
			}else {
				addErrorMessage("Ingresar RUC.");
			}
			return;
		}
		
		if(nombreRazonSocial.equals("")) {
			if(ruc.equals("")) {
				if(tipoComprobante.equals("B")) {
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
		
		if(clienteSelected==null) {
			Cliente cliente = new Cliente();
			
			cliente.setPerson(persona);
			cliente.setRazonSocial(nombreRazonSocial);
			cliente.setNombreComercial("");   
			cliente.setRuc(ruc);
			cliente.setDireccion(direccion);
			cliente.setPersonaNatural(tipoComprobante.equals("B")?true:false);
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
			clienteSelected.setRuc(ruc);
			clienteSelected.setNombreComercial(nombreRazonSocial);
			clienteSelected.setDireccion(direccion);
			clienteService.save(clienteSelected);
		}
		
		DocumentoVenta documentoVenta = new DocumentoVenta();
		documentoVenta.setCliente(clienteSelected);
		documentoVenta.setDocumentoVentaRef(null);
		documentoVenta.setSucursal(navegacionBean.getSucursalLogin());
		documentoVenta.setTipoComprobante(tipoComprobante);
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
					if(d.getCuota().getCuotaTotal() == d.getImporteVenta()) {
						d.getCuota().setPagoTotal("S");
					}else {
						d.getCuota().setAdelanto(d.getImporteVenta());
					}
					
					cuotaService.save(d.getCuota()); 
				}
				
				if(d.getVoucher()!=null) {
					d.getVoucher().setGeneraDocumento(true); 
					voucherService.save(d.getVoucher());
				}
					
			}  
			
			lstDetalleDocumentoVenta.clear();// claer es limpiar en ingles prueba
			ruc = "";
			nombreRazonSocial = "";
			direccion = "";
			calcularTotales();
			addInfoMessage("Se guardó el documento correctamente.");
			
		}else {
			addErrorMessage("No se puede guardar el documento."); 
			return;
		}
		
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
		if(tipoComprobante.equals("B")) {
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
		if(tipoComprobante.equals("B")) {
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
		detalle.setEstado(true);
		lstDetalleDocumentoVenta.add(detalle);
		
		calcularTotales();
		persona = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson(); 
		addInfoMessage("Voucher importada correctamente."); 
		
	}
	
	public void calcularTotales() {
		opInafecta=BigDecimal.ZERO;
		importeTotal=BigDecimal.ZERO;
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				opInafecta= opInafecta.add(d.getImporteVenta());
				importeTotal= importeTotal.add(d.getImporteVenta());
			}
		}
	} 
	
	public void eliminarDetalleVenta(int index) {
		lstDetalleDocumentoVenta.remove(index);
		lstDetalleDocumentoVenta.remove(index);
		calcularTotales();
		if(lstDetalleDocumentoVenta.isEmpty()) {
			clienteSelected = null;
			ruc = "";
			nombreRazonSocial = "";
			direccion = "";
			cuotaSelected = null;
			
		}
		
	}
	
	public void iniciarDatosDocVenta() {
		
		documentoVentaNew= new DocumentoVenta(); 
		documentoVentaNew.setFechaEmision(new Date());
		
	}
	
	public void listarSerie() {
		lstSerieDocumento = serieDocumentoService.findByTipoComprobanteAndSucursal(tipoComprobante, navegacionBean.getSucursalLogin());
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
               
                Page<DocumentoVenta> pageDocumentoVenta=null;
               
                
                pageDocumentoVenta= documentoVentaService.findByEstado(estado, pageable);
                
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
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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
	
	
}
