package com.ehuman.sso.auth.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@IdClass(HuEmpleadoToken.class)
//human.hu_empls_token_ws
@Table(name="HU_EMPLS_TOKEN_WS")
public class EmpleadosTokenWs {
	
	@Id
	@Column(name="NUM_CIA")	private Long numCia;
	
	@Id
	@Column(name="NUM_EMP")
	private Long numEmp;
	
	@Column(name="TOKEN")
	private String token;
	
	@Column(name="FECHA_MOV")
	private Date fechaMov;

	
	
	public Long getNumCia() {
		return numCia;
	}

	public void setNumCia(Long numCia) {
		this.numCia = numCia;
	}

	public Long getNumEmp() {
		return numEmp;
	}

	public void setNumEmp(Long numEmp) {
		this.numEmp = numEmp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

	public Date getFechaMov() {
		return fechaMov;
	}

	public void setFechaMov(Date fechaMov) {
		this.fechaMov = fechaMov;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmpleadosTokenWs [numCia=");
		builder.append(numCia);
		builder.append(", numEmp=");
		builder.append(numEmp);
		builder.append(", token=");
		builder.append(token);
		builder.append(", fechaMov=");
		builder.append(fechaMov);
		builder.append("]");
		return builder.toString();
	}
	
	

}
