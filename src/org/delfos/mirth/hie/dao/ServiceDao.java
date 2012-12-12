package org.delfos.mirth.hie.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public interface ServiceDao {

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);
	
	public SimpleJdbcTemplate getJdbcTemplate();
	
	public Service getServiceByAltId(String altId);
	
}
