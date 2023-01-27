package com.model.aldasa.prospeccion.bean;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;

import com.model.aldasa.util.BaseBean;

@ManagedBean
public class LoadImageBean extends BaseBean{
	
	 public StreamedContent graphicImage;

	 public byte[] getImage2() throws IOException {
		 String ruta = "C:\\img\\1.jpeg";

		  File file = new File(ruta);
		  byte[] bytes = Files.readAllBytes(file.toPath());     
		  return bytes;
      
    }
	
	@PostConstruct
	public void init() {

            
	}
	

	
	
}
