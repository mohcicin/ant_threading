package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

import metier.imetier;
import metier.metierimpl;

public class MyViewer1 extends JFrame{

	private MyPanel my;
	private JButton calculate;
	private imetier dao;
	private Graph gr;
	
	public MyViewer1(String tt){
		try {
			this.setTitle(tt);
			this.setSize(800,500);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			dao = new metierimpl();
			
			//my = new MyPanel(this);
			//my.setMyview(this);
			calculate = new JButton("Start calcul");
			calculate.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					startAction();
					//System.exit(0);
				}
			});
			//this.add(my);
			this.add(monpanel());
			this.setVisible(true);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(">>> "+e.getMessage());
		}
	}
	
	public JPanel monpanel(){
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(2, 1));
		
		JPanel p1 = new JPanel();
		p1.setPreferredSize(new Dimension(50, 50));
		p1.add(calculate);
		
		pan.add(p1);
		pan.add(my);
		return pan;
	}
	
	public void startAction(){
		System.out.println("in viewer >> "+my.getCentre().size());
		gr = dao.initGraph(0.1, 0.1, 0.1, 0.1,my.getCentre());
		HashMap<String, City> mycity = new HashMap<String, City>();
		
		HashMap<Integer, HashMap<Ant, List<Arc>>> bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
		HashMap<Ant, List<Arc>> dt = new HashMap<Ant, List<Arc>>();
		
		Ant[] ant = new Ant[gr.getCities().size()];
		
		for (int i = 0; i < gr.getCities().size(); i++) {
			ant[i] = new Ant("cicin ant "+i, gr,gr.getCities().get(i),dao);
			gr.getAnts().add(ant[i]);
			mycity.put(ant[i].getName(), gr.getCities().get(i));
		}
		
		//gr.getAnts().add(ant[0]);
		try {
			
			for (int j = 0; j < 10; j++) {
				
				System.out.println("Start iteration "+j);
				dt = new HashMap<Ant, List<Arc>>();
				for (int i = 0; i < ant.length; i++) {
					ant[i].findTour();
					//dt = new HashMap<Ant, List<Arc>>();
					//System.out.println("begin <<< "+i +" << "+ant[i].getName());
					dt.put(ant[i], ant[i].getArcs());
				}
				
				
				bestFound.put(j, dt);
				
				
				dao.updateGlobalPheromone(gr);
				
				File file = new File("iteration_"+j+".txt");
		        FileWriter fw = new FileWriter(file);
		        PrintWriter pw = new PrintWriter(fw);
		        
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
			
			my.setData(dao.calculBestTour(bestFound));
			my.repaint();
			
		
	        //output.close();
		} catch (Exception e2) {
			// TODO: handle exception
			System.out.println(">>> "+e2.getMessage());
		}
		
	}
	
}
