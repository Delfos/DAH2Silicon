package org.delfos.mirth.hie.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcServiceDao implements ServiceDao {
	
	private SimpleJdbcTemplate jdbcTemplate;
	
	private static final String SERVICE_BY_ALTID_SELECT = "select SERVICIO_ID, CODIGO, DESCRIPCION, ALTID from EDU_SERVICIO where ALTID = ?";

	public SimpleJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public Service getServiceByAltId(String altId) {
		List<Service> matches = this.jdbcTemplate.query(SERVICE_BY_ALTID_SELECT, 
				new ParameterizedBeanPropertyRowMapper<Service>(){
				
					public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
					
						Service service = new Service();
						service.setId(rs.getInt("SERVICIO_ID"));
						service.setCode(rs.getString("CODIGO"));
						service.setDescription(rs.getString("DESCRIPCION"));
						service.setAltId(rs.getString("ALTID"));
					
						return service;
						
					}
				}, altId); 
			
			if(matches.size() > 0){
			
				return matches.get(0);
			
			}else{ //No se encuentra el código DAE de la cama
				
				Service service = new Service();
				service.setCode("XXX");
				service.setAltId("XXX");
				
				return service;
				
			}
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
