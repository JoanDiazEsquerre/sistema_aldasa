package com.model.aldasa.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.stereotype.Component;

@Component
@ManagedBean
@SessionScoped
public class ClientBean {
	
//	private List<Client> lstClients; 
	
	private String estado = "ACT";
	
	@PostConstruct
	public void init() {

//		listarProyectos();
	}
	
	public void listarCliente() {
//		listProyecto = proyectoService.findByStatus(estado);
		
	}

}
