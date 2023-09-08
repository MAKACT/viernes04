package com.ehuman.sso.auth.models;

import java.io.Serializable;

public class HuEmpleadoToken implements Serializable {
	

	
	private static final long serialVersionUID = 1L;
	private Long numCia;
	private Long numEmp;
	
	
	
	public HuEmpleadoToken() {}
	
	public HuEmpleadoToken(Long numCia, Long numEmp) {
		super();
		this.numCia = numCia;
		this.numEmp = numEmp;
	}
	
	
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
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	

}
