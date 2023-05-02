package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Prepago;

public interface PrepagoRepository extends JpaRepository<Prepago, Integer>{
	
	Page<Prepago> findByGeneraDocumento(boolean generaDocumento, Pageable pageable);


}
