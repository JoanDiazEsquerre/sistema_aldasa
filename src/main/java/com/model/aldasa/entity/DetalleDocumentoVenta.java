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
@Table(name = "detalledocumentoventa")
public class DetalleDocumentoVenta{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventa")
	private DocumentoVenta documentoVenta;
	
	@ManyToOne
	@JoinColumn(name="idproducto")
	private Producto producto;
	
	private String descripcion;
	
	private Double amortizacion;
	
	private Double interes;
	
	@Column(name="importeventa")
	private Double importeVenta;

	@ManyToOne
	@JoinColumn(name="idcuota")
	private Cuota cuota;
	
		
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getAmortizacion() {
		return amortizacion;
	}
	public void setAmortizacion(Double amortizacion) {
		this.amortizacion = amortizacion;
	}
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Double getImporteVenta() {
		return importeVenta;
	}
	public void setImporteVenta(Double importeVenta) {
		this.importeVenta = importeVenta;
	}
	public Cuota getCuota() {
		return cuota;
	}
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}

	
	
}
