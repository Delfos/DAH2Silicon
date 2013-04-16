package org.delfos.mirth.hie.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public interface HospitalizePatientDao {

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);
	
	public SimpleJdbcTemplate getJdbcTemplate();
	
	public HospitalizePatient getHospitalizePatient(int icu);	

	
}
