package org.delfos.mirth.hie.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcNurseUnitDao implements NurseUnitDao {

	private SimpleJdbcTemplate jdbcTemplate;
	
	private static final String NURSEUNIT_BY_ALTID_SELECT = "select UNIDADENFERMERIA_ID, CODIGO, DESCRIPCION, ALTID from EDU_UNIDADENFERMERIA where ALTID = ?";	
	
	public SimpleJdbcTemplate getJdbcSimpleJdbcTemplate() {
		return jdbcTemplate;
	}

	public NurseUnit getNurseUnitByAltId(String altId) {
		List<NurseUnit> matches = this.jdbcTemplate.query(NURSEUNIT_BY_ALTID_SELECT, 
				new ParameterizedBeanPropertyRowMapper<NurseUnit>(){
				
					public NurseUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
					
						NurseUnit nurseUnit = new NurseUnit();
						nurseUnit.setId(rs.getInt("UNIDADENFERMERIA_ID"));
						nurseUnit.setCode(rs.getString("CODIGO"));
						nurseUnit.setDescription(rs.getString("DESCRIPCION"));
						nurseUnit.setAltId(rs.getString("ALTID"));
												
						return nurseUnit;
						
					}
				}, altId); 
			
			if(matches.size() > 0){
			
				return matches.get(0);
			
			}else{ //No se encuentra el código DAE de la cama
				
				NurseUnit nurseUnit = new NurseUnit();
				nurseUnit.setCode("XXX");
				nurseUnit.setAltId("XXX");
			
				return nurseUnit;
				
			}
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
