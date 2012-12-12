package org.delfos.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * La clase <code>ApplicationContextProvider</code> proporciona acceso al contexto de aplicaciones
 * de Spring.
 * 
 * @author alopezg
 *
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		AppContext.setApplicationContext(ctx);

	}

}
