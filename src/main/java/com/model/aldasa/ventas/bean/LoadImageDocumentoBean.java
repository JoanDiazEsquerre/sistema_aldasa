package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;

@ManagedBean
public class LoadImageDocumentoBean {
	
	public StreamedContent graphicImage;
	 public String nombreArchivo = "";

	 public byte[] getImage() throws IOException {
//		 String ruta = "C:\\IMG-DOCUMENTO-VENTA\\0.png";
		 String ruta = "/home/imagen/voucher_separaciones/0.png"; 
		 if(!nombreArchivo.equals("")) {
//			 ruta = "C:\\IMG-DOCUMENTO-VENTA\\"+nombreArchivo;
			 ruta = "/home/imagen/voucher_separaciones/"+nombreArchivo;
		 }
		 
		 System.out.println(ruta);

		  File file = new File(ruta);
		  byte[] byteImage = Files.readAllBytes(file.toPath());     
		  return byteImage;
     
   }
	
	@PostConstruct
	public void init() {

           
	}

	public StreamedContent getGraphicImage() {
		return graphicImage;
	}

	public void setGraphicImage(StreamedContent graphicImage) {
		this.graphicImage = graphicImage;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}	
}
