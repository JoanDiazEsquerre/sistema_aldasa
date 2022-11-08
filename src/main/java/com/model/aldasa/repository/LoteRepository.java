package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Lote;

public interface LoteRepository extends JpaRepository<Lote, Integer> {

	List<Lote> findByStatus(boolean status);
	Lote findByNumberLote(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM lote WHERE name=:name AND id <> :idLote ")
	Lote findByNumberLoteException(String name, int idLote);
}
