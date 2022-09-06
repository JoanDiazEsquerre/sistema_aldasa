package com.model.pavita.service;

import java.util.List;

import com.model.pavita.entity.Usuario;

public interface UsuarioService {
	
	List<Usuario> findByStatus(int estado);
//	List<Usuario> findByRolGP(String estado);

}
