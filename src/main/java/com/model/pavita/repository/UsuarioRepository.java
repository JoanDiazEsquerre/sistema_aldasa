package com.model.pavita.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.pavita.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	Usuario findByUsername(String username);
	Usuario findById(String id);
	List<Usuario> findByStatus(int status);
	
	//@Query(value = "select p from user p WHERE p.estado=:estado order by p.nombre")
	//List<Usuario> findByRolJP(@Param(value = "estado") String estado);
	
	//@Query(value = "select p from Usuario p WHERE p.estado=:estado and p.idRol=13 order by p.nombre")
//	List<Usuario> findByRolGP(@Param(value = "estado") String estado);

	
	
}
