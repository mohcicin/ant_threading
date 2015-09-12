package threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import metier.imetier;
import metier.metierimpl;
import models.BestFound;
import utils.SerializeObject;
import view.MyViewer;
import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

public class TCalculator extends Thread{

	private MyViewer mavue;
	private boolean go = false;

	private List<TAnt_Agent> agents = new ArrayList<TAnt_Agent>();

	private static int NUM = 0;
	private static int NUM2 = 1;
	private int nbr =0;

	private double aph;
	private double bt;
	private double ru0;
	private double mu0;
	private double nbrant; 

	private imetier dao;
	private Graph gr;

	private static Ant myant;


	private static List<Ant> recievs_ants = new ArrayList<Ant>();

	HashMap<String, City> mycity = new HashMap<String, City>();

	//HashMap<Integer, HashMap<Ant, List<Arc>>> bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
	//private static HashMap<Ant, List<Arc>> dt = new HashMap<Ant, List<Arc>>();

	int ct ;
	Ant[] ant ;

	//private ACLMessage msg1;

	public TCalculator(MyViewer v,Graph g,List<TAnt_Agent> in){
		this.mavue = v;
		this.gr = g;
		this.agents = in;
		
		dao = new metierimpl();


		recievs_ants = new ArrayList<Ant>();


		mycity = new HashMap<String, City>();

		//bestFound = new HashMap<Integer, HashMap<Ant,List<Arc>>>();
		//dt = new HashMap<Ant, List<Arc>>();

		ct = gr.getCities().size();
		ant = new Ant[ct];

		for (int i = 0; i < ct; i++) {
			ant[i] = new Ant("cicinant"+i, gr,gr.getCities().get(i),dao);
			//gr.getAnts().add(ant[i]);
			mycity.put(ant[i].getName(), gr.getCities().get(i));
		}

		go = true;


	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();


		while(true){
			try {
				if(go){
					try {
						System.out.println("Send data2 to agents");
						for (int i = 0; i < agents.size(); i++) {
							ACLMessage msgout = new ACLMessage(ACLMessage.INFORM);
							msgout.addReceiver(new AID(agents.get(i).getName().split("@")[0], AID.ISLOCALNAME));
							msgout.setContent("OK");
							send(msgout);
						}

						go = false;
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(">>EROR1 Eror confirm in calculator "+e.getMessage());
					}	
				}

				cleangarbage();

				MessageTemplate tm= MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

				ACLMessage msgin=receive(tm);

				if(msgin != null){
					NUM2++;


					if(msgin.getContent().toString().equals("ok")){
						//recievs_ants.add((Ant)msgin.getContentObject()); --new serializable
						++nbr;
						//recievs_ants.add(SerializeObject.DeserializableObjectAnt(msgin.getSender().getName().split("@")[0]+".ant"));

						System.out.println(NUM2+" Recieve PERPOSE FROM ants  "+recievs_ants.size()+" # "+gr.getCities().size()+" << "+new Date());
						//if(recievs_ants.size() == gr.getCities().size()){
						try {
							if(nbr == agents.size()){

								for (int i = 0; i < agents.size(); i++) {
									recievs_ants.add(SerializeObject.DeserializableObjectAnt(agents.get(i).getName().split("@")[0]+".ant"));
								}


								gr.setAnts(new ArrayList<Ant>());
								//gr.getAnts().clear(); 

								for (int i = 0; i < recievs_ants.size(); i++) {
									gr.getAnts().add(recievs_ants.get(i));
								}

								myant = dao.CalculBestIteration(recievs_ants);
								//System.out.println(">> best itant "+myant.getArcs().toString());
								//SerializeObject.serializableObjectBest(myant,"best.txt");

								if(myant != null){
									SerializeObject.serializableObjectBestAnt(new BestFound(myant.getName()+"_"+myant.getStart().getName(), 0, myant.getArcs()), "bestants/best_"+NUM+".txt");
								}

								//bestFound.put(NUM, dt);

								gr = dao.updateGlobalPheromone(gr);

								ACLMessage  msg1=new ACLMessage(ACLMessage.REQUEST);

								for (int j = 0; j < agents.size(); j++) {
									msg1.addReceiver(new AID(agents.get(j).getName().split("@")[0], AID.ISLOCALNAME));
								}

								//msg1.setContentObject((Serializable) gr);
								msg1.setContent("ok");
								SerializeObject.serializableObjectGraph(gr, "calculator.ant");
								send(msg1);

								/** init ants ***/

								System.out.println("SEND request with End iteration "+NUM);
								NUM++;

								recievs_ants = new ArrayList<Ant>();//.clear();
								//dt.clear(); // = new HashMap<Ant, List<Arc>>();

								nbr=0;
							}
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("Exception Calculator PERPOSE "+e.getMessage());
						}
						//}
					}


					if(NUM == 5){

						try {
							List<Ant> bestAnt = new ArrayList<Ant>();
							//SerializeObject.DeserializableObjectBest("best.txt");

							for (int i = 0; i < NUM; i++) {
								bestAnt.add(SerializeObject.DeserializableObjectBestAnt("bestants/best_"+i+".txt"));
							}

							System.out.println(">>> best ant from file "+bestAnt.toString());
							/*
							 for (int i = 0; i < bestAnt.size(); i++) {
								dt.put(bestAnt.get(i), bestAnt.get(i).getArcs());
							}
							 */

							//bestFound.put(0, dao.CalculBestIteration(bestAnt).getArcs());

							HashMap<Integer, List<Arc>> rs = new HashMap<Integer, List<Arc>>();
							rs.put(0, dao.CalculBestIteration(bestAnt).getArcs());
							mavue.getjPanel2().setData(rs);//dao.calculBestTour(bestFound)
							mavue.getjPanel2().repaint();

							for (int i = 0; i < agents.size(); i++) {
								agents.get(i).suspend();
							}

							doSuspend();


							//System.exit(0);

						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("error in draw "+e.getMessage());
						}
					}
				}else{
					System.out.println("Calculator in waiting msg block perpose calc out");
					block();
				}



			} catch (Exception e) {
				// TODO: handle exception
				e.getStackTrace();
				System.out.println(">> Eror Propose in calculator "+e);
			}





		}

	}

	public void doDelete() {
		// TODO Auto-generated method stub
		super.doDelete();
	}

	private void cleangarbage(){
		long minRunningMemory = (1024*1024);

		Runtime runtime = Runtime.getRuntime();

		System.out.println("******** memory "+runtime.totalMemory() +"/" + runtime.freeMemory());
		runtime.gc();
		System.gc();
	}
}
