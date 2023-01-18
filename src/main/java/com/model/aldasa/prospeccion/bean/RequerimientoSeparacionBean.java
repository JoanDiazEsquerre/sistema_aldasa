package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class RequerimientoSeparacionBean  extends BaseBean implements Serializable {
	

	@PostConstruct
	public void init() {
		
	}

}
