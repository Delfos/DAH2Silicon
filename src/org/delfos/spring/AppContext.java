package org.delfos.spring;

import org.springframework.context.ApplicationContext;

/**
 * La clase <code>AppContext</code> proporciona acceso al context de aplicación de Spring.
 *  
 * @author alopezg
 *
 */
public class AppContext {

	private static ApplicationContext ctx;
	
	public static void setApplicationContext(ApplicationContext applicationContext){
		ctx = applicationContext;		
	}
	
	public static ApplicationContext getApplicationContext(){
	
		return ctx;
	}
	
}
