package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Province;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.ProvinceService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class ProspeccionBean  extends BaseBean{
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{prospectionService}")
	private ProspectionService prospectionService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{prospectionDetailService}")
	private ProspectionDetailService prospectionDetailService;
	
	@ManagedProperty(value = "#{actionService}")
	private ActionService actionService;
	
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;
	
	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;
	
	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;
	
	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;
	
	@ManagedProperty(value = "#{prospectService}")
	private ProspectService prospectService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	private LazyDataModel<Prospection> lstProspectionLazy;
	
	private Prospection prospectionSelected;
	private Prospection prospectionNew;
	private ProspectionDetail prospectionDetailSelected;
	private RequerimientoSeparacion requerimientoSeparacionSelected;
	private ProspectionDetail prospectionDetailNew;
	private ProspectionDetail prospectionDetailAgendaNew;
	private Usuario usuarioLogin = new Usuario();
	private Person personNew;
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;
	private Project proyectoPlantilla;
	private PlantillaVenta plantillaVentaNew;

	private String status = "En seguimiento";
	private String rutaImagen;
	private String titleDialog,statusSelected, resultSelected;
	private boolean mostrarBotonCambioEstado;
	private Integer numMuestraImagen;
	private Date fechaImag1, fechaImag2, fechaImag3, fechaImag4, fechaImag5, fechaImag6, fechaImag7, fechaImag8, fechaImag9, fechaImag10, fechaEnvioSunat ;
	private BigDecimal montoImag1, montoImag2, montoImag3, montoImag4, montoImag5, montoImag6, montoImag7, montoImag8, montoImag9, montoImag10;
	private String nroOperImag1, nroOperImag2, nroOperImag3, nroOperImag4, nroOperImag5, nroOperImag6, nroOperImag7, nroOperImag8, nroOperImag9, nroOperImag10;
	
	
    private UploadedFile file;
    
    private UploadedFile file1;
    private UploadedFile file2;
    private UploadedFile file3;
    private UploadedFile file4;
    private UploadedFile file5;
    private UploadedFile file6;
    private UploadedFile file7;
    private UploadedFile file8;
    private UploadedFile file9;
    private UploadedFile file10;
    
	
	private List<Prospect> lstProspect;
	private List<Person> lstPersonAssessor;
	private List<SelectItem> countriesGroup;
	private List<Project> lstProject;
	private List<Manzana> lstManzanaPlantilla;
	private List<ProspectionDetail> lstProspectionDetail = new ArrayList<>();
	private List<ProspectionDetail> lstProspectionDetailAgenda = new ArrayList<>();
	private List<Action> lstActions;
	private List<Country> lstCountry;
	private List<Department> lstDepartment;
	private List<Province> lstProvince;
	private List<District> lstDistrict;
	private List<Manzana> lstManzana = new ArrayList<>();
	private List<Lote> lstLote = new ArrayList<>();
	private List<Lote> lstLotePlantilla = new ArrayList<>();
	private List<RequerimientoSeparacion> lstReqSepSelected;
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
	private Manzana manzanaSelected;
	private Manzana manzanaPlantilla;
	private Lote loteSelected;
	
	@PostConstruct
	public void init() {

		usuarioLogin = navegacionBean.getUsuarioLogin();
		listarProspect();
		listarPersonasAssessor();
		listarProject();
		listarActions();
		
		status = "En seguimiento";
		
		iniciarLazy();
		listarProject();
		listarActions();
		listarPais();
		
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
        prospectionNew = new Prospection();
        prospectionNew.setDateStart(new Date());
        newPerson();
        iniciarDatosPlantilla();    
	}
	
	public void savePlantillaVenta() {
		if(plantillaVentaNew.getProspecto()==null) {
			addErrorMessage("Debes seleccionar Prospecto.");
			return;
		}
		
		if(proyectoPlantilla ==null) {
			addErrorMessage("Debes seleccionar Proyecto.");
			return;
		}
		if(manzanaPlantilla ==null) {
			addErrorMessage("Debes seleccionar Manzana.");
			return;
		}
		
		if(plantillaVentaNew.getLote()==null) {
			addErrorMessage("Debes seleccionar Lote.");
			return;
		}
		
		if(plantillaVentaNew.getTipoPago().equals("")) {
			addErrorMessage("Debes seleccionar Tipo de Pago.");
			return;
		}
		
		if(plantillaVentaNew.getMontoVenta()==null) {
			addErrorMessage("Debes seleccionar Lote.");
			return;
		}
		
		if(plantillaVentaNew.getMontoVenta()==null) {
			addErrorMessage("Debes seleccionar Lote.");
			return;
		}
	}
	
	public void iniciarDatosPlantilla() {
		plantillaVentaNew = new PlantillaVenta();
	}
	
	public void menorarImagen() {
		if(numMuestraImagen !=1) {
			numMuestraImagen--;
		}
	}
	
	public void aumentarImagen() {
		if(numMuestraImagen !=10) {
			numMuestraImagen++;
		}
	}
	
//	public byte[] obtenerImagen() throws IOException {
//		loadImageBean.setNombreArchivo(requerimientoSeparacionSelected.getNombreImagen());
//		return loadImageBean.getImage();
//	}
	
	
	public String extension(String filename) {
		String valor = "" ;
		int index = filename.lastIndexOf('.');
		if (index == -1) {
			return valor;
		} else {
			valor = filename.substring(index + 1);
		}
		return valor;
	}
	
	
	public void listarPais() {
		lstCountry = (List<Country>) countryService.findAll();
	}
	
	public void listarDepartamentos() {
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(countrySelected !=null) {
			lstDepartment = departmentService.findByCountry(countrySelected);
		}
	}
	
	public void listarProvincias() {
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(departmentSelected !=null) {
			lstProvince = provinceService.findByDepartment(departmentSelected); 
		}
	}
	
	public void listarDistritos() {
		lstDistrict = new ArrayList<>();
		
		if(provinceSelected!=null) {
			lstDistrict = districtService.findByProvince(provinceSelected); 
		}
	}
	
	public void cargarManzanasByProyecto() {
		lstManzana = manzanaService.findByProject(prospectionSelected.getProject().getId());
	}
	
	public void listarLotes() {
		if(manzanaSelected != null) {
			lstLote = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(prospectionSelected.getProject(), manzanaSelected, "%%");
		}else {
			lstLote = new ArrayList<>();
		}
	}
	
	public void listarManzanaPlantilla() {
		manzanaPlantilla=null;
		plantillaVentaNew.setLote(null);
		if(proyectoPlantilla != null) {
			lstManzanaPlantilla = manzanaService.findByProject(proyectoPlantilla.getId());
		}else {
			lstManzanaPlantilla = new ArrayList<>();
		}
	}
	
	public void listarLotesPlantilla() {
		if(manzanaPlantilla != null) {
			lstLotePlantilla = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoPlantilla, manzanaPlantilla, "%%");
		}else {
			lstLotePlantilla = new ArrayList<>();
		}
	}
	
	
	public void listarProspect() {		
		if (Perfiles.ADMINISTRADOR.getName().equals(usuarioLogin.getProfile().getName()) ) {
			lstProspect = prospectService.findAll();
		}else if(Perfiles.ASESOR.getName().equals(usuarioLogin.getProfile().getName())) {	
			lstProspect = prospectService.findByPersonAssessor(usuarioLogin.getPerson());
		} else if (Perfiles.SUPERVISOR.getName().equals(usuarioLogin.getProfile().getName())) {
			lstProspect = prospectService.findByPersonSupervisor(usuarioLogin.getPerson());
		}
	}
	
	public void listarProject() {
		lstProject=projectService.findByStatusAndSucursal(true, navegacionBean.getSucursalLogin());
	}
	
	public void listarActions() {
		lstActions=actionService.findByStatus(true);
	}
	
	public void listarPersonasAssessor() {
		List<Usuario> lstUsersAssesor = new ArrayList<>();
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstUsersAssesor = usuarioService.findByProfileIdAndStatus(2, true);
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
	
	public void savePerson() {
		if(personNew.getSurnames().equals("") || personNew.getSurnames()==null) {
			addErrorMessage("Falta ingresar Apellidos.");
			return;
		}
		if(personNew.getNames().equals("") || personNew.getNames()==null) {
			addErrorMessage("Falta ingresar Nombres.");
			return;
		}
		
		if(personNew.getPhone().equals("") && personNew.getCellphone().equals("")) {
			addErrorMessage("Ingrese telefono o celular.");
			return;
		}
		if(districtSelected==null) {
			addErrorMessage("Ingrese distrito.");
			return;
		} else {
			personNew.setDistrict(districtSelected);
		}
		
		if (!personNew.getDni().equals("")) {
			Person buscarPersona = personService.findByDni(personNew.getDni());
			if (buscarPersona != null) {
				personNew.setId(buscarPersona.getId());
				Prospect buscarProspecto = prospectService.findByPersonAndSucursal(buscarPersona, navegacionBean.getSucursalLogin());
				if (buscarProspecto != null) {
					Date fechaRest = sumaRestarFecha(buscarProspecto.getDateBlock(), 180);
					if(fechaRest.after(new Date())) {
						if (buscarProspecto.getPersonAssessor() != null) {
							Usuario buscarInactivo = usuarioService.findByPerson(buscarProspecto.getPersonAssessor());
							addErrorMessage("El prospecto está a cargo por el asesor "+ buscarProspecto.getPersonAssessor().getSurnames() + " "+ buscarProspecto.getPersonAssessor().getNames());
							return;
						} else if (buscarProspecto.getPersonSupervisor() != null) {
							addErrorMessage("El prospecto está a cargo por el supervisor "+ buscarProspecto.getPersonSupervisor().getSurnames() + " "+ buscarProspecto.getPersonSupervisor().getNames());
							return;
						} 
					}else {
						if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
							buscarProspecto.setPersonAssessor(null);
						} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
							buscarProspecto.setPersonAssessor(usuarioLogin.getPerson());
							buscarProspecto.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
						} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
							buscarProspecto.setPersonSupervisor(usuarioLogin.getPerson());
						}
						
					
						personService.save(personNew);
						buscarProspecto.setDateBlock(new Date());
						prospectService.save(buscarProspecto);
						
						Prospection prospection = prospectionService.findByProspectAndStatus(buscarProspecto, "En seguimiento");
						if(prospection!=null) {
							prospection.setStatus("Terminado");
 							prospection.setResult("Rechazado");
 							prospectionService.save(prospection);
 							
						}
					    limpiarDatosCiudades();
					    addInfoMessage("El prospecto se guardó correctamente");
						newPerson();
						return;
					}
				}
			}
		}
		
		
		Prospect prospectNew = new Prospect();
		
		Person person =personService.save(personNew);
		
		prospectNew.setDateBlock(new Date());
		prospectNew.setPerson(person);
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			prospectNew.setPersonAssessor(null);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
			prospectNew.setPersonAssessor(usuarioLogin.getPerson());
			prospectNew.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			prospectNew.setPersonSupervisor(usuarioLogin.getPerson());
		}
		
		prospectService.save(prospectNew);
	    limpiarDatosCiudades();
	    addInfoMessage("El prospecto se guardó correctamente");
		newPerson();
		listarProspect();
		
	}
	
	public void limpiarDatosCiudades() {
		countrySelected=null;
		departmentSelected=null;
		provinceSelected=null;
		districtSelected=null;
		lstDepartment.clear();
		lstProvince.clear();
		lstDistrict.clear();
	}
	
	public Date sumaRestarFecha(Date fecha, int sumaresta){
        Calendar calendar = Calendar.getInstance();
        try{

            calendar.setTime(fecha);
            
            calendar.add(Calendar.DAY_OF_WEEK, sumaresta);
     
        }
        catch(Exception e)
        {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
    }
	
	public void newPerson() {
		personNew = new Person();
		personNew.setStatus(true);
	}
	
	public void newProspectionDetail() {
		prospectionDetailNew = new ProspectionDetail();
		prospectionDetailNew.setProspection(prospectionSelected);
		prospectionDetailNew.setDate(new Date()); 
	}
	
	public void newProspectionDetailAgenda() {
		prospectionDetailAgendaNew = new ProspectionDetail();
		prospectionDetailAgendaNew.setProspection(prospectionSelected);
	}
	
	
	public void completarDatosPersona() {
		Person buscarPorDni = personService.findByDni(personNew.getDni());
		if(buscarPorDni!=null) {
			personNew = buscarPorDni;
			personNew.setPhone("");
			personNew.setCellphone("");
			
			cargarCuidadPersona(personNew);
	
		}	
	}
	
	public void cargarCuidadPersona(Person person) {
		if(person.getDistrict()!=null) {
			countrySelected = person.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = person.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = person.getDistrict().getProvince();
			listarDistritos();
			districtSelected = person.getDistrict();
			
		}
	}
	
	public void iniciarLazy() {

		lstProspectionLazy = new LazyDataModel<Prospection>() {
			private List<Prospection> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Prospection getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Prospection pros : datasource) {
                    if (pros.getId() == intRowKey) {
                        return pros;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Prospection prospection) {
                return String.valueOf(prospection.getId());
            }

			@Override
			public List<Prospection> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String originContact="%"+ (filterBy.get("originContact")!=null?filterBy.get("originContact").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String assessor="%"+ (filterBy.get("personAssessor.surnames")!=null?filterBy.get("personAssessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String dniProspecto="%"+ (filterBy.get("prospect.person.dni")!=null?filterBy.get("prospect.person.dni").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String surnamesProspecto="%"+ (filterBy.get("prospect.person.surnames")!=null?filterBy.get("prospect.person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String surnamesAsesor="%"+ (filterBy.get("personAssessor.surnames")!=null?filterBy.get("personAssessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String surnamesSupervisor="%"+ (filterBy.get("personSupervisor.surnames")!=null?filterBy.get("personSupervisor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";

				 Sort sort=Sort.by("dateStart").descending();
	                if(sortBy!=null) {
	                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
	                	   if(entry.getValue().getOrder().isAscending()) {
	                		   sort = Sort.by(entry.getKey()).descending();
	                	   }else {
	                		   sort = Sort.by(entry.getKey()).ascending();
	                		   
	                	   }
	                	}
	                }
				
				Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<Prospection> pageProspection=null;
				
				
				if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
					pageProspection= prospectionService.findAllByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndStatusAndProjectSucursal(surnamesSupervisor, surnamesAsesor, surnamesProspecto, dniProspecto, originContact,assessor, status, navegacionBean.getSucursalLogin(), pageable);
				} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
					pageProspection= prospectionService.findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatusAndProjectSucursal(surnamesProspecto, dniProspecto, originContact,assessor,usuarioLogin.getPerson(), status, navegacionBean.getSucursalLogin(), pageable);
				} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
					pageProspection= prospectionService.findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatusAndProjectSucursal(surnamesProspecto, dniProspecto, originContact,assessor,usuarioLogin.getPerson(), status, navegacionBean.getSucursalLogin(),  pageable);
				}
				
				setRowCount((int) pageProspection.getTotalElements());
				return datasource = pageProspection.getContent();
			}
		};
	}
	
	public void mensajeERROR(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void mensajeINFO(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void saveNewProspection() {
		if(prospectionNew.getProspect()==null) {
			mensajeERROR(" Seleccione un prospecto.");
			return;
		}else {
			Prospection searchProspection = prospectionService.findByProspectAndStatus(prospectionNew.getProspect(), "En seguimiento");
			if(searchProspection != null) {
				if(searchProspection.getPersonAssessor()!=null) {
					mensajeERROR("El prospecto está en seguimiento por el asesor \n" + searchProspection.getPersonAssessor().getNames()+" "+searchProspection.getPersonAssessor().getSurnames());
					return;
				}
			}
		}
		
		if(prospectionNew.getPersonAssessor()==null) {
			mensajeERROR(" Seleccione un Asesor.");
			return;
		}
		
		if(prospectionNew.getDateStart()==null) {
			mensajeERROR(" Seleccione una fecha de inicio.");
			return;
		}
		
		if(districtSelected==null) {
			mensajeERROR("Completar Ciudad de Contacto");
			return;
		}
		
		Usuario userAsesor = usuarioService.findByPerson(prospectionNew.getPersonAssessor());
		
		prospectionNew.setPersonSupervisor(userAsesor.getTeam().getPersonSupervisor());
		prospectionNew.setDateRegister(new Date());
		prospectionNew.setStatus("En seguimiento");
		prospectionNew.setPorcentage(0);
		prospectionNew.setDistrict(districtSelected);
		Prospection nuevo= prospectionService.save(prospectionNew);
		if(nuevo!=null) {
			Prospect prospectActualiza = prospectionNew.getProspect();
			prospectActualiza.setPersonAssessor(prospectionNew.getPersonAssessor());
			prospectActualiza.setPersonSupervisor(userAsesor.getTeam().getPersonSupervisor());
			prospectService.save(prospectActualiza);
			
			addInfoMessage("Se registró correctamente el prospecto \n"+ nuevo.getProspect().getPerson().getSurnames()+" "+ nuevo.getProspect().getPerson().getNames());
			prospectionNew = new Prospection();
			prospectionNew.setDateStart(new Date());
			lstDepartment = new ArrayList<>();
			lstProvince = new ArrayList<>();
			lstDistrict = new ArrayList<>();
			countrySelected=null;
		}
	}
	
	public void modifyProspection() {
		manzanaSelected=null;
		loteSelected=null;
		titleDialog ="PROSPECTO: "+ prospectionSelected.getProspect().getPerson().getSurnames()+" "+ prospectionSelected.getProspect().getPerson().getNames();

		lstProspectionDetail = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,false);
		lstProspectionDetailAgenda = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,true);
		if(prospectionSelected.getStatus().equals("Terminado")) {
			mostrarBotonCambioEstado = false;
		}else {
			mostrarBotonCambioEstado = true;
		}
		
		newProspectionDetail();
		newProspectionDetailAgenda();
		cargarManzanasByProyecto();
		cargarRequerimiento();
	}
	
	public void saveActionProspection() {
		
		if(prospectionDetailNew.getDate()==null) {
			addErrorMessage("Selecciona Fecha y Hora.");
			return;
		}
		
		
		prospectionDetailNew.setScheduled(false); 
		
		ProspectionDetail save = prospectionDetailService.save(prospectionDetailNew);
		if(save!=null) {
			addInfoMessage("Se registró correctamente la acción.");
			lstProspectionDetail = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,false);
			
			if(prospectionDetailNew.getAction().getPorcentage()> prospectionSelected.getPorcentage()) {
				prospectionSelected.setPorcentage(prospectionDetailNew.getAction().getPorcentage());
				prospectionService.save(prospectionSelected);
			}
			
			
			newProspectionDetail();
		}
	}
	
	public void deleteDetailAction() {
		prospectionDetailService.delete(prospectionDetailSelected);
		lstProspectionDetail = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,false);
		int porcentajeUpdate =0;
		if(!lstProspectionDetail.isEmpty() || lstProspectionDetail != null) {
			for(ProspectionDetail detail:lstProspectionDetail) {
				if(detail.getAction().getPorcentage() > porcentajeUpdate) {
					porcentajeUpdate=detail.getAction().getPorcentage();
				}
			}
		}
		prospectionSelected.setPorcentage(porcentajeUpdate); 
		prospectionService.save(prospectionSelected);
		
	}
	
	public void deleteDetailActionAgenda() {
		prospectionDetailService.delete(prospectionDetailSelected);
		lstProspectionDetailAgenda = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,true);
	}
	
	public void deleteRequerimieto() {
		requerimientoSeparacionService.delete(requerimientoSeparacionSelected);
		cargarRequerimiento();
	}
	
	public void saveActionScheduledProspection() {
		if(prospectionDetailAgendaNew.getDate()==null) {
			addErrorMessage("Selecciona Fecha y Hora.");
			return;
		}
		
		prospectionDetailAgendaNew.setScheduled(true); 
		
		ProspectionDetail save = prospectionDetailService.save(prospectionDetailAgendaNew);
		if(save!=null) {
			addInfoMessage("Se agendó correctamente la acción.");
			newProspectionDetailAgenda();
			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionAndScheduled(prospectionSelected,true);
		}
		
		
	}
	
	public void saveCambioEstado() {
		prospectionSelected.setStatus(statusSelected);
		if(statusSelected.equals("En seguimiento")) {
			prospectionSelected.setResult(null); 
		}else {
			prospectionSelected.setResult(resultSelected);
			if(resultSelected.equals("Exitoso")) {
				Prospect pro = prospectionSelected.getProspect();
				pro.setDateBlock(new Date()); 
				prospectService.save(pro);
			}
		}
		
		prospectionService.save(prospectionSelected);
		addInfoMessage("Se cambio correctamente el estado a: " + prospectionSelected.getStatus());
	}
	
	public void cargarRequerimiento() {
		lstReqSepSelected = requerimientoSeparacionService.findByProspection(prospectionSelected);
	}
	
	public void generarRequerimiento() {	
		if(manzanaSelected == null) {
			addErrorMessage("Ingresar manzana");
			return;
		}
		
		if(loteSelected == null) {
			addErrorMessage("Ingresar número de lote");
			return;
		}else {
			Lote l = loteService.findById(loteSelected.getId());
			if (!l.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
				addErrorMessage("El lote se encuentra " + l.getStatus());
				return;
			}
			for(RequerimientoSeparacion re : lstReqSepSelected) {
				if(re.getEstado().equals("Pendiente") && re.getLote().getId()==loteSelected.getId()) {
					addErrorMessage("Requerimiento registrado");
					return;
				}
			}
			

		}
		
		
		if(file == null) {
			addErrorMessage("Seleccione una imagen (voucher)");
			return;
		}
		
		RequerimientoSeparacion requerimientoSeparacion = new RequerimientoSeparacion();
		requerimientoSeparacion.setLote(loteSelected);
		requerimientoSeparacion.setFecha(new Date());
		requerimientoSeparacion.setEstado("Pendiente");
		requerimientoSeparacion.setProspection(prospectionSelected); 
		requerimientoSeparacion.setNombreImagen("");
		
		RequerimientoSeparacion guardarReq = requerimientoSeparacionService.save(requerimientoSeparacion);
		
		if(guardarReq != null) {
			String rename = guardarReq.getId() + "." + getExtension(file.getFileName());
			guardarReq.setNombreImagen(rename);
			requerimientoSeparacionService.save(guardarReq);
			
            subirArchivo(rename);
            addInfoMessage("Se guardo correctamente el requerimiento");
			cargarRequerimiento();
			
		}else {
			addErrorMessage("No se pudo guardar el requerimiento");
		}
		
	}

	public void subirArchivo(String nombre) {
//        File result = new File("/home/imagen/voucher_separaciones/" + nombre);
//        File result = new File("C:\\IMG-ALDASA\\" + nombre);
        File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaVoucher() + nombre);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(result);

            byte[] buffer = new byte[1024];

            int bulk;

            // Here you get uploaded picture bytes, while debugging you can see that 34818
            InputStream inputStream = file.getInputStream();

            while (true) {

                bulk = inputStream.read(buffer);

                if (bulk < 0) {

                    break;

                } //end of if

                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();

            } //end fo while(true)

            fileOutputStream.close();
            inputStream.close();

            FacesMessage msg = new FacesMessage("El archivo subió correctamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage error = new FacesMessage("The files were not uploaded!");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }
    }
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
	
	public String convertirHora (Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}
	
	
	public Converter getConversorProspect() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Prospect c = null;
                    for (Prospect si : lstProspect) {
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
                    return ((Prospect) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorManzana() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzana) {
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
                    return ((Manzana) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorManzanaPlantilla() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzanaPlantilla) {
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
                    return ((Manzana) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorLote() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Lote c = null;
                    for (Lote si : lstLote) {
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
                    return ((Lote) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorLotePlantilla() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Lote c = null;
                    for (Lote si : lstLotePlantilla) {
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
                    return ((Lote) value).getId() + "";
                }
            }
        };
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
	
	public Converter getConversorCountry() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Country c = null;
                    for (Country si : lstCountry) {
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
                    return ((Country) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorDepartment() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Department c = null;
                    for (Department si : lstDepartment) {
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
                    return ((Department) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProvince() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Province c = null;
                    for (Province si : lstProvince) {
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
                    return ((Province) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorDistrict() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	District c = null;
                    for (District si : lstDistrict) {
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
                    return ((District) value).getId() + "";
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
	
	public List<Prospect> completeProspect(String query) {
        List<Prospect> lista = new ArrayList<>();
        for (Prospect c : lstProspect) {
            if (c.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getPerson().getNames().toUpperCase().contains(query.toUpperCase()) || c.getPerson().getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
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
	

	public LazyDataModel<Prospection> getLstProspectionLazy() {
		return lstProspectionLazy;
	}
	public void setLstProspectionLazy(LazyDataModel<Prospection> lstProspectionLazy) {
		this.lstProspectionLazy = lstProspectionLazy;
	}
	public Prospection getProspectionSelected() {
		return prospectionSelected;
	}
	public void setProspectionSelected(Prospection prospectionSelected) {
		this.prospectionSelected = prospectionSelected;
	}
	public ProspectionService getProspectionService() {
		return prospectionService;
	}
	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public List<Prospect> getLstProspect() {
		return lstProspect;
	}
	public void setLstProspect(List<Prospect> lstProspect) {
		this.lstProspect = lstProspect;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public List<Person> getLstPersonAssessor() {
		return lstPersonAssessor;
	}
	public void setLstPersonAssessor(List<Person> lstPersonAssessor) {
		this.lstPersonAssessor = lstPersonAssessor;
	}
	public List<SelectItem> getCountriesGroup() {
		return countriesGroup;
	}
	public void setCountriesGroup(List<SelectItem> countriesGroup) {
		this.countriesGroup = countriesGroup;
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
	public Prospection getProspectionNew() {
		return prospectionNew;
	}
	public void setProspectionNew(Prospection prospectionNew) {
		this.prospectionNew = prospectionNew;
	}
	public String getTitleDialog() {
		return titleDialog;
	}
	public void setTitleDialog(String titleDialog) {
		this.titleDialog = titleDialog;
	}
	public List<ProspectionDetail> getLstProspectionDetail() {
		return lstProspectionDetail;
	}
	public void setLstProspectionDetail(List<ProspectionDetail> lstProspectionDetail) {
		this.lstProspectionDetail = lstProspectionDetail;
	}
	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}
	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}
	public ProspectionDetail getProspectionDetailSelected() {
		return prospectionDetailSelected;
	}
	public void setProspectionDetailSelected(ProspectionDetail prospectionDetailSelected) {
		this.prospectionDetailSelected = prospectionDetailSelected;
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
	public ProspectionDetail getProspectionDetailNew() {
		return prospectionDetailNew;
	}
	public void setProspectionDetailNew(ProspectionDetail prospectionDetailNew) {
		this.prospectionDetailNew = prospectionDetailNew;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public ProspectService getProspectService() {
		return prospectService;
	}
	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public Person getPersonNew() {
		return personNew;
	}
	public void setPersonNew(Person personNew) {
		this.personNew = personNew;
	}
	public boolean isMostrarBotonCambioEstado() {
		return mostrarBotonCambioEstado;
	}
	public void setMostrarBotonCambioEstado(boolean mostrarBotonCambioEstado) {
		this.mostrarBotonCambioEstado = mostrarBotonCambioEstado;
	}
	public ProspectionDetail getProspectionDetailAgendaNew() {
		return prospectionDetailAgendaNew;
	}
	public void setProspectionDetailAgendaNew(ProspectionDetail prospectionDetailAgendaNew) {
		this.prospectionDetailAgendaNew = prospectionDetailAgendaNew;
	}
	public List<ProspectionDetail> getLstProspectionDetailAgenda() {
		return lstProspectionDetailAgenda;
	}
	public void setLstProspectionDetailAgenda(List<ProspectionDetail> lstProspectionDetailAgenda) {
		this.lstProspectionDetailAgenda = lstProspectionDetailAgenda;
	}
	public String getStatusSelected() {
		return statusSelected;
	}
	public void setStatusSelected(String statusSelected) {
		this.statusSelected = statusSelected;
	}
	public String getResultSelected() {
		return resultSelected;
	}
	public void setResultSelected(String resultSelected) {
		this.resultSelected = resultSelected;
	}
	public CountryService getCountryService() {
		return countryService;
	}
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
	public DepartmentService getDepartmentService() {
		return departmentService;
	}
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	public ProvinceService getProvinceService() {
		return provinceService;
	}
	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
	}
	public DistrictService getDistrictService() {
		return districtService;
	}
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
	public List<Country> getLstCountry() {
		return lstCountry;
	}
	public void setLstCountry(List<Country> lstCountry) {
		this.lstCountry = lstCountry;
	}
	public List<Department> getLstDepartment() {
		return lstDepartment;
	}
	public void setLstDepartment(List<Department> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}
	public List<Province> getLstProvince() {
		return lstProvince;
	}
	public void setLstProvince(List<Province> lstProvince) {
		this.lstProvince = lstProvince;
	}
	public List<District> getLstDistrict() {
		return lstDistrict;
	}
	public void setLstDistrict(List<District> lstDistrict) {
		this.lstDistrict = lstDistrict;
	}
	public Department getDepartmentSelected() {
		return departmentSelected;
	}
	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}
	public Province getProvinceSelected() {
		return provinceSelected;
	}
	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}
	public District getDistrictSelected() {
		return districtSelected;
	}
	public void setDistrictSelected(District districtSelected) {
		this.districtSelected = districtSelected;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public Country getCountrySelected() {
		return countrySelected;
	}
	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public Manzana getManzanaSelected() {
		return manzanaSelected;
	}
	public void setManzanaSelected(Manzana manzanaSelected) {
		this.manzanaSelected = manzanaSelected;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public List<Lote> getLstLote() {
		return lstLote;
	}
	public void setLstLote(List<Lote> lstLote) {
		this.lstLote = lstLote;
	}
	public List<RequerimientoSeparacion> getLstReqSepSelected() {
		return lstReqSepSelected;
	}
	public void setLstReqSepSelected(List<RequerimientoSeparacion> lstReqSepSelected) {
		this.lstReqSepSelected = lstReqSepSelected;
	}
	public RequerimientoSeparacion getRequerimientoSeparacionSelected() {
		return requerimientoSeparacionSelected;
	}
	public void setRequerimientoSeparacionSelected(RequerimientoSeparacion requerimientoSeparacionSelected) {
		this.requerimientoSeparacionSelected = requerimientoSeparacionSelected;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	public Project getProyectoPlantilla() {
		return proyectoPlantilla;
	}
	public void setProyectoPlantilla(Project proyectoPlantilla) {
		this.proyectoPlantilla = proyectoPlantilla;
	}
	public List<Manzana> getLstManzanaPlantilla() {
		return lstManzanaPlantilla;
	}
	public void setLstManzanaPlantilla(List<Manzana> lstManzanaPlantilla) {
		this.lstManzanaPlantilla = lstManzanaPlantilla;
	}
	public Manzana getManzanaPlantilla() {
		return manzanaPlantilla;
	}
	public void setManzanaPlantilla(Manzana manzanaPlantilla) {
		this.manzanaPlantilla = manzanaPlantilla;
	}
	public List<Lote> getLstLotePlantilla() {
		return lstLotePlantilla;
	}
	public void setLstLotePlantilla(List<Lote> lstLotePlantilla) {
		this.lstLotePlantilla = lstLotePlantilla;
	}
	public PlantillaVenta getPlantillaVentaNew() {
		return plantillaVentaNew;
	}
	public void setPlantillaVentaNew(PlantillaVenta plantillaVentaNew) {
		this.plantillaVentaNew = plantillaVentaNew;
	}
	public Integer getNumMuestraImagen() {
		return numMuestraImagen;
	}
	public void setNumMuestraImagen(Integer numMuestraImagen) {
		this.numMuestraImagen = numMuestraImagen;
	}
	public UploadedFile getFile1() {
		return file1;
	}
	public void setFile1(UploadedFile file1) {
		this.file1 = file1;
	}
	public UploadedFile getFile2() {
		return file2;
	}
	public void setFile2(UploadedFile file2) {
		this.file2 = file2;
	}
	public UploadedFile getFile3() {
		return file3;
	}
	public void setFile3(UploadedFile file3) {
		this.file3 = file3;
	}
	public UploadedFile getFile4() {
		return file4;
	}
	public void setFile4(UploadedFile file4) {
		this.file4 = file4;
	}
	public UploadedFile getFile5() {
		return file5;
	}
	public void setFile5(UploadedFile file5) {
		this.file5 = file5;
	}
	public UploadedFile getFile6() {
		return file6;
	}
	public void setFile6(UploadedFile file6) {
		this.file6 = file6;
	}
	public UploadedFile getFile7() {
		return file7;
	}
	public void setFile7(UploadedFile file7) {
		this.file7 = file7;
	}
	public UploadedFile getFile8() {
		return file8;
	}
	public void setFile8(UploadedFile file8) {
		this.file8 = file8;
	}
	public UploadedFile getFile9() {
		return file9;
	}
	public void setFile9(UploadedFile file9) {
		this.file9 = file9;
	}
	public UploadedFile getFile10() {
		return file10;
	}
	public void setFile10(UploadedFile file10) {
		this.file10 = file10;
	}
	public Date getFechaImag1() {
		return fechaImag1;
	}
	public void setFechaImag1(Date fechaImag1) {
		this.fechaImag1 = fechaImag1;
	}
	public Date getFechaImag2() {
		return fechaImag2;
	}
	public void setFechaImag2(Date fechaImag2) {
		this.fechaImag2 = fechaImag2;
	}
	public Date getFechaImag3() {
		return fechaImag3;
	}
	public void setFechaImag3(Date fechaImag3) {
		this.fechaImag3 = fechaImag3;
	}
	public Date getFechaImag4() {
		return fechaImag4;
	}
	public void setFechaImag4(Date fechaImag4) {
		this.fechaImag4 = fechaImag4;
	}
	public Date getFechaImag5() {
		return fechaImag5;
	}
	public void setFechaImag5(Date fechaImag5) {
		this.fechaImag5 = fechaImag5;
	}
	public Date getFechaImag6() {
		return fechaImag6;
	}
	public void setFechaImag6(Date fechaImag6) {
		this.fechaImag6 = fechaImag6;
	}
	public Date getFechaImag7() {
		return fechaImag7;
	}
	public void setFechaImag7(Date fechaImag7) {
		this.fechaImag7 = fechaImag7;
	}
	public Date getFechaImag8() {
		return fechaImag8;
	}
	public void setFechaImag8(Date fechaImag8) {
		this.fechaImag8 = fechaImag8;
	}
	public Date getFechaImag9() {
		return fechaImag9;
	}
	public void setFechaImag9(Date fechaImag9) {
		this.fechaImag9 = fechaImag9;
	}
	public Date getFechaImag10() {
		return fechaImag10;
	}
	public void setFechaImag10(Date fechaImag10) {
		this.fechaImag10 = fechaImag10;
	}
	public Date getFechaEnvioSunat() {
		return fechaEnvioSunat;
	}
	public void setFechaEnvioSunat(Date fechaEnvioSunat) {
		this.fechaEnvioSunat = fechaEnvioSunat;
	}
	public BigDecimal getMontoImag1() {
		return montoImag1;
	}
	public void setMontoImag1(BigDecimal montoImag1) {
		this.montoImag1 = montoImag1;
	}
	public BigDecimal getMontoImag2() {
		return montoImag2;
	}
	public void setMontoImag2(BigDecimal montoImag2) {
		this.montoImag2 = montoImag2;
	}
	public BigDecimal getMontoImag3() {
		return montoImag3;
	}
	public void setMontoImag3(BigDecimal montoImag3) {
		this.montoImag3 = montoImag3;
	}
	public BigDecimal getMontoImag4() {
		return montoImag4;
	}
	public void setMontoImag4(BigDecimal montoImag4) {
		this.montoImag4 = montoImag4;
	}
	public BigDecimal getMontoImag5() {
		return montoImag5;
	}
	public void setMontoImag5(BigDecimal montoImag5) {
		this.montoImag5 = montoImag5;
	}
	public BigDecimal getMontoImag6() {
		return montoImag6;
	}
	public void setMontoImag6(BigDecimal montoImag6) {
		this.montoImag6 = montoImag6;
	}
	public BigDecimal getMontoImag7() {
		return montoImag7;
	}
	public void setMontoImag7(BigDecimal montoImag7) {
		this.montoImag7 = montoImag7;
	}
	public BigDecimal getMontoImag8() {
		return montoImag8;
	}
	public void setMontoImag8(BigDecimal montoImag8) {
		this.montoImag8 = montoImag8;
	}
	public BigDecimal getMontoImag9() {
		return montoImag9;
	}
	public void setMontoImag9(BigDecimal montoImag9) {
		this.montoImag9 = montoImag9;
	}
	public BigDecimal getMontoImag10() {
		return montoImag10;
	}
	public void setMontoImag10(BigDecimal montoImag10) {
		this.montoImag10 = montoImag10;
	}
	public String getNroOperImag1() {
		return nroOperImag1;
	}
	public void setNroOperImag1(String nroOperImag1) {
		this.nroOperImag1 = nroOperImag1;
	}
	public String getNroOperImag2() {
		return nroOperImag2;
	}
	public void setNroOperImag2(String nroOperImag2) {
		this.nroOperImag2 = nroOperImag2;
	}
	public String getNroOperImag3() {
		return nroOperImag3;
	}
	public void setNroOperImag3(String nroOperImag3) {
		this.nroOperImag3 = nroOperImag3;
	}
	public String getNroOperImag4() {
		return nroOperImag4;
	}
	public void setNroOperImag4(String nroOperImag4) {
		this.nroOperImag4 = nroOperImag4;
	}
	public String getNroOperImag5() {
		return nroOperImag5;
	}
	public void setNroOperImag5(String nroOperImag5) {
		this.nroOperImag5 = nroOperImag5;
	}
	public String getNroOperImag6() {
		return nroOperImag6;
	}
	public void setNroOperImag6(String nroOperImag6) {
		this.nroOperImag6 = nroOperImag6;
	}
	public String getNroOperImag7() {
		return nroOperImag7;
	}
	public void setNroOperImag7(String nroOperImag7) {
		this.nroOperImag7 = nroOperImag7;
	}
	public String getNroOperImag8() {
		return nroOperImag8;
	}
	public void setNroOperImag8(String nroOperImag8) {
		this.nroOperImag8 = nroOperImag8;
	}
	public String getNroOperImag9() {
		return nroOperImag9;
	}
	public void setNroOperImag9(String nroOperImag9) {
		this.nroOperImag9 = nroOperImag9;
	}
	public String getNroOperImag10() {
		return nroOperImag10;
	}
	public void setNroOperImag10(String nroOperImag10) {
		this.nroOperImag10 = nroOperImag10;
	}
	
}
