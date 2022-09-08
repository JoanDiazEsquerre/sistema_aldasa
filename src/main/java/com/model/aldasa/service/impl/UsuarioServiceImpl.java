package com.model.aldasa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.UsuarioRepository;
import com.model.aldasa.service.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public List<Usuario> findByStatus(boolean status) {
		return usuarioRepository.findByStatus(status);
	}
	
//	@Override
//	public List<Usuario> findByRolGP(String estado) {
//		return usuarioRepository.findByRolGP(estado);
//	}

}
