
package com.vsm.human.sso.msal.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("aad")
class BasicConfiguration {
	

	private String msGraphEndpointHost;

	public void setMsGraphEndpointHost(String msGraphEndpointHost) {
		this.msGraphEndpointHost = msGraphEndpointHost;
	}

	public String getMsGraphEndpointHost() {
		return msGraphEndpointHost;
	}
}