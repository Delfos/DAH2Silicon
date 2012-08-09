package org.delfos.mirth.hie;

import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * La clase <code>HL72SiliconFactory</code> es una factoria de 
 * <code>HL72SiliconConverter</code>
 * 
 * @author alopezg
 *
 */
public class HL72SiliconFactory {
	
	private static final Logger log = Logger.getLogger(HL72SiliconConverter.class);
	
	//Devuelve la implementación por defecto  de la interfaz <code>HL7SiliconConverter</code>
	public static final String DEFAULT = "default";
	
	private static Hashtable<String, HL72SiliconConverter> convertes;
	
	static{
		
		log.debug("Inicio creación HL72SiliconFactory");
		
		convertes = new Hashtable<String, HL72SiliconConverter>(10);		
		DefaultHL72SiliconConverter defaultConverter = new DefaultHL72SiliconConverter();
		HL72SiliconConverter A01Converter = new A01HL72SiliconConverter();		
		HL72SiliconConverter A03Converter = new A03HL72SiliconConverter();
		HL72SiliconConverter A11Converter = new A11HL72SiliconConverter();
		HL72SiliconConverter A13Converter = new A13HL72SiliconConverter();
		HL72SiliconConverter A02Converter = new A02HL72SiliconConverter();
		HL72SiliconConverter A12Converter = new A12HL72SiliconConverter();
		HL72SiliconConverter A08Converter = new A08HL72SiliconConverter();
		
		convertes.put("ADT^A01", A01Converter);
		convertes.put("ADT^A03", A03Converter);
		convertes.put("ADT^A11", A11Converter);
		convertes.put("ADT^A02", A02Converter);
		convertes.put("ADT^A12", A12Converter);		
		convertes.put("ADT^A13", A13Converter);
		convertes.put("ADT^A08", A08Converter);
		convertes.put("ADT^A18", defaultConverter);
		convertes.put("ADT^A22", defaultConverter);
		convertes.put("ADT^A52", defaultConverter);
		convertes.put(DEFAULT, defaultConverter);
		
		log.debug("HL72SiliconFactory creada con éxito");
		
	}
	
	private HL72SiliconFactory(){};
	
	/**
	 * Devuelve la instancia de la interfaz <code>HL72SiliconConverter</code> específica para la 
	 * conversión del tipo de mensaje HL7 indicado.
	 * 
	 * @param msgType tipo de mensaje HL7. Los tipos válidos son: ADT^A01, ADT^A011, ADT^A02,
	 * 		ADT^A12, ADT^A03, ADT^A13, ADT^A08, ADT^A18, ADT^A22 y ADT^A52.
	 * @return Convertidor para el tipo de messaje indicado.
	 * @throws IllegalArgumentException cuando el tipo de mensaje indicado no es válido.
	 */
	public static HL72SiliconConverter getHL72SiliconConverter(String msgType) 
		throws IllegalArgumentException{
		
		log.info("Solitado convertidor de tipo: " + msgType);
		
		HL72SiliconConverter result = convertes.get(msgType);
		
		if(result == null){
			IllegalArgumentException ex = new IllegalArgumentException("El tipo de mensaje " + msgType + 
				" no es válido");
			
			log.warn(ex);
			
			throw ex; 
		}
		
		return result;
		
	}
	
}
