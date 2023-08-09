package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Banco;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.BancoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class RequerimientoSeparacionBean  extends BaseBean implements Serializable {
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{actionService}")
	private ActionService actionService;
	
	@ManagedProperty(value = "#{prospectionDetailService}")
	private ProspectionDetailService prospectionDetailService;
	
	@ManagedProperty(value = "#{prospectionService}")
	private ProspectionService prospectionService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{voucherService}")
	private VoucherService voucherService;
	
	private LazyDataModel<RequerimientoSeparacion> lstReqSepLazy;

	private RequerimientoSeparacion requerimientoSeparacionSelected;
	private CuentaBancaria cuentaBancariaSelected;

	
	private Date fechaSeparacion, fechaVencimiento;
	private String estado = "Pendiente";
	private String tipoTransaccion="";
	private String numeroTransaccion="";
	private boolean apruebaConta = false;
	private boolean ejecutaRequerimiento = false;
	private boolean mostrarBoton = false ;
	private int cantidad=0;
	private BigDecimal monto;
	private Date fechaOperacion = new Date() ;
	
	
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();

	@PostConstruct
	public void init() {
		permisosAprobaciones();
		iniciarLazy();
		listarCuentaBancaria();
	}
	
	public void ocultarBotonAprobar() {
		mostrarBoton=false;
	}
	
	public void botonVer() {
		mostrarBoton = false;
		cuentaBancariaSelected = null;
		monto = null;
		tipoTransaccion = "";
		numeroTransaccion = "";
		fechaOperacion = null;
	}
	
	public void listarCuentaBancaria() {
		lstCuentaBancaria=cuentaBancariaService.findByEstado(true);
	}
	
	public void permisosAprobaciones() {
		if(navegacionBean.getUsuarioLogin().getProfile().getId() == Perfiles.CONTABILIDAD.getId()) {
			apruebaConta= true;
		}
		
		if(navegacionBean.getUsuarioLogin().getProfile().getId() == Perfiles.ASISTENTE_ADMINISTRATIVO.getId()) {
			ejecutaRequerimiento= true;
		}
		
		if(navegacionBean.getUsuarioLogin().getProfile().getId() == Perfiles.ADMINISTRADOR.getId()) {
			apruebaConta= true;
			ejecutaRequerimiento= true;
		}
	}
	
	public void modifyRequerimiento() {
		fechaSeparacion = new Date();
		fechaVencimiento = sumaRestarFecha(fechaSeparacion, 7);
		
	}
	
	public void ejecutarRequerimiento() {
		if(fechaSeparacion == null) {
			addErrorMessage("Ingresar una fecha de Separación");
			return;
		}
		if(fechaVencimiento==null) {
			addErrorMessage("Ingresar una fecha de separación");
			return;
		}
		
		if(fechaSeparacion.after(fechaVencimiento)) {
			addErrorMessage("La fecha de vencimiento tiene que ser mayor a fecha de separación");
			return;
		}
		
			
		Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
		if(lote.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
			
			requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.EJECUTADO.getDescripcion());
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			
			lote.setStatus(EstadoLote.SEPARADO.getName());
			lote.setFechaSeparacion(fechaSeparacion);
			lote.setFechaVencimiento(fechaVencimiento);
			lote.setPersonSupervisor(requerimientoSeparacionSelected.getProspection().getPersonSupervisor());
			lote.setPersonAssessor(requerimientoSeparacionSelected.getProspection().getPersonAssessor());
			loteService.save(lote);
			
			ProspectionDetail detalle = new ProspectionDetail();
			detalle.setDate(fechaSeparacion);
			detalle.setScheduled(false);
			
			Optional<Action> accion = actionService.findById(9);
			
			detalle.setAction(accion.get());
			detalle.setProspection(requerimientoSeparacionSelected.getProspection());
			
			ProspectionDetail save = prospectionDetailService.save(detalle);
			if(save!=null) {
				
				if(detalle.getAction().getPorcentage()> requerimientoSeparacionSelected.getProspection().getPorcentage()) {
					requerimientoSeparacionSelected.getProspection().setPorcentage(detalle.getAction().getPorcentage());
					prospectionService.save(requerimientoSeparacionSelected.getProspection());
				}
				
			}	
			addInfoMessage("Se ejecutó separación correctamente.");
			
		}else {
			addErrorMessage("El lote se encuentra " + lote.getStatus());
		}		

	}
	
	public Date sumaRestarFecha(Date fecha, int sumaresta){
        Calendar calendar = Calendar.getInstance();
        try{

            calendar.setTime(fecha);
            
            calendar.add(Calendar.DAY_OF_WEEK, sumaresta);
     
        }
        catch(Exception e)
        {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
    }
	
	public void iniciarLazy() {

		lstReqSepLazy = new LazyDataModel<RequerimientoSeparacion>() {
			private List<RequerimientoSeparacion> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public RequerimientoSeparacion getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (RequerimientoSeparacion req : datasource) {
                    if (req.getId() == intRowKey) {
                        return req;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(RequerimientoSeparacion requerimientoSeparacion) {
                return String.valueOf(requerimientoSeparacion.getId());
            }

			@Override
			public List<RequerimientoSeparacion> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String Prospect="%"+ (filterBy.get("prospection.prospect.person.surnames")!=null?filterBy.get("prospection.prospect.person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String Supervisor="%"+ (filterBy.get("prospection.personSupervisor.surnames")!=null?filterBy.get("prospection.personSupervisor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String Assessor="%"+ (filterBy.get("prospection.personAssessor.surnames")!=null?filterBy.get("prospection.personAssessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";

				 Sort sort=Sort.by("fecha").descending();
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
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<RequerimientoSeparacion> pageReqSep=null;
				
				pageReqSep= requerimientoSeparacionService.findAllByEstado(estado, pageable);
				
				
				setRowCount((int) pageReqSep.getTotalElements());
				cantidad = getRowCount();
				return datasource = pageReqSep.getContent();
			}
		};
	}

	public void rechazarRequerimiento () {
		requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.RECHAZADO.getDescripcion());
		requerimientoSeparacionService.save(requerimientoSeparacionSelected);
	}

	
	public void validar() {
		
		if(cuentaBancariaSelected == null) {
			addErrorMessage("Seleccionar cuenta bancaria.");
			return;
		}
		if(monto==null) {
			addErrorMessage("Ingresar monto.");
			return;
		}
		if(tipoTransaccion.equals("")) {
			addErrorMessage("Seleccionar tipo de transacción.");
			return;
		}
		if(numeroTransaccion.equals("")) {
			addErrorMessage("Ingresar Nro. Transacción.");
			return;
		}
		if(fechaOperacion==null) {
			addErrorMessage("Ingresar fecha de operación.");
			return;
		}
		
		Voucher validarVoucher = voucherService.findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(cuentaBancariaSelected, monto, tipoTransaccion, numeroTransaccion, fechaOperacion);
		if(validarVoucher!=null) {
			mostrarBoton=false;
			addErrorMessage("El vaucher ya existe.");
			return;
		}else {
			addInfoMessage("Voucher validado.");
			mostrarBoton=true;
			
		}
		
	}
	
	public void aprobar() {
		
		Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
		if(lote.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
			Voucher voucher = new Voucher();
			voucher.setCuentaBancaria(cuentaBancariaSelected);
			voucher.setMonto(monto);
			voucher.setTipoTransaccion(tipoTransaccion);
			voucher.setNumeroTransaccion(numeroTransaccion);
			voucher.setFechaOperacion(fechaOperacion);
			voucher.setRequerimientoSeparacion(requerimientoSeparacionSelected);
			voucher.setEstado(true);
			voucher.setGeneraDocumento(false);
			voucherService.save(voucher);
			
			requerimientoSeparacionSelected.setEstado("Atendido");
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			
			lote.setStatus(EstadoLote.SEPARADO.getName());
			lote.setFechaSeparacion(new Date());
			lote.setFechaVencimiento(sumaRestarFecha(new Date(), 7));
			lote.setPersonSupervisor(requerimientoSeparacionSelected.getProspection().getPersonSupervisor());
			lote.setPersonAssessor(requerimientoSeparacionSelected.getProspection().getPersonAssessor());
			
			loteService.save(lote);
			
			addInfoMessage("Aprobado correctamente.");
		}else {
			addErrorMessage("El lote se encuentra " + lote.getStatus());

		}		
		

	}
	
	public void obtenerBanco() {
		if(cuentaBancariaSelected!=null) {
			System.out.println("BANCO SELECCIONADO: "+ cuentaBancariaSelected.getBanco().getAbreviatura());
		}else {
			System.out.println("SIN SELECCIONAR ");
		}
		
	}
	
	public void obtenerTipoT() {
		System.out.println("TT SELECCIONADO: "+ tipoTransaccion);
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
	
	
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public LazyDataModel<RequerimientoSeparacion> getLstReqSepLazy() {
		return lstReqSepLazy;
	}
	public void setLstReqSepLazy(LazyDataModel<RequerimientoSeparacion> lstReqSepLazy) {
		this.lstReqSepLazy = lstReqSepLazy;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public RequerimientoSeparacion getRequerimientoSeparacionSelected() {
		return requerimientoSeparacionSelected;
	}
	public void setRequerimientoSeparacionSelected(RequerimientoSeparacion requerimientoSeparacionSelected) {
		this.requerimientoSeparacionSelected = requerimientoSeparacionSelected;
	}
	public Date getFechaSeparacion() {
		return fechaSeparacion;
	}
	public void setFechaSeparacion(Date fechaSeparacion) {
		this.fechaSeparacion = fechaSeparacion;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public ActionService getActionService() {
		return actionService;
	}
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}
	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}
	public ProspectionService getProspectionService() {
		return prospectionService;
	}
	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public boolean isApruebaConta() {
		return apruebaConta;
	}
	public void setApruebaConta(boolean apruebaConta) {
		this.apruebaConta = apruebaConta;
	}
	public boolean isEjecutaRequerimiento() {
		return ejecutaRequerimiento;
	}
	public void setEjecutaRequerimiento(boolean ejecutaRequerimiento) {
		this.ejecutaRequerimiento = ejecutaRequerimiento;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public CuentaBancaria getCuentaBancariaSelected() {
		return cuentaBancariaSelected;
	}
	public void setCuentaBancariaSelected(CuentaBancaria cuentaBancariaSelected) {
		this.cuentaBancariaSelected = cuentaBancariaSelected;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public boolean isMostrarBoton() {
		return mostrarBoton;
	}
	public void setMostrarBoton(boolean mostrarBoton) {
		this.mostrarBoton = mostrarBoton;
	}
	

	
}
