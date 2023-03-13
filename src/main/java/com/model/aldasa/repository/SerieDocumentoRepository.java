package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;

public interface SerieDocumentoRepository extends JpaRepository<SerieDocumento, Integer>{
	
	List<SerieDocumento> findByTipoDocumentoAndSucursal(String tipoDocumento, Sucursal sucural);


}
