package threads;

import java.util.ArrayList;
import java.util.List;

import plateforme.AntContoneur;
import metier.imetier;
import metier.metierimpl;
import view.MyViewer;
import entite.Ant;
import entite.Graph;

public class TViewer extends Thread{

	private MyViewer mavue;
	
	private List<Double> db = new ArrayList<Double>();
	
	private double aph;
	private double bt;
	private double ru0;
	private double mu0;
	private double nbrant; 
	
	private imetier dao;
	private Graph gr;
	
	public TViewer(MyViewer v){
		this.mavue = v;
		this.mavue.setVisible(true);
		this.dao = new metierimpl();
		this.gr = new Graph();
		
		 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
		
		
		try {
			
			
			//db = (List<Double>)ev.getParameter(0);
			//System.out.println("sended data "+db.toString());
			aph = 0.1; //db.get(0);
			bt  = 0.1; // db.get(1);
			ru0 =  0.1; //db.get(2);
			mu0 = 0.1; // db.get(3);
			nbrant = 2; // db.get(4);
			
			
			
			gr = dao.initGraph(aph, bt, ru0, mu0,this.mavue.getjPanel2().getCentre());
			
			Ant ant[];
			ant = new Ant[gr.getCities().size()];
			
			List<TAnt_Agent> lsag = new ArrayList<TAnt_Agent>();
			
			
			TCalculator calc = new TCalculator(mavue, gr, lsag);
			calc.start();
			
			
			
			for (int i = 0; i < gr.getCities().size(); i++) {
				//gr.getCities().get(i).setSuccesseur(dao.load_seccusseur_new(gr.getCities().get(i), gr));
				ant[i] = new Ant("cicinant"+i, gr,gr.getCities().get(i),dao);
				gr.getAnts().add(ant[i]);
				
				TAnt_Agent ant_agent = new TAnt_Agent(ant[i],i,calc);
				lsag.add(ant_agent);
				ant_agent.start();
				
			}
			
		
			/*
			for (int i = 0; i < gr.getCities().size(); i++) {
				System.out.println("****************** GR City "+gr.getCities().get(i).getName()+" *** "+gr.getCities().get(i).getSuccesseur());
			}
			*/
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error ag>>  "+e.getMessage());
		}
	}
}
