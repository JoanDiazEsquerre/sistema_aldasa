package com.model.aldasa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "planillaventa")
public class PlanillaVenta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="idlote")
    private Lote lote;
	
	@ManyToOne
    @JoinColumn(name="idprospecto")
    private Prospect prospecto;
	
	private String estado;
	
	@Column(name="montoventa")
	private BigDecimal montoVenta;
	
	@Column(name="tipopago")
	private String tipoPago;
	
	@Column(name="montoinicial")
	private BigDecimal montoInicial;
	
	@Column(name="numerocuota")
	private String numeroCuota;

	private BigDecimal interes;

	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	public Prospect getProspecto() {
		return prospecto;
	}
	public void setProspecto(Prospect prospecto) {
		this.prospecto = prospecto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public String getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(String numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	
	
}
