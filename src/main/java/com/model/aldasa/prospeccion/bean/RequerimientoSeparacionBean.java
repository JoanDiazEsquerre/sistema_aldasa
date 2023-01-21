package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.model.file.UploadedFiles;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class RequerimientoSeparacionBean  extends BaseBean implements Serializable {
	

	@PostConstruct
	public void init() {
		
	}


}
