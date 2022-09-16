package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.UsuarioRepository;
import com.model.aldasa.service.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario entity) {
		return usuarioRepository.save(entity);
	}

	@Override
	public void delete(Usuario entity) {
		usuarioRepository.delete(entity);
	}
	
	@Override
	public List<Usuario> findByStatus(boolean status) {
		return usuarioRepository.findByStatus(status);
	}
	
	@Override
	public Usuario findByPerson(Person person) {
		return usuarioRepository.findByPerson(person);
	}
	
	@Override
	public Usuario findByUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}
	
	@Override
	public Usuario findByUsernameException(String username, int idUser){
		return usuarioRepository.findByUsernameException(username, idUser);
	}
	
	@Override
	public Usuario findByByPersonException(int idPersona, int idUser) {
		return usuarioRepository.findByByPersonException(idPersona, idUser);
	}
	

}
