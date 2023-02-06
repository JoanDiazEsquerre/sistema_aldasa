package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.model.aldasa.util.BaseBean;

@ManagedBean
public class LoadImageBean extends BaseBean{
	
	 public StreamedContent graphicImage;
	 public String nombreArchivo = "";

	 public byte[] getImage() throws IOException {
		 String ruta = "C:\\IMG-ALDASA\\0.jpg";
//		 String ruta = "/home/imagen/voucher_separaciones/0.png"; 
		 if(!nombreArchivo.equals("")) {
			 ruta = "C:\\IMG-ALDASA\\"+nombreArchivo;
//			 ruta = "/home/imagen/voucher_separaciones/"+nombreArchivo;
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
