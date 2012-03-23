package org.delfos.task;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * The class <code>FileGarbageCollector</code> remove all files older than the specified age
 * from the specified directories list.
 * 
 * @author seamlg
 */
public class FileGarbageCollector extends TimerTask {
	
	private static final Logger log = Logger.getLogger(FileGarbageCollector.class);
	
	/**
	 * One second.
	 */
	public static final long ONE_SECOND = 1000;
	
	/**
	 * One minute.
	 */
	public static final long ONE_MINUTE = 60*ONE_SECOND;
	
	/**
	 * One hour.
	 */
	public static final long ONE_HOUR = 60*ONE_MINUTE;
	
	/**
	 * One day. 
	 */
	public static final long ONE_DAY = 24*ONE_HOUR;
	
	/**
	 * One week.
	 */
	public static final long ONE_WEEK = 7*ONE_DAY;
	
	/**
	 * One month.
	 */
	public static final long ONE_MONTH = 30*ONE_DAY;
	
	/**
	 * One year.
	 */
	public static final long ONE_YEAR = 365*ONE_DAY;	

	/**
	 * List of directories
	 */
	private Vector<File> dirs;
	
	/**
	 * Maximum age of the files
	 */
	private long maxAge;
	
	/**
	 * Create a new instance of the class <code>FileGarbageCollector</code> with 
	 * <code>maxAge</code> = 0 and an empty directories list.
	 */
	public FileGarbageCollector(){
		this.maxAge = 0;
		this.dirs = new Vector<File>();
	}
	
	/**
	 * Create a new instance of the class <code>FileGarbageCollector</code> with the 
	 * specifies <code>maxAge</code> and directories list.
	 * 
	 * @param maxAge
	 * @param dirs
	 */
	public FileGarbageCollector(long maxAge, Vector<File> dirs){
		this.maxAge = maxAge;
		this.dirs = dirs;
	}
	
	public Vector<File> getDirs() {
		return dirs;
	}

	public void setDirs(Vector<File> dirs) {
		this.dirs = dirs;
	}

	public long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}
	
	public void addDir(File dir){
		this.dirs.add(dir);		
	}

	/**
	 * Delete all files older than the specified <code>maxAge</code> from the directories 
	 *  specified in the list <code>dirs</code>.
	 */
	@Override
	public void run() {
		
		log.debug("Throw method run.");
		
		if (this.dirs == null || this.maxAge < 0)
			return;
		
		Enumeration<File> dirsEnum = this.dirs.elements();
		
		while(dirsEnum.hasMoreElements()){
			
			File dir = dirsEnum.nextElement();
			
			try{
				this.deleteFilesIfOlderMaxAge(dir);
			}catch(IOException ex){
				log.warn("Impossible to delete file " + (dir).getAbsolutePath() + ". " + 
						ex.getMessage());
			}
		
		}

	}
	
	private void deleteFilesIfOlderMaxAge(File dir) throws IOException{
		
		File[] files = dir.listFiles();
		
		for(File file : files){
			
			if(FileUtils.isFileOlder(file, this.scheduledExecutionTime() - this.maxAge)){			
				FileUtils.forceDelete(file);
				log.debug("Deleted file: " + file.getAbsolutePath());
			}
			
		}		

		
	}

}
