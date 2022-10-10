package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	Usuario findByUsernameAndStatus(String username,boolean status);
	Usuario findByUsername(String username);
	Usuario findById(String id);
	List<Usuario> findByStatus(boolean status);
	
	Usuario findByPerson(Person person);
	
    @Query(nativeQuery = true,value = " SELECT * FROM user  WHERE username = :username and id<>:idUser")
    Usuario findByUsernameException(String username, int idUser);
    
    @Query(nativeQuery = true,value = " SELECT * FROM user  WHERE idPerson = :idPersona and id<>:idUser")
    Usuario findByByPersonException(int idPersona, int idUser);
    
	//@Query(value = "select p from user p WHERE p.estado=:estado order by p.nombre")
	//List<Usuario> findByRolJP(@Param(value = "estado") String estado);
	
	//@Query(value = "select p from Usuario p WHERE p.estado=:estado and p.idRol=13 order by p.nombre")
//	List<Usuario> findByRolGP(@Param(value = "estado") String estado);
    
    List<Usuario> findByProfileIdAndStatus(Integer idProfile,boolean status);
    List<Usuario> findByTeamPersonSupervisorAndStatus(Person personSupervisor, boolean status);
	
	
}
