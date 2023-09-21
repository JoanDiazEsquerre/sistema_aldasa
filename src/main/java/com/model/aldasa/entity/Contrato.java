package com.model.aldasa.entity;

import java.math.BigDecimal;
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
@Table(name = "contrato")
public class Contrato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idlote")
	private Lote lote;
	
	@Column(name="fechaventa")
	Date fechaVenta;
	
	@Column(name="montoventa")
	BigDecimal montoVenta;
	
	@Column(name="tipopago")
	String tipoPago;
	
	@Column(name="montoinicial")
	BigDecimal montoInicial;
	
	@Column(name="nrocuota")
	Integer numeroCuota;
	
	BigDecimal interes;
	
	@ManyToOne
	@JoinColumn(name="idpersonventa")
	private Person personVenta;
	
	@ManyToOne
	@JoinColumn(name="idpersonventa2")
	private Person personVenta2;
	
	@ManyToOne 
	@JoinColumn(name="idpersonventa3")
	private Person personVenta3;
	
	@ManyToOne
	@JoinColumn(name="idpersonventa4")
	private Person personVenta4;
	
	@ManyToOne
	@JoinColumn(name="idpersonventa5")
	private Person personVenta5;
	
	private boolean estado;
	
	@Column(name="fechaprimeracuota")
	Date fechaPrimeraCuota;

	@Column(name="cancelaciontotal")
	private boolean cancelacionTotal;
	
	private boolean firma;
	
	@Column(name="pagoindependizacion")
	private boolean pagoIndependizacion;
	
	@ManyToOne
	@JoinColumn(name="idcomisiones")
	private Comisiones comisiones;
	
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
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public Integer getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public Person getPersonVenta() {
		return personVenta;
	}
	public void setPersonVenta(Person personVenta) {
		this.personVenta = personVenta;
	}
	public Person getPersonVenta2() {
		return personVenta2;
	}
	public void setPersonVenta2(Person personVenta2) {
		this.personVenta2 = personVenta2;
	}
	public Person getPersonVenta3() {
		return personVenta3;
	}
	public void setPersonVenta3(Person personVenta3) {
		this.personVenta3 = personVenta3;
	}
	public Person getPersonVenta4() {
		return personVenta4;
	}
	public void setPersonVenta4(Person personVenta4) {
		this.personVenta4 = personVenta4;
	}
	public Person getPersonVenta5() {
		return personVenta5;
	}
	public void setPersonVenta5(Person personVenta5) {
		this.personVenta5 = personVenta5;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Date getFechaPrimeraCuota() {
		return fechaPrimeraCuota;
	}
	public void setFechaPrimeraCuota(Date fechaPrimeraCuota) {
		this.fechaPrimeraCuota = fechaPrimeraCuota;
	}
	public boolean isCancelacionTotal() {
		return cancelacionTotal;
	}
	public void setCancelacionTotal(boolean cancelacionTotal) {
		this.cancelacionTotal = cancelacionTotal;
	}
	public boolean isFirma() {
		return firma;
	}
	public void setFirma(boolean firma) {
		this.firma = firma;
	}
	public boolean isPagoIndependizacion() {
		return pagoIndependizacion;
	}
	public void setPagoIndependizacion(boolean pagoIndependizacion) {
		this.pagoIndependizacion = pagoIndependizacion;
	}
	public Comisiones getComisiones() {
		return comisiones;
	}
	public void setComisiones(Comisiones comisiones) {
		this.comisiones = comisiones;
	}
	
	
}
