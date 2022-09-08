package com.model.aldasa.service;

import java.util.List;

import com.model.aldasa.entity.Usuario;

public interface UsuarioService {
	
	List<Usuario> findByStatus(boolean estado);
//	List<Usuario> findByRolGP(String estado);

}
