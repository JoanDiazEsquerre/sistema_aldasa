package com.model.aldasa;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
<<<<<<< HEAD
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
=======
>>>>>>> 9541d95084344c775346e7d655b17faedf6f48a8
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.impl.UserDetailService;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailService userDetailsService ;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
<<<<<<< HEAD
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
=======
	@Inject
	private NavegacionBean navegacionBean;
>>>>>>> 9541d95084344c775346e7d655b17faedf6f48a8
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("USER");
		auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
		//auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder).;
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		 http.authorizeRequests()
         .antMatchers("/secured/view/**").fullyAuthenticated()
         .antMatchers("/secured/admin/**", "/secured/view/admin/**").access("hasRole('ROLE_SUPERUSER')")
         .antMatchers("/index.xhtml", "/index.html", "/login.xhtml", "/javax.faces.resources/**", "/home.xhtml").permitAll()
         .and()
         .formLogin()
              .defaultSuccessUrl("/secured/view/home.xhtml").successForwardUrl("/secured/view/home.xhtml")
         .successHandler(customAuthenticationSuccessHandler)

         .and()
         .logout().logoutSuccessUrl("/index.xhtml").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutUrl("/logout")
         .and()
         //.exceptionHandling().accessDeniedPage("/error.xhtml")
         //.and()
         .csrf().disable();
	}

    
}
