package org.delfos.mirth.hie.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public interface BedDao {
	
	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);
	
	public SimpleJdbcTemplate getJdbcTemplate();
	
	public Bed getBedByAltId(String altId);
	
}
