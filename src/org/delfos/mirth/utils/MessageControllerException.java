package org.delfos.mirth.utils;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

public class MessageControllerException extends Exception {

	@Override
	public String getMessage() {
		
		if(getCause() instanceof CannotGetJdbcConnectionException){
			return "1";
		}else {
			return "0";
		}	

	}
	
}
