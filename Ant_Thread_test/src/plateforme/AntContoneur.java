package plateforme;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Serializable;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class AntContoneur implements Serializable{

	private static AntContoneur me;
	private Runtime runtime ;
	private ProfileImpl pi;
	
	private AntContoneur (){
		/*
		try {
			Runtime runtime = Runtime.instance();
			ProfileImpl pi = new ProfileImpl(false );
			pi.setParameter(ProfileImpl.MAIN_HOST, "localhost");
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
	}
	
	public AgentController startNewConteneur(String nm,String paquege,Object[] ob){
		AgentController SuperAg=null;
		try {
			Runtime runtime = Runtime.instance();
			ProfileImpl pi = new ProfileImpl(false );
			pi.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			
			
			AgentContainer c = runtime.createAgentContainer(pi);
			SuperAg = c.createNewAgent(nm, paquege, ob);//new Object[]{c}
			SuperAg.start();
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SuperAg;
	}

	public static AntContoneur getMe() {
		if(me == null){
			me = new AntContoneur();
		}
		return me;
	}

	public static void setMe(AntContoneur me) {
		AntContoneur.me = me;
	}
}
