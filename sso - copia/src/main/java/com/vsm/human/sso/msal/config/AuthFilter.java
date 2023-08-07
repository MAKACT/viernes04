package com.vsm.human.sso.msal.config;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.MsalException;
import com.vsm.human.sso.msal.dto.AccesoAzureDto;

@Component
public class AuthFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	
	@Autowired
	AuthHelper authHelper;

	AccesoAzureDto accesoAzureDto;

	@Value("${msal.auth.page.filter.acceso.azure}")
	private String SCRIPT_ACCESO_AZURE;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	};

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			try {

				String currentUri = httpRequest.getRequestURL().toString();
				String queryStr = httpRequest.getQueryString();
				String fullUrl = currentUri + (queryStr != null ? "?" + queryStr : "");
				String[] arrayObtenerCompania = fullUrl.split("/");
				String companiaActual = arrayObtenerCompania[arrayObtenerCompania.length - 1];
				String[] limpiarNombreCompania;

				if (companiaActual.contains("?") == true) {
					limpiarNombreCompania = companiaActual.split("\\?");
					companiaActual = limpiarNombreCompania[0];
				}

				accesoAzureDto = obtenerCredencialesAzure(companiaActual);

				if (containsAuthenticationCode(httpRequest)) {
					authHelper.processAuthenticationCodeRedirect(httpRequest, currentUri, fullUrl,
							accesoAzureDto.getClientId(), accesoAzureDto.getSecretKey(), accesoAzureDto.getAuthority());

					((HttpServletResponse) response).sendRedirect(currentUri);

					chain.doFilter(request, response);
					return;
				}

				if (!isAuthenticated(httpRequest)) {
					authHelper.sendAuthRedirect(httpRequest, httpResponse, null, accesoAzureDto.getRedirectUriSignin(),
							accesoAzureDto.getClientId(), accesoAzureDto.getSecretKey(), accesoAzureDto.getAuthority());
					return;
				}

				if (isAccessTokenExpired(httpRequest)) {
					updateAuthDataUsingSilentFlow(httpRequest, httpResponse, accesoAzureDto.getClientId(),
							accesoAzureDto.getSecretKey(), accesoAzureDto.getAuthority());
				}
			} catch (MsalException authException) {
				SessionManagementHelper.removePrincipalFromSession(httpRequest);
				authHelper.sendAuthRedirect(httpRequest, httpResponse, null, accesoAzureDto.getRedirectUriSignin(),
						accesoAzureDto.getClientId(), accesoAzureDto.getSecretKey(), accesoAzureDto.getAuthority());
				return;
			} catch (Throwable exc) {
				httpResponse.setStatus(500);
				logger.error(exc.getMessage());
				request.setAttribute("error", exc.getMessage());
				request.getRequestDispatcher("/error").forward(request, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private AccesoAzureDto obtenerCredencialesAzure(String compania) {
		String sql = SCRIPT_ACCESO_AZURE + " '" + compania + "'";

		List<AccesoAzureDto> acesso_azure = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(AccesoAzureDto.class));

		AccesoAzureDto accesoAzureDto = new AccesoAzureDto();

		for (AccesoAzureDto acceso : acesso_azure) {
			accesoAzureDto.setNum_cia(acceso.getNum_cia());
			accesoAzureDto.setAuthority(acceso.getAuthority());
			accesoAzureDto.setClientId(acceso.getClientId());
			accesoAzureDto.setCve_compania(acceso.getCve_compania());
			accesoAzureDto.setSecretKey(acceso.getSecretKey());
			accesoAzureDto.setRedirectUriSignin(acceso.getRedirectUriSignin());
		}
		return accesoAzureDto;
	}

	private boolean containsAuthenticationCode(HttpServletRequest httpRequest) {
		Map<String, String[]> httpParameters = httpRequest.getParameterMap();

		boolean isPostRequest = httpRequest.getMethod().equalsIgnoreCase("POST");
		boolean containsErrorData = httpParameters.containsKey("error");
		boolean containIdToken = httpParameters.containsKey("id_token");
		boolean containsCode = httpParameters.containsKey("code");

		return isPostRequest && containsErrorData || containsCode || containIdToken;
	}

	private boolean isAccessTokenExpired(HttpServletRequest httpRequest) {
		IAuthenticationResult result = SessionManagementHelper.getAuthSessionObject(httpRequest);
		return result.expiresOnDate().before(new Date());
	}

	private boolean isAuthenticated(HttpServletRequest request) {
		return request.getSession().getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME) != null;
	}

	private void updateAuthDataUsingSilentFlow(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			String clientId, String clientSecret, String authority) throws Throwable {
		IAuthenticationResult authResult = authHelper.getAuthResultBySilentFlow(httpRequest, httpResponse, clientId,
				clientSecret, authority);
		SessionManagementHelper.setSessionPrincipal(httpRequest, authResult);
	}
}
