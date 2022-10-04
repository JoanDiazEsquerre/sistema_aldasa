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
@Table(name = "prospection")
public class Prospection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idprospect")
	Prospect prospect;
	
	@ManyToOne
	@JoinColumn(name="idpersonassessor")
	Person personAssessor;
	
	@ManyToOne
	@JoinColumn(name="idpersonsupervisor")
	Person personSupervisor;
	
	@Column(name="dateregister")
	Date dateRegister;
	
	@Column(name="datestart")
	Date dateStart;
	
	@Column(name="origincontact")
	String originContact;
	
	String status;
	int porcentage;
	
	@ManyToOne
	@JoinColumn(name="idproject")
	Project project;

	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Prospect getProspect() {
		return prospect;
	}
	public void setProspect(Prospect prospect) {
		this.prospect = prospect;
	}
	public Date getDateRegister() {
		return dateRegister;
	}
	public void setDateRegister(Date dateRegister) {
		this.dateRegister = dateRegister;
	}
	public String getOriginContact() {
		return originContact;
	}
	public void setOriginContact(String originContact) {
		this.originContact = originContact;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public int getPorcentage() {
		return porcentage;
	}
	public void setPorcentage(int porcentage) {
		this.porcentage = porcentage;
	}
	public Person getPersonAssessor() {
		return personAssessor;
	}
	public void setPersonAssessor(Person personAssessor) {
		this.personAssessor = personAssessor;
	}
	public Person getPersonSupervisor() {
		return personSupervisor;
	}
	public void setPersonSupervisor(Person personSupervisor) {
		this.personSupervisor = personSupervisor;
	}
	
	

}
