package com.ehuman.sso.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ehuman.sso.auth.models.EmpleadosTokenWs;

@Repository
public interface EmpleadoTokenDao extends JpaRepository<EmpleadosTokenWs, Long> {
	public abstract EmpleadosTokenWs findByNumCiaAndNumEmp(Long numCia, Long NumEmp);

}
