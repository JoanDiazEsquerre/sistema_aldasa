package com.model.aldasa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comision")
public class Comision {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="fechaini")
	private Date fechaIni;
	
	@Column(name="fechacierre")
	private Date fechaCierre;
	
	@Column(name="comisioncontado")
	private Integer comisionContado;
	
	@Column(name="comisioncredito")
	private Integer comisionCredito;
		
	@Column(name="basicojunior")
	private BigDecimal basicoJunior;
	
	@Column(name="bonojunior")
	private BigDecimal bonoJunior;
	
	@Column(name="basicosenior")
	private BigDecimal basicoSenior;
	
	@Column(name="bonosenior")
	private BigDecimal bonoSenior;
	
	@Column(name="basicomaster")
	private BigDecimal basicoMaster;
	
	@Column(name="bonomaster")
	private BigDecimal bonoMaster;

	@Column(name="comisionsupervisor")
	private Integer comisionSupervisor;
	
	@Column(name="comisionmetasupervisor")
	private Integer comisionMetaSupervisor;

	@Column(name="bonometasupervisor")
	private BigDecimal bonoMetaSupervisor;

	private Integer subgerente;
	
	@Column(name="metaonline")
	private Integer metaOnline;
	
	@Column(name="primeraventacreditoonline")
	private BigDecimal primeraVentaCreditoOnline;
	
	@Column(name="primeraventacontadoonline")
	private BigDecimal primeraVentaContadoOnline;
	
	@Column(name="bonocreditoonline")
	private BigDecimal bonoCreditoOnline;
	
	@Column(name="bonocontadoonline")
	private BigDecimal bonoContadoOnline;
	
	private Boolean estado;
	
	@Column(name="metaasesorexterno")
	private Integer metaAsesorExterno;
	
	@Column(name="basicoasesorexterno")
	private BigDecimal basicoAsesorExterno;
	
	@Column(name="comisionsupervisoronline")
	private BigDecimal comisionSupervisorOnline;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public Integer getComisionContado() {
		return comisionContado;
	}
	public void setComisionContado(Integer comisionContado) {
		this.comisionContado = comisionContado;
	}
	public Integer getComisionCredito() {
		return comisionCredito;
	}
	public void setComisionCredito(Integer comisionCredito) {
		this.comisionCredito = comisionCredito;
	}

	public Integer getComisionSupervisor() {
		return comisionSupervisor;
	}
	public void setComisionSupervisor(Integer comisionSupervisor) {
		this.comisionSupervisor = comisionSupervisor;
	}
	public Integer getComisionMetaSupervisor() {
		return comisionMetaSupervisor;
	}
	public void setComisionMetaSupervisor(Integer comisionMetaSupervisor) {
		this.comisionMetaSupervisor = comisionMetaSupervisor;
	}
	
	public Integer getSubgerente() {
		return subgerente;
	}
	public void setSubgerente(Integer subgerente) {
		this.subgerente = subgerente;
	}
	public Integer getMetaOnline() {
		return metaOnline;
	}
	public void setMetaOnline(Integer metaOnline) {
		this.metaOnline = metaOnline;
	}

	public BigDecimal getBasicoJunior() {
		return basicoJunior;
	}
	public void setBasicoJunior(BigDecimal basicoJunior) {
		this.basicoJunior = basicoJunior;
	}
	public BigDecimal getBonoJunior() {
		return bonoJunior;
	}
	public void setBonoJunior(BigDecimal bonoJunior) {
		this.bonoJunior = bonoJunior;
	}
	public BigDecimal getBasicoSenior() {
		return basicoSenior;
	}
	public void setBasicoSenior(BigDecimal basicoSenior) {
		this.basicoSenior = basicoSenior;
	}
	public BigDecimal getBonoSenior() {
		return bonoSenior;
	}
	public void setBonoSenior(BigDecimal bonoSenior) {
		this.bonoSenior = bonoSenior;
	}
	public BigDecimal getBasicoMaster() {
		return basicoMaster;
	}
	public void setBasicoMaster(BigDecimal basicoMaster) {
		this.basicoMaster = basicoMaster;
	}
	public BigDecimal getBonoMaster() {
		return bonoMaster;
	}
	public void setBonoMaster(BigDecimal bonoMaster) {
		this.bonoMaster = bonoMaster;
	}
	public BigDecimal getBonoMetaSupervisor() {
		return bonoMetaSupervisor;
	}
	public void setBonoMetaSupervisor(BigDecimal bonoMetaSupervisor) {
		this.bonoMetaSupervisor = bonoMetaSupervisor;
	}
	public BigDecimal getPrimeraVentaCreditoOnline() {
		return primeraVentaCreditoOnline;
	}
	public void setPrimeraVentaCreditoOnline(BigDecimal primeraVentaCreditoOnline) {
		this.primeraVentaCreditoOnline = primeraVentaCreditoOnline;
	}
	public BigDecimal getPrimeraVentaContadoOnline() {
		return primeraVentaContadoOnline;
	}
	public void setPrimeraVentaContadoOnline(BigDecimal primeraVentaContadoOnline) {
		this.primeraVentaContadoOnline = primeraVentaContadoOnline;
	}
	public BigDecimal getBonoCreditoOnline() {
		return bonoCreditoOnline;
	}
	public void setBonoCreditoOnline(BigDecimal bonoCreditoOnline) {
		this.bonoCreditoOnline = bonoCreditoOnline;
	}
	public BigDecimal getBonoContadoOnline() {
		return bonoContadoOnline;
	}
	public void setBonoContadoOnline(BigDecimal bonoContadoOnline) {
		this.bonoContadoOnline = bonoContadoOnline;
	}
	public BigDecimal getBasicoAsesorExterno() {
		return basicoAsesorExterno;
	}
	public void setBasicoAsesorExterno(BigDecimal basicoAsesorExterno) {
		this.basicoAsesorExterno = basicoAsesorExterno;
	}
	public BigDecimal getComisionSupervisorOnline() {
		return comisionSupervisorOnline;
	}
	public void setComisionSupervisorOnline(BigDecimal comisionSupervisorOnline) {
		this.comisionSupervisorOnline = comisionSupervisorOnline;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Integer getMetaAsesorExterno() {
		return metaAsesorExterno;
	}
	public void setMetaAsesorExterno(Integer metaAsesorExterno) {
		this.metaAsesorExterno = metaAsesorExterno;
	}

	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Comision) && (id != null)
            ? id.equals(((Comision) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
