package threads;
 
import utils.SerializeObject;
import entite.Ant;

public class TAnt_Agent extends Thread{

	private Ant myant;
	private int code;

	private int go;
	private TCalculator master;

	public TAnt_Agent(Ant in,int c,TCalculator in2){
		super(in.getName());
		this.myant = in;
		this.code = c;
		
		this.master = in2;
		
		SerializeObject.serializableObjectAnt(myant, "ant_agent"+getName()+".ant");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		while(true){
			try {
				//*myant.findTour();

				cleangarbage();

				switch (this.go) {
				case 0:
					prepaINFORM();
					break;
				case 1:
					prepaPERPOSE();
					break;

				default:
					wait();
					break;
				}

				 



			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(">Ctch errpt >agent > "+e.getMessage());
			}
		}
	}

	public void doDelete() {
		// TODO Auto-generated method stub
		destroy();
	}

	private void cleangarbage(){
		long minRunningMemory = (1024*1024);

		Runtime runtime = Runtime.getRuntime();

		if(runtime.freeMemory()<minRunningMemory)
			System.gc();
	}
	
	private synchronized void prepaINFORM(){
		System.out.println("ANT in INFORM REcieve INFORM From "+getName());
		try {
			myant = SerializeObject.DeserializableObjectAnt("ant_agent"+getName()+".ant");
			myant.initAnt();
			myant.findTour();

			SerializeObject.serializableObjectAnt(myant, getName()+".ant");
			
			wait();
			SerializeObject.PutQueue(getName(),1);
			this.master.notify();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception Ant Inform "+e.getMessage());
		}

		System.out.println("ANT in INFORM send PERPOSE To  "+getName());
		
	}
	
	private synchronized void prepaPERPOSE(){
		System.out.println("Ant REQUEST Receive REQUEST FROM "+getName());

		//myant.setG((Graph) msg.getContentObject()); -- desrialize object
		//if(msg.getContent().toString().equals("ok")){
		try {
			myant = SerializeObject.DeserializableObjectAnt("ant_agent"+getName()+".ant");
			myant.setG(SerializeObject.DeserializableObjectGraph("calculator.ant"));
			myant.initAnt();
			myant.findTour();

			SerializeObject.serializableObjectAnt(myant, getName()+".ant");

			wait();
			SerializeObject.PutQueue(getName(),1);
			this.master.notify();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception ant REQUEST "+e.getMessage());
		}

		System.out.println("ANT in Request send PERPOSE TO "+getName());
		
	}
}
