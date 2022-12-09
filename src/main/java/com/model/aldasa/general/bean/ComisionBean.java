package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.service.ComisionService;

@ManagedBean
@ViewScoped
public class ComisionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	
	private List<Comision> listComision;
	private Comision comisionSelected;

	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarComision();
		
	}
	
	public void listarComision (){
		listComision= comisionService.findAll();
	}
	
	public void newComision() {
		tituloDialog="NUEVA COMISIÓN";
		comisionSelected=new Comision();
		
	}
	
	public void modifyComision( ) {
		tituloDialog="MODIFICAR COMISIÓN";
		
	}

	public List<Comision> getListComision() {
		return listComision;
	}

	public void setListComision(List<Comision> listComision) {
		this.listComision = listComision;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public Comision getComisionSelected() {
		return comisionSelected;
	}

	public void setComisionSelected(Comision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}

	public ComisionService getComisionService() {
		return comisionService;
	}

	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	
	

}
