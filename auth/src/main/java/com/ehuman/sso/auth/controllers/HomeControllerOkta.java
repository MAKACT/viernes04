package com.ehuman.sso.auth.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.ehuman.sso.auth.services.ConsultaBDService;






@CrossOrigin(origins = "*")
@RestController
public class HomeControllerOkta {
private static final Logger LOG = (Logger) LoggerFactory.getLogger(HomeControllerOkta.class);

@Autowired
private ConsultaBDService consulta;


	
	
    @GetMapping("/okta/v1.0")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user, Model model) throws ResponseStatusException {
    	LOG.info("Aplicacion SSO-OKTA Versión 20230509");
    	LOG.info("Ingresa a /okta/v1.0 con  user: "+ user.getFullName());
    	LOG.info("user atributes : "+ user.getAttributes().toString());
    	LOG.info("user.getEmailVerified(): "+user.getEmailVerified());
 
    		
    	if(user.getEmailVerified()){//trae boleano
    		LOG.info("Email enontrado en okta");
    		
    		//cambios para evitar vulnerabilidad en redireccion
    		String urlRedirect= consulta.getUrlToken(user.getEmail());
    		LOG.info("urlRedirect: "+ urlRedirect);
    	    String[ ]sep =  urlRedirect.split("=");
    	    String token= sep[1];
    	    LOG.info("token " + token);
    		if(token.equals("1")) {
    			LOG.info("Ingreso con email: " + user.getEmail());
    			model.addAttribute("email","El empleado con correo "+ user.getEmail() + " no esta activo.");
    		    //return new RedirectView("/error/error");
    			return new ModelAndView("error/error");
    			
    		}else {
    			LOG.info("Manda a url: "+urlRedirect);
    			return new ModelAndView("redirect:"+urlRedirect);
    		}

    		
    	}else {
    		LOG.info("Ocurrio error");
    		model.addAttribute("email","El empleado con correo "+ user.getEmail() + " no cuenta con registro en Okta");
    		return new ModelAndView("error/nousuariookta");
			
    	}
    }

    
    @GetMapping("/okta/v1.1")
    public ModelAndView home2(@AuthenticationPrincipal OidcUser user, Model model) /*throws ResponseStatusException*/ {
    	LOG.info("Ingresa a get /okta/v1.1 con user: "+ user.getFullName());
    	LOG.info("user atributes : "+ user.getAttributes().toString());
    	LOG.info("user.getEmailVerified(): "+user.getEmailVerified());
    	
    		
    	if(user.getEmailVerified()){//trae boleano
    		LOG.info("Email encontrado en okta");
    		
    		//cambios para evitar vulnerabilidad en redireccion
    		String urlRedirect= consulta.getUrlToken(user.getEmail());
    		LOG.info("urlRedirect: "+ urlRedirect);
    	    String[ ]sep =  urlRedirect.split("=");
    	    String token= sep[1];
    	    LOG.info("token " + token);
    		if(token.equals("1")) {
    			LOG.info("Ingreso con email: " + user.getEmail());
    			model.addAttribute("email","El empleado con correo "+ user.getEmail() + " no esta activo.");
    			return new ModelAndView("error/error");
    			
    		}else {
    			LOG.info("Manda a template");
    			model.addAttribute("url", urlRedirect);
    			return new ModelAndView("error/url");
    		}
    		
    	}else {
    		LOG.info("Ocurrio error");
    		model.addAttribute("email","El empleado con correo "+ user.getEmail() + " no cuenta con registro en Okta");
    		return new ModelAndView("error/nousuariookta");
    	}
    	
    }
    
    
    @GetMapping("/okta/vBase")
    public String home3() {
    	return "v1.050923";
    }


    	
}
    
    

