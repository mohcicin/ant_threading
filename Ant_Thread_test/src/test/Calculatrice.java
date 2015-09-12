package test;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import utils.SerializeObject;
import view.PointsCoordinate;
import entite.Ant;
import entite.City;
import entite.Graph;

public class Calculatrice {

	public static void main(String[] args) {
		try {
			HashMap<Long, Long> res = new HashMap<Long, Long>();
			

			res.put(7L, 1L);
			res.put(2L, 1L);
			res.put(4L, 1L);
			res.put(0L, 1L);
			res.put(6L, 1L);
			
			SortedSet<Long> keys = new TreeSet<Long>(res.keySet());
			
			//System.out.println(">> "+SerializeObject.DeserializableObjectBest("best.txt"));
			List<City> prs = new ArrayList<City>();
			prs.add(new City("c1", null));
			prs.add(new City("c2", null));
			prs.add(new City("c3", null));
			 OutputStream file = new FileOutputStream("yourFile.dat",true);
		      OutputStream buffer = new BufferedOutputStream(file);
		      ObjectOutput output = new ObjectOutputStream(buffer);
		      for(City p:prs){
		    	  output.writeObject(p);
		      }
		      output.close(); 
		      
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("yourFile.dat"));

		    Object obj = null;

		    while ((obj = inputStream.readObject()) != null) {
		        System.out.println(">> "+((City) obj).toString());
		    }
		    inputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("hola "+e.getMessage());
		}
	}
}

