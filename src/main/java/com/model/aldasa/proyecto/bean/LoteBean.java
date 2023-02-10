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
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
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
                + "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
                + "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
                + "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
                + "</w:abstractNum>";

 String cTAbstractNumDecimalXML = 
        "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
                + "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
                + "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
                + "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
                + "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
                + "</w:abstractNum>";

				
 CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
	CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
	XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
	XWPFNumbering numbering = document.createNumbering();
	BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
	BigInteger numID = numbering.addNum(abstractNumID);
	

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("LA PARTE VENDEDORA ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");
		run.setFontFamily("Century Gothic");
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);



		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText(
				"UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO LA CRUZ – COD. PREDIO. 7_6159260_80375, "
						+ "ÁREA HA. 3.6000 U.C. 80375, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° 02272200, DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° II- SEDE CHICLAYO.");

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE III Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("SEGUNDO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		XWPFRun runDesObj = paragrapha.createRun();
		runDesObj.setText("POR EL PRESENTE CONTRATO, LA PARTE VENDEDORA VENDE A LA PARTE COMPRADORA EL (LOS) LOTE(S) DE TERRENO(S) "
				+ "POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");
		runDesObj.setFontFamily("Century Gothic");
		runDesObj.setFontSize(9);
		
		
	

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setText("1. MANZANA ------ LOTE -------- (ÁREA TOTAL --------- M2)");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA ---- LOTE ---- "
				+ "EN LA CUAL CONSTA UN ÁREA DE ---- M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO PARTIDA ELECTRÓNICA: 02272200");
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("Área del lote:     --------m2");
		run.addBreak();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("Perímetro del lote:	--------ml ");
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : Con Calle ----; con ---- ml.");
		run.addBreak();
		run.setText("Fondo         : Con -------; con ------ ml.");
		run.addBreak();
		run.setText("Derecha     : Con ---------; con ---- ml.");
		run.addBreak();
		run.setText("Izquierda    : Con ---------; con ----- ml.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA PARTE VENDEDORA SE COMPROMETE A ENTREGAR LA UNIDAD INMOBILIARIA MATERIA DE LA PRESENTE COMPRA VENTA EN LAS "
				+ "CONDICIONES QUE SE ESTIPULAN EN EL PRESENTE CONTRATO Y QUE LA PARTE COMPRADORA ACEPTAN EXPRESAMENTE.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("TERCERO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("EL PRECIO PACTADO POR LA VENTA DE EL (LOS) LOTE(S) DESCRITO EN LA CLÁUSULA SEGUNDA ES DE "
				+ "S./------------- (-------------- Y -----/100 SOLES), EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("COMO INICIAL, EL MONTO DE S./---------- (---------- Y ---/100 SOLES) CON DEPÓSITO(S) EN CUENTA DEL "
				+ "BANCO INTERBANK, CON NÚMERO DE CUENTA 8983003839960, CCI 00389800300383996043 Y/O EN CUENTA DEL BANCO "
				+ "BBVA CON NÚMERO DE CUENTA  0011 0285 01 00180945, CCI 011 285 000100180945 46 Y/O EN CUENTA DE CAJA TRUJILLO "
				+ "CON NÚMERO DE CUENTA 122321409341, CCI 80201200232140934153 Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES 210-01-4775107, "
				+ "CCI 80104521001477510763, EN DÓLARES 210-02-3090720, CCI 80104521002309072062, A FAVOR DE LA PARTE VENDEDORA, "
				+ "EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE EL (LOS) LOTE(S) SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO "
				+ "DIRECTO QUE BRINDA LA PARTE VENDEDORA, PARA LO CUAL LA PARTE COMPRADORA TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN "
				+ "SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("- PAGO DEL SALDO EN ---------- CUOTAS MENSUALES.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR LA PARTE COMPRADORA SE "
				+ "EFECTUARÁN EL DÍA -------- DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE LA PARTE VENDEDORA, LA CUAL PERTENECE "
				+ "AL BANCO INTERBANK, CON NÚMERO DE CUENTA 8983003839960, CCI 00389800300383996043 Y/O EN CUENTA DEL BANCO BBVA CON "
				+ "NÚMERO DE CUENTA  0011 0285 01 00180945, CCI 011 285 000100180945 46 Y/O EN CUENTA DE CAJA TRUJILLO CON NÚMERO DE "
				+ "CUENTA 122321409341, CCI 80201200232140934153 Y/O EN CUENTAS DE CAJA PIURA EN SOLES 210-01-4775107, CCI 80104521001477510763, "
				+ "EN DÓLARES 210-02-3090720, CCI 80104521002309072062; SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O "
				+ "REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, LA PARTE COMPRADORA INCURRE EN MORA, AUTOMÁTICAMENTE LA PARTE "
				+ "VENDEDORA RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y DOCUMENTOS "
				+ "EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER "
				+ "CANCELADO LAS ANTERIORES.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE ESTA CLÁUSULA "
				+ "TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("CUARTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE ------- "
				+ "MESES, MZ -----  LOTE (S) -------DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, LA PARTE COMPRADORA UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE EL (LOS) LOTE(S), "
				+ "SOLICITARÁ A LA PARTE VENDEDORA, LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, "
				+ "PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES "
				+ "EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, PARA LO CUAL LA PARTE COMPRADORA CORRERÁ CON LOS GASTOS Y TRÁMITES "
				+ "QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA PARTE COMPRADORA DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL "
				+ "INMUEBLE, MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA "
				+ "O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”. ");
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA VENTA DE (LOS) LOTE(S), COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A EL (LOS) LOTE(S), SIN "
				+ "RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A EL (LOS) LOTE(S).");
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE EL (LOS) LOTE(S), SE REALIZARÁ A LA CANCELACIÓN DEL SALDO "
				+ "POR PARTE DE LA PARTE COMPRADORA CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE LA PARTE VENDEDORA, "
				+ "PARA LUEGO REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE.");
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y EL (LOS) LOTE(S) QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("QUINTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA PARTE VENDEDORA DECLARA QUE TRANSFIERE A LA PARTE COMPRADORA EL (LOS) LOTE(S) OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE EL (LOS) LOTE(S) MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO EL (LOS) LOTE(S).");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A EL (LOS) LOTE(S), NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("QUE, EL (LOS) LOTE(S) NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("QUE, EL (LOS) LOTE(S) NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("SEXTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR "
				+ "LA INDEPENDIZACIÓN DE SU(S) LOTE(S)  ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR "
				+ "LA INDEPENDIZACIÓN DE SU(S) LOTE(S)  ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) EL (LOS) LOTE(S) Y DECLARADO(S) "
				+ "ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("SÉPTIMO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN EL (LOS) LOTE(S) "
				+ "MATERIA DE VENTA DEL CONTRATO.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA, AFIRMAMENTO DE CALLES PRINCIPALES.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("OTORGAMIENTO DE MINUTAS Y ESCRITURAS PÚBLICAS A LA CANCELACIÓN DE LOS SALDOS POR COMPRA DE EL (LOS) LOTE(S).");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("OCTAVO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA PARTE VENDEDORA SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN "
				+ "DE MEJORAS, QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA "
				+ "DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE LA CUAL SERÁN DE CARGO DE LA PARTE COMPRADORA.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("NOVENO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("ES DE CARGO DE  LA PARTE COMPRADORA EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LA PARTE COMPRADORA SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");

		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO PRIMERO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD "
				+ "CON EL ARTÍCULO N°1345 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, "
				+ "A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA "
				+ "Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, "
				+ "SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS "
				+ "RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, "
				+ "NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO SEGUNDO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, "
				+ "SOMETERSE A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y "
				+ "SEÑALANDO COMO TALES, LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO TERCERO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, LUGARES A "
				+ "LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE MEDIANTE CARTA CURSADA POR "
				+ "CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS "
				+ "EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO "
				+ "DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO CUARTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("SI EL PAGO DE EL (LOS) LOTE(S) QUE ADQUIEREN LA PARTE COMPRADORA SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE "
				+ "OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA PREPAGO DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ "
				+ "INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER "
				+ "ACORDADO CON “LA PARTE VENDEDORA”.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO QUINTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE LA PARTE COMPRADORA DE TRES(03) "
				+ "CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE "
				+ "ESTE CONTRATO, LA PARTE VENDEDORA PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL "
				+ "SALDO DEUDOR A  LA PARTE COMPRADORA, PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE LA PARTE COMPRADORA "
				+ "HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN LA PARTE "
				+ "COMPRADORA SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR "
				+ "LA PARTE COMPRADORA, ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO "
				+ "UNA IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE "
				+ "SE HAYAN EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE LA PARTE VENDEDORA.");
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setText("DÉCIMO SEXTO.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setBold(true);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS NORMAS DEL "
				+ "CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS --------------- (------) "
				+ "DÍAS DEL MES DE  --------- DE 202-- (---------------).");
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		XWPFTable table = document.createTable();

        //Creating first Row
        XWPFTableRow row1 = table.getRow(0);
        row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
        row1.addNewTableCell().setText("");

        //Creating second Row
        XWPFTableRow row2 = table.createRow();
        row2.getCell(0).setText("Second Row, First Column");
        row2.getCell(1).setText("Second Row, Second Column");

        //create third row
        XWPFTableRow row3 = table.createRow();
        row3.getCell(0).setText("Third Row, First Column");
        row3.getCell(1).setText("Third Row, Second Column");
		
		
		
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
