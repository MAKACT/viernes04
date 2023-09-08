package com.spring.boot.security.saml.dto;

public class EmployeeDto {

	private String NUM_CIA;
	private String NUM_EMP;
	private Long NUMERO_COMPANIA;
	private String CLAVE_COMPANIA;
	//Anexo atributos para  generar recursos tkn akct
	private String APELL_PAT;
	private String APELL_MAT;
	private String NOMBRE;

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

	
	//Anexo atributos para  generar recursos tkn akct
	public String getAPELL_PAT() {
		return APELL_PAT;
	}

	public void setAPELL_PAT(String aPELL_PAT) {
		APELL_PAT = aPELL_PAT;
	}

	public String getAPELL_MAT() {
		return APELL_MAT;
	}

	public void setAPELL_MAT(String aPELL_MAT) {
		APELL_MAT = aPELL_MAT;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	
	
	
}
