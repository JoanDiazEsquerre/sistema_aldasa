package com.model.aldasa.proyecto.bean;


import java.io.Serializable;
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

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class LoteBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	private List<Lote> listLote;
	private Lote loteSelected;
	private boolean estado = true;
	private LazyDataModel<Lote> lstLoteLazy;
	
	private String tituloDialog;

	
	@PostConstruct
	public void init() {
		listarLote();
	}
	public void listarLote (){
		listLote=loteService.findByStatus(estado);
	}
	public void newLote() {
		tituloDialog="NUEVO LOTE";
		loteSelected=new Lote();
		loteSelected.setStatus(true);
		loteSelected.setNumberLote("");
		
	}
	
	public void modifyLote( ) {
		tituloDialog="MODIFICAR LOTE";
		
	}
	
	public void iniciarLazy() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				
				String dni="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				String surnamesPerson="%"+ (filterBy.get("person.surnames")!=null?filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
//				String surnamesAssessor="%"+ (filterBy.get("personAssessor.surnames")!=null?filterBy.get("personAssessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";

				Pageable pageable = PageRequest.of(first/pageSize, pageSize);
				
				Page<Prospect> pageLote = null; 
				
				if(usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
					// si es Administrador
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonDniLike(surnamesPerson,dni,pageable);
					}else {
						pagePerson= prospectService.findByPersonSurnamesLike(surnamesPerson,pageable);
					}
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
					// si es asesor
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessor(dni, surnamesPerson, usuarioLogin.getPerson(), pageable); 
					}else {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonAssessor(surnamesPerson, usuarioLogin.getPerson(), pageable); 
					}
					
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())){
					// Es supervisor
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisor(dni, surnamesPerson, usuarioLogin.getPerson(), pageable); 
					}else {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonSupervisor(surnamesPerson, usuarioLogin.getPerson(), pageable); 
					}
				}
				
				
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}

	
	public void saveLote() {
		if(loteSelected.getNumberLote().equals("") || loteSelected.getNumberLote()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar número de lote."));
			listarLote();
			return ;
		} 
		if (tituloDialog.equals("NUEVO LOTE")) {
			Lote validarExistencia = loteService.findByNumberLote(loteSelected.getNumberLote());
			if (validarExistencia == null) {
				loteService.save(loteSelected);
				newLote();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarLote();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
				listarLote();
			}
		} else {
			Lote validarExistencia = loteService.findByNumberLoteException(loteSelected.getNumberLote(), loteSelected.getId());
			if (validarExistencia == null) {
				loteService.save(loteSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarLote();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
				listarLote();
			}
		}
		
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public List<Lote> getListLote() {
		return listLote;
	}
	public void setListLote(List<Lote> listLote) {
		this.listLote = listLote;
	}
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
	}
	

	

	
	
}
