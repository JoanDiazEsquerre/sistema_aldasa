package com.model.aldasa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String username;
	private String password;
	boolean status;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="idprofile")
	private Profile profile;
	
	@ManyToOne
	@JoinColumn(name="idteam")
	private Team team;
	
	@Column(name="modificarcronograma")
	private boolean modificarCronograma;
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public boolean isModificarCronograma() {
		return modificarCronograma;
	}
	public void setModificarCronograma(boolean modificarCronograma) {
		this.modificarCronograma = modificarCronograma;
	}	
	
}
