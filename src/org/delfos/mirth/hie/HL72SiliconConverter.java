package org.delfos.mirth.hie;

/**
 * La interfaz <code>HL72SiliconConverter</code> define los métodos necesarios para convertir 
 * el HL7 en formato XML que envía admisiones en el HL7 en formato XML que espera Silicon.
 * 
 * @author alopezg
 */
public interface HL72SiliconConverter {
	
	/**
	 * Convierte el HL7 en formato XML indicado en el HL7 en formato XML esperado por Silicon.
	 * 
	 * @param msg mensaje HL7 en XML versión 2.3.1
	 * @return mensaje HL7 en XML esperado por Silicon
	 * @throws IllegalArgumentException cuando el mensaje indicado no es válido.
	 */
	public String convert(String msg) throws IllegalArgumentException;

}
