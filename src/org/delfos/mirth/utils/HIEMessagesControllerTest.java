package org.delfos.mirth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.preparser.PreParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

import junit.framework.TestCase;

public class HIEMessagesControllerTest extends TestCase {
	
	private File srcDir = new File("E:/novale");
	private File hookFile = new File("E:/novale/hookdir/file.hook");
	private File procDir = new File("E:/novale/procdir");
	private File bakDir = new File("E:/novale/bak");

	@Override
	public void setUp(){
		
		/*
		try{			
			FileUtils.cleanDirectory(procDir);
			
			Iterator<File> files = FileUtils.listFiles(this.bakDir, null, false).iterator();
			
			while(files.hasNext()){
				
				FileUtils.copyFileToDirectory(files.next(), srcDir);
				
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
		*/		
		
	}
	
	/**
	 * Move files to the process directory.
	 */
	@Test
	public void testProcessMessages_C1() {
		
		MessagesController controller = new HIEMessagesController(this.srcDir.getAbsolutePath(),
				this.procDir.getAbsolutePath(),this.hookFile.getAbsolutePath(), 3);
		
		try {
			controller.processMessages();
		} catch (MessageControllerException e) {			
			super.fail(e.getMessage());
		}
		
		int numberOfFiles = FileUtils.listFiles(this.procDir, null, false).size();
		
		super.assertEquals(4, numberOfFiles);
		
	}
	
	@Test
	public void testProcessMessages_C2(){
		
		File file = new File("E:/novale/ADT0000007.ADM");
		
		try {
			FileUtils.copyFileToDirectory(file, this.procDir);
		} catch (IOException e) {
			super.fail(e.getMessage());
		}
		
		int numberOfFiles = FileUtils.listFiles(this.procDir, null, false).size();
		
		super.assertEquals(1, numberOfFiles);		
		
	}
	
	public static void main(String[] args) throws Exception{
		
		FileInputStream fis = new FileInputStream("E:/novale/ADT0000002.ADM");
		
		String[] pathSpec = {"MSH-9-2"}; 
		PreParser preParser = new PreParser();
		String[] result = preParser.getFields(IOUtils.toString(fis), pathSpec);
		
		System.out.println("Message type: " + result[0]);
		
		/*
		Parser parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		
		Terser terser = new Terser(parser.parse(IOUtils.toString(fis)));
		String type = terser.get("MSH-9-2");
		*/
		
		fis.close();
		
		//System.out.println("Message type: " + type);
		
	}

}
