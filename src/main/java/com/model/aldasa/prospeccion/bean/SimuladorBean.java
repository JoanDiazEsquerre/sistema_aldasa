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
		
		if(montoTotal==null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingrese el monto total");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		
		if(montoTotal==null) {
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
			
			for(int i=0; i<numeroCuotas;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+1)+"");
				filaCouta.setInicial(0.0);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(0.0);
				filaCouta.setCuotaTotal(cuota);
				lstSimulador.add(filaCouta);
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


}
