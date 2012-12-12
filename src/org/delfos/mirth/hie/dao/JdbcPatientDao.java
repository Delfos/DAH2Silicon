package org.delfos.mirth.hie.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcPatientDao implements PatientDao {

	private SimpleJdbcTemplate jdbcTemplate;
	
	private static final String PATIENT_BY_NHC_SELECT = "select * from EDU_PACIENTE where nhc = ?";
	
	public SimpleJdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	public Patient getPatientByNhc(String nhc) {
		
		List<Patient> matches = this.jdbcTemplate.query(PATIENT_BY_NHC_SELECT, 
				new ParameterizedBeanPropertyRowMapper<Patient>(){
				
					public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
					
						Patient patient = new Patient();
												
						patient.setNhc(rs.getString("NHC"));
						patient.setCip(rs.getString("CIP"));
						patient.setName(rs.getString("NOMBRE"));
						patient.setSurname(rs.getString("APELLIDO"));					
					
						return patient;
						
					}
				}, nhc); 
			 
		
		if(matches.size() > 0){
			
			return matches.get(0);
		
		}else{ //No se encuentra el código DAE de la cama
			
			return null;
			
		}
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
