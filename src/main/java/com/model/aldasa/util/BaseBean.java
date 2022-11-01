/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model.aldasa.util;

//import com.webapp.base.bean.SessionBean;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.PrimeFaces;


/**
 *
 * @author Jhon
 */
public class BaseBean {

     public BaseBean() {
    }

    public void init() {
    }
    

    public ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public ServletContext getServletContext() {
        return (ServletContext) this.getExternalContext().getContext();
    }

    public HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public HttpServletResponse getServletResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public String getRequestParameter(String name) {
        return (String) getExternalContext().getRequestParameterMap().get(name);
    }

    public Map getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    public void addInfoMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO,"Información", msg);
        PrimeFaces.current().ajax().update("growl");
    }

    public void addWarnMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_WARN,"Alerta", msg);
        PrimeFaces.current().ajax().update("growl");
    }

    public void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR,"Error", msg);
        PrimeFaces.current().ajax().update("growl");
    }

    public void addFatalMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_FATAL,"Fatal", msg);
        PrimeFaces.current().ajax().update("growl");
    }
   
     
    /*private void addMessage(FacesMessage.Severity severity, String message) {
        FacesMessage facesMessage = new FacesMessage(severity, "", message);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }*/
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }


    public Object getBean(String bean) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ValueExpression ve = app.getExpressionFactory().
                createValueExpression(fc.getELContext(),
                String.format("#{%s}", bean),
                Object.class);
        return ve.getValue(fc.getELContext());
    }

    /* public SessionBean getSessionBean() {
        return (SessionBean) this.getBean("sessionBean");
    }*/

    
    public void removeViewBean(String beanName) {
        if (FacesContext.getCurrentInstance().getViewRoot() != null) {
            FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(beanName);
        }
    }

    public void removeSessionBean(String beanName) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey(beanName)) {
            fc.getExternalContext().getSessionMap().remove(beanName);
        }
    }
}
