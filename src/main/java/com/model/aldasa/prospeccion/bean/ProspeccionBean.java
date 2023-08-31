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
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
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
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.ImagenPlantillaVentaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.PlantillaVentaService;
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
import com.model.aldasa.ventas.bean.LoadImageDocumentoBean;

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
	
	@ManagedProperty(value = "#{plantillaVentaService}")
	private PlantillaVentaService plantillaVentaService;
	
	@ManagedProperty(value = "#{imagenPlantillaVentaService}")
	private ImagenPlantillaVentaService imagenPlantillaVentaService;
	
	@ManagedProperty(value = "#{loadImagePlantillaBean}")
	private LoadImagePlantillaBean loadImagePlantillaBean;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	private LazyDataModel<Prospection> lstProspectionLazy;
	private LazyDataModel<PlantillaVenta> lstPlantillaLazy;
	
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
	private PlantillaVenta plantillaVentaSelected;
	private CuentaBancaria cuentaBancariaSelected;

	private String status = "En seguimiento";
	private String estadoPlantillaFilter = "Pendiente";
	private String rutaImagen;
	private String titleDialog,statusSelected, resultSelected;
	private boolean mostrarBotonCambioEstado;
	private String imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10;
	
    private UploadedFile file;
    
    private UploadedFile file1,file2,file3,file4,file5,file6,file7,file8,file9,file10;
	
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
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
	private Manzana manzanaSelected;
	private Manzana manzanaPlantilla;
	private Lote loteSelected;
	
	private BigDecimal monto;
	private Date fechaOperacion = new Date() ;
	private String tipoTransaccion, numeroTransaccion;
	
	@PostConstruct
	public void init() {

		usuarioLogin = navegacionBean.getUsuarioLogin();
		listarProspect();
		listarPersonasAssessor();
		listarProject();
		listarActions();
		
		status = "En seguimiento";
		
		iniciarLazy();
		
		estadoPlantillaFilter = "Pendiente";
		iniciarPlantillaLazy();
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
        lstCuentaBancaria=cuentaBancariaService.findByEstadoAndSucursal(true, navegacionBean.getSucursalLogin());
        
	}
	
	public void aprobarPlatilla() {
		plantillaVentaSelected.setEstado("Aprobado");
		plantillaVentaService.save(plantillaVentaSelected);
		addInfoMessage("Se aprobó la plantilla de venta correctamente."); 
		PrimeFaces.current().executeScript("PF('plantillaNewDialog').hide();"); 

	}
	
	public void rechazarPlantilla() {
		plantillaVentaSelected.setEstado("Rechazado");
		plantillaVentaService.save(plantillaVentaSelected);
		addInfoMessage("Se rechazó la plantilla de venta correctamente."); 
		PrimeFaces.current().executeScript("PF('plantillaNewDialog').hide();"); 

	}
	
	public void validaVoucher() {
		if(cuentaBancariaSelected == null) {
			addErrorMessage("Seleccionar una cuenta bancaria.");
			return;
		}
		
		if(monto == null) {
			addErrorMessage("Ingresar un monto.");
			return;
		}
		
		if(tipoTransaccion.equals("")) {
			addErrorMessage("Seleccionar un tipo de transacción.");
			return;
		}
		
		if(numeroTransaccion.equals("")) {
			addErrorMessage("Seleccionar un numero de Operación.");
			return;
		}
		
		if(fechaOperacion == null) {
			addErrorMessage("Ingresar una fecha de operación.");
			return;
		}
		
		List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(true, fechaOperacion, monto, numeroTransaccion, cuentaBancariaSelected, tipoTransaccion);
		if(!buscarImagen.isEmpty()) {
			addErrorMessage("Ya existe el voucher.");
		}else {
			addInfoMessage("Voucher aceptable"); 
		}
	}
	
	
	public void verVoucher() {
		cuentaBancariaSelected = null;
		monto = null;
		tipoTransaccion = "";
		numeroTransaccion = "";
		fechaOperacion = new Date();
		
		loadImagePlantillaBean.setNombreArchivo("0.png");
		imagen1 = "";
		imagen2 = "";
		imagen3 = "";
		imagen4 = "";
		imagen5 = "";
		imagen6 = "";
		imagen7 = "";
		imagen8 = "";
		imagen9 = "";
		imagen10 = "";
		
		String nombreBusqueda = "%"+plantillaVentaSelected.getId() +"_%";
		
		List<ImagenPlantillaVenta> lstImagenPlantilla = imagenPlantillaVentaService.findByNombreLikeAndEstado(nombreBusqueda, true);
		int contador = 1;
		for(ImagenPlantillaVenta i:lstImagenPlantilla) {
			if(contador==1) {
				imagen1 = i.getNombre();
			}
			if(contador==2) {
				imagen2 = i.getNombre();
			}
			if(contador==3) {
				imagen3 = i.getNombre();
			}
			if(contador==4) {
				imagen4 = i.getNombre();
			}
			if(contador==5) {
				imagen5 = i.getNombre();
			}
			if(contador==6) {
				imagen6 = i.getNombre();
			}
			if(contador==7) {
				imagen7 = i.getNombre();
			}
			if(contador==8) {
				imagen8 = i.getNombre();
			}
			if(contador==9) {
				imagen9 = i.getNombre();
			}
			if(contador==10) {
				imagen10 = i.getNombre();
			}
			contador ++;
		}
//			PrimeFaces.current().executeScript("PF('voucherDocumentoDialog').show();");
	
	}
	
	public void validaDatosPlantilla() {
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
		}else {
			Lote lote = loteService.findById(plantillaVentaNew.getLote().getId());
			if(lote.getStatus().equals("Vendido")) {
				addErrorMessage("El lote se encuentra vendido.");
				return;
			}
		}
		
		if(plantillaVentaNew.getMontoVenta()==null) {
			addErrorMessage("Debes ingresar monto de venta.");
			return;
		}
		
		if(plantillaVentaNew.getTipoPago().equals("CRÉDITO")) {
			if(plantillaVentaNew.getMontoInicial()==null) {
				addErrorMessage("Debes ingresar monto inicial.");
				return;
			}
			
			if(plantillaVentaNew.getNumeroCuota().equals("")) {
				addErrorMessage("Debes ingresar el plazo a pagar.");
				return;
			}
			
			if(plantillaVentaNew.getInteres()==null) {
				addErrorMessage("Debes ingresar interes.");
				return;
			}
		}

		if(file1 == null && file2 == null && file3 == null && file4 == null && file5 == null && file6 == null && file7 == null && file8 == null && file9 == null && file10 == null) {
			addErrorMessage("Debes ingresar al menos una imagen.");
			return;
		}
		
		PrimeFaces.current().executeScript("PF('savePlantillaVenta').show();"); 
	}
	
	public void savePlantillaVenta() {
		
		plantillaVentaNew.setEstado("Pendiente");
		PlantillaVenta plantilla = plantillaVentaService.save(plantillaVentaNew);
		
		if(plantilla == null) {
			addErrorMessage("No se pudo guardar.");
			return;
		}else {
			subirImagenes(plantilla.getId() + "", plantilla);
			addInfoMessage("Se guardo correctamente.");
			iniciarDatosPlantilla();
			
		}
		
		PrimeFaces.current().executeScript("PF('savePlantillaVenta').hide();"); 
		
	}
	
	public void subirImagenes(String idPlantilla, PlantillaVenta plantilla) {
		
		if(file1 != null) {
			String rename = idPlantilla +"_1" + "." + getExtension(file1.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file1);
		}
		if(file2 != null) {
			String rename = idPlantilla +"_2" + "." + getExtension(file2.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file2);
		}
		if(file3 != null) {
			String rename = idPlantilla +"_3" + "." + getExtension(file3.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file3);
		}
		if(file4 != null) {
			String rename = idPlantilla +"_4" + "." + getExtension(file4.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file4);
		}
		if(file5 != null) {
			String rename = idPlantilla +"_5" + "." + getExtension(file5.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file5);
		}
		if(file6 != null) {
			String rename = idPlantilla +"_6" + "." + getExtension(file6.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file6);
		}
		if(file7 != null) {
			String rename = idPlantilla +"_7" + "." + getExtension(file7.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file7);
		}
		if(file8 != null) {
			String rename = idPlantilla +"_8" + "." + getExtension(file8.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file8);
		}
		if(file9 != null) {
			String rename = idPlantilla +"_9" + "." + getExtension(file9.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file9);
		}
		if(file10 != null) {
			String rename = idPlantilla +"_10" + "." + getExtension(file10.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file10);
		}
	}

	public void subirArchivo(String nombre, UploadedFile file) {
	//  File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
	//  File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
	  File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta() + nombre);
	
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
	
	public void iniciarDatosPlantilla() {
		plantillaVentaNew = new PlantillaVenta();
		proyectoPlantilla =null;
		manzanaPlantilla = null;
		file1=null;
		file2=null;
		file3=null;
		file4=null;
		file5=null;
		file6=null;
		file7=null;
		file8=null;
		file9=null;
		file10=null;
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
		if(Perfiles.ASESOR.getName().equals(usuarioLogin.getProfile().getName())) {	
			lstProspect = prospectService.findByPersonAssessor(usuarioLogin.getPerson());
		} else if (Perfiles.SUPERVISOR.getName().equals(usuarioLogin.getProfile().getName())) {
			lstProspect = prospectService.findByPersonSupervisor(usuarioLogin.getPerson());
		}else {
			lstProspect = prospectService.findAll();
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
	
	public void iniciarPlantillaLazy() {

		lstPlantillaLazy = new LazyDataModel<PlantillaVenta>() {
			private List<PlantillaVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public PlantillaVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (PlantillaVenta plantillaVenta : datasource) {
                    if (plantillaVenta.getId() == intRowKey) {
                        return plantillaVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(PlantillaVenta plantillaVenta) {
                return String.valueOf(plantillaVenta.getId());
            }

			@Override
			public List<PlantillaVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				
				String prospect="%"+ (filterBy.get("prospecto.person.surnames")!=null?filterBy.get("prospecto.person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String project="%"+ (filterBy.get("lote.project.name")!=null?filterBy.get("lote.project.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String manzana="%"+ (filterBy.get("lote.manzana.name")!=null?filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String lote="%"+ (filterBy.get("lote.numberLote")!=null?filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				
				 Sort sort=Sort.by("id").descending();
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
				Page<PlantillaVenta> pagePlantillaVenta=null;
				
				if(navegacionBean.getUsuarioLogin().getProfile().getId().equals(Perfiles.ASESOR.getId())) {
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonAssessor(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), navegacionBean.getUsuarioLogin().getPerson(), pageable);
				}else if(navegacionBean.getUsuarioLogin().getProfile().getId().equals(Perfiles.SUPERVISOR.getId())) {
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonSupervisor(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), navegacionBean.getUsuarioLogin().getPerson(), pageable);
				}else{
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), pageable);
	
				}
			
				
				setRowCount((int) pagePlantillaVenta.getTotalElements());
				return datasource = pagePlantillaVenta.getContent();
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
	
	public Converter getConversorCuentaBancaria() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancaria) {
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
                    return ((CuentaBancaria) value).getId() + "";
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
	public PlantillaVentaService getPlantillaVentaService() {
		return plantillaVentaService;
	}
	public void setPlantillaVentaService(PlantillaVentaService plantillaVentaService) {
		this.plantillaVentaService = plantillaVentaService;
	}
	public LazyDataModel<PlantillaVenta> getLstPlantillaLazy() {
		return lstPlantillaLazy;
	}
	public void setLstPlantillaLazy(LazyDataModel<PlantillaVenta> lstPlantillaLazy) {
		this.lstPlantillaLazy = lstPlantillaLazy;
	}
	public String getEstadoPlantillaFilter() {
		return estadoPlantillaFilter;
	}
	public void setEstadoPlantillaFilter(String estadoPlantillaFilter) {
		this.estadoPlantillaFilter = estadoPlantillaFilter;
	}
	public PlantillaVenta getPlantillaVentaSelected() {
		return plantillaVentaSelected;
	}
	public void setPlantillaVentaSelected(PlantillaVenta plantillaVentaSelected) {
		this.plantillaVentaSelected = plantillaVentaSelected;
	}
	public ImagenPlantillaVentaService getImagenPlantillaVentaService() {
		return imagenPlantillaVentaService;
	}
	public void setImagenPlantillaVentaService(ImagenPlantillaVentaService imagenPlantillaVentaService) {
		this.imagenPlantillaVentaService = imagenPlantillaVentaService;
	}
	public String getImagen1() {
		return imagen1;
	}
	public void setImagen1(String imagen1) {
		this.imagen1 = imagen1;
	}
	public String getImagen2() {
		return imagen2;
	}
	public void setImagen2(String imagen2) {
		this.imagen2 = imagen2;
	}
	public String getImagen3() {
		return imagen3;
	}
	public void setImagen3(String imagen3) {
		this.imagen3 = imagen3;
	}
	public String getImagen4() {
		return imagen4;
	}
	public void setImagen4(String imagen4) {
		this.imagen4 = imagen4;
	}
	public String getImagen5() {
		return imagen5;
	}
	public void setImagen5(String imagen5) {
		this.imagen5 = imagen5;
	}
	public String getImagen6() {
		return imagen6;
	}
	public void setImagen6(String imagen6) {
		this.imagen6 = imagen6;
	}
	public String getImagen7() {
		return imagen7;
	}
	public void setImagen7(String imagen7) {
		this.imagen7 = imagen7;
	}
	public String getImagen8() {
		return imagen8;
	}
	public void setImagen8(String imagen8) {
		this.imagen8 = imagen8;
	}
	public String getImagen9() {
		return imagen9;
	}
	public void setImagen9(String imagen9) {
		this.imagen9 = imagen9;
	}
	public String getImagen10() {
		return imagen10;
	}
	public void setImagen10(String imagen10) {
		this.imagen10 = imagen10;
	}
	public LoadImagePlantillaBean getLoadImagePlantillaBean() {
		return loadImagePlantillaBean;
	}
	public void setLoadImagePlantillaBean(LoadImagePlantillaBean loadImagePlantillaBean) {
		this.loadImagePlantillaBean = loadImagePlantillaBean;
	}
	public CuentaBancaria getCuentaBancariaSelected() {
		return cuentaBancariaSelected;
	}
	public void setCuentaBancariaSelected(CuentaBancaria cuentaBancariaSelected) {
		this.cuentaBancariaSelected = cuentaBancariaSelected;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	
}
