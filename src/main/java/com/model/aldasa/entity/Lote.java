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
	
	@ManyToOne
	@JoinColumn(name="idmanzana")
	private Manzana manzana;

	@ManyToOne
	@JoinColumn(name="idproject")
	private Project project;
	
	private String status;
	
	private double area, perimetro;

	@Column(name="medidafrontal")
	private double medidaFrontal;
	
	@Column(name="medidafondo")
	private double medidaFondo;
	
	@Column(name="medidaderecha")
	private double medidaDerecha;
	
	@Column(name="medidaizquierda")
	private double medidaIzquierda;
	
	@Column(name="limitefrontal")
	private String limiteFrontal;
	
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
	public Double getArea() {
		return area;
	}
	public void setArea(Double area) {
		this.area = area;
	}
	public Double getPerimetro() {
		return perimetro;
	}
	public void setPerimetro(Double perimetro) {
		this.perimetro = perimetro;
	}
	public double getMedidaIzquierda() {
		return medidaIzquierda;
	}
	public void setMedidaIzquierda(double medidaIzquierda) {
		this.medidaIzquierda = medidaIzquierda;
	}
	public String getLimiteFrontal() {
		return limiteFrontal;
	}
	public void setLimiteFrontal(String limiteFrontal) {
		this.limiteFrontal = limiteFrontal;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public void setPerimetro(double perimetro) {
		this.perimetro = perimetro;
	}
	public void setMedidaFrontal(double medidaFrontal) {
		this.medidaFrontal = medidaFrontal;
	}
	public void setMedidaFondo(double medidaFondo) {
		this.medidaFondo = medidaFondo;
	}
	public void setMedidaDerecha(double medidaDerecha) {
		this.medidaDerecha = medidaDerecha;
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
