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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.delfos.mirth.hie.dao.Bed;
import org.delfos.mirth.hie.dao.JdbcBedDao;
import org.delfos.mirth.hie.dao.JdbcNurseUnitDao;
import org.delfos.mirth.hie.dao.JdbcServiceDao;
import org.delfos.mirth.hie.dao.NurseUnit;
import org.delfos.mirth.hie.dao.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


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
	static final String A40_TRANSFORMER = "a40.transformer";
	
	protected static XPathExpression xPathExpDAEBed;
	protected static final String SILICON_BED_CODE = "silicon.bed.code";
	
	protected static XPathExpression xPathExpDAEOldBed;
	protected static final String SILICON_OLD_BED_CODE = "silicon.old.bed.code";
	
	protected static XPathExpression xPathExpNurseUnit;
	protected static final String SILICON_NURSE_UNIT = "silicon.nurse.unit";
	
	protected static XPathExpression xPathExpOldNurseUnit;
	protected static final String SILICON_OLD_NURSE_UNIT = "silicon.old.nurse.unit";
	
	protected static XPathExpression xPathService;
	protected static final String SILICON_SERVICE = "silicon.service";
	
	protected static XPathExpression xPathExpAdmitType;	
	
	private static DocumentBuilder docBuilder;
	
	//Entorno para la compilación de expresiones XPath
	protected static final XPath xPath = XPathFactory.newInstance().newXPath();
	
	//Accesos a base de datos de Silicon
	private static JdbcBedDao jdbcBedDao;
	private static JdbcServiceDao jdbcServiceDao;
	private static JdbcNurseUnitDao jdbcNurseUnitDao;
	
	public static JdbcNurseUnitDao getJdbcNurseUnitDao() {
		return jdbcNurseUnitDao;
	}

	public static void setJdbcNurseUnitDao(JdbcNurseUnitDao jdbcNurseUnit) {
		AbstractHL7SiliconConverter.jdbcNurseUnitDao = jdbcNurseUnit;
	}

	public static JdbcServiceDao getJdbcServiceDao() {
		return jdbcServiceDao;
	}

	public static void setJdbcServiceDao(JdbcServiceDao jdbcServiceDao) {
		AbstractHL7SiliconConverter.jdbcServiceDao = jdbcServiceDao;
	}

	// TODO - Tener en cuenta que la codificaci�n de caracteres tiene que ser UTF-8	
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
		
		InputStream A40XsltIs = AbstractHL7SiliconConverter.class.getResourceAsStream("dae.hl7_adt_a40.xsl");
		Source A40XsltSource = new StreamSource(A40XsltIs);				

		
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
			
			Transformer A40Trans = transFact.newTransformer(A40XsltSource);
			transformers.put(A40_TRANSFORMER, A40Trans);
			log.trace("Instancia del Transformer para mensajes ADT_A40: " + A40Trans);

			//Se obtiene una instancia de DocumentBuilder
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(false);
			docBuilder = fact.newDocumentBuilder();
			
			log.trace("Instancia de DocumentBuilder: " + docBuilder);
			
			//Expresiones XPath
			xPathExpDAEBed = xPath.compile("//PV1/PV1.3/PL.3");
			xPathExpDAEOldBed = xPath.compile("//PV1/PV1.6/PL.3");
			xPathExpNurseUnit = xPath.compile("//PV1/PV1.3/PL.1");
			xPathExpOldNurseUnit = xPath.compile("//PV1/PV1.6/PL.1");
			xPathService = xPath.compile("//PV1/PV1.10");
			xPathExpAdmitType = xPath.compile("//PV1/PV1.2");
			
		
		}catch(Exception ex){
			throw new ExceptionInInitializerError(ex);			
		}
	
	}
	
	/**
	 * Obtiene el resultado de ejecutar el preprocesado, el procesado y el postprocesado.
	 * 
	 */
	public String convert(String msg) throws IllegalArgumentException{
		return postProcess(process(preProcess(msg)));
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
	
	/**
	 * Asigna los parámetros al transformador indicado.
	 * @param node
	 * @param transformer
	 * @throws XPathExpressionException
	 */
	protected void setParameters(Node node, Transformer transformer) throws XPathExpressionException{
	
		//Código de la cama de DAE
		String DAEBedCode = xPathExpDAEBed.evaluate(node);		
		log.debug("Código de cama recibido desde DAE: " + DAEBedCode);
		
		String siliconBedCode = getSiliconBedCode(DAEBedCode);
		log.debug("Correspondiente código de la cama de Silicon: " + siliconBedCode);
		
		//Código de la antigua cama de DAE
		String DAEOldBedCode = xPathExpDAEOldBed.evaluate(node);	
		log.debug("Código de la antigua cama recibido desde DAE: " + DAEOldBedCode);
		
		String siliconOldBedCode = getSiliconBedCode(DAEOldBedCode);
		log.debug("Correspondiente códigode la antigua cama de silicon: " + siliconOldBedCode);

		//Código de la unidad de enfermería de DAE
		String DAENurseUnitCode = xPathExpNurseUnit.evaluate(node);
		log.debug("Código de la unidad de enfermería recibida desde DAE: " + DAENurseUnitCode);
		
		String siliconNurseUnitCode = getSiliconNurseUnitCode(DAENurseUnitCode);
		log.debug("Correspondiente código de la unidad de enfermería de Silicon: " + siliconNurseUnitCode);
		
		//Código de la antigua unidad de enfermería de DAE
		String DAEOldNurseUnitCode = xPathExpOldNurseUnit.evaluate(node);
		log.debug("Código de la antigua unidad de enfermería recibida desde DAE: " + DAEOldNurseUnitCode);
		
		String siliconOldNurseUnitCode = getSiliconNurseUnitCode(DAEOldNurseUnitCode);
		log.debug("Correspondiente código de la antigua unidad de enfermería de Silicon: " + siliconOldNurseUnitCode);
		
		//Código del servicio
		String DAEServiceCode = xPathService.evaluate(node);
		log.debug("Código del servicio recibido desde DAE: " + DAEServiceCode);
		
		String siliconServiceCode = getSiliconServiceCode(DAEServiceCode);
		log.debug("Correspondiente código del servicio de Silicon: " + siliconServiceCode);
		
		//Asignación del parámetros al transformador xml
		//TODO - Obtener los códigos de Silicon
		transformer.setParameter(SILICON_BED_CODE, siliconBedCode);
		transformer.setParameter(SILICON_OLD_BED_CODE, siliconOldBedCode);
		transformer.setParameter(SILICON_NURSE_UNIT, siliconNurseUnitCode);
		transformer.setParameter(SILICON_OLD_NURSE_UNIT, siliconOldNurseUnitCode);
		transformer.setParameter(SILICON_SERVICE, siliconServiceCode);
		
	}
	
	private static URIResolver getHIEURIResolver(){
		
		return new URIResolver(){
			public Source resolve (String href, String base){
				
				InputStream hrefIs = AbstractHL7SiliconConverter.class.getResourceAsStream(href);		
				return new StreamSource(hrefIs);			

			}
				
		};
		
	}	
	
	//Establece el objeto de conexión a la vista de camas de Silicon
	protected static void setJdbcBedDao(JdbcBedDao dao){		
		jdbcBedDao = dao;		
	}
	
	protected static JdbcBedDao getJdbcBedDao (){
		return jdbcBedDao;
	}
	
	//Obtiene el código de cama de Silicon a partir del código de cama de DAE
	protected static String getSiliconBedCode(String daeBedCode){
		
		Bed siliconBed = jdbcBedDao.getBedByAltId(daeBedCode);
		return siliconBed.getDescription();
		
	}	
	
	protected static String getSiliconServiceCode (String daeServiceCode){
		
		Service service = jdbcServiceDao.getServiceByAltId(daeServiceCode);
		return service.getCode();
	
	}
	
	protected static String getSiliconNurseUnitCode (String daeNurseUnitCode){
		
		NurseUnit nurseUnit = jdbcNurseUnitDao.getNurseUnitByAltId(daeNurseUnitCode);
		return nurseUnit.getCode();
		
	}
	
	/**
	 * Preprocesado del mensaje.
	 * 
	 * @param msg mensaje original
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected String preProcess(String msg) throws IllegalArgumentException{
		log.debug("Mensaje. Preprocesado por defecto.");
		return msg;
	}
	
	/**
	 * Procesado del mensaje.
	 * 
	 * @param msg mensaje resultado del preprocesado.
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected String process(String msg) throws IllegalArgumentException{
		log.debug("Mensaje. Procesado por defecto.");
		return msg;
	}
	
	/**
	 * Postprocesado del mensaje.
	 * 
	 * Si el tipo de admisión del mensaje es hospital, descarta el mensaje.
	 * 
	 * @param msg mensaje resultado del procesado.
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected String postProcess(String msg) throws IllegalArgumentException{
		
		log.debug("Mensaje. Postprocesado por defecto.");
		
		if(!isValidAdmitType(msg)){
			IllegalArgumentException ex = new IllegalArgumentException("ERROR-00. El tipo de ingreso no es válido");
			log.warn(ex);
			throw ex;
		}
		
		return msg;
	}
	
	/**
	 * Devuelve <code>false</code> para los ingresos de Hospital de Día. Para el resto de ingresos devuelve 
	 * <code>true</code>. 
	 * 
	 * @param admitType
	 * @return
	 */
	protected boolean isValidAdmitType(String msg) throws IllegalArgumentException{
		
		try{
		
			log.debug("Mensaje. Validación del tipo de ingreso.");
			
			byte[] msgBytes = msg.getBytes("UTF-8");
			ByteArrayInputStream bais = new ByteArrayInputStream(msgBytes);
			Document doc = docBuilder.parse(bais);
			DOMSource src = new DOMSource(doc);
			
			String admitType = xPathExpAdmitType.evaluate(src.getNode());
			
			log.debug("Mensaje. Tipo de ingreso: " + admitType);
			
			if(admitType.equals("H")){
				return false;
			}		
			
			return true;
		
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		String resource = AbstractHL7SiliconConverter.class.getResource("dae.hl7_adt_a01.xsl").toString();
		
		//String resource = Thread.currentThread().getContextClassLoader().getResource(
				//"hl7_adt_a01.xsl").toString();
		
		//InputStream resource = Class.class.getResourceAsStream("/xslt/hl7_adt_a01.xsl");
		System.out.println("Resource: " + resource);
		
	}
	
	

}
