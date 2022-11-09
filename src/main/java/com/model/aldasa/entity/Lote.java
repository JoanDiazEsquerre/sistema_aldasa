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
@Table(name = "lote")
public class Lote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="numberlote")
	private String numberLote;
	
	private String status;
	
	@ManyToOne
	@JoinColumn(name="idmanzana")
	private Manzana manzana;

	@ManyToOne
	@JoinColumn(name="idproject")
	private Project project;

	@Column(name="medidafrontal")
	private Double medidaFrontal;
	
	@Column(name="medidafondo")
	private Double medidaFondo;
	
	@Column(name="medidaderecha")
	private Double medidaDerecha;
	
	@Column(name="limitefondo")
	private String limiteFondo;
	
	@Column(name="limiteizquierda")
	private String limiteIzquierda;
	
	@Column(name="limitederecha")
	private String limiteDerecha;
	
	private String comentario;
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumberLote() {
		return numberLote;
	}
	public void setNumberLote(String numberLote) {
		this.numberLote = numberLote;
	}
	public Manzana getManzana() {
		return manzana;
	}
	public void setManzana(Manzana manzana) {
		this.manzana = manzana;
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
	public Double getMedidaFrontal() {
		return medidaFrontal;
	}
	public void setMedidaFrontal(Double medidaFrontal) {
		this.medidaFrontal = medidaFrontal;
	}
	public Double getMedidaFondo() {
		return medidaFondo;
	}
	public void setMedidaFondo(Double medidaFondo) {
		this.medidaFondo = medidaFondo;
	}
	public Double getMedidaDerecha() {
		return medidaDerecha;
	}
	public void setMedidaDerecha(Double medidaDerecha) {
		this.medidaDerecha = medidaDerecha;
	}
	public String getLimiteFondo() {
		return limiteFondo;
	}
	public void setLimiteFondo(String limiteFondo) {
		this.limiteFondo = limiteFondo;
	}
	public String getLimiteIzquierda() {
		return limiteIzquierda;
	}
	public void setLimiteIzquierda(String limiteIzquierda) {
		this.limiteIzquierda = limiteIzquierda;
	}
	public String getLimiteDerecha() {
		return limiteDerecha;
	}
	public void setLimiteDerecha(String limiteDerecha) {
		this.limiteDerecha = limiteDerecha;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	
	
	
	
	

	@Override
    public boolean equals(Object other) {
        return (other instanceof Lote) && (id != null)
            ? id.equals(((Lote) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }

}
