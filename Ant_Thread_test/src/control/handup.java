package control;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import plateforme.contoneur;
import plateforme.main;
import threads.TViewer;
import entite.Ant;

public class handup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ClassLoader classLoader = Ant.class.getClassLoader();
		    File classpathRoot = new File(classLoader.getResource("").getPath());
			File root = new File(classpathRoot.getParent());
			File[] listOfFiles = root.listFiles();

			for (File file : listOfFiles) {
			       if(FilenameUtils.getExtension(file.getName()).equals("txt") || 
			    	  FilenameUtils.getExtension(file.getName()).equals("ant") ){
			    	   if(file.isFile()){
			    		   file.delete();
			    	   }else{
			    		  // FileUtils.deleteDirectory(file);
			    	   }
			       }
			       
			       if(file.getName().equals("bestants")){
			    	  File[] tmp = file.listFiles();  
			    	  for (int i = 0; i < tmp.length; i++) {
						tmp[i].delete();
					}
			       }
			}
			
			new TViewer(v)
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
