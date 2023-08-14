package com.ehuman.sso.auth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ehuman.sso.auth.services.ConsultaBDService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/humanSSO")
public class EmpleadoController {
	
	@Autowired
	private ConsultaBDService consulta;



	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoController.class);

	@GetMapping("/valida")
	//public String empleadoRegistrado(@RequestParam Long numEmp, @RequestParam Long numComp) {
	public ModelAndView empleadoRegistrado(@RequestParam String email, Model model) {
		LOG.info("Ingresa a empleadoRegistrado con email" + email);
		LOG.info("URL con token" + consulta.getUrlToken(email));
		//return consulta.getUrlToken(numEmp, numComp);
		String urlRedirect= consulta.getUrlToken(email);
		LOG.info("urlRedirect: "+ urlRedirect);
	    String[ ]sep =  urlRedirect.split("=");
//	    for(int i=0; i<sep.length;i++) {
//	    	LOG.info("sep[i]"+ sep[i]);
//	    }
	    String token= sep[1];
	    LOG.info("token " + token);
		if(token.equals("1")) {
			LOG.info("Ingreso con email: " + email);
			model.addAttribute("email","El empleado con correo "+ email + " no esta activo.");
		    //return new RedirectView("/error/error");
			return new ModelAndView("error/error");
		}
		//return new RedirectView(urlRedirect);
		return new ModelAndView("redirect:"+urlRedirect);
		//return consulta.getUrlToken(email);
		/*return new ModelAndView(consulta.getUrlToken(email));*/
	}
	
	@GetMapping("/validaurl")
	//public String empleadoRegistrado(@RequestParam Long numEmp, @RequestParam Long numComp) {
	public ModelAndView empleadoRegistradoUrl(@RequestParam String email, Model model ) {
		LOG.info("Ingresa a empleadoRegistrado con email" + email);
		LOG.info("URL con token" + consulta.getUrlToken(email));
		//return consulta.getUrlToken(numEmp, numComp);
		
		String urlRedirect= consulta.getUrlToken(email);
		LOG.info("urlRedirect: "+ urlRedirect);
	    String[ ]sep =  urlRedirect.split("=");

	    String token= sep[1];
	    LOG.info("token " + token);
		if(token.equals("1")) {
			LOG.info("Ingreso con email: " + email);
			model.addAttribute("email","El empleado con correo "+ email + " no esta activo.");
		    //return new RedirectView("/error/error");
			return new ModelAndView("error/error");
		}
		model.addAttribute("url", urlRedirect);
		return new ModelAndView("error/url");
		//return consulta.getUrlToken(email);
		
	}

	

}
