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
@Table(name = "person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="civilstatus")
	private String civilStatus;
	
	@Column(name="monthentry")
	private String monthEntry;
	
	private String dni,names, surnames, address, phone, cellphone, occupation, gender,email;
	private boolean status;
	
	@ManyToOne
    @JoinColumn(name="iddistrict")
    private District district;
	
	@ManyToOne
    @JoinColumn(name="idpersonconyuge")
    private Person personconyuge;
	
	
	public Person getPersonconyuge() {
		return personconyuge;
	}
	public void setPersonconyuge(Person personconyuge) {
		this.personconyuge = personconyuge;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getSurnames() {
		return surnames;
	}
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getCivilStatus() {
		return civilStatus;
	}
	public void setCivilStatus(String civilStatus) {
		this.civilStatus = civilStatus;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMonthEntry() {
		return monthEntry;
	}
	public void setMonthEntry(String monthEntry) {
		this.monthEntry = monthEntry;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Person) && (id != null)
            ? id.equals(((Person) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
