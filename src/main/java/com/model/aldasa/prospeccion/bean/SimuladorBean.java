package com.model.aldasa.prospeccion.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Simulador;

@Named
@Component
@ManagedBean
@SessionScoped
public class SimuladorBean {
	
	private Double montoTotal;
	private Double montoInicial;
	private Double montoDeuda=0.0;
	private Integer numeroCuotas;
	private Integer porcentaje=0; 
	private String textoDeuda =String.format("%,.2f",montoDeuda);
	private List<Simulador> lstSimulador = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
	}
	
	public void onPageLoad(){
		
	}

	public void calcularPorcentaje() {
		switch (numeroCuotas) {
		case 6:
			porcentaje=0;
			break;
		case 12:
			porcentaje=6;
			break;
		case 18:
			porcentaje=9;
			break;
		case 24:
			porcentaje=12;
			break;
		case 36:
			porcentaje=18;
			break;
		case 48:
			porcentaje=24;
			break;
		case 60:
			porcentaje=30;
			break;

		}
	}
	
	public void calcularMontoDeuda() {
		if (montoTotal == null || montoInicial == null) {
			montoDeuda = 0.0;
			textoDeuda = String.format("%,.2f",montoDeuda);
		} else{
			montoDeuda = montoTotal-montoInicial;
			textoDeuda = String.format("%,.2f",montoDeuda);
		}
		
	}
	
	public void simularCuotas () {
		lstSimulador.clear();
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota(0) ;
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(0.0);
		filaInicio.setInteres(0.0);
		filaInicio.setCuotaTotal(0.0);
		
		lstSimulador.add(filaInicio);
		
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public Double getMontoInicial() {
		return montoInicial;
	}

	public void setMontoInicial(Double montoInicial) {
		this.montoInicial = montoInicial;
	}

	public Integer getNumeroCuotas() {
		return numeroCuotas;
	}

	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}

	public Integer getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Integer porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Double getMontoDeuda() {
		return montoDeuda;
	}

	public void setMontoDeuda(Double montoDeuda) {
		this.montoDeuda = montoDeuda;
	}

	public String getTextoDeuda() {
		return textoDeuda;
	}

	public void setTextoDeuda(String textoDeuda) {
		this.textoDeuda = textoDeuda;
	}

	public List<Simulador> getLstSimulador() {
		return lstSimulador;
	}

	public void setLstSimulador(List<Simulador> lstSimulador) {
		this.lstSimulador = lstSimulador;
	}


}
