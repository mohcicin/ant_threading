package agents;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

import metier.imetier;
import metier.metierimpl;

import view.MyViewer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;



public class Calculator2 extends Agent{

	private MyViewer mavue;
	private boolean go = false;
	
	private List<AgentController> agents = new ArrayList<AgentController>();
	
	private static int NUM = 0;
	
	private double aph;
	private double bt;
	private double ru0;
	private double mu0;
	private double nbrant; 
	
	private imetier dao;
	private Graph gr;
	
	private List<Double> db = new ArrayList<Double>();
	
	private List<Ant> recievs_ants = new ArrayList<Ant>();
	
	HashMap<String, City> mycity = new HashMap<String, City>();
	
	HashMap<Integer, HashMap<Ant, List<Arc>>> bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
	HashMap<Ant, List<Arc>> dt = new HashMap<Ant, List<Arc>>();
	
	int ct ;
	Ant[] ant ;
	
	private ACLMessage msg1;
	
	

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		dao = new metierimpl();
		
		Object[] parms=getArguments();
		
		
		if(parms != null){
			if(parms.length == 3){
				this.mavue = (MyViewer) parms[0];
				
				this.gr = (Graph) parms[1];
				
				this.agents = (List<AgentController>) parms[2];
			}
		}
		
		
		recievs_ants = new ArrayList<Ant>();
		
		
		mycity = new HashMap<String, City>();
		
		bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
		dt = new HashMap<Ant, List<Arc>>();
		
		ct = gr.getCities().size();
		ant = new Ant[ct];
		
		for (int i = 0; i < ct; i++) {
			ant[i] = new Ant("cicinant"+i, gr,gr.getCities().get(i),dao);
			//gr.getAnts().add(ant[i]);
			mycity.put(ant[i].getName(), gr.getCities().get(i));
		}
		
		go = true;
		
		
		//System.out.println(">> gr "+gr.toString());
		System.out.println(">> start up calculator "+this.agents.size());
		/*
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					for (int i = 0; i < agents.size(); i++) {
						ACLMessage msgout = new ACLMessage(ACLMessage.INFORM);
						msgout.addReceiver(new AID(agents.get(i).getName().split("@")[0], AID.ISLOCALNAME));
						msgout.setContent("OK");
						send(msgout);
						System.out.println("Send data to "+agents.get(i).getName());
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">> Eror confirm in calculator "+e.getMessage());
				}
			}
		});
		*/
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				try {
					System.out.println(">>> in calcultor");
					if(go){
						try {
							for (int i = 0; i < agents.size(); i++) {
								ACLMessage msgout = new ACLMessage(ACLMessage.INFORM);
								msgout.addReceiver(new AID(agents.get(i).getName().split("@")[0], AID.ISLOCALNAME));
								msgout.setContent(""+NUM);
								send(msgout);
								System.out.println("Send data2 to "+agents.get(i).getName());
							}
							go =false;
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println(">> Eror confirm in calculator "+e.getMessage());
						}
					}
					
						MessageTemplate tm= MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

						ACLMessage msgin=receive(tm);
						
						if(msgin != null){
							System.out.println(">PErpose>> "+msgin.getContent());
							NUM++;
							
							
							msg1=new ACLMessage(ACLMessage.REQUEST);
							
							for (int j = 0; j < agents.size(); j++) {
								msg1.addReceiver(new AID(agents.get(j).getName().split("@")[0], AID.ISLOCALNAME));
							}
							
							msg1.setContent(""+NUM);
							send(msg1);
							
						}else{
							System.out.println("msg block perpose calc");
							block();
						}
						
						
						
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">> Eror Propose in calculator "+e.getMessage());
				}
				
				
				
				
				
				
			}
		});
		/*
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					
					if(agents != null  && gr != null){
						
						for (int i = 0; i < agents.size(); i++) {
							
							MessageTemplate tm=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchSender(new AID(agents.get(i).getName().split("@")[0], AID.ISLOCALNAME)));

							//tm = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchSender(new AID(agents.get(i).getName().split("@")[0], AID.ISLOCALNAME)));
							//MessageTemplate.MatchPerformative(ACLMessage.INFORM);
							
							ACLMessage msg=receive(tm);
							
							
							if(msg != null){
								System.out.println("msg recu "+msg.getSender().getLocalName());
								recievs_ants.add((Ant)msg.getContentObject());
							}else{
								
								System.out.println("msg block");
								block();
							}
						}
						
						if(recievs_ants.size() == gr.getCities().size()){
							if(recievs_ants.size() == agents.size()){
								
								gr.setAnts(new ArrayList<Ant>());
								
								for (int i = 0; i < recievs_ants.size(); i++) {
									
									dt.put(recievs_ants.get(i), recievs_ants.get(i).getArcs());
									
									gr.getAnts().add(recievs_ants.get(i));
								}
								
								
								bestFound.put(NUM, dt);
								
								dao.updateGlobalPheromone(gr);
								
								msg1=new ACLMessage(ACLMessage.PROPOSE);
								
								for (int j = 0; j < agents.size(); j++) {
									msg1.addReceiver(new AID(agents.get(j).getName().split("@")[0], AID.ISLOCALNAME));
								}
								
								msg1.setContentObject((Serializable) gr);
								send(msg1);
								
								System.out.println("End iteration "+NUM);
								NUM++;
								
								recievs_ants = new ArrayList<Ant>();
								dt = new HashMap<Ant, List<Arc>>();
								
							}
						}
					}else{
						System.out.println("wlo agents ni gr");
						block();
					}
					
					if(NUM > 11){
						
						mavue.getjPanel2().setData(dao.calculBestTour(bestFound));
						mavue.getjPanel2().repaint();
						
						for (int i = 0; i < agents.size(); i++) {
							agents.get(i).kill();
						}
						
						doDelete();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">> sup calulator "+e.getMessage());
				}
			}
		});
		*/
	}
	
	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		super.doDelete();
	}

}
