package com.model.aldasa.proyecto.bean;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import com.model.aldasa.entity.Manzana;
import com.model.aldasa.service.ManzanaService;

@ManagedBean
@ViewScoped
public class ManzanaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	private List<Manzana> listManzana;
	private Manzana manzanaSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarManzana();
	}
	public void listarManzana (){
		listManzana= manzanaService.findByStatusOrderByNameAsc(estado);
	}
	public void newManzana() {
		tituloDialog="NUEVA MANZANA";
		manzanaSelected=new Manzana();
		manzanaSelected.setStatus(true);
		manzanaSelected.setName(""); 
		
	}
	
	public void modifyManzana( ) {
		tituloDialog="MODIFICAR MANZANA";
		
	}
	
	
	public void saveManzana() {
		if(manzanaSelected.getName().equals("") || manzanaSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre de la manzana."));
			listarManzana();
			return ;
		} 
		if (tituloDialog.equals("NUEVA MANZANA")) {
			Manzana validarExistencia = manzanaService.findByName(manzanaSelected.getName());
			if (validarExistencia == null) {
				manzanaService.save(manzanaSelected);
				newManzana();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarManzana();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La manzana ya existe."));
				listarManzana();
			}
		} else {
			Manzana validarExistencia = manzanaService.findByNameException(manzanaSelected.getName(), manzanaSelected.getId());
			if (validarExistencia == null) {
				manzanaService.save(manzanaSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarManzana();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La manzana ya existe."));
				listarManzana();
			}
		}
		
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	
	public Manzana getManzanaSelected() {
		return manzanaSelected;
	}
	public void setManzanaSelected(Manzana manzanaSelected) {
		this.manzanaSelected = manzanaSelected;
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
	public List<Manzana> getListManzana() {
		return listManzana;
	}
	public void setListManzana(List<Manzana> listManzana) {
		this.listManzana = listManzana;
	}

	
	

	
	
}
