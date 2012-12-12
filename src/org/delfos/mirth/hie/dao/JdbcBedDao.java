package org.delfos.mirth.hie.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcBedDao implements BedDao {

	private SimpleJdbcTemplate jdbcTemplate;
	
	private static final String BED_BY_ALTID_SELECT = "select CAMA_ID, DESCRIPCION, ALTID from EDU_CAMA where ALTID = ?";
	
	public Bed getBedByAltId(String altId) {
		
		List<Bed> matches = this.jdbcTemplate.query(BED_BY_ALTID_SELECT, 
			new ParameterizedBeanPropertyRowMapper<Bed>(){
			
				public Bed mapRow(ResultSet rs, int rowNum) throws SQLException {
				
					Bed bed = new Bed();
					bed.setId(rs.getInt("CAMA_ID"));
					bed.setDescription(rs.getString("DESCRIPCION"));
					bed.setAltId(rs.getString("ALTID"));
					
					return bed;
					
				}
			}, altId); 
		
		if(matches.size() > 0){
		
			return matches.get(0);
		
		}else{ //No se encuentra el código DAE de la cama
			
			Bed bed = new Bed();
			bed.setDescription("X" + altId);
			bed.setAltId(altId);
			
			return bed;
			
		}
		
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}
	
	public SimpleJdbcTemplate getJdbcTemplate(){
		return this.jdbcTemplate;
	}

}
