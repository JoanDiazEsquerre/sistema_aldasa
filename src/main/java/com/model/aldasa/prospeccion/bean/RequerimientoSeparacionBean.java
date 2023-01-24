package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;

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
	
	private LazyDataModel<RequerimientoSeparacion> lstReqSepLazy;

	private RequerimientoSeparacion requerimientoSeparacionSelected;
	
	private Date fechaSeparacion, fechaVencimiento;
	private String estado = "Pendiente";
	private String cambioEstado;
	private String titleDialog;



	@PostConstruct
	public void init() {
		iniciarLazy();
	}
	
	public void modifyRequerimiento() {
		titleDialog ="REQUERIMIENTO DE SEPARACIÓN: ";
		cambioEstado = requerimientoSeparacionSelected.getEstado();
		fechaSeparacion = new Date();
		fechaVencimiento = sumaRestarFecha(fechaSeparacion, 7);
		
	}
	
	public void saveReqSep() {
		if(cambioEstado.equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar estado"));
			return;
		}
		
		if(cambioEstado.equals(EstadoRequerimientoSeparacionType.APROBADO.getDescripcion()) && fechaSeparacion.after(fechaVencimiento)) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La fecha de vencimiento tiene que ser mayor a fecha de separación"));
			return;
		}
		
		if(cambioEstado.equals(EstadoRequerimientoSeparacionType.RECHAZADO.getDescripcion())) {
			requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.RECHAZADO.getDescripcion());
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente." ));
			
		}else if(cambioEstado.equals(EstadoRequerimientoSeparacionType.APROBADO.getDescripcion())){
			
			Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
			if(lote.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
				
				requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.APROBADO.getDescripcion());
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
				
				FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente." ));

			}else {
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote se encuentra " + lote.getStatus()));

			}
			
		} else {
			requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.PENDIENTE.getDescripcion());
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			
			Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
			lote.setStatus(EstadoLote.DISPONIBLE.getName());
			lote.setFechaSeparacion(null);
			lote.setFechaVencimiento(null);
			lote.setPersonAssessor(null);
			lote.setPersonSupervisor(null);
			lote.setPersonVenta(null);
			
			loteService.save(lote);

			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente." ));

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
				return datasource = pageReqSep.getContent();
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
	public String getTitleDialog() {
		return titleDialog;
	}
	public void setTitleDialog(String titleDialog) {
		this.titleDialog = titleDialog;
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
	public String getCambioEstado() {
		return cambioEstado;
	}
	public void setCambioEstado(String cambioEstado) {
		this.cambioEstado = cambioEstado;
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

	
	
}
