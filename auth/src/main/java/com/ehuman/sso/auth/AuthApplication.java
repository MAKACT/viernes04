package com.ehuman.sso.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

//@RestController
@SpringBootApplication
public class AuthApplication extends SpringBootServletInitializer {

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(AuthApplication.class);
	  public static void main(String[] args) {
	  SpringApplication.run(AuthApplication.class, args); }
	 
	
	

	    @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthApplication.class);
	    }
	    
	    
//	   
}
