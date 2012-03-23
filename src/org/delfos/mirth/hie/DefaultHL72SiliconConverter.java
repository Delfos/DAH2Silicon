package org.delfos.mirth.hie;

import org.apache.log4j.Logger;

/**
 * Implementación por defecto de la interfaz <code>HL72SiliconConvert</code>. No hace ninguna 
 * transformación en el mensaje de entrada.
 * 
 * @author alopezg
 */
public class DefaultHL72SiliconConverter implements HL72SiliconConverter {

	private static final Logger log = Logger.getLogger(DefaultHL72SiliconConverter.class);
	
	protected DefaultHL72SiliconConverter(){};
	
	public String convert(String msg){
		log.debug("Mensaje inicial HL7-XML v2.3: \n" + msg);
		log.debug("Mensaje inicial HL7-XML v2.5: \n" + msg);
		return msg;
	}

}
