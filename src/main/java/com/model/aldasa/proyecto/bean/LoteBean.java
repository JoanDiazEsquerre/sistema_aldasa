package com.model.aldasa.proyecto.bean;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.ComisionesService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class LoteBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	private List<Lote> lstLotes;
	private List<Person> lstPerson;
	
	private StreamedContent fileDes;
	private StreamedContent fileImg;
	
	private String nombreArchivo = "Contrato.docx";
		
	private Person personAsesorSelected;
	private Lote loteSelected;
	private Team teamSelected;
	private Usuario usuarioLogin;
	
	
	private Project projectFilter;
	private Manzana manzanaFilter;
	private Manzana manzanaFilterMapeo;
	private Person personVenta;
	
	private String status = "";
	private String tituloDialog;
	private String nombreLoteSelected="";
	private int cantidadLotes=0;
	private Date fechaSeparacion, fechaVencimiento,fechaVendido;
	
	private boolean loteVendido = false;
	
	private LazyDataModel<Lote> lstLoteLazy;
	private List<Manzana> lstManzana = new ArrayList<>();
	private List<Project> lstProject = new ArrayList<>();
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor = new ArrayList<>();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 

	
	@PostConstruct
	public void init() {
		usuarioLogin = navegacionBean.getUsuarioLogin();
		listarProject();
		listarManzanas();
		listarPersonas();
		iniciarLazy();
		cargarAsesorPorEquipo();
		lstTeam=teamService.findByStatus(true);

	}
	
	public void generarWord() throws IOException, XmlException {

		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN, DE UNA PARTE, ALDASA INMOBILIARIA S.A.C.,"
				+ " CON RUC Nº 20607274526, REPRESENTADA POR SU GERENTE GENERAL ALAN CRUZADO BALCÁZAR, IDENTIFICADO CON DNI. "
				+ "N° 44922055, DEBIDAMENTE INSCRITO EN LA PARTIDA ELECTRÓNICA Nº 11352661 DEL REGISTRO DE PERSONAS JURÍDICAS "
				+ "DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN CAL. LOS AMARANTOS NRO. 245 URB. FEDERICO "
				+ "VILLARREAL, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; A QUIEN SE LE DENOMINARÁ EN "
				+ "LO SUCESIVO LA PARTE VENDEDORA; A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) Dante Montalván Santisteban , "
				+ "DE OCUPACIÓN  ESTILISTA, ESTADO CIVIL CASADO,  IDENTIFICADO(A)  CON DNI N° 16712921, PARA ESTE ACTO Y "
				+ "MARÍA VIOLETA PISCOYA DAMIAN, DE OCUPACIÓN  ESTILISTA, ESTADO CIVIL CASADA,  IDENTIFICADO(A)  CON DNI "
				+ "N° 16712921, PARA ESTE ACTO, AMBOS CON DOMICILIO EN AV. VICTOR BELAUNDE OESTE 843 URB. EL RETABLO, DEL "
				+ "DISTRITO DE COMAS PROVINCIA DE LIMA, DEPARTAMENTO DE LIMA, CELULAR  944817916, "
				+ "CORREO ELECTRÓNICO _______ A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ LA PARTE COMPRADORA, EL CONTRATO "
				+ "SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");
		runPrimero.setBold(true);
		runPrimero.setFontFamily("Century Gothic");
		runPrimero.setFontSize(9);
		runPrimero.setBold(true);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		
		String cTAbstractNumBulletXML = 
				  "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\uF06E\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\u2013\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\u26Ac\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = 
				  "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0.\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1.\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2.\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				

		XWPFParagraph paragrapha = document.createParagraph();
		XWPFRun runa = paragrapha.createRun();
		runa.setText("ANTEDEDENTES");
		runa.setBold(true);
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);

		XWPFParagraph paragrapharun2 = document.createParagraph();
		XWPFRun run2 = paragrapharun2.createRun();
		run2.setText("LA PARTE VENDEDORA ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");
		run2.setFontFamily("Century Gothic");
		run2.setFontFamily("Century Gothic");
		run2.setFontSize(9);

		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setText(
				"UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO LA CRUZ – COD. PREDIO. 7_6159260_80375, "
						+ "ÁREA HA. 3.6000 U.C. 80375, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° 02272200, DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° II- SEDE CHICLAYO.");

		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE III Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");
		
		
		
		paragrapha = document.createParagraph();
		XWPFRun runSegundo = paragrapha.createRun();
		runSegundo.setText("SEGUNDO.");
		runSegundo.setBold(true);
		runSegundo.setFontFamily("Century Gothic");
		runSegundo.setFontSize(9);
		runSegundo.setBold(true);
		runSegundo.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		XWPFRun runObj = paragrapha.createRun();
		runObj.setText("OBJETO");
		runObj.setBold(true);
		runObj.setFontFamily("Century Gothic");
		runObj.setFontSize(9);
		
		paragrapha = document.createParagraph();
		XWPFRun runDesObj = paragrapha.createRun();
		runDesObj.setText("POR EL PRESENTE CONTRATO, LA PARTE VENDEDORA VENDE A LA PARTE COMPRADORA EL (LOS) LOTE(S) DE TERRENO(S) "
				+ "POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");
		runDesObj.setFontFamily("Century Gothic");
		runDesObj.setFontSize(9);
		
		
		
		CTNumbering cTNumbering2 = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
		CTAbstractNum cTAbstractNum2 = cTNumbering2.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum2 = new XWPFAbstractNum(cTAbstractNum2);
		XWPFNumbering numbering2 = document.createNumbering();
		BigInteger abstractNumID2 = numbering2.addAbstractNum(abstractNum2);
		BigInteger numID2 = numbering2.addNum(abstractNumID2);

		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID2);
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setBold(true);
		runa.setText("MANZANA ------ LOTE -------- (ÁREA TOTAL --------- M2)");
		runa.addBreak();
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA ---- LOTE ---- "
				+ "EN LA CUAL CONSTA UN ÁREA DE ---- M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO PARTIDA ELECTRÓNICA: 02272200");
		
		runa.addBreak();
		runa.addBreak();
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setBold(true);
		runa.setUnderline(UnderlinePatterns.SINGLE); 
		runa.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");
		

		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID2);
		runa = paragrapha.createRun();
		runa.setFontFamily("Century Gothic");
		runa.setFontSize(9);
		runa.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE III Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}

	public void newLote() {
		nombreLoteSelected="";
		tituloDialog="NUEVO LOTE";
		
		loteSelected=new Lote();
		loteSelected.setStatus("Disponible");
		
		listarManzanas();
		listarProject();
		listarPersonas();
	}
	
	public void modifyLote( ) {
		tituloDialog="MODIFICAR LOTE";
		
		if(loteSelected.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			loteVendido = true;
		}else {
			loteVendido = false;
		}
		
		nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+" / Lote: "+loteSelected.getNumberLote();
				
		fechaSeparacion = loteSelected.getFechaSeparacion();
		fechaVencimiento = loteSelected.getFechaVencimiento();
		fechaVendido = loteSelected.getFechaVendido();
		personVenta = loteSelected.getPersonVenta();
		
		
		Usuario usuarioAsesor = usuarioService.findByPerson(loteSelected.getPersonAssessor()); 
		if(usuarioAsesor != null) {
			teamSelected = usuarioAsesor.getTeam();
		}else {
			teamSelected = null;
		}
		
		
		cargarAsesorPorEquipo();
		personAsesorSelected = loteSelected.getPersonAssessor();

				
		listarManzanas();
		listarProject();
		listarPersonas();
	}
	
	public void changeCmbEstado() {
		if(loteSelected.getStatus().equals("Separado")) {
			if(fechaSeparacion == null) {
				fechaSeparacion = new Date(); 
				fechaVencimiento=sumarDiasAFecha(new Date(), 7);
			}else {
				
			}
		}
		
		if(loteSelected.getStatus().equals("Vendido")) {
			loteSelected.setTipoPago("Contado");
			if(fechaVendido == null) {
				fechaVendido=new Date();
			}
		}
		

	}
	
	public void calcularFechaVencimiento() {
		if(fechaSeparacion != null) {
			fechaVencimiento = sumarDiasAFecha(fechaSeparacion, 7);
		}
	}
	
	public Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	
	public void listarManzanas (){
		if(projectFilter == null) {
			lstManzana = manzanaService.findByStatusOrderByNameAsc(true);
		}else {
			lstManzana= manzanaService.findByProject(projectFilter.getId());
		}
		
		manzanaFilterMapeo = null;
		if(!lstManzana.isEmpty() && lstManzana != null) {
			manzanaFilterMapeo = lstManzana.get(0);
		}
		
	}
	
	public void listarProject(){
		lstProject= projectService.findByStatus(true);
	}
	
	public void listarLotes(){		
		lstLotes= loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter,manzanaFilterMapeo, "%%");
		cantidadLotes=0;
		if(!lstLotes.isEmpty()) {
			cantidadLotes = lstLotes.size();
		}
	}
	
	public void iniciarLazy() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
				String numberLote="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String manzana = "";
				if(manzanaFilter != null) {
					manzana = manzanaFilter.getName();
				}
				
				String proyecto = "%%";
				if(projectFilter != null) {
					proyecto = projectFilter.getName();
				}
				
                                
                Sort sort=Sort.by("numberLote").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
				Page<Lote> pageLote;
//				if(projectFilter.equals("")) {
//					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(numberLote,"%"+manzana+"%","%"+status+"%", pageable);
//				}else {
					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(numberLote, "%"+manzana+"%",proyecto,"%"+status+"%", pageable);
				
//				}
				setRowCount((int) pageLote.getTotalElements());
				return datasource = pageLote.getContent();
			}
		};
	}
	
	public void fileDownloadView() {
        fileImg = DefaultStreamedContent.builder()
                .name("proyecto"+projectFilter.getId()+".jpg")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/recursos/images/proyectos/proyecto"+projectFilter.getId()+".jpg"))
                .build();
    }
	
	public void saveLote() {
		if(loteSelected.getNumberLote().equals("") || loteSelected.getNumberLote()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar número de lote."));
			return ;
		}
		
		if(loteSelected.getManzana()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una manzana."));
			return ;
		} 
		
		if(loteSelected.getProject()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar un proyecto."));
			return ;
		} 
		
		loteSelected.setFechaSeparacion(fechaSeparacion);
		loteSelected.setFechaVencimiento(fechaVencimiento);
		loteSelected.setPersonSupervisor(null);
		loteSelected.setPersonAssessor(null);
		loteSelected.setPersonVenta(null);
		loteSelected.setFechaVendido(null);


		if(loteSelected.getStatus().equals(EstadoLote.SEPARADO.getName())) {
			if(fechaSeparacion == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una fecha separación."));
				return ;
			}
			
			if(fechaVencimiento == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar una fecha vencimiento."));
				return ;
			}
			
			if(teamSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar equipo."));
				return ;
			}else if (teamSelected != null) {
				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
			}
			
			if (personAsesorSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar asesor."));
				return ;
			}else if (personAsesorSelected != null) {
				loteSelected.setPersonAssessor(personAsesorSelected);
			}
		}

		if(loteSelected.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			if(personVenta == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (personVenta != null) {
				loteSelected.setPersonVenta(personVenta);
			}
			
			if(teamSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (teamSelected != null) {
				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
			}
			
			if (personAsesorSelected == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (personAsesorSelected != null) {
				loteSelected.setPersonAssessor(personAsesorSelected);
			}
			
			if(fechaVendido == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
				return ;
			}else if (fechaVendido !=null) {
				loteSelected.setFechaVendido(fechaVendido);
			}
			
			if(loteSelected.getTipoPago().equals("Contado")) {
				loteSelected.setMontoInicial(null);
				loteSelected.setNumeroCuota(null);
				if (loteSelected.getMontoVenta() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
					return ;
				}
			}
			
			if(loteSelected.getTipoPago().equals("Crédito")) {
				if (loteSelected.getMontoVenta() == null || loteSelected.getMontoInicial() == null || loteSelected.getNumeroCuota() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Completar todos los datos de venta."));
					return ;
				}
			}
		} else {
			loteSelected.setMontoVenta(null);
			loteSelected.setTipoPago(null);
		}
		
		//*********************************
		
		
		if (tituloDialog.equals("NUEVO LOTE")) {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProject(loteSelected.getNumberLote(), loteSelected.getManzana(), loteSelected.getProject());
			if (validarExistencia == null) {
				Lote lote = loteService.save(loteSelected);
				generarComision(lote);
				newLote();
				
				if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
					loteVendido=true;
				}else {
					loteVendido=false;
				}
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				fechaSeparacion = null;
				fechaVencimiento = null;
				fechaVendido=null;
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
			}
		} else {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProjectException(loteSelected.getNumberLote(), loteSelected.getManzana().getId(), loteSelected.getProject().getId(), loteSelected.getId());
			if (validarExistencia == null) {
				Lote lote = loteService.save(loteSelected);
				generarComision(lote);
				
				if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
					loteVendido=true;
				}else {
					loteVendido=false;
				}
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+"/ lote: "+loteSelected.getNumberLote();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El lote ya existe."));
			}
		}
		
		
	}
	
	
	public void generarComision(Lote lote) {
		
		if (lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			
			Comisiones validarExistencia = comisionesService.findByLote(lote);
			Usuario usuarioAsesor = usuarioService.findByPerson(lote.getPersonAssessor());
			Comision comision = comisionService.findByFechaIniLessThanEqualAndFechaCierreGreaterThanEqual(lote.getFechaVendido(), lote.getFechaVendido());
		
			if(comision != null){
				
				Comisiones comisiones = new Comisiones();
				if(validarExistencia != null) {
					comisiones.setId(validarExistencia.getId());
				}
				comisiones.setLote(lote);
				if(usuarioAsesor.getTeam().getName().equals("ONLINE")) {
					List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndTipoPagoAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), lote.getPersonAssessor().getDni(),lote.getTipoPago(), comision.getFechaIni(), comision.getFechaCierre());
					if(lstLotesVendidos.size()==1) {
						if (lote.getTipoPago().equals("Contado")) {
							comisiones.setComisionAsesor(comision.getPrimeraVentaContadoOnline());
						}else {
							comisiones.setComisionAsesor(comision.getPrimeraVentaCreditoOnline());
						}
					}else {
						if (lote.getTipoPago().equals("Contado")) {
							comisiones.setComisionAsesor(comision.getBonoContadoOnline());
						}else {
							comisiones.setComisionAsesor(comision.getBonoCreditoOnline());
						}
					}
					comisiones.setComisionSupervisor((lote.getMontoVenta()*comision.getComisionSupervisorOnline())/100);
					comisiones.setTipoEmpleado("O");
					
				}else if (usuarioAsesor.getTeam().getName().equals("INTERNOS") || usuarioAsesor.getTeam().getName().equals("EXTERNOS")) {
					if (lote.getTipoPago().equals("Contado")) {
						comisiones.setComisionAsesor((lote.getMontoVenta()*comision.getComisionContado())/100);
					}else {
						comisiones.setComisionAsesor((lote.getMontoVenta()*comision.getComisionCredito())/100);
					}
					comisiones.setComisionSupervisor(0);
					comisiones.setTipoEmpleado(usuarioAsesor.getTeam().getName().equals("INTERNOS")?"I": "E");
					
				}else {
					List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), lote.getPersonSupervisor(), "%%", comision.getFechaIni(), comision.getFechaCierre());
					
					MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comision, true, lote.getPersonSupervisor());
					
					boolean alcanzaMeta = false;
					if (metaSupervisor != null) {
						int meta  = metaSupervisor.getMeta();
						if(lstLotesVendidos.size() >= meta) {
							alcanzaMeta = true;
							for (Lote lt:lstLotesVendidos) {
								Comisiones comConsulta = comisionesService.findByLote(lt);
								if(comConsulta != null) {
									comConsulta.setComisionSupervisor((lt.getMontoVenta()*comision.getComisionMetaSupervisor())/100);
									comisionesService.save(comConsulta);

								}
							} 
						}
					}
					
					if (lote.getTipoPago().equals("Contado")) {
						comisiones.setComisionAsesor((lote.getMontoVenta()*comision.getComisionContado())/100);
					}else {
						comisiones.setComisionAsesor((lote.getMontoVenta()*comision.getComisionCredito())/100);
					}
					
					if(!alcanzaMeta) {
						comisiones.setComisionSupervisor((lote.getMontoVenta()*comision.getComisionSupervisor())/100);

					}else {
						comisiones.setComisionSupervisor((lote.getMontoVenta()*comision.getComisionMetaSupervisor())/100);
					}

				}
				comisiones.setComisionSubgerente((lote.getMontoVenta()*comision.getSubgerente())/100);
				comisiones.setEstado(true);
				comisionesService.save(comisiones);
			}

		}else {
			Comisiones comisionesrelacionados = comisionesService.findByLote(lote);
			
			if (comisionesrelacionados != null) {
				comisionesrelacionados.setEstado(false);
				comisionesService.save(comisionesrelacionados);

			}
		}
	}
	
	public void calcularAreaPerimetro() {
		if(loteSelected.getMedidaFrontal() != null && loteSelected.getMedidaFrontal() >0) {
			if(loteSelected.getMedidaDerecha() != null && loteSelected.getMedidaDerecha() >0) {
				if(loteSelected.getMedidaIzquierda() != null && loteSelected.getMedidaIzquierda() >0) {
					if(loteSelected.getMedidaFondo() != null && loteSelected.getMedidaFondo() >0) {
						double area1 = (loteSelected.getMedidaFrontal()*loteSelected.getMedidaDerecha())/2;
						double area2 = (loteSelected.getMedidaIzquierda()*loteSelected.getMedidaFondo())/2;
						
						loteSelected.setArea(area1+area2);
						loteSelected.setPerimetro(loteSelected.getMedidaFrontal()+loteSelected.getMedidaDerecha()+loteSelected.getMedidaIzquierda()+loteSelected.getMedidaFondo());
					}
				}
			}
		}
	}
	
	public Converter getConversorTeam() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Team c = null;
                    for (Team si : lstTeam) {
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
                    return ((Team) value).getId() + "";
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
	
	public Converter getConversorPerson() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPerson) {
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
	
	public List<Manzana> completeManzana(String query) {
        List<Manzana> lista = new ArrayList<>();
        for (Manzana c : lstManzana) {
            if (c.getName().toUpperCase().contains(query.toUpperCase()) ) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	public void cargarAsesorPorEquipo() {
		lstPersonAsesor = new ArrayList<>();
		List<Usuario> lstUsuarios = new ArrayList<>();
		
    	personAsesorSelected = null;
		if(teamSelected!= null) {
			lstUsuarios = usuarioService.findByTeam(teamSelected);
		}
		
		if(!lstUsuarios.isEmpty()){
			for(Usuario user : lstUsuarios) {
				lstPersonAsesor.add(user.getPerson());
			}
		}
	}
	
	public Converter getConversorPersonAsesor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Person c = null;
                    for (Person si : lstPersonAsesor) {
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
	
	public List<Person> completePersonAsesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAsesor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	
	public String getNombreLoteSelected() {
		return nombreLoteSelected;
	}
	public void setNombreLoteSelected(String nombreLoteSelected) {
		this.nombreLoteSelected = nombreLoteSelected;
	}
	public Manzana getManzanaFilter() {
		return manzanaFilter;
	}
	public void setManzanaFilter(Manzana manzanaFilter) {
		this.manzanaFilter = manzanaFilter;
	}
	public List<Lote> getLstLotes() {
		return lstLotes;
	}
	public void setLstLotes(List<Lote> lstLotes) {
		this.lstLotes = lstLotes;
	}
	public Manzana getManzanaFilterMapeo() {
		return manzanaFilterMapeo;
	}
	public void setManzanaFilterMapeo(Manzana manzanaFilterMapeo) {
		this.manzanaFilterMapeo = manzanaFilterMapeo;
	}
	public int getCantidadLotes() {
		return cantidadLotes;
	}
	public void setCantidadLotes(int cantidadLotes) {
		this.cantidadLotes = cantidadLotes;
	}
	public Date getFechaSeparacion() {
		return fechaSeparacion;
	}
	public void setFechaSeparacion(Date fechaSeparacion) {
		this.fechaSeparacion = fechaSeparacion;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Date getFechaVendido() {
		return fechaVendido;
	}
	public void setFechaVendido(Date fechaVendido) {
		this.fechaVendido = fechaVendido;
	}

	public List<Person> getLstPerson() {
		return lstPerson;
	}

	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public Person getPersonVenta() {
		return personVenta;
	}

	public void setPersonVenta(Person personVenta) {
		this.personVenta = personVenta;
	}

	public Team getTeamSelected() {
		return teamSelected;
	}

	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}

	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public Person getPersonAsesorSelected() {
		return personAsesorSelected;
	}
	public void setPersonAsesorSelected(Person personAsesorSelected) {
		this.personAsesorSelected = personAsesorSelected;
	}
	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}
	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public boolean isLoteVendido() {
		return loteVendido;
	}
	public void setLoteVendido(boolean loteVendido) {
		this.loteVendido = loteVendido;
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

	public SimpleDateFormat getSdfM() {
		return sdfM;
	}

	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}

	public SimpleDateFormat getSdfY() {
		return sdfY;
	}

	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}

	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}

	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}

	public StreamedContent getFileImg() {
		return fileImg;
	}

	public void setFileImg(StreamedContent fileImg) {
		this.fileImg = fileImg;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	
}
