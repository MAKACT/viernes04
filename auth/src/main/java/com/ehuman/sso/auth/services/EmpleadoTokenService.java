package com.ehuman.sso.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.ehuman.sso.auth.dao.EmpleadoTokenDao;
import com.ehuman.sso.auth.dto.EmpleadoDto;
import com.ehuman.sso.auth.dto.EmpleadoTokenDto;
import com.ehuman.sso.auth.models.EmpleadosTokenWs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmpleadoTokenService {
	
	@Autowired
	private EmpleadoTokenDao empeadoDao;

	
	
	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoTokenService.class);
	
	// vefificar datos exitentes en HU_EMPLS_TOKEN_WS
		public EmpleadoTokenDto getEmpleadoToken(Long numCia, Long numEmp) {
			LOG.info("Engresa a EmpleadoTokenDto con datos de empleado numcia :"+numCia+ " y numEmp: " +numEmp);
			EmpleadosTokenWs empleadoT = new EmpleadosTokenWs();
			EmpleadoTokenDto empDto =  new EmpleadoTokenDto();
			empleadoT= empeadoDao.findByNumCiaAndNumEmp(numCia, numEmp);
			LOG.info("EmpleadoTOKEN: " +empleadoT);
		    /*if(empleadoT.getFechaMov()!= null && empleadoT.getNumCia()!= null && empleadoT.getNumEmp()!= null && empleadoT.getToken()!= null) {*/
			if(empleadoT!= null) {
			empDto = this.modelToDto(empleadoT);
			LOG.info("Datos en empleado dto post busqueda y conversion"+empDto.toString());
		    }
			return empDto;
		}
			
		
			
				
		public EmpleadoTokenDto updateEmpleadoToken(EmpleadoTokenDto empleadoTDto, String username)	{
			LOG.info("Ingresa a updateEmpleadoToken con datos" +empleadoTDto.toString()+ "username: "+username);
			String token = this.getJWTToken(username, 5000);
		    empleadoTDto.setToken(token);
		    empleadoTDto.setFechaMov(new Date(System.currentTimeMillis()));
		    LOG.info("Datos de empleado a actualizar en BD: "+ empleadoTDto.toString());
		    empeadoDao.save(this.dtoToModel(empleadoTDto));
		    return empleadoTDto;
		    }
		    
		    
		
			
			
		
		
		
		public EmpleadoTokenDto addRegistroEmpleado(EmpleadoDto empDto) {
			LOG.info("Ingresa a  addRegistroEmpleado con datos para registro de empleado: " + empDto.toString());
			EmpleadoTokenDto empTkDto =  new EmpleadoTokenDto();
			empTkDto.setFechaMov(new Date(System.currentTimeMillis()));
			empTkDto.setNumCia(empDto.getNum_cia());
			empTkDto.setNumEmp(empDto.getNum_emp());
			String username = empDto.getApell_pat()+" "+empDto.getApell_mat()+" "+empDto.getNombre();
			empTkDto.setToken(getJWTToken(username, 5000));
			LOG.info("Empleado a registrar: "+empTkDto.toString());
			empeadoDao.save(this.dtoToModel(empTkDto));
			return empTkDto;
		}
		
		
			
			
		
		// convertir model a dto
		public EmpleadoTokenDto modelToDto(EmpleadosTokenWs empToken) {
			LOG.info("Ingresa a modelToDto con datos emEmpleadosTokenWs: "+ empToken.toString());
			EmpleadoTokenDto empTDto = new EmpleadoTokenDto();
			empTDto.setNumCia(empToken.getNumCia());
			empTDto.setNumEmp(empToken.getNumEmp());
			empTDto.setToken(empToken.getToken());
			empTDto.setFechaMov(empToken.getFechaMov());
			LOG.info("regresa EmpleadoTokenDto: "+ empTDto.toString() );
			return empTDto;
		}
		
		//convertir dto a model
		public EmpleadosTokenWs dtoToModel(EmpleadoTokenDto empTokDto) {
			LOG.info("Ingresa a dtoToModel con EmpleadoTokenDto:" +empTokDto.toString());
			EmpleadosTokenWs emp = new EmpleadosTokenWs();
			emp.setNumCia(empTokDto.getNumCia());
			emp.setNumEmp(empTokDto.getNumEmp());
			emp.setToken(empTokDto.getToken());
			emp.setFechaMov(empTokDto.getFechaMov());
			LOG.info("regresa EmpleadosTokenWs: "+ emp.toString() );
			return emp;
		}
		
	

		
		
		public String getJWTToken(String username, long tokenExp) {		
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
