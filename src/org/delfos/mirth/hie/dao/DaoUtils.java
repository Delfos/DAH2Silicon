package org.delfos.mirth.hie.dao;

import org.delfos.spring.AppContext;
import org.springframework.context.ApplicationContext;

public class DaoUtils {

	private static final ApplicationContext ctx = AppContext.getApplicationContext();
	
	public static Patient getPatientByNhc(String nhc){
		
		PatientDao patientDao = (PatientDao)ctx.getBean("jdbcPatientDao");
		return patientDao.getPatientByNhc(nhc);
		
	}
	
}
