package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import utils.probability;

import metier.imetier;
import metier.metierimpl;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

public class application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		imetier dao = new metierimpl();
		Graph gr = dao.initGraph(0.1, 0.1, 0.1, 0.1,null);
		HashMap<String, City> mycity = new HashMap<String, City>();
        
		Ant[] ant = new Ant[gr.getCities().size()];
		
		for (int i = 0; i < gr.getCities().size(); i++) {
			ant[i] = new Ant("cicin ant "+i, gr,gr.getCities().get(i),dao);
			gr.getAnts().add(ant[i]);
			mycity.put(ant[i].getName(), gr.getCities().get(i));
		}
		
		//gr.getAnts().add(ant[0]);
		double n=0;
		Random rn = new Random();
		//Ant me = new Ant("cici", gr, gr.getCities().get(0), dao);
		//gr.getAnts().add(me);
		try {
			//File file = new File("example.txt");
	        //BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for (int j = 0; j < 10; j++) {
				
				System.out.println("Start iteration "+j);
					
				//output.write(">>>>>>>>>>>>>>>>>>>>>>< iteration "+j+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
				/*
				me.findTour();
0	00000000000000000000000000!			dao.updateGlobalPheromone(gr);
				me.initAnt();
				me.setActuel(gr.getCities().get(0));
				*/
				
				for (int i = 0; i < ant.length; i++) {
					ant[i].findTour();
				}
				
				dao.updateGlobalPheromone(gr);
				
				File file = new File("iteration_"+j+".txt");
		        FileWriter fw = new FileWriter(file);
		        PrintWriter pw = new PrintWriter(fw);
		        
		        /*
		        for (int i = 0; i < gr.getArcs().size(); i++) {
					pw.println(gr.getArcs().get(i).getSrc().getName() +" \\ "+gr.getArcs().get(i).getDest().getName()+" >> "+gr.getArcs().get(i).getPheromone());
				}
		        */
		        List<List<Arc>> md = new ArrayList<List<Arc>>();
		        
				for (int i = 0; i < ant.length; i++) {
					pw.println("Best length "+((Ant)ant[i]).getLongeur());
					pw.println("Traget "+((Ant)ant[i]).getArcs());
					ant[i].initAnt();
					ant[i].setActuel(mycity.get(ant[i].getName()));
					md.add(ant[i].getArcs());
				}
				pw.close();
				
				gr.getBestTours().put(j, md);
				System.out.println("End iteration "+j);
				
				//output.write(">>>>>>>>>>>>>>>>>>>>>> fin iteration "+j+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n");
			}
	        //output.close();
		} catch (Exception e2) {
			// TODO: handle exception
			System.out.println(">>> "+e2.getMessage());
		}
		
	}

}
