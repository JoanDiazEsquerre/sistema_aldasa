package com.model.aldasa.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

public class BaseBean {
	
	public BaseBean() {
		
	}
	
	public void init() {
		
	}
	
	public void addInfoMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO,"Información", msg);
        PrimeFaces.current().ajax().update("growl1");
    }

    public void addWarnMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_WARN,"Alerta", msg);
        PrimeFaces.current().ajax().update("growl1");
    }

    public void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR,"Error", msg);
        PrimeFaces.current().ajax().update("growl1");
    }

    public void addFatalMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_FATAL,"Fatal", msg);
        PrimeFaces.current().ajax().update("growl1");
    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

}
