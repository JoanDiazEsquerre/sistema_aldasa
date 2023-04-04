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
@Table(name = "comisiones")
public class Comisiones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="idlote")
    private Lote lote;
	
	@Column(name="comisionasesor")
	private BigDecimal comisionAsesor;
	
	@Column(name="comisionsupervisor")
	private BigDecimal comisionSupervisor;
	
	@Column(name="comisionsubgerente")
	private BigDecimal comisionSubgerente;
	
	private boolean estado;
	
	@Column(name="tipoempleado")
	private String tipoEmpleado;
	
	
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
	public BigDecimal getComisionAsesor() {
		return comisionAsesor;
	}
	public void setComisionAsesor(BigDecimal comisionAsesor) {
		this.comisionAsesor = comisionAsesor;
	}
	public BigDecimal getComisionSupervisor() {
		return comisionSupervisor;
	}
	public void setComisionSupervisor(BigDecimal comisionSupervisor) {
		this.comisionSupervisor = comisionSupervisor;
	}
	public BigDecimal getComisionSubgerente() {
		return comisionSubgerente;
	}
	public void setComisionSubgerente(BigDecimal comisionSubgerente) {
		this.comisionSubgerente = comisionSubgerente;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTipoEmpleado() {
		return tipoEmpleado;
	}
	public void setTipoEmpleado(String tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}
	
	

}
