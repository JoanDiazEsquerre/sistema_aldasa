package com.model.pavita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.pavita.entity.Project;

public interface ProyectoRepository extends JpaRepository<Project, Integer> {
//	@Query(value = "select p from Proyecto p LEFT JOIN p.cartera c LEFT JOIN c.categoria WHERE p.estado=:estado")
//	List<Proyecto> findByEstado(@Param(value = "estado") String estado);
//	
	
	List<Project> findByStatus(String status);
	

}
																																												