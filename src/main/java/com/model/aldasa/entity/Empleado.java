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
@Table(name = "empleado")
public class Empleado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Person person;
	
	@Column(name="sueldobasico")
	private BigDecimal sueldoBasico;
	
	private boolean estado;
	
	@ManyToOne
	@JoinColumn(name="idarea")
	private Area area;

	@Column(name="fechaingreso")
	private Date fechaIngreso;
	
	@Column(name="fechasalida")
	private Date fechaSalida;
	
	private String cargo;
	
	private boolean planilla;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	private boolean externo;
	
	@ManyToOne
	@JoinColumn(name="idteam")
	private Team team;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public BigDecimal getSueldoBasico() {
		return sueldoBasico;
	}
	public void setSueldoBasico(BigDecimal sueldoBasico) {
		this.sueldoBasico = sueldoBasico;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public Date getFechaSalida() {
		return fechaSalida;
	}
	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public boolean isExterno() {
		return externo;
	}
	public void setExterno(boolean externo) {
		this.externo = externo;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public boolean isPlanilla() {
		return planilla;
	}
	public void setPlanilla(boolean planilla) {
		this.planilla = planilla;
	}
	
	
}
