package agents;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;



public class CopyOfCalculator extends GuiAgent{

	private MyViewer mavue;
	private AgentContainer ac;
	
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
				mycity = new HashMap<String, City>();
				
				bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
				dt = new HashMap<Ant, List<Arc>>();
				
				ct = gr.getCities().size();
				ant = new Ant[ct];
				
				System.out.println(">>> queue "+CopyOfCalculator.this.createMessageQueue().getMaxSize());
				
				for (int i = 0; i < ct; i++) {
					ant[i] = new Ant("cicinant"+i, gr,gr.getCities().get(i),dao);
					gr.getAnts().add(ant[i]);
					mycity.put(ant[i].getName(), gr.getCities().get(i));
					
					AgentController Agent = this.ac.createNewAgent(ant[i].getName().replaceAll(" ", ""), "agents.Ant_agent", new Object[]{ant[i]});
					Agent.start();
					
					agents.add(Agent);
				}
				
				go = true;
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("error >>  "+e.getMessage());
			}
			break;

		case 1 :
			try {
				
				System.out.println("hopa out");
				
				for (int i = 0; i < agents.size(); i++) {
					agents.get(i).kill();
				}
				
				agents = new ArrayList<AgentController>();
				
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("kill "+e.getMessage());
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		dao = new metierimpl();
		
		//this.mavue = new MyViewer();
		this.mavue.setVisible(true);
		
		Object[] parms=getArguments();
		this.ac = (AgentContainer) parms[0];
		
		recievs_ants = new ArrayList<Ant>();
		dt = new HashMap<Ant, List<Arc>>();
		
		System.out.println(">> start up");
		
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
		
	}
	
	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		super.doDelete();
	}

}
