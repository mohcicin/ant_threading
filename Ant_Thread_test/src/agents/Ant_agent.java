package agents;

import java.io.Serializable;
import java.util.Date;

import utils.SerializeObject;

import entite.Ant;
import entite.Graph;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Ant_agent extends Agent{

	private Ant myant;
	
	private int go;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		Object[] obj = getArguments();
		myant = (Ant) obj[0];
		SerializeObject.serializableObjectAnt(myant, "ant_agent"+getAID().getLocalName()+".ant");
		
		System.out.println("Bonsoir je suis l'agent ANT  "+getAID().getLocalName()+" >> "+getAID().getName());
		System.out.println(">>> "+myant.toString());
		//*System.out.println(">>> statrt ant "+this.getName()); //myant.toString());
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					//*myant.findTour();
					
					cleangarbage();
					
					MessageTemplate tm1=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchSender(new AID("SuperAgent", AID.ISLOCALNAME)));
					MessageTemplate tm2=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchSender(new AID("SuperAgent", AID.ISLOCALNAME)));
					
					
					MessageTemplate tm = MessageTemplate.or(tm1, tm2);
					ACLMessage msg=receive(tm);
					
					

					if(msg != null){
						if(msg.getPerformative() == ACLMessage.INFORM){
							if(msg.getContent().equals("OK")){
								System.out.println("ANT in INFORM REcieve INFORM From "+getName());
								try {
									myant = SerializeObject.DeserializableObjectAnt("ant_agent"+getAID().getLocalName()+".ant");
									myant.initAnt();
									myant.findTour();
									
									SerializeObject.serializableObjectAnt(myant, getAID().getLocalName()+".ant");

									ACLMessage msg1=new ACLMessage(ACLMessage.PROPOSE);
									msg1.addReceiver(new AID("SuperAgent", AID.ISLOCALNAME));
									
									//msg1.setContentObject((Serializable) myant);
									msg1.setContent("ok");
									send(msg1);
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println("Exception Ant Inform "+e.getMessage());
								}
								
								System.out.println("ANT in INFORM send PERPOSE To  "+getName());
							}
						}else if(msg.getPerformative() == ACLMessage.REQUEST){
							System.out.println("Ant REQUEST Receive REQUEST FROM "+getName());
							
							//myant.setG((Graph) msg.getContentObject()); -- desrialize object
							//if(msg.getContent().toString().equals("ok")){
								try {
									myant = SerializeObject.DeserializableObjectAnt("ant_agent"+getAID().getLocalName()+".ant");
									myant.setG(SerializeObject.DeserializableObjectGraph("calculator.ant"));
									myant.initAnt();
									myant.findTour();

									SerializeObject.serializableObjectAnt(myant, getAID().getLocalName()+".ant");
									
									ACLMessage msg1=new ACLMessage(ACLMessage.PROPOSE);
									msg1.addReceiver(new AID("SuperAgent", AID.ISLOCALNAME));

									//msg1.setContentObject((Serializable) myant);
									msg1.setContent("ok");
									send(msg1);
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println("Exception ant REQUEST "+e.getMessage());
								}
								
								System.out.println("ANT in Request send PERPOSE TO "+getName());
							//}
							
						}
					}else{
						System.out.println("Bloc ANt waiting data INFORM.REQUEST "+getName());
						block();
					}
					
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">Ctch errpt >agent > "+e.getMessage());
				}
			}
		});
		
		/*
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					
					MessageTemplate tm=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(new AID("SuperAgent", AID.ISLOCALNAME)));
					ACLMessage msg=receive(tm);
					
					if(msg != null){
						myant.setG((Graph) msg.getContentObject());
						
							myant.findTour();

							ACLMessage msg1=new ACLMessage(ACLMessage.INFORM);
							msg1.addReceiver(new AID("SuperAgent", AID.ISLOCALNAME));

							msg1.setContentObject((Serializable) myant);
							send(msg1);
							
							System.out.println("prepa & send msg from "+getAID().getLocalName());
							
					}else{
						System.out.println("waiting data ");
						block();
					}
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">> agent eroor "+e.getMessage());
				}
			}
		});
		*/
	}

	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		System.out.println(getAID().getName()+" is kill");
		super.doDelete();
	}
	
	private void cleangarbage(){
		long minRunningMemory = (1024*1024);

		Runtime runtime = Runtime.getRuntime();

		if(runtime.freeMemory()<minRunningMemory)
		 System.gc();
	}
}
