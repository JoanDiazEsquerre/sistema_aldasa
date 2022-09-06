package com.model.pavita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.model.pavita.service.impl.UserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailService userDetailsService ;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
         .antMatchers("/index.xhtml", "/index.html", "/login.xhtml", "/javax.faces.resources/**").permitAll()
         .and()
         .formLogin()
         .and()
         .logout().logoutSuccessUrl("/index.xhtml").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutUrl("/logout")
         .and()
         //.exceptionHandling().accessDeniedPage("/error.xhtml")
         //.and()
         .csrf().disable();
	}

    
}
