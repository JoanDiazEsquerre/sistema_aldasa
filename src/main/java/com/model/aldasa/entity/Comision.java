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
@Table(name = "comision")
public class Comision {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	int contado;
	int credito;
	
	@Column(name="metasupervisor")
	int metaSupervisor;	
	
	int supervisor;
	int subgerente;
	int meta;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getContado() {
		return contado;
	}
	public void setContado(int contado) {
		this.contado = contado;
	}
	public int getCredito() {
		return credito;
	}
	public void setCredito(int credito) {
		this.credito = credito;
	}
	public int getMetaSupervisor() {
		return metaSupervisor;
	}
	public void setMetaSupervisor(int metaSupervisor) {
		this.metaSupervisor = metaSupervisor;
	}
	public int getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(int supervisor) {
		this.supervisor = supervisor;
	}
	public int getSubgerente() {
		return subgerente;
	}
	public void setSubgerente(int subgerente) {
		this.subgerente = subgerente;
	}
	public int getMeta() {
		return meta;
	}
	public void setMeta(int meta) {
		this.meta = meta;
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
