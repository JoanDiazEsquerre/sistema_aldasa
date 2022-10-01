package com.model.aldasa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prospect")
public class Prospect {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	Person person;
	
	@ManyToOne
	@JoinColumn(name="iduserassessor")
	Usuario usuarioAssessor;
	
	
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
	public Usuario getUsuarioAssessor() {
		return usuarioAssessor;
	}
	public void setUsuarioAssessor(Usuario usuarioAssessor) {
		this.usuarioAssessor = usuarioAssessor;
	}
	
	
	

}
