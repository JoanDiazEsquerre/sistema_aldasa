package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Usuario;

public interface UsuarioService {
	
	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario user);
	void delete(Usuario user);
	List<Usuario> findByStatus(boolean estado);
	Usuario findByPerson(Person person);
	Usuario findByUsernameAndStatus(String username, boolean status);
	Usuario findByUsername(String username);
	Usuario findByUsernameException(String username, int idUser);
	Usuario findByPersonException(int idPersona, int idUser);
	List<Usuario> findByProfileIdAndStatus(Integer idProfile,boolean status);
	List<Usuario> findByTeamPersonSupervisorAndStatus(Person personSupervisor,boolean status);
	
	Page<Usuario> findByUsernameLikeAndStatus(String username, boolean status, Pageable pageable);


}
