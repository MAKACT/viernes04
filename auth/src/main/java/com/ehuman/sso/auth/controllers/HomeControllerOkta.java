package com.ehuman.sso.auth.controllers;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "*")
@RestController
public class HomeControllerOkta {
private static final Logger LOG = (Logger) LoggerFactory.getLogger(HomeControllerOkta.class);
	
	
    @GetMapping("/okta/v1.0")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) throws ResponseStatusException {
    	LOG.info("Ingresa a get /okta/v1.0 con  user: "+ user.getFullName());
    	LOG.info("user atributes : "+ user.getAttributes().toString());
    	LOG.info("user.getEmailVerified(): "+user.getEmailVerified());
    	Boolean email= user.getAttribute("email_verified");
    		
    	if(user.getEmailVerified()){//trae boleano
    		LOG.info("Email enontrado en okta");
    		LOG.info("redireccion: " + "/humanSSO/valida?email="+user.getEmail());
    		return new ModelAndView("forward:/humanSSO/valida?email="+user.getEmail());
    		//return "redirect:/humanSSO/valida?email="+user.getEmail();
    		
    	}else {
    		LOG.info("Ocurrio error");
			throw new ResponseStatusException(
					  HttpStatus.NOT_FOUND, "entity not found");
    		
			
    }
    }
    
    
    @GetMapping("/okta/v1.1")
    public ModelAndView home2(@AuthenticationPrincipal OidcUser user) /*throws ResponseStatusException*/ {
    	LOG.info("Ingresa a get /okta/v1.1 con user: "+ user.getFullName());
    	LOG.info("user atributes : "+ user.getAttributes().toString());
    	LOG.info("user.getEmailVerified(): "+user.getEmailVerified());
    	
    		
    	if(user.getEmailVerified()){//trae boleano
    		LOG.info("Email enontrado en okta");
    		LOG.info("redireccion: " + "/humanSSO/valida?email="+user.getEmail());
    		//model.addAttribute("email", user.getEmailVerified());
    		return new ModelAndView("forward:/humanSSO/validaurl?email="+user.getEmail());
    		//return "redirect:/humanSSO/valida?email="+user.getEmail();
    		
    	}else {
    		LOG.info("Ocurrio error");
			throw new ResponseStatusException(
					  HttpStatus.NOT_FOUND, "entity not found");
        }
    	
    }
    
    
    
//    public String Home(@AuthenticationPrincipal OidcUser user)throws JsonProcessingException{
//    	
//    	return user.getClaimAsString(user.getAccessTokenHash());
//    }



}
