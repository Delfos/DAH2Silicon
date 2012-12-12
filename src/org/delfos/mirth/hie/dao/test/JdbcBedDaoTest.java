package org.delfos.mirth.hie.dao.test;

import java.beans.beancontext.BeanContext;

import org.delfos.mirth.hie.dao.Bed;
import org.delfos.mirth.hie.dao.JdbcBedDao;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import junit.framework.TestCase;

public class JdbcBedDaoTest extends TestCase{

	private static BeanFactory beanFtc;
	
	static {
		
		beanFtc = new FileSystemXmlApplicationContext("E:/Grifols/projects/java/mirth/mirth_utils/spring/spring_mirth_hie.xml");		
		
	}
	
	public void testGetBedByAltId_C00() {

		String altId = "65050";
		JdbcBedDao jdbcBedDao = (JdbcBedDao)beanFtc.getBean("jdbcBedDao");
		Bed bed = jdbcBedDao.getBedByAltId(altId);
		
		System.out.println("Id: " + bed.getId());
		System.out.println("Description: " + bed.getDescription());
		System.out.println("AltId: " + bed.getAltId());
		
		assertEquals("402-1", bed.getDescription());
		
	}
	
}
