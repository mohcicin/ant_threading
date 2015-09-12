package threads;

import agents.ACLMessage;
import agents.AID;
import agents.MessageTemplate;
import utils.SerializeObject;
import entite.Ant;

public class TAnt_Agent extends Thread{

	private Ant myant;
	private int code;
	
	private int go;

	public TAnt_Agent(Ant in,int c){
		this.myant = in;
		this.code = c;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		Object[] obj = getArguments();
		myant = (Ant) obj[0];
		SerializeObject.serializableObjectAnt(myant, "ant_agent"+getAID().getLocalName()+".ant");
		
		System.out.println("Bonsoir je suis l'agent ANT  "+getAID().getLocalName()+" >> "+getAID().getName());
		System.out.println(">>> "+myant.toString());
		
		while(true){
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
	}
	
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
