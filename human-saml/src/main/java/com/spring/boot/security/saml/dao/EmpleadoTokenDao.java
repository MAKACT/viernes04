package com.spring.boot.security.saml.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.boot.security.saml.models.EmpleadosTokenWs;

@Repository
public interface EmpleadoTokenDao  extends JpaRepository<EmpleadosTokenWs, Long>{
	public abstract EmpleadosTokenWs findByNumCiaAndNumEmp(Long numCia, Long NumEmp);


}
