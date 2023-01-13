package com.model.aldasa.entity;

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
	private Double basicoJunior;
	
	@Column(name="bonojunior")
	private Double bonoJunior;
	
	@Column(name="basicosenior")
	private Double basicoSenior;
	
	@Column(name="bonosenior")
	private Double bonoSenior;
	
	@Column(name="basicomaster")
	private Double basicoMaster;
	
	@Column(name="bonomaster")
	private Double bonoMaster;

	@Column(name="comisionsupervisor")
	private Integer comisionSupervisor;
	
	@Column(name="comisionmetasupervisor")
	private Integer comisionMetaSupervisor;

	@Column(name="bonometasupervisor")
	private Double bonoMetaSupervisor;

	private Integer subgerente;
	
	@Column(name="metaonline")
	private Integer metaOnline;
	
	@Column(name="primeraventacreditoonline")
	private Double primeraVentaCreditoOnline;
	
	@Column(name="primeraventacontadoonline")
	private Double primeraVentaContadoOnline;
	
	@Column(name="bonocreditoonline")
	private Double bonoCreditoOnline;
	
	@Column(name="bonocontadoonline")
	private Double bonoContadoOnline;
	
	private Boolean estado;
	
	@Column(name="metaasesorexterno")
	private Integer metaAsesorExterno;
	
	@Column(name="basicoasesorexterno")
	private Double basicoAsesorExterno;
	
	@Column(name="comisionsupervisoronline")
	private Double comisionSupervisorOnline;

	
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
	public Double getBasicoJunior() {
		return basicoJunior;
	}
	public void setBasicoJunior(Double basicoJunior) {
		this.basicoJunior = basicoJunior;
	}
	public Double getBonoJunior() {
		return bonoJunior;
	}
	public void setBonoJunior(Double bonoJunior) {
		this.bonoJunior = bonoJunior;
	}
	public Double getBasicoSenior() {
		return basicoSenior;
	}
	public void setBasicoSenior(Double basicoSenior) {
		this.basicoSenior = basicoSenior;
	}
	public Double getBonoSenior() {
		return bonoSenior;
	}
	public void setBonoSenior(Double bonoSenior) {
		this.bonoSenior = bonoSenior;
	}
	public Double getBasicoMaster() {
		return basicoMaster;
	}
	public void setBasicoMaster(Double basicoMaster) {
		this.basicoMaster = basicoMaster;
	}
	public Double getBonoMaster() {
		return bonoMaster;
	}
	public void setBonoMaster(Double bonoMaster) {
		this.bonoMaster = bonoMaster;
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
	public Double getBonoMetaSupervisor() {
		return bonoMetaSupervisor;
	}
	public void setBonoMetaSupervisor(Double bonoMetaSupervisor) {
		this.bonoMetaSupervisor = bonoMetaSupervisor;
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
	public Double getPrimeraVentaCreditoOnline() {
		return primeraVentaCreditoOnline;
	}
	public void setPrimeraVentaCreditoOnline(Double primeraVentaCreditoOnline) {
		this.primeraVentaCreditoOnline = primeraVentaCreditoOnline;
	}
	public Double getPrimeraVentaContadoOnline() {
		return primeraVentaContadoOnline;
	}
	public void setPrimeraVentaContadoOnline(Double primeraVentaContadoOnline) {
		this.primeraVentaContadoOnline = primeraVentaContadoOnline;
	}
	public Double getBonoCreditoOnline() {
		return bonoCreditoOnline;
	}
	public void setBonoCreditoOnline(Double bonoCreditoOnline) {
		this.bonoCreditoOnline = bonoCreditoOnline;
	}
	public Double getBonoContadoOnline() {
		return bonoContadoOnline;
	}
	public void setBonoContadoOnline(Double bonoContadoOnline) {
		this.bonoContadoOnline = bonoContadoOnline;
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
	public Double getBasicoAsesorExterno() {
		return basicoAsesorExterno;
	}
	public void setBasicoAsesorExterno(Double basicoAsesorExterno) {
		this.basicoAsesorExterno = basicoAsesorExterno;
	}
	public Double getComisionSupervisorOnline() {
		return comisionSupervisorOnline;
	}
	public void setComisionSupervisorOnline(Double comisionSupervisorOnline) {
		this.comisionSupervisorOnline = comisionSupervisorOnline;
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
