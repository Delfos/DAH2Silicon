package org.delfos.mirth.hie.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class JdbcHospitalizePatientDao implements HospitalizePatientDao {

	private SimpleJdbcTemplate jdbcTemplate;
	
	/**
	 * Obtiene el paciente ingresado con el ICU indicado.
	 */
	private static final String HOSPITALIZE_PATIENT_BY_ICU_SELECT = "select * from edu_ingreso where icu = ? " + 
			"and finingreso is null";
	
	public HospitalizePatient getHospitalizePatient(int icu) {
		
		List<HospitalizePatient> matches = this.jdbcTemplate.query(HOSPITALIZE_PATIENT_BY_ICU_SELECT, 
				new ParameterizedBeanPropertyRowMapper<HospitalizePatient>(){
				
					public HospitalizePatient mapRow(ResultSet rs, int rowNum) throws SQLException {
					
						HospitalizePatient hp = new HospitalizePatient();
												
						hp.setIcu(rs.getInt("ICU"));
						hp.setStartHosp(rs.getDate("INICIOINGRESO"));
						hp.setEndHosp(rs.getDate("FININGRESO"));					
					
						return hp;
						
					}
				}, icu); 
			 
		
		if(matches.size() > 0){
			
			return matches.get(0);
		
		}else{ //No se encuentra el código DAE de la cama
			
			return null;
			
		}
	}

	public SimpleJdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;		
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
