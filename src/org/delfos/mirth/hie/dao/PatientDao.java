package org.delfos.mirth.hie.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public interface PatientDao {
	
	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);
	
	public SimpleJdbcTemplate getJdbcTemplate();
	
	public Patient getPatientByNhc(String nhc);	

}
