package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;



@Component
@ManagedBean
@SessionScoped
public class ReporteProspeccionBean {
	
	@Inject
	private NavegacionBean navegacionBean;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProspectService prospectService;
	
	@Autowired
	private ActionService actionService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProspectionDetailService prospectionDetailService;
	
	private Usuario usuarioLogin = new Usuario();
	
	private List<SelectItem> countriesGroup;
	private List<Person> lstPersonAssessor;
	private List<Action> lstActions;
	private List<Project> lstProject;
	private List<ProspectionDetail> lstProspectionDetailReporte;
	
	private Date fechaIni,fechaFin;
	private String origenContactoSelected,prospectSurnames="";
	private String nombreArchivo = "Reporte_Documento_Compra.xls";
	
	private Prospect prospectSelected;
	private Person personAssessorSelected;
	private Action actionSelected;
	private Project projectSelected;
	
	private StreamedContent fileDes;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct
	public void init() {
		fechaIni = new Date() ;
		fechaFin = new Date() ;

		countriesGroup = new ArrayList<>();
        SelectItemGroup europeCountries = new SelectItemGroup("Redes Sociales");
        europeCountries.setSelectItems(new SelectItem[]{
            new SelectItem("WhatsAap", "WhatsApp"),
            new SelectItem("Facebook", "Facebook"),
            new SelectItem("Instagram", "Instagram"),
            new SelectItem("Google/SEM", "Google/SEM"),
            new SelectItem("Google/SEO", "Google/SEO")
        });
        countriesGroup.add(europeCountries);
        
        listarActions();
        listarProject();
	}
	
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());
		listarPersonasAssessor();
	}
	
	public void procesarExcel() {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Acciones");

        CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
        //CellStyle styleSubTotal =UtilsXls.styleCell(workbook,'X');
        //CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

        Row rowTituloHoja = sheet.createRow(0);
        Cell cellTituloHoja = rowTituloHoja.createCell(0);
        cellTituloHoja.setCellValue("Reporte de Acciones");
        cellTituloHoja.setCellStyle(styleBorder);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

        Row rowSubTitulo = sheet.createRow(1);
        Cell cellSubIndex = rowSubTitulo.createCell(0);cellSubIndex.setCellValue("INDEX");cellSubIndex.setCellStyle(styleBorder);
        Cell cellSubDoc = rowSubTitulo.createCell(1);cellSubDoc.setCellValue("DOCUMENTO");cellSubDoc.setCellStyle(styleBorder);
        Cell cellSubSerie = rowSubTitulo.createCell(2);cellSubSerie.setCellValue("SERIE - NUMERO");cellSubSerie.setCellStyle(styleBorder);
        Cell cellSubFechaEmision = rowSubTitulo.createCell(3);cellSubFechaEmision.setCellValue("FECHA EMISION");cellSubFechaEmision.setCellStyle(styleBorder);
        Cell cellSubFechaVen = rowSubTitulo.createCell(4);cellSubFechaVen.setCellValue("FECHA VENCIMIENTO");cellSubFechaVen.setCellStyle(styleBorder);
        Cell cellSubTipo = rowSubTitulo.createCell(5);cellSubTipo.setCellValue("TIPO PROVEEDOR");cellSubTipo.setCellStyle(styleBorder);
        Cell cellSubTipoProv = rowSubTitulo.createCell(6);cellSubTipoProv.setCellValue("PROVEEDOR");cellSubTipoProv.setCellStyle(styleBorder);
        Cell cellSubMoneda = rowSubTitulo.createCell(7);cellSubMoneda.setCellValue("MONEDA");cellSubMoneda.setCellStyle(styleBorder);
        Cell cellSubTotal = rowSubTitulo.createCell(8);cellSubTotal.setCellValue("TOTAL");cellSubTotal.setCellStyle(styleBorder);
        Cell cellSubNeto = rowSubTitulo.createCell(9);cellSubNeto.setCellValue("NETO");cellSubNeto.setCellStyle(styleBorder);
        Cell cellSubCondicion = rowSubTitulo.createCell(10);cellSubCondicion.setCellValue("CONDICION");cellSubCondicion.setCellStyle(styleBorder);
        Cell cellSubTrans = rowSubTitulo.createCell(11);cellSubTrans.setCellValue("TRANSPORTE");cellSubTrans.setCellStyle(styleBorder);
      
        
     

        for (int j = 0; j <= 11; j++) {
            sheet.autoSizeColumn(j);
        }
        try {
            ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            fileDes = DefaultStreamedContent.builder()
                    .name(nombreArchivo)
                    .contentType("aplication/xlsx")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
   

    }
	

	public void buscarReporte() {
		lstProspectionDetailReporte = new ArrayList<ProspectionDetail>();
		String personSurnames="%"+prospectSurnames+"%";
		String assessorDni="%%";
		String action="%%";
		String originContact="%"+origenContactoSelected+"%";
		String project="%%";
		
		
		if(personAssessorSelected!=null) {
			assessorDni="%"+personAssessorSelected.getDni()+"%";
		}
		
		if(actionSelected!=null) {
			action= "%"+actionSelected.getDescription()+"%";
		}
		
		if(projectSelected!=null) {
			project="%"+ projectSelected.getName()+"%";
		}
		
		fechaFin.setHours(23);
		
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,assessorDni,"%%",action,originContact,project);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,"%"+usuarioLogin.getPerson().getDni()+"%","%%",action,originContact,project);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,assessorDni,"%"+usuarioLogin.getPerson().getDni()+"%",action,originContact,project);
		}
	}
	
	
	
	public void listarActions() {
		lstActions=actionService.findByStatus(true);
	}
	
	public void listarProject() {
		lstProject=projectService.findByStatus(true);
	}
	
	public void mensajeERROR(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void mensajeINFO(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void listarPersonasAssessor() {
		List<Usuario> lstUsersAssesor = new ArrayList<>();
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstUsersAssesor = usuarioService.findByProfileIdAndStatus(Perfiles.ASESOR.getId(), true);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {	
			lstUsersAssesor.add(usuarioLogin);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstUsersAssesor = usuarioService.findByTeamPersonSupervisorAndStatus(usuarioLogin.getPerson(), true);
		}
		
		lstPersonAssessor = new ArrayList<>();
		
		if(lstUsersAssesor!=null) {
			if(!lstUsersAssesor.isEmpty()) {
				for(Usuario ase :lstUsersAssesor ) {
					lstPersonAssessor.add(ase.getPerson());
				}
			}
		}
	}
	
	public Converter getConversorPersonAssessor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPersonAssessor) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Person) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorAction() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Action c = null;
                    for (Action si : lstActions) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Action) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProject() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProject) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Project) value).getId() + "";
                }
            }
        };
    }
	
	public List<Person> completePersonAssesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAssessor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	
	
	
	
	
	

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}

	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

	public List<SelectItem> getCountriesGroup() {
		return countriesGroup;
	}

	public void setCountriesGroup(List<SelectItem> countriesGroup) {
		this.countriesGroup = countriesGroup;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public ProspectService getProspectService() {
		return prospectService;
	}

	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}

	public Prospect getProspectSelected() {
		return prospectSelected;
	}

	public void setProspectSelected(Prospect prospectSelected) {
		this.prospectSelected = prospectSelected;
	}

	public List<Person> getLstPersonAssessor() {
		return lstPersonAssessor;
	}

	public void setLstPersonAssessor(List<Person> lstPersonAssessor) {
		this.lstPersonAssessor = lstPersonAssessor;
	}

	public Person getPersonAssessorSelected() {
		return personAssessorSelected;
	}

	public void setPersonAssessorSelected(Person personAssessorSelected) {
		this.personAssessorSelected = personAssessorSelected;
	}

	public Action getActionSelected() {
		return actionSelected;
	}

	public void setActionSelected(Action actionSelected) {
		this.actionSelected = actionSelected;
	}

	public ActionService getActionService() {
		return actionService;
	}

	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}

	public List<Action> getLstActions() {
		return lstActions;
	}

	public void setLstActions(List<Action> lstActions) {
		this.lstActions = lstActions;
	}

	public String getOrigenContactoSelected() {
		return origenContactoSelected;
	}

	public void setOrigenContactoSelected(String origenContactoSelected) {
		this.origenContactoSelected = origenContactoSelected;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public List<Project> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}

	public Project getProjectSelected() {
		return projectSelected;
	}

	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}

	public List<ProspectionDetail> getLstProspectionDetailReporte() {
		return lstProspectionDetailReporte;
	}

	public void setLstProspectionDetailReporte(List<ProspectionDetail> lstProspectionDetailReporte) {
		this.lstProspectionDetailReporte = lstProspectionDetailReporte;
	}

	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}

	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}

	public String getProspectSurnames() {
		return prospectSurnames;
	}

	public void setProspectSurnames(String prospectSurnames) {
		this.prospectSurnames = prospectSurnames;
	}



	public StreamedContent getFileDes() {
		return fileDes;
	}

	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	
	
}
