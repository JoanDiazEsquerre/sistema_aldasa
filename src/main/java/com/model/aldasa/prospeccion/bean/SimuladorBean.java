package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Simulador;

@ManagedBean
@ViewScoped
public class SimuladorBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String montoTotalText,montoInicialText;
	private Double montoTotal,montoInteres;
	private Double montoInicial;
	private Double montoDeuda=0.0;
	private Integer numeroCuotas;
	private Integer porcentaje=0; 
	private String textoDeuda =String.format("%,.2f",montoDeuda);
	private List<Simulador> lstSimulador = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		limpiar();
	}
	
	public void limpiar() {
		montoTotalText=null;
		montoInicialText=null;
		montoTotal=null;
		montoInicial=null;
		montoDeuda=0.0;
		textoDeuda =String.format("%,.2f",montoDeuda);
		porcentaje=0; 
		numeroCuotas=6;
		lstSimulador.clear();
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
	
	public void validaMontoTotal() {
		lstSimulador.clear();
		if(montoTotalText!=null || !montoTotalText.isEmpty()) {
			if(montoTotalText.substring(0,montoTotalText.length()).matches("[0-9]*")) {
				montoTotal = Double.valueOf(montoTotalText);
				calcularMontoDeuda();
			}else {
				montoTotal=null;
				montoDeuda = 0.0;
				textoDeuda = String.format("%,.2f",montoDeuda);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "MONTO TOTAL incorrecto, solo ingresar caracteres numéricos");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
	}
	
	public void validaMontoInicial() {
		lstSimulador.clear();
		if(montoInicialText!=null || !montoInicialText.isEmpty()) {
			if(montoInicialText.substring(0,montoInicialText.length()).matches("[0-9]*")) {
				montoInicial = Double.valueOf(montoInicialText);
				calcularMontoDeuda();
			}else {
				montoInicial=null;
				montoDeuda = 0.0;
				textoDeuda = String.format("%,.2f",montoDeuda);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "MONTO INICIAL incorrecto, solo ingresar caracteres numéricos");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
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
		
		if(montoTotalText!=null || !montoTotalText.isEmpty()) {
			if(!montoTotalText.substring(0,montoTotalText.length()).matches("[0-9]*")) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "MONTO TOTAL incorrecto, solo ingresar caracteres numéricos");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
		}
		
		if(montoInicialText!=null || !montoInicialText.isEmpty()) {
			if(!montoInicialText.substring(0,montoInicialText.length()).matches("[0-9]*")) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "MONTO INICIAL incorrecto, solo ingresar caracteres numéricos");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
		}
		
		if(montoTotal==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el monto total");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(montoInicial==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el monto inicial");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(montoInicial >= montoTotal) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El monto inicial tiene que ser menor al monto total");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(porcentaje==null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el porcentaje de interés");
            PrimeFaces.current().dialog().showMessageDynamic(message);
            return;
        }
		
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota("0");
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(0.0);
		filaInicio.setInteres(0.0);
		filaInicio.setCuotaTotal(0.0);
		lstSimulador.add(filaInicio);
		
		if(porcentaje==0) {
			montoInteres=0.0;
			
			double cuota = montoDeuda / numeroCuotas;
			
			
			double sumaTotal=0.0;
			List<Simulador> listaPrevia = new ArrayList<>();
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(0.0);
				filaCouta.setCuotaSI((double)Math.round(cuota));
				filaCouta.setInteres(0.0);
				filaCouta.setCuotaTotal((double)Math.round(cuota));
				listaPrevia.add(filaCouta);
				
				sumaTotal = sumaTotal + filaCouta.getCuotaSI();
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(0.0);
			filaPrimeraCuota.setCuotaSI(montoDeuda-sumaTotal);
			filaPrimeraCuota.setInteres(0.0);
			filaPrimeraCuota.setCuotaTotal(montoDeuda-sumaTotal);
			lstSimulador.add(filaPrimeraCuota);
			
			
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(montoDeuda);
			filaTotal.setInteres(0.0);
			filaTotal.setCuotaTotal(montoDeuda);
			lstSimulador.add(filaTotal);
			
		}else {
			double sumaTotal=0.0;
			
			double porc=porcentaje;
			double porcMin= (porc/100);
			montoInteres = montoDeuda*porcMin;
			
			double cuota = montoDeuda / numeroCuotas;
			double sumaItems=0.0;
			double sumaInteresItem=0.0;
			List<Simulador> listaPrevia = new ArrayList<>();
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(0.0);
				filaCouta.setCuotaSI((double)Math.round(cuota));
				filaCouta.setInteres((double)Math.round(montoInteres/numeroCuotas));
				filaCouta.setCuotaTotal(filaCouta.getCuotaSI()+filaCouta.getInteres());
				listaPrevia.add(filaCouta);
				
				sumaItems = sumaItems + filaCouta.getCuotaSI();
				sumaInteresItem = sumaInteresItem+filaCouta.getInteres();
				sumaTotal=sumaTotal+filaCouta.getCuotaTotal();
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(0.0);
			filaPrimeraCuota.setCuotaSI(montoDeuda-sumaItems);
			filaPrimeraCuota.setInteres(montoInteres-sumaInteresItem);
			filaPrimeraCuota.setCuotaTotal(filaPrimeraCuota.getCuotaSI()+filaPrimeraCuota.getInteres());
			sumaTotal=sumaTotal+filaPrimeraCuota.getCuotaTotal();
			lstSimulador.add(filaPrimeraCuota);
			
			double sumaCuotaSI=0.0;
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(sumaItems+filaPrimeraCuota.getCuotaSI());
			filaTotal.setInteres(montoInteres);
			filaTotal.setCuotaTotal(sumaTotal);
			lstSimulador.add(filaTotal);
			
		}
		
		
		
		
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

	public Double getMontoInteres() {
		return montoInteres;
	}

	public void setMontoInteres(Double montoInteres) {
		this.montoInteres = montoInteres;
	}

	public String getMontoTotalText() {
		return montoTotalText;
	}

	public void setMontoTotalText(String montoTotalText) {
		this.montoTotalText = montoTotalText;
	}

	public String getMontoInicialText() {
		return montoInicialText;
	}

	public void setMontoInicialText(String montoInicialText) {
		this.montoInicialText = montoInicialText;
	}

	
	

}
