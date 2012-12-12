package org.delfos.mirth.hie.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public interface NurseUnitDao {
	
	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate);
	
	public SimpleJdbcTemplate getJdbcSimpleJdbcTemplate();
	
	public NurseUnit getNurseUnitByAltId(String altId);

}
