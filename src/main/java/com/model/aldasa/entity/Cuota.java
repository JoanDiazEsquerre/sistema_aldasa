package com.model.aldasa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuota")
public class Cuota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="fechapago")
	private Date fechaPago;
	
	@Column(name="cuotasi")
	private Double cuotaSI;
	
	private Double interes;
	
	@Column(name="cuotatotal")
	private Double cuotaTotal;

	private Double adelante;
	
	@Column(name="pagototal")
	private String pagoTotal;
	
	@ManyToOne
	@JoinColumn(name="idcontrato")
	private Contrato contrato;

	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getCuotaSI() {
		return cuotaSI;
	}
	public void setCuotaSI(Double cuotaSI) {
		this.cuotaSI = cuotaSI;
	}
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Double getCuotaTotal() {
		return cuotaTotal;
	}
	public void setCuotaTotal(Double cuotaTotal) {
		this.cuotaTotal = cuotaTotal;
	}
	public Double getAdelante() {
		return adelante;
	}
	public void setAdelante(Double adelante) {
		this.adelante = adelante;
	}
	public String getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(String pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	
}
