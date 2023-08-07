package com.vsm.human.sso.msal.dto;

public class AccesoAzureDto {
	

	private Long num_cia;

	private String cve_compania;

	private String clientId;

	private String authority;

	private String secretKey;

	private String redirectUriSignin;

	public Long getNum_cia() {
		return num_cia;
	}

	public void setNum_cia(Long num_cia) {
		this.num_cia = num_cia;
	}

	public String getCve_compania() {
		return cve_compania;
	}

	public void setCve_compania(String cve_compania) {
		this.cve_compania = cve_compania;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getRedirectUriSignin() {
		return redirectUriSignin;
	}

	public void setRedirectUriSignin(String redirectUriSignin) {
		this.redirectUriSignin = redirectUriSignin;
	}

}
