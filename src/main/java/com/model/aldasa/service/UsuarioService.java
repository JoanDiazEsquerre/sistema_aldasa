package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Usuario;

public interface UsuarioService {
	
	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario user);
	void delete(Usuario user);
	List<Usuario> findByStatus(boolean estado);
	Usuario findByPerson(Person person);
	Usuario findByUsername(String username);
	Usuario findByUsernameException(String username, int idUser);
	Usuario findByByPersonException(int idPersona, int idUser);

}
