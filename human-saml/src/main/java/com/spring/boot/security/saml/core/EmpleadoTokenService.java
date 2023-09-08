package com.spring.boot.security.saml.core;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.spring.boot.security.saml.controllers.LandingController;
import com.spring.boot.security.saml.dao.EmpleadoTokenDao;
import com.spring.boot.security.saml.dto.EmpleadoTokenDto;
import com.spring.boot.security.saml.dto.EmployeeDto;
import com.spring.boot.security.saml.models.EmpleadosTokenWs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class EmpleadoTokenService {
	
	private EmpleadoTokenDao empeadoDao;

	private static final Logger LOG = LoggerFactory.getLogger(LandingController.class);

	
	
	//actualiza o genera un nuevo usuario en tabla tokens

		public EmpleadoTokenDto getTokenFinal(List<EmployeeDto> empleadoDtoList) {
			
			EmpleadoTokenDto newempTkEnc =  new EmpleadoTokenDto();
			String username = empleadoDtoList.get(0).getAPELL_PAT()+" "+ 
					empleadoDtoList.get(0).getAPELL_MAT()+" "+
					empleadoDtoList.get(0).getNOMBRE();
			LOG.info("username: " + username);
			//String tk = getJWTToken(username, 5000);
	
			EmpleadoTokenDto empTkEnc =  new EmpleadoTokenDto();
			empTkEnc=getEmpleadoToken(empleadoDtoList.get(0).getNUMERO_COMPANIA(), Long.valueOf(empleadoDtoList.get(0).getNUM_EMP()));
			LOG.info("Empleado encontrado en tabla HU_EMPLS_TOKEN_WS: " +empTkEnc.toString());
			if(empTkEnc.getNumCia()!= null && empTkEnc.getNumEmp()!= null && empTkEnc.getFechaMov()!= null &&empTkEnc.getToken()!= null) {

				LOG.info("Valida atributos completos " +empTkEnc.toString());
				newempTkEnc=updateEmpleadoToken(empTkEnc, username);
				//return empTkEnc.toString();

			}else {
				LOG.info("Ingresa para registrar un nuevo empeado en la tabla de token " +empleadoDtoList.get(0).toString());
				newempTkEnc =addRegistroEmpleado(empleadoDtoList.get(0));
				//return empTkEnc.toString();

				}
			
			return newempTkEnc;
			
		}
		
	
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
	
	
	public EmpleadoTokenDto addRegistroEmpleado(EmployeeDto empDto) {
		LOG.info("Ingresa a  addRegistroEmpleado con datos para registro de empleado: " + empDto.toString());
		EmpleadoTokenDto empTkDto =  new EmpleadoTokenDto();
		empTkDto.setFechaMov(new Date(System.currentTimeMillis()));
		empTkDto.setNumCia(empDto.getNUMERO_COMPANIA());
		empTkDto.setNumEmp(Long.valueOf( empDto.getNUM_EMP()));
		String username = empDto.getAPELL_PAT()+" "+empDto.getAPELL_MAT()+" "+empDto.getNOMBRE();
		empTkDto.setToken(getJWTToken(username, 5000));
		LOG.info("Empleado a registrar: "+empTkDto.toString());
		empeadoDao.save(this.dtoToModel(empTkDto));
		return empTkDto;
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
}
