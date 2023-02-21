package com.model.aldasa.proyecto.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.util.EstadoLote;

@ManagedBean
@ViewScoped
public class ContratoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	private List<Lote> lstLotesSinContrato;
	
	private Lote loteSelected;
	
	private String nombreLoteSelected;
	
	@PostConstruct
	public void init() {
		

	}
	
	public void listarLotesSinContrato() {
		lstLotesSinContrato = new ArrayList<>();
		List<Lote> lstLotesVendidos = loteService.findByStatus(EstadoLote.VENDIDO.getName());
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
	}
	
	public void eliminarLoteSelected() {
		loteSelected=null;
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


	
	
	
}
