package org.delfos.mirth.hie.tests;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;

import org.w3c.dom.Document;



public class MainTest {
	
	
	public static void main (String[] args) throws  Exception{
		
		File xmlFile = new File("E:/tmp/xmlfile.xml");
		File xslFile = new File("E:/tmp/xslfile.xsl");
		
		Source xmlSource = new StreamSource(xmlFile);
		Source xslSource = new StreamSource(xslFile);
		Result result = new StreamResult(System.out);
		
		TransformerFactory transFact = TransformerFactory.newInstance();
		
		Transformer trans = transFact.newTransformer(xslSource);
		trans.setParameter("apellido", "Rastrollo");
		
		trans.transform(xmlSource, result);
		
		/*
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		
		String exp = "/ADT_A02/PV1/PV1.3/PL.3";
		
		XPathExpression xPathExpression = xPath.compile(exp);
		
		FileInputStream fis = new FileInputStream("E:/tmp/1349694652647A02_01.ADM.xml");
		
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		docBuildFact.setNamespaceAware(false);
		DocumentBuilder docBuilder = docBuildFact.newDocumentBuilder();
		Document doc = docBuilder.parse(fis);
		
		String result = xPathExpression.evaluate(doc);
		
		System.out.println(result);
		*/
	}

}
