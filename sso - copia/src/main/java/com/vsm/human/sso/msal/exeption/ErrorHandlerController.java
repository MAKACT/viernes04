package com.vsm.human.sso.msal.exeption;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorHandlerController implements ErrorController {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

	private static final String PATH = "/error";

	@RequestMapping(value = PATH)
	public ModelAndView returnErrorPage(HttpServletRequest req, HttpServletResponse response) {
		   ModelAndView mav = new ModelAndView("error");

		mav.addObject("error", "Existe algún error con tu correo, por favor verifícalo con el administrador");
		logger.warn("Existe algún error con tu correo, por favor verifícalo con el administrador");

		return mav;
	}

	public String getErrorPath() {
		return PATH;
	}

}
