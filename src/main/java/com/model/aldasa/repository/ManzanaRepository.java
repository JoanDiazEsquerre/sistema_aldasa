package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Manzana;

public interface ManzanaRepository extends JpaRepository<Manzana, Integer> {

	List<Manzana> findByStatus(boolean status);
	Manzana findByName(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM manzana WHERE name=:name AND id <> :idManzana ")
	Manzana findByNameException(String name, int idManzana);
}
