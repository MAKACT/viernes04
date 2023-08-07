package com.vsm.human.sso.msal.config;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.vsm.human.sso.msal.dto.EmployeeDto;

@Controller
public class AuthPageController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(AuthPageController.class);	
	

	@Autowired
	AuthHelper authHelper;
	
	public  String DOMINIO = "/human-msal-1.0.0_1/";

	@Value("${msal.auth.page.controller.login.url.succes}")
	private String LOGIN_SAML_SUCCESS;

	@Value("${msal.auth.page.controller.login.url.error}")
	private String LOGIN_SAML_ERROR;

	@Value("${msal.auth.page.controller.post.logout.redirect-uri}")
	private String POST_LOGOUT_REDIRECT_URI;

	@Value("${msal.auth.page.controller.end.session.endpoint}")
	private String END_SESSION_ENDPOINT;

	@Value("${msal.auth.page.controller.error.login.many.emails}")
	private String MANY_EMAILS_MESSAGE;

	@Value("${msal.auth.page.controller.error.login.empty.email}")
	private String EMPTY_EMAIL_MESSAGE;

	@Value("${msal.auth.page.controller.error.login.simlate.count}")
	private String SIMULATE_ACOUNT_MESSAGE;

	@Value("${msal.auth.page.controller.query.select.get.credentials}")
	private String QUERY_GET_CREDENTIALS;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	String errorLoginMessage;

	@RequestMapping("/human-msal-1.0.0_1/human-msal/human-msal4j")
	public String homepage(){
		logger.info("index");
		return "index";
	}

	@RequestMapping("/human-msal-1.0.0_1/human-msal4j/secure/aad/{empresa}")
	public ModelAndView securePage(HttpServletRequest httpRequest, HttpServletResponse response,
			@PathVariable(value = "empresa") String empresa) throws ParseException, Exception {

		ModelAndView mav = new ModelAndView("auth_page");
		setAccountInfo(mav, httpRequest, empresa);

		return mav;
	}

	private void setAccountInfo(ModelAndView model, HttpServletRequest httpRequest, String compania) throws Exception {

		IAuthenticationResult auth = SessionManagementHelper.getAuthSessionObject(httpRequest);

		model.addObject("compania", compania);
		model.addObject("errorLogin", errorLoginMessage);
		model.addObject("urlRedirect", recoveryUrlRedirectWorkSocial(auth, model));
	}

	private String recoveryUrlRedirectWorkSocial(IAuthenticationResult auth, ModelAndView model) throws Exception {

		String responseUrlRedirectWorkSocial = "";
		String emailUser = auth.account().username();

		if (!emailUser.equals("null")) {

			String sql = QUERY_GET_CREDENTIALS + " '" + emailUser + "' ";

			List<EmployeeDto> employeesList = jdbcTemplate.query(sql,
					BeanPropertyRowMapper.newInstance(EmployeeDto.class));

			if (employeesList.isEmpty()) {

				errorLoginMessage = EMPTY_EMAIL_MESSAGE + " Sin correo electronico";
				responseUrlRedirectWorkSocial = LOGIN_SAML_ERROR;

			} else if (employeesList.size() > 1) {

				errorLoginMessage = MANY_EMAILS_MESSAGE;
				responseUrlRedirectWorkSocial = LOGIN_SAML_ERROR;

			} else if (employeesList.size() == 1) {

				if (employeesList.get(0).getNUMERO_COMPANIA() < 5000L) {
					responseUrlRedirectWorkSocial = LOGIN_SAML_SUCCESS + "CompanyNumber="
							+ employeesList.get(0).getCLAVE_COMPANIA() + "&EmployeeNumber="
							+ employeesList.get(0).getNUM_EMP();
				} else {
					errorLoginMessage = SIMULATE_ACOUNT_MESSAGE;
					responseUrlRedirectWorkSocial = LOGIN_SAML_ERROR;
				}

			}
		} else {

			errorLoginMessage = EMPTY_EMAIL_MESSAGE;
			responseUrlRedirectWorkSocial = LOGIN_SAML_ERROR;
			logger.error(errorLoginMessage);
			logger.error(responseUrlRedirectWorkSocial);
		}

		return responseUrlRedirectWorkSocial;
	}
}
