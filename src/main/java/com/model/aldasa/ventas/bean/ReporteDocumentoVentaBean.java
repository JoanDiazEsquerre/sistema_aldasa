package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ReporteDocumentoVentaBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
   	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;

	
	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	
	private StreamedContent fileDes;
	
	private boolean estado = true;
	private String nombreArchivo = "Reporte de Documento de Ventas.xlsx";
	private Date fechaIni, fechaFin;
	
	private Sucursal sucursal;
	
	private List<Sucursal> lstSucursal;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct
	public void init() {
		
		lstSucursal =sucursalService.findByEmpresaAndEstado(navegacionBean.getSucursalLogin().getEmpresa(), true);
		sucursal = lstSucursal.get(0);
		iniciarLazy();
		fechaIni = new Date();
		fechaFin = new Date();
		
	}
	
	
	public void iniciarLazy() {

		lstDocumentoVentaLazy = new LazyDataModel<DocumentoVenta>() {
			private List<DocumentoVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public DocumentoVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (DocumentoVenta documentoVenta : datasource) {
                    if (documentoVenta.getId() == intRowKey) {
                        return documentoVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(DocumentoVenta documentoVenta) {
                return String.valueOf(documentoVenta.getId());
            }

			@Override
			public List<DocumentoVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("estado").ascending();
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
               
                Page<DocumentoVenta> pageDocumentoVenta=null;
                
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                if(sucursal == null) {
                    pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, navegacionBean.getSucursalLogin().getEmpresa(), fechaIni, fechaFin, pageable);

                }else {
                    pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndFechaEmisionBetween(estado, navegacionBean.getSucursalLogin(), fechaIni, fechaFin, pageable);
                }
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
            }
		};
	}

	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Documento Venta");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFechaEmision = rowSubTitulo.createCell(0);cellSubFechaEmision.setCellValue("FECHA DE EMISION DEL COMPROBANTE DE PAGO O DOCUMENTO");cellSubFechaEmision.setCellStyle(styleTitulo);
		Cell cellSubFechaVencimiento = rowSubTitulo.createCell(1);cellSubFechaVencimiento.setCellValue("FECHA DE VENCIMIENTO O FECHA DE PAGO");cellSubFechaVencimiento.setCellStyle(styleTitulo);
		Cell cellSubTipo = rowSubTitulo.createCell(2);cellSubTipo.setCellValue("TIPO");cellSubTipo.setCellStyle(styleTitulo);
		Cell cellSubSerie = rowSubTitulo.createCell(3);cellSubSerie.setCellValue("N° SERIE/N° SERIE MAQ REGIS");cellSubSerie.setCellStyle(styleTitulo);
		Cell cellSubNumero = rowSubTitulo.createCell(4);cellSubNumero.setCellValue("NUMERO");cellSubNumero.setCellStyle(styleTitulo);
		Cell cellSubTipo2 = rowSubTitulo.createCell(5);cellSubTipo2.setCellValue("TIPO");cellSubTipo2.setCellStyle(styleTitulo);
		Cell cellSubNumero2 = rowSubTitulo.createCell(6);cellSubNumero2.setCellValue("NUMERO");cellSubNumero2.setCellStyle(styleTitulo);
		Cell cellSubNombre = rowSubTitulo.createCell(7);cellSubNombre.setCellValue("APELLIDOS Y NOMBRES, DENOMINACION O RAZON SOCIAL");cellSubNombre.setCellStyle(styleTitulo);
		Cell cellSubTotal = rowSubTitulo.createCell(8);cellSubTotal.setCellValue("VALOR FACTURADO DE LA EXPORTACION");cellSubTotal.setCellStyle(styleTitulo);
		
		List<DocumentoVenta> lstDocumentoVenta = new ArrayList<>();
		if(sucursal == null) {
			lstDocumentoVenta= documentoVentaService.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, navegacionBean.getSucursalLogin().getEmpresa(), fechaIni, fechaFin);

        }else {
        	lstDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndFechaEmisionBetween(estado, navegacionBean.getSucursalLogin(), fechaIni, fechaFin);
        }

		int index = 1;

			if (!lstDocumentoVenta.isEmpty()) {
				for (DocumentoVenta d : lstDocumentoVenta) {
					Row rowDetail = sheet.createRow(index);
					Cell cellfechaEmision = rowDetail.createCell(0);cellfechaEmision.setCellValue(sdf.format(d.getFechaEmision()));cellfechaEmision.setCellStyle(styleBorder);
					Cell cellFechaVencimiento = rowDetail.createCell(1);cellFechaVencimiento.setCellValue(sdf.format(d.getFechaEmision()));cellFechaVencimiento.setCellStyle(styleBorder);
					Cell cellTipo = rowDetail.createCell(2);cellTipo.setCellValue(d.getTipoDocumento().getCodigo());cellTipo.setCellStyle(styleBorder);
					Cell cellSerie = rowDetail.createCell(3);cellSerie.setCellValue(d.getSerie());cellSerie.setCellStyle(styleBorder);
					Cell cellNumero = rowDetail.createCell(4);cellNumero.setCellValue(d.getNumero());cellNumero.setCellStyle(styleBorder);
					Cell cellTipo2 = rowDetail.createCell(5);cellTipo2.setCellValue(d.getTipoDocumento().getAbreviatura());cellTipo2.setCellStyle(styleBorder);
					Cell cellRuc = rowDetail.createCell(6);cellRuc.setCellValue(d.getRuc());cellRuc.setCellStyle(styleBorder);
					Cell cellRazonSocial = rowDetail.createCell(7);cellRazonSocial.setCellValue(d.getRazonSocial());cellRazonSocial.setCellStyle(styleBorder);
					Cell cellTotal = rowDetail.createCell(8);cellTotal.setCellValue(d.getTotal().doubleValue());cellTotal.setCellStyle(styleBorder);
					
					index++;
				}
			}
		
		
		for (int j = 0; j <= 9; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
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
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
	
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public LazyDataModel<DocumentoVenta> getLstDocumentoVentaLazy() {
		return lstDocumentoVentaLazy;
	}
	public void setLstDocumentoVentaLazy(LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy) {
		this.lstDocumentoVentaLazy = lstDocumentoVentaLazy;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	
}
