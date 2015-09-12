package plateforme;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class contoneur {

	public contoneur(){
		try {

			/*
			Runtime runtime = Runtime.instance();
			ProfileImpl pi = new ProfileImpl(false );
			pi.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer c = runtime.createAgentContainer(pi);
			AgentController SuperAg = c.createNewAgent("Myconteneur", "agents.Viewer", new Object[]{c});
			SuperAg.start();

			 */
			Runtime runtime = Runtime.instance();
			ProfileImpl pi = new ProfileImpl(false );
			pi.setParameter(ProfileImpl.MAIN_HOST, "localhost");
			AgentContainer c = runtime.createAgentContainer(pi);
			
			AgentController SuperAg = c.createNewAgent("Myconteneur", "agents.Viewer", new Object[]{c});
			SuperAg.start();

			/*
			AgentController Agent = c.createNewAgent("Agent1", "Agents.agent", new Object[]{});
			Agent.start();
			 */




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
