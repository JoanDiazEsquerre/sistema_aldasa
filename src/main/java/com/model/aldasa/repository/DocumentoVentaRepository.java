package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;

public interface DocumentoVentaRepository  extends JpaRepository<DocumentoVenta, Integer>   {
	
	DocumentoVenta findByDocumentoVentaRefAndEstado(DocumentoVenta documentoVenta, boolean estado);

	
	List<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByDocumentoVentaRefAndTipoDocumentoAndEstado(DocumentoVenta documentoVenta, TipoDocumento tipoDocumento, boolean estado);
	List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionAndEnvioSunat(boolean estado, Sucursal sucursal, Date fechaEmision, boolean envioSunat);
	
	Page<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat, TipoDocumento tipoDocumento,  Pageable pageable);

	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat,  Pageable pageable);

	
	//PARA EL REPORTE DE DOCUMENTOS DE VENTAS
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user, Pageable pageable);

	Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user, Pageable pageable);
	Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user, Pageable pageable);

	// PARA DESCARGA DE CABECERA
	List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user);
	List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user);

	List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user);
	List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user);



}
