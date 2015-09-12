package agents;

import java.io.Serializable;
import java.util.Date;

import entite.Ant;
import entite.Graph;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Ant_agent2 extends Agent{

	private Ant myant;
	
	private int go;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		//*System.out.println("Bonsoir je suis l'agent  "+getAID().getLocalName());
		Object[] obj = getArguments();
		myant = (Ant) obj[0];
		//System.out.println(">>> "+myant.toString());
		//*System.out.println(">>> statrt ant "+this.getName()); //myant.toString());
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					//*myant.findTour();
					System.out.println(">>> in ant ");
					MessageTemplate tm1=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchSender(new AID("SuperAgent", AID.ISLOCALNAME)));
					MessageTemplate tm2=MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchSender(new AID("SuperAgent", AID.ISLOCALNAME)));
					
					
					MessageTemplate tm = MessageTemplate.or(tm1, tm2);
					ACLMessage msg=receive(tm);
					
					if(msg != null){
						if(msg.getPerformative() == ACLMessage.INFORM){
								System.out.println("Receive from calculator in INFORM "+msg.getContent());

								ACLMessage msg1=new ACLMessage(ACLMessage.PROPOSE);
								msg1.addReceiver(new AID("SuperAgent", AID.ISLOCALNAME));
								int d = Integer.parseInt(msg.getContent())*2;
								msg1.setContent(""+d);
								send(msg1);
								
						}else if(msg.getPerformative() == ACLMessage.REQUEST){
							System.out.println("Receive from calculator in REQUEST "+msg.getContent());
							
							ACLMessage msg1=new ACLMessage(ACLMessage.PROPOSE);
							msg1.addReceiver(new AID("SuperAgent", AID.ISLOCALNAME));

							int d = Integer.parseInt(msg.getContent())*2;
							msg1.setContent(""+d);
							send(msg1);
						}
					}else{
						System.out.println("waiting data INFORM.REQUEST "+getName());
						block();
					}
					
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(">>agent > "+e.getMessage());
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
}
