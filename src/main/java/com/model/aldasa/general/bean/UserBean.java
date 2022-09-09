package com.model.aldasa.general.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;

@Component
@ManagedBean
@SessionScoped
public class UserBean {
	
	@Autowired
	private UsuarioService usuarioService; 
	
	private List<Usuario> lstUsers;
	private Usuario userSelected;
	private boolean estado=true;
	
	@PostConstruct
	public void init() {
		listarUsuarios();
		
	}
	
	public void listarUsuarios() {
		lstUsers=usuarioService.findByStatus(estado);
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List<Usuario> getLstUsers() {
		return lstUsers;
	}

	public void setLstUsers(List<Usuario> lstUsers) {
		this.lstUsers = lstUsers;
	}

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Usuario getUserSelected() {
		return userSelected;
	}

	public void setUserSelected(Usuario userSelected) {
		this.userSelected = userSelected;
	}
}
