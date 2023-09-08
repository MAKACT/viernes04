package com.ehuman.sso.auth.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.ehuman.sso.auth.dto.EmpleadoDto;
import com.ehuman.sso.auth.dto.EmpleadoTokenDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class EmpleadoTokenJTService {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoTokenJTService.class);

	
	
	
	public EmpleadoTokenDto getEmpleadoTokenJ(Long nCia, Long nEMpl) {
	
	String sqlEmpleadoToken= "SELECT * FROM HUMAN.HU_EMPLS_TOKEN_WS WHERE NUM_CIA="+ nCia+" AND NUM_EMP ="+nEMpl;
	LOG.info("Queryr sqlEmpleadoToken: "+ sqlEmpleadoToken);
	EmpleadoTokenDto empToken =  null;
	
	List<EmpleadoTokenDto> empT =  jdbcTemplate.query(sqlEmpleadoToken, BeanPropertyRowMapper.newInstance(EmpleadoTokenDto.class));
	LOG.info("empT: "+empT);
	if(!empT.isEmpty()) {
		empToken= empT.get(0);
	}else {
		empToken =  new EmpleadoTokenDto();
	}
	return empToken;
	}
	
	
	public EmpleadoTokenDto addRegistroEmpleadoJ(EmpleadoDto empDto) {
		EmpleadoTokenDto empTkDto =  new EmpleadoTokenDto();
		empTkDto.setFechaMov(new Date(System.currentTimeMillis()));
		empTkDto.setNumCia(empDto.getNum_cia());
		empTkDto.setNumEmp(empDto.getNum_emp());
		String username = empDto.getApell_pat()+" "+empDto.getApell_mat()+" "+empDto.getNombre();
		empTkDto.setToken(getJWTTokenJ(username, 5000));
		
		jdbcTemplate.update( "INSERT INTO HUMAN.HU_EMPLS_TOKEN_WS  VALUES (?, ?, ?, ?)", empDto.getNum_cia(), empDto.getNum_emp(), getJWTTokenJ(username, 5000), new Date(System.currentTimeMillis()));
		LOG.info("Empleado a registrar: "+empTkDto.toString());
		return empTkDto;
	}
	
	
	public void updateEmpleadoTokenJ(EmpleadoTokenDto empleadoTDto, String username)	{
		LOG.info("Ingresa a updateEmpleadoTokenJ con datos" +empleadoTDto.toString()+ "username: "+username);
		String token = this.getJWTTokenJ(username, 5000);
	    empleadoTDto.setToken(token);
	    empleadoTDto.setFechaMov(new Date(System.currentTimeMillis()));
	    LOG.info("Datos de empleado a actualizar en BD: "+ empleadoTDto.toString());
	    String sqlUp="UPDATE  HU_EMPLS_TOKEN_WS set TOKEN= ?, FECHA_MOV= ? where NUM_CIA= ? AND NUM_EMP= ?";
	    jdbcTemplate.update( sqlUp, token, new Date(System.currentTimeMillis()), empleadoTDto.getNumEmp(), empleadoTDto.getNumEmp());
	    LOG.info("sql a ejecutar: " +jdbcTemplate.update( sqlUp, token, new Date(System.currentTimeMillis()), empleadoTDto.getNumEmp(), empleadoTDto.getNumEmp()));
	   
	  }
	
	
	public String getJWTTokenJ(String username, long tokenExp) {		
		LOG.info("ingresa a getJWTToken con username:  "+ username +" tokenExp:  " + tokenExp);
		String secretKey = "humanRHD";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");		
		String token = Jwts.builder().setId("venturssotfJWT")
									 .setSubject(username)
									 .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
									 .setIssuedAt(new Date(System.currentTimeMillis()))
									 .setExpiration(new Date(System.currentTimeMillis() + tokenExp))
									 //.serializeToJsonWith(secretKey)
									 .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
									 .compact();
		LOG.info("token generado:" +token);
		return  token;
	}
	
}
