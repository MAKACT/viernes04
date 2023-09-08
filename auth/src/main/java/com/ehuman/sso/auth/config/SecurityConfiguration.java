package com.ehuman.sso.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.ehuman.sso.auth.config.SecurityConfiguration;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

private static final Logger LOG = (Logger) LoggerFactory.getLogger(SecurityConfiguration.class);
	
	private final ClientRegistrationRepository clientRegistrationRepository;
	
	
	public SecurityConfiguration(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        
        LOG.info("this.clientRegistrationRepository " +this.clientRegistrationRepository );
    }
	  
	
	OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
            new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("https://eland-ws-03.humaneland.net/sso");
    	LOG.info("Ingreso a oidcLogoutSuccessHandler: " + successHandler);
        return successHandler;
    }
	
	 @Bean
	    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
	        http.authorizeHttpRequests((requests) -> requests
	            // allow anonymous access to the root page
	            .antMatchers("/").permitAll()
	            // all other requests
	            .anyRequest().authenticated());
	        http.logout((logout) -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));
	        // enable OAuth2/OIDC
	        http.oauth2Login();
	        http.oauth2ResourceServer().jwt();

	        return http.build();
	    }
	
}
