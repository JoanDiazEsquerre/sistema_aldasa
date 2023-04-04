package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Simulador;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class SimuladorBean extends BaseBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal montoTotal,montoInteres;
	private BigDecimal montoInicial;
	private BigDecimal montoDeuda=BigDecimal.ZERO;
	private Integer numeroCuotas;
	private Integer porcentaje=0; 
	private List<Simulador> lstSimulador = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		limpiar();
	}
	
	public void limpiar() {
		
		montoTotal=null;
		montoInicial=null;
		montoDeuda=BigDecimal.ZERO;
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
		lstSimulador.clear();

		if (montoTotal == null || montoInicial == null) {
			montoDeuda = BigDecimal.ZERO;
		} else{
			montoDeuda = montoTotal.subtract(montoInicial);
		}
		
	}
	
	public void simularCuotas () {
		lstSimulador.clear();
		
		if(montoTotal==null) {
			addErrorMessage("Ingresar monto total.");
			return;
		}else if(montoTotal==BigDecimal.ZERO) {
			addErrorMessage("El monto total debe ser mayor a 0.");
			return;
		}
		
		if(montoInicial==null) {
			addErrorMessage("Ingresar monto inicial.");
			return;
		}else if(montoInicial==BigDecimal.ZERO) {
			addErrorMessage("El monto inicial debe ser mayor a 0.");
			return;
		}
		
		if(porcentaje==null) {
           addErrorMessage("Ingrese el porcentaje de interés");
            return;
        }
		
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota("0");
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(BigDecimal.ZERO);
		filaInicio.setInteres(BigDecimal.ZERO);
		filaInicio.setCuotaTotal(BigDecimal.ZERO);
		lstSimulador.add(filaInicio);
		
		if(porcentaje==0) {
			montoInteres=BigDecimal.ZERO;
			
			BigDecimal cuota = montoDeuda.divide(new BigDecimal(numeroCuotas));
			
			
			BigDecimal sumaTotal=BigDecimal.ZERO;
			List<Simulador> listaPrevia = new ArrayList<>();
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(BigDecimal.ZERO);
				filaCouta.setCuotaTotal(cuota);
				listaPrevia.add(filaCouta);
				
				sumaTotal = sumaTotal.add(filaCouta.getCuotaSI());
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaSI(montoDeuda.subtract(sumaTotal));
			filaPrimeraCuota.setInteres(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaTotal(montoDeuda.subtract(sumaTotal));
			lstSimulador.add(filaPrimeraCuota);
			
			
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(montoDeuda);
			filaTotal.setInteres(BigDecimal.ZERO);
			filaTotal.setCuotaTotal(montoDeuda);
			lstSimulador.add(filaTotal);
			
		}else {
			BigDecimal sumaTotal=BigDecimal.ZERO;
			
			BigDecimal porc= new BigDecimal(porcentaje);
			BigDecimal porcMin= porc.divide(new BigDecimal(100));
			montoInteres = montoDeuda.multiply(porcMin);
			
			BigDecimal cuota = montoDeuda.divide(new BigDecimal(numeroCuotas));
			BigDecimal sumaItems=BigDecimal.ZERO;
			BigDecimal sumaInteresItem=BigDecimal.ZERO;
			List<Simulador> listaPrevia = new ArrayList<>();
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(montoInteres.divide( new BigDecimal(numeroCuotas)));
				filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				listaPrevia.add(filaCouta);
				
				sumaItems = sumaItems.add(filaCouta.getCuotaSI());
				sumaInteresItem = sumaInteresItem.add(filaCouta.getInteres());
				sumaTotal=sumaTotal.add(filaCouta.getCuotaTotal());
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaSI(montoDeuda.subtract(sumaItems));
			filaPrimeraCuota.setInteres(montoInteres.subtract(sumaInteresItem));
			filaPrimeraCuota.setCuotaTotal(filaPrimeraCuota.getCuotaSI().add(filaPrimeraCuota.getInteres()));
			sumaTotal=sumaTotal.add(filaPrimeraCuota.getCuotaTotal());
			lstSimulador.add(filaPrimeraCuota);
			
			BigDecimal sumaCuotaSI=BigDecimal.ZERO;
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(sumaItems.add(filaPrimeraCuota.getCuotaSI()));
			filaTotal.setInteres(montoInteres);
			filaTotal.setCuotaTotal(sumaTotal);
			lstSimulador.add(filaTotal);
			
		}	
	}
	
	
	
	
	

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	public BigDecimal getMontoInteres() {
		return montoInteres;
	}
	public void setMontoInteres(BigDecimal montoInteres) {
		this.montoInteres = montoInteres;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getMontoDeuda() {
		return montoDeuda;
	}
	public void setMontoDeuda(BigDecimal montoDeuda) {
		this.montoDeuda = montoDeuda;
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
	public List<Simulador> getLstSimulador() {
		return lstSimulador;
	}
	public void setLstSimulador(List<Simulador> lstSimulador) {
		this.lstSimulador = lstSimulador;
	}
	

}
