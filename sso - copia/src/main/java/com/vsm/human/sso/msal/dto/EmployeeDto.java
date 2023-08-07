package com.vsm.human.sso.msal.dto;

public class EmployeeDto {

	
	private String NUM_CIA;
	private String NUM_EMP;
	private Long NUMERO_COMPANIA;
	private String CLAVE_COMPANIA;

	public String getNUM_CIA() {
		return NUM_CIA;
	}

	public void setNUM_CIA(String nUM_CIA) {
		NUM_CIA = nUM_CIA;
	}

	public String getNUM_EMP() {
		return NUM_EMP;
	}

	public void setNUM_EMP(String nUM_EMP) {
		NUM_EMP = nUM_EMP;
	}

	public Long getNUMERO_COMPANIA() {
		return NUMERO_COMPANIA;
	}

	public void setNUMERO_COMPANIA(Long nUMERO_COMPANIA) {
		NUMERO_COMPANIA = nUMERO_COMPANIA;
	}

	public String getCLAVE_COMPANIA() {
		return CLAVE_COMPANIA;
	}

	public void setCLAVE_COMPANIA(String cLAVE_COMPANIA) {
		CLAVE_COMPANIA = cLAVE_COMPANIA;
	}

}
