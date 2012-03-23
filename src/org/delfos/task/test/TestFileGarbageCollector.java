package org.delfos.task.test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.delfos.task.FileGarbageCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestFileGarbageCollector extends TestCase {

	private Hashtable<String, File> dirs = new Hashtable<String, File>(3);
	
	public static final String EMPTY_DIR = "empty.dir";
	public static final String DIR1 = "dir1";
	public static final String DIR2 = "dir2";	
	
	public void addDir(String key, File dir){
		
		this.dirs.put(key, dir);
		
	}	
	
	public void setDirs(Hashtable<String, File> dirs){
		this.dirs = dirs;
	}
	
	/**
	 * The <code>maxAge</coded> parameter is less than 0.	
	 */
	public void testRun_C1(){
		
		long maxAge = -1;
		
		int emptyDirInitFiles = this.dirs.get(EMPTY_DIR).listFiles().length;
		int dir1InitFiles = this.dirs.get(DIR1).listFiles().length;
		int dir2InitFiles = this.dirs.get(DIR2).listFiles().length;
		
		Vector<File> dirs = new Vector<File>(this.dirs.values());		
		FileGarbageCollector fgc = new FileGarbageCollector(maxAge, dirs);
		fgc.run();
		
		int emptyDirEndFiles = this.dirs.get(EMPTY_DIR).listFiles().length;
		int dir1EndFiles = this.dirs.get(DIR1).listFiles().length;
		int dir2EndFiles = this.dirs.get(DIR2).listFiles().length;
		
		super.assertEquals(emptyDirInitFiles, emptyDirEndFiles);
		super.assertEquals(dir1InitFiles, dir1EndFiles);
		super.assertEquals(dir2InitFiles, dir2EndFiles);
		
	}
	
	/**
	 * The directories list is null
	 */
	public void testRun_C2(){
		
		GregorianCalendar now = new GregorianCalendar();
		FileGarbageCollector fgc = new FileGarbageCollector(now.getTimeInMillis(), null);
		fgc.run();
		
	}
	
	/**
	 * The <code>maxAge</code> parameter is less than 0 and the directories list is null.
	 */
	public void testRun_C3(){
		
		GregorianCalendar now = new GregorianCalendar();
		FileGarbageCollector fgc = new FileGarbageCollector(now.getTimeInMillis(), null);
		fgc.run();
		
	}
	
	/**
	 * The directories list is empty.
	 */
	public void testRun_C4(){
		
		FileGarbageCollector fgc = new FileGarbageCollector( new GregorianCalendar().getTimeInMillis(),
			new Vector<File>());		
		fgc.run();
		
	}
	
	/**
	 * There are no files in the directories.
	 */
	public void testRun_C5(){
		
		int emptyDirInitFiles = this.dirs.get(EMPTY_DIR).listFiles().length;
		
		FileGarbageCollector fgc = new FileGarbageCollector();
		fgc.setMaxAge( new GregorianCalendar().getTimeInMillis() );
		fgc.addDir( this.dirs.get(EMPTY_DIR) );
		
		fgc.run();
		
		int emptyDirEndFiles = this.dirs.get(EMPTY_DIR).listFiles().length;
		
		assertEquals(emptyDirInitFiles, emptyDirEndFiles);
		assertEquals(0, emptyDirEndFiles);
		
	}
	
	/**
	 * There are files in the directories.
	 */
	public void testRun_C6() throws Exception{
		
		FileGarbageCollector fgc = new FileGarbageCollector();		
		fgc.setDirs( new Vector<File>(this.dirs.values()) );
		
		GregorianCalendar now = new GregorianCalendar();
		
		//Delete files older than a year
		GregorianCalendar aYearAgo = new GregorianCalendar();
		aYearAgo.setTimeInMillis( now.getTimeInMillis() - FileGarbageCollector.ONE_YEAR);
		fgc.setMaxAge(FileGarbageCollector.ONE_YEAR);
		
		Timer timer = new Timer();
		timer.schedule(fgc, new Date());
		//fgc.run();
		Thread.currentThread().sleep(5000);		
		
		
		File[] dir1Files = this.dirs.get(DIR1).listFiles();
		
		for(File file : dir1Files){
			assertFalse( FileUtils.isFileOlder(file, aYearAgo.getTimeInMillis()) );
		}
		
		File[] dir2Files = this.dirs.get(DIR2).listFiles();
		
		for(File file : dir1Files){
			assertFalse( FileUtils.isFileOlder(file, aYearAgo.getTimeInMillis()) );
		}
		
		//Delete files older than one hour
		fgc = new FileGarbageCollector();		
		fgc.setDirs( new Vector<File>(this.dirs.values()) );
		
		GregorianCalendar anHourAgo = new GregorianCalendar();
		anHourAgo.setTimeInMillis( now.getTimeInMillis() - FileGarbageCollector.ONE_HOUR);
		fgc.setMaxAge(FileGarbageCollector.ONE_HOUR);
		
		timer = new Timer();
		timer.schedule(fgc, new Date());
		//fgc.run();
		Thread.currentThread().sleep(5000);
		
		dir1Files = this.dirs.get(DIR1).listFiles();
		
		for(File file : dir1Files){
			assertFalse( FileUtils.isFileOlder(file, anHourAgo.getTimeInMillis()) );
		}
		
		dir2Files = this.dirs.get(DIR2).listFiles();
		
		for(File file : dir1Files){
			assertFalse( FileUtils.isFileOlder(file, anHourAgo.getTimeInMillis()) );
		}
		
		//Delete all files
		fgc = new FileGarbageCollector();		
		fgc.setDirs( new Vector<File>(this.dirs.values()) );
		fgc.setMaxAge(0);
		
		timer.schedule(fgc, new Date());
		//fgc.run();
		Thread.currentThread().sleep(5000);
		timer.purge();
		
		dir1Files = this.dirs.get(DIR1).listFiles();		
		assertTrue(dir1Files.length == 0);
		
		dir2Files = this.dirs.get(DIR2).listFiles();		
		assertTrue(dir2Files.length == 0);
	
	}
	
	@Override
	protected void setUp() throws Exception {

		super.setUp();
		
		this.addDir(EMPTY_DIR, new File("E:/novale/empty"));
		this.addDir(DIR1, new File("E:/novale/dir1"));
		this.addDir(DIR2, new File("E:/novale/dir2"));

		this.createDirs();
		
		File dir1 = this.dirs.get(this.DIR1);
		this.createFiles(dir1);
		
		File dir2 = this.dirs.get(this.DIR2);
		this.createFiles(dir2);
		
	}



	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
		
		//this.deleteDirs();
	}




	private void createDirs() throws IOException{
		
		Enumeration<File> dirs = this.dirs.elements();
		
		while(dirs.hasMoreElements()){
			FileUtils.forceMkdir(dirs.nextElement());			
		}
		
	}
	
	private void deleteDirs() throws IOException{
	
		Enumeration<File> dirs = this.dirs.elements();
		
		while(dirs.hasMoreElements()){
			FileUtils.deleteDirectory(dirs.nextElement());
		}
		
	}
	
	private void createFiles(File dir) throws IOException{
		
		GregorianCalendar now = new GregorianCalendar();
		
		GregorianCalendar aYearBefore = new GregorianCalendar();
		aYearBefore.setTimeInMillis( now.getTimeInMillis() - 2*FileGarbageCollector.ONE_YEAR );		
		GregorianCalendar anHourBefore = new GregorianCalendar();
		anHourBefore.setTimeInMillis( now.getTimeInMillis() - 2*FileGarbageCollector.ONE_HOUR );
		
		String[] currentFiles = new String[]{"file0", "file1"};
		String[] anHourAgoFiles = new String[]{"file2", "file3"};
		String[] aYearAgoFiles = new String[]{"file4", "file5"};
		
		for(String fileName : currentFiles){	
			
			File file = new File(dir, fileName);	
			FileUtils.touch(file);
			
		}
		
		for(String fileName: anHourAgoFiles){
			
			File file = new File(dir, fileName);			
			FileUtils.touch(file);
			file.setLastModified(anHourBefore.getTimeInMillis());
			
		}
		
		for(String fileName: aYearAgoFiles){
			
			File file = new File(dir, fileName);			
			FileUtils.touch(file);			
			file.setLastModified(aYearBefore.getTimeInMillis());
			
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		PropertyConfigurator.configure("E:/Grifols/projects/java/mirth/mirth_utils/conf/log4j.properties");		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/org/seamlg/mirth/hie/spring_mirth_hie.xml");
		
	}
	 
	
}
