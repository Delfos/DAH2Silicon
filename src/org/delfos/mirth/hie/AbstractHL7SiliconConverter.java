package org.delfos.mirth.hie;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public abstract class AbstractHL7SiliconConverter implements HL72SiliconConverter {

	private static final Logger log = Logger.getLogger(AbstractHL7SiliconConverter.class);
	
	static final String UTF8 = "UTF-8";
	
	static final Hashtable<String, Transformer> transformers = new Hashtable<String, Transformer>(10);
	
	static final String A01_TRANSFORMER = "a01.transformer";
	static final String A03_TRANSFORMER = "a03.transformer";
	static final String A11_TRANSFORMER = "a11.transformer";
	static final String A13_TRANSFORMER = "a13.transformer";
	static final String A02_TRANSFORMER = "a02.transformer";
	static final String A12_TRANSFORMER = "a12.transformer";
	static final String A08_TRANSFORMER = "a08.transformer";
	
	private static DocumentBuilder docBuilder;
	
	// TODO - Tener en cuenta que la codificación de caracteres tiene que ser UTF-8	
	static{
		
		TransformerFactory transFact = TransformerFactory.newInstance();
		transFact.setURIResolver(getHIEURIResolver());
		
		log.trace("Instancia de TransformerFactory: " + transFact);
		
		//Hojas de estilos XSLT		
		
		InputStream A01XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a01.xsl");		
		Source A01XsltSource = new StreamSource(A01XsltIs);
		
		InputStream A03XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a03.xsl");
		Source A03XsltSource = new StreamSource(A03XsltIs);
		
		InputStream A11XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a11.xsl");
		Source A11XsltSource = new StreamSource(A11XsltIs);
		
		InputStream A13XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a13.xsl");
		Source A13XsltSource = new StreamSource(A13XsltIs);
		
		InputStream A02XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a02.xsl");
		Source A02XsltSource = new StreamSource(A02XsltIs);
		
		InputStream A12XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a12.xsl");
		Source A12XsltSource = new StreamSource(A12XsltIs);		
		
		InputStream A08XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a08.xsl");
		Source A08XsltSource = new StreamSource(A08XsltIs);				
		
		try{
		
			Transformer A01Trans = transFact.newTransformer(A01XsltSource);			
			transformers.put(A01_TRANSFORMER, A01Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A01: " + A01Trans);
			
			Transformer A03Trans = transFact.newTransformer(A03XsltSource);
			transformers.put(A03_TRANSFORMER, A03Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A03: " + A03Trans);
			
			Transformer A11Trans = transFact.newTransformer(A11XsltSource);
			transformers.put(A11_TRANSFORMER, A11Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A11: " + A11Trans);			
			
			Transformer A13Trans = transFact.newTransformer(A13XsltSource);
			transformers.put(A13_TRANSFORMER, A13Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A13: " + A13Trans);			
			
			Transformer A02Trans = transFact.newTransformer(A02XsltSource);
			transformers.put(A02_TRANSFORMER, A02Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A02: " + A02Trans);			
			
			Transformer A12Trans = transFact.newTransformer(A12XsltSource);
			transformers.put(A12_TRANSFORMER, A12Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A12: " + A12Trans);			
			
			Transformer A08Trans = transFact.newTransformer(A08XsltSource);
			transformers.put(A08_TRANSFORMER, A08Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A08: " + A08Trans);
			
			
			//Se obtiene una instancia de DocumentBuilder
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(false);
			docBuilder = fact.newDocumentBuilder();
			
			log.trace("Instancia de DocumentBuilder: " + docBuilder);
		
		}catch(Exception ex){
			throw new ExceptionInInitializerError(ex);			
		}
	
	}
	
	/**
	 * Elimina el namespace del mensaje. 
	 */
	protected Source dropNameSpace(String xmlMsg) throws IllegalArgumentException{
		
		try{
			
			log.debug("Inicio borrado de namespaces");
					
			byte[] msgBytes = xmlMsg.getBytes("UTF8");		
			ByteArrayInputStream bais = new ByteArrayInputStream(msgBytes);		
			
			Document doc = docBuilder.parse(bais);
			Element root = doc.getDocumentElement();	
			root.removeAttribute("xmlns");
		
			log.debug("Fin borrado de namespaces");
			
			return new DOMSource(doc);
		
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}
		
	}	
	
	private static URIResolver getHIEURIResolver(){
		
		return new URIResolver(){
			public Source resolve (String href, String base){
				
				InputStream hrefIs = AbstractHL7SiliconConverter.class.getResourceAsStream(href);		
				return new StreamSource(hrefIs);			

			}
				
		};
		
	}	
	
	public static void main(String[] args) throws Exception{
		
		String resource = AbstractHL7SiliconConverter.class.getResource("dae.hl7_adt_a01.xsl").toString();
		
		//String resource = Thread.currentThread().getContextClassLoader().getResource(
				//"hl7_adt_a01.xsl").toString();
		
		//InputStream resource = Class.class.getResourceAsStream("/xslt/hl7_adt_a01.xsl");
		System.out.println("Resource: " + resource);
		
	}
	
	

}
