package com.model.pavita.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.pavita.entity.Usuario;
import com.model.pavita.repository.UsuarioRepository;
import com.model.pavita.service.UsuarioService;

@Service("usuarioService")
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public List<Usuario> findByStatus(int status) {
		return usuarioRepository.findByStatus(status);
	}
	
//	@Override
//	public List<Usuario> findByRolGP(String estado) {
//		return usuarioRepository.findByRolGP(estado);
//	}

}
