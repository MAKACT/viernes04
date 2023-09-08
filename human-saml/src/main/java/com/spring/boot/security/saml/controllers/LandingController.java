package com.spring.boot.security.saml.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.boot.security.saml.core.EmpleadoTokenService;
import com.spring.boot.security.saml.dto.EmpleadoTokenDto;
import com.spring.boot.security.saml.dto.EmployeeDto;
import com.spring.boot.security.saml.stereotypes.CurrentUser;

@Controller
public class LandingController {
	@Value("${auth.page.controller.login.url.succes}")
	private String LOGIN_SAML_SUCCESS;

	@Value("${auth.page.controller.query.select.get.credentials}")
	private String QUERY_GET_CREDENTIALS;

	@Value("${auth.page.controller.query.select.get.credentials.implants}")
	private String QUERY_GET_CREDENTIALS_IMPLANTS;
	
	// cambios tk skct 
	
	/*@Value("${auth.page.controller.query.select.get.credentials.token}")
	private String QUERY_GET_CREDENTIALS_TOKEN;*/

	@Value("${auth.page.controller.error.login.many.emails}")
	private String MANY_EMAILS_MESSAGE;

	@Value("${auth.page.controller.error.login.empty.email}")
	private String EMPTY_EMAIL_MESSAGE;

	@Value("${auth.page.controller.error.login.simlate.count}")
	private String SIMULATE_ACOUNT_MESSAGE;

	@Value("${auth.page.controller.login.url.error}")
	private String LOGIN_SAML_ERROR;

	String errorLoginMessage;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EmpleadoTokenService empleadoTkService;

	private static final Logger LOG = LoggerFactory.getLogger(LandingController.class);

	@GetMapping("/landing")
	public String landing(@CurrentUser User user, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null)
			LOG.debug("Current authentication instance from security context is null");
		else
			LOG.debug("Current authentication instance from security context: {}", this.getClass().getSimpleName());

		LOG.info("Correo que se esta logeando:  " + user.getUsername());
		
		LOG.info("Correo que se esta logeando con lowerCase:  " + user.getUsername().toLowerCase());
		
		String urlRedirect = recuperarUrlRedireccion(user.getUsername().toLowerCase());
		
		
		

		model.addAttribute("username", user.getUsername());

		model.addAttribute("urlRedirect", urlRedirect);
		
		model.addAttribute("errorLogin", errorLoginMessage);

		return "pages/landing";
	}

	public String recuperarUrlRedireccion(String userEmail) {

		String responseUrlRedirectWorkSocial = "";

		String sql = QUERY_GET_CREDENTIALS + " '" + userEmail.trim() + "' ";
		
		LOG.info("Query base de datos hu_empls:  " + sql);

		List<EmployeeDto> employeesList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(EmployeeDto.class));

		if (employeesList.isEmpty()) {

			String sqlImplants = QUERY_GET_CREDENTIALS_IMPLANTS + " '" + userEmail.trim() + "' ";
			
			LOG.info("Query base de datos implants:  " + sqlImplants);

			List<EmployeeDto> employeesListImplants = jdbcTemplate.query(sqlImplants,
					BeanPropertyRowMapper.newInstance(EmployeeDto.class));

			if (employeesListImplants.isEmpty()) {
				
				LOG.info("Ningun resultado encontrado:  " + userEmail);

				errorLoginMessage = EMPTY_EMAIL_MESSAGE + " Sin correo electronico";
				responseUrlRedirectWorkSocial = LOGIN_SAML_ERROR;

			} else {

				responseUrlRedirectWorkSocial = urlRedireccion(employeesListImplants);

			}

		} else {

			responseUrlRedirectWorkSocial = urlRedireccion(employeesList);

		}

		return responseUrlRedirectWorkSocial;
	}

	public String urlRedireccion(List<EmployeeDto> employeesList) {

		String urlResponse = "";
		
		//cambio AKCT
		EmpleadoTokenDto empTkEnc =  new EmpleadoTokenDto();

		if (employeesList.size() > 1) {

			errorLoginMessage = MANY_EMAILS_MESSAGE;
			urlResponse = LOGIN_SAML_ERROR;

		} else if (employeesList.size() == 1) {

			LOG.debug("Numero de compañia  " + employeesList.get(0).getNUMERO_COMPANIA());
			
			//cambios para usar token akct
			
			empTkEnc= empleadoTkService.getTokenFinal(employeesList);
			urlResponse = LOGIN_SAML_SUCCESS + "tokenUrl=" + empTkEnc.getToken();
			
			
			
			
			/*urlResponse = LOGIN_SAML_SUCCESS + "CompanyNumber=" + employeesList.get(0).getCLAVE_COMPANIA()
					+ "&EmployeeNumber=" + employeesList.get(0).getNUM_EMP();*/
			
			//fin de los cambios akct
			
			LOG.info("Acceso correcto  numero de empleado:  " +  employeesList.get(0).getNUM_EMP());

		}

		return urlResponse;
	}

}
