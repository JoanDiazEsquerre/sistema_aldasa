package com.model.aldasa;

import java.util.Arrays;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({SecurityConfig.class})
@EnableJpaRepositories("com.model.aldasa.repository")
@EntityScan("com.model.aldasa.entity")
@ComponentScan("com.model.aldasa")
@SpringBootApplication
public class AldasaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AldasaApplication.class, args);
	}
	
	@Bean
	  ServletRegistrationBean jsfServletRegistration (ServletContext servletContext) {
	      //spring boot only works if this is set
	      servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
//	      servletContext.setInitParameter ("primefaces.THEME", "bluesky");
//          servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
//          servletContext.setInitParameter("com.sun.faces.expressionFactory","com.sun.el.ExpressionFactoryImpl");
//          servletContext.setInitParameter("primefaces.UPLOADER","commons");
	      //registration
	      ServletRegistrationBean srb = new ServletRegistrationBean();
	      srb.setServlet(new FacesServlet());
	      srb.setUrlMappings(Arrays.asList("*.xhtml"));
	      srb.setLoadOnStartup(1);
	      return srb;
	  }

}
