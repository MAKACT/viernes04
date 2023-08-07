  package com.vsm.human.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SsoApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(SsoApplication.class);
	
	public static void main(String[] args) {			
		SpringApplication.run(SsoApplication.class, args);
		logger.info("..::: LISTO HUMAN SSO version 2.0.0 :::...");		
	}

}
