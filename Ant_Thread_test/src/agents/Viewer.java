package agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import plateforme.AntContoneur;

import metier.imetier;
import metier.metierimpl;

import view.MyViewer;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Viewer extends GuiAgent{

	private MyViewer mavue;
	private AgentContainer ac;
	
	private List<Double> db = new ArrayList<Double>();
	
	private double aph;
	private double bt;
	private double ru0;
	private double mu0;
	private double nbrant; 
	
	private imetier dao;
	private Graph gr;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		mavue = new MyViewer(this);
		mavue.setVisible(true);
		dao = new metierimpl();
		gr = new Graph();
		
		Object[] parms=getArguments();
		this.ac = (AgentContainer) parms[0];
		
	}

	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		System.out.println(getAID().getName()+" is kill");
		super.doDelete();
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getType()) {
		case 0:
			try {
				
				
				db = (List<Double>)ev.getParameter(0);
				System.out.println("sended data "+db.toString());
				aph = 0.1; //db.get(0);
				bt  = 0.1; // db.get(1);
				ru0 =  0.1; //db.get(2);
				mu0 = 0.1; // db.get(3);
				nbrant = 2; // db.get(4);
				
				
				
				gr = dao.initGraph(aph, bt, ru0, mu0,this.mavue.getjPanel2().getCentre());
				
				Ant ant[];
				ant = new Ant[gr.getCities().size()];
				
				List<AgentController> lsag = new ArrayList<AgentController>();
				
				
				
				for (int i = 0; i < gr.getCities().size(); i++) {
					//gr.getCities().get(i).setSuccesseur(dao.load_seccusseur_new(gr.getCities().get(i), gr));
					ant[i] = new Ant("cicinant"+i, gr,gr.getCities().get(i),dao);
					gr.getAnts().add(ant[i]);
					
					lsag.add(AntContoneur.getMe().startNewConteneur(ant[i].getName().replaceAll(" ", ""), "agents.Ant_agent", new Object[]{ant[i]}));
				}
				
				AntContoneur.getMe().startNewConteneur("SuperAgent", "agents.Calculator", new Object[]{mavue,gr,lsag});
				
				/*
				for (int i = 0; i < gr.getCities().size(); i++) {
					System.out.println("****************** GR City "+gr.getCities().get(i).getName()+" *** "+gr.getCities().get(i).getSuccesseur());
				}
				*/
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error ag>>  "+e.getMessage());
			}
			break;

		case 1 :
			try {
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("kill "+e.getMessage());
			}
			break;
		default:
			break;
		}
	}
	
	
}
