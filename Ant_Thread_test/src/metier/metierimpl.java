package metier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import view.PointsCoordinate;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

public class metierimpl implements imetier,Serializable {

	private static Map<Arc, Double> maps = new HashMap<Arc, Double>();
	private static List<Double> vals = new ArrayList<Double>();
	private static List<City> lsct = new ArrayList<City>();
	private static List<Arc> lsarc = new ArrayList<Arc>();
	private static HashMap<Double, Ant> res = new HashMap<Double, Ant>();
	
	public metierimpl(){
		maps = new HashMap<Arc, Double>();
		vals = new ArrayList<Double>();
		lsct = new ArrayList<City>();
		lsarc = new ArrayList<Arc>();
		res = new HashMap<Double, Ant>();
	}
	
	@Override
	public List<Arc> load_seccusseur(City src, Graph g, List<City> m) {
		//List<Arc> sec = new ArrayList<Arc>();
		//lsarc.clear();
		lsarc = new ArrayList<Arc>();
		/*
		for(Arc a:g.getArcs()){
			System.out.println("actuel >> "+src.getName()+" ## "+checkcity(a.getDest(), m));
			if(a.getSrc().getName().equals(src.getName()) &&  !checkcity(a.getDest(), m)){
				lsarc.add(a);
			}
		}
		
		*/
		for (int i = 0; i < src.getSuccesseur().size(); i++) {
			if(!checkcity(src.getSuccesseur().get(i).getDest(), m)){
				lsarc.add(src.getSuccesseur().get(i));
			}
		}
		
		return lsarc;
	}

	@Override
	public Arc linkcity(City s, City d,Graph g) {
		// TODO Auto-generated method stub
		for(Arc a:g.getArcs()){
			if(a.getSrc().getName().equals(s.getName()) &&  a.getDest().getName().equals(d.getName())){
				return a;
			}
		}
		return null;
	}

	@Override
	public boolean checkcity(City f, List<City> m) {
		// TODO Auto-generated method stub
		//List<String> cm = new ArrayList<String>();

		boolean x = false;
		
		for (int i = 0; i < m.size(); i++) {
			if(m.get(i).getName().equals(f.getName())){
				x = true;
				break;
			}
		}
/*
		for(City c:m){
			cm.add(c.getName());
		}
		
		if(cm.contains(f.getName())) return true;
		*/
		return x;
	}

	@Override
	public Graph updateGlobalPheromone(Graph g)throws Exception {
		// TODO Auto-generated method stub
		Graph tmp = g;
		double old;
		double newph=0;
		File file = new File("phormone.txt");
        /*
		for(Arc ac:g.getArcs()){
			old = ac.getPheromone();
			FileWriter fw = new FileWriter(file, true);
			
	        PrintWriter pw = new PrintWriter(fw);
			pw.println("old phromone for edge "+ac.getSrc().getName() +" \\ "+ac.getDest().getName()+" >> "+ac.getPheromone()+"\n");
			for(Ant an:g.getAnts()){
				for(Arc a:an.getArcs()){
					if(a.getSrc().getName().equals(ac.getSrc().getName()) && a.getDest().getName().equals(ac.getDest().getName())){
						newph = newph + a.getPheromone();
					}
				}
			}
			ac.setPheromone((1-g.getRu())*old + newph);
			pw.println("new phromone for edge "+ac.getSrc().getName() +" \\ "+ac.getDest().getName()+" >> "+ac.getPheromone()+"\n");
			//output.write("****************************************************************** \n");
			pw.close();
		}
		*/
		for (int i = 0; i < g.getArcs().size(); i++) {
			old = g.getArcs().get(i).getPheromone();
			FileWriter fw = new FileWriter(file, true);
			newph =0;
	        PrintWriter pw = new PrintWriter(fw);
			pw.println("old phromone for edge "+g.getArcs().get(i).getSrc().getName() +" \\ "+g.getArcs().get(i).getDest().getName()+" >> "+g.getArcs().get(i).getPheromone()+"\n");
			for(Ant an:g.getAnts()){
				for(Arc a:an.getArcs()){
					if(a.getSrc().getName().equals(g.getArcs().get(i).getSrc().getName()) && a.getDest().getName().equals(g.getArcs().get(i).getDest().getName())){
						newph = newph + a.getPheromone();
					}
				}
			}
			//g.getArcs().get(i).setPheromone((1-g.getRu())*old + newph);
			tmp.getArcs().get(i).setPheromone((1-g.getRu())*old + newph);
			pw.println("new phromone for edge "+g.getArcs().get(i).getSrc().getName() +" \\ "+g.getArcs().get(i).getDest().getName()+" >> "+g.getArcs().get(i).getPheromone()+"\n");
			//output.write("****************************************************************** \n");
			pw.close();
		}
		//output.close();
		
		return tmp;
	}

	@Override
	public double CalcAllPheromone(Graph g) {
		double ph =0;
		for(Ant a:g.getAnts()){
			ph += (1/a.getLongeur());
		}
		return ph;
	}

	@Override
	public double calcprobamove(Arc i, double alpha, double beta) {
		// TODO Auto-generated methowd stub
		double d = Math.pow(i.getPheromone(), alpha)*Math.pow((1/i.getDistance()), beta);
		//System.out.println(">>> "+d + " <<< "+Math.pow(i.getPheromone(), alpha)*Math.pow((1/i.getDistance()), beta));
		return d;
	}

	@Override
	public Arc calculbestmove(City i, Graph g, List<City> m, double alpha,
			double beta) {
		// TODO Auto-generated method stub
		Arc ac = null;
		
		//List<Arc> sec = this.load_seccusseur(i, g, m);
		lsarc.clear();
		//lsarc = new ArrayList<Arc>();
		lsarc = this.load_seccusseur(i, g, m);
		
		maps.clear();
		vals.clear();
		
		//maps = new HashMap<Arc, Double>();
		//vals = new ArrayList<Double>();
		double invs,inv;
		
		//System.out.println("From "+i +" IS >> "+sec +" <<< "+sec.size());
		if(lsarc.size() > 0){
			for(Arc a:lsarc){
				invs = this.calcprobainvsmove(a.getDest(), lsarc, alpha, beta);
				if(invs != 0){
					inv = this.calcprobamove(a, alpha, beta)/invs;
					//System.out.println("best move from "+i +" >to >> "+a.getDest().getName()+" is " +inv+" / "+invs);
					maps.put(a, inv);
					vals.add(inv);
				}
			}
			Collections.sort(vals);
			double x;
			for(Arc a:maps.keySet()){
				x = maps.get(a);
				//System.out.println("Proba arc  "+a +" In all >> "+x);
				if(x == vals.get(vals.size()-1)){
					ac = a;
					break;
				}
			}
		}
		
		return ac;
	}

	@Override
	public double calcprobainvsmove(City i, List<Arc> ar,double alpha,
			double beta) {
		// TODO Auto-generated method stub
		double vl=0;
		for(Arc c:ar){
			if(ar.size() == 1){
				vl = vl + this.calcprobamove(c, alpha, beta);
			}else{
				if(!c.getDest().getName().equals(i.getName())){
					vl = vl + this.calcprobamove(c, alpha, beta);
				}	
			}
			
		}
		return vl;
	}

	/*
	@Override
	public Graph initGraph(double alpha, double beta,double ph0,double ru) {
		// TODO Auto-generated method stub
		Graph gr = new Graph();
		List<City> ct = new ArrayList<City>();
		
		gr.setAlpha(alpha);
		gr.setBeta(beta);
		gr.setPheromone0(ph0);
		gr.setRu(ru);
		
		City a = new City("A");
		City b = new City("B");
		City c = new City("C");
		City d = new City("D");
		City e = new City("E");
		City f = new City("F");
		City g = new City("G");
		
		ct.add(a);
		ct.add(b);
		ct.add(c);
		ct.add(d);
		ct.add(e);
		ct.add(f);
		ct.add(g);
		
		for (City cw:ct) {
			gr.getCities().add(cw);
		}
		
		List<Arc> ac = new ArrayList<Arc>();
		ac.add(new Arc(a, f ,2,ph0));
		ac.add(new Arc(a, b, 4,ph0));
		ac.add(new Arc(a, c,5,ph0));
		ac.add(new Arc(a, d , 3,ph0));
		ac.add(new Arc(a, e , 3,ph0));
		ac.add(new Arc(a, g , 3,ph0));
		
		
		
		ac.add(new Arc(b , a ,4,ph0));
		ac.add(new Arc(b,d,4,ph0));
		ac.add(new Arc(b,f,2,ph0));
		ac.add(new Arc(b,g,3,ph0));
		ac.add(new Arc(b,c,3,ph0));
		ac.add(new Arc(b,e,3,ph0));
		
		ac.add(new Arc(c,a,5,ph0));
		ac.add(new Arc(c,d,6,ph0));
		ac.add(new Arc(c,e,1,ph0));
		ac.add(new Arc(c,b,3,ph0));
		ac.add(new Arc(c,f,1,ph0));
		ac.add(new Arc(c,g,1,ph0));
		
		ac.add(new Arc(d,a,3,ph0));
		ac.add(new Arc(d,b,4,ph0));
		ac.add(new Arc(d,c,6,ph0));
		ac.add(new Arc(d,e,5,ph0));
		ac.add(new Arc(d,g,2,ph0));
		ac.add(new Arc(d,f,2,ph0));
		
		ac.add(new Arc(e,c,1,ph0));
		ac.add(new Arc(e,d,5,ph0));
		ac.add(new Arc(e,g,2,ph0));
		ac.add(new Arc(e,a,3,ph0));
		ac.add(new Arc(e,b,3,ph0));
		ac.add(new Arc(e,f,2,ph0));
		
		ac.add(new Arc(f,a,2,ph0));
		ac.add(new Arc(f,b,2,ph0));
		ac.add(new Arc(f,c,1,ph0));
		ac.add(new Arc(f,d,2,ph0));
		ac.add(new Arc(f,e,2,ph0));
		ac.add(new Arc(f,g,1,ph0));
		
		
		ac.add(new Arc(g,b,3,ph0));
		ac.add(new Arc(g,d,2,ph0));
		ac.add(new Arc(g,e,2,ph0));
		ac.add(new Arc(g,f,1,ph0));
		ac.add(new Arc(g,a,3,ph0));
		ac.add(new Arc(g,c,1,ph0));
		
		for(Arc ar:ac){
			gr.getArcs().add(ar);
		}
		
		return gr;
	}
	*/

	@Override
	public Graph initGraph(double alpha, double beta,double ph0,double ru,List<PointsCoordinate> pc) {
		// TODO Auto-generated method stub
		Graph gr = new Graph();
		
		Graph gr2 = new Graph();
		//List<City> ct = new ArrayList<City>();
		lsct.clear();
		//lsct = new ArrayList<City>();
		
		gr.setAlpha(alpha);
		gr.setBeta(beta);
		gr.setPheromone0(ph0);
		gr.setRu(ru);
		
		lsct = this.initDistance(pc);
		for (City cw:lsct) {
			gr.getCities().add(cw);
		}
		
		List<Arc> ac = new ArrayList<Arc>();
		
		
		
		for (int i = 0; i < lsct.size(); i++) {
			for (int j = 0; j < lsct.size(); j++) {
				if(!lsct.get(i).getName().equals(lsct.get(j).getName())){
					ac.add(new Arc(lsct.get(i), lsct.get(j), this.calculdistance(lsct.get(i), lsct.get(j)), ph0));
				}
			}
		}
		
		for(Arc ar:ac){
			//System.out.println(">> "+ar);
			gr.getArcs().add(ar);
		}
		
		gr2 = gr;
		List<City> tmp = new ArrayList<City>();
		List<Arc> arctmp = new ArrayList<Arc>();
		for (int i = 0; i < gr.getCities().size(); i++) {
			City ct = gr.getCities().get(i);
			arctmp = load_seccusseur_new(ct, null, ac);
			ct.setSuccesseur(arctmp);
			tmp.add(ct);
		}
		
		gr.setCities(tmp);
		
		/*
		for (int i = 0; i < tmp.size(); i++) {
			System.out.println("*************** begin **"+tmp.get(i).getName());
			System.out.println(tmp.get(i).getSuccesseur().toString());
		}
		*/
		
		
		
		return gr;
	}
	@Override
	public double longthOfTour(List<Arc> ac) {
		// TODO Auto-generated method stub
		double vl =0;
		for(Arc a:ac){
			vl += a.getDistance();
		}
		if(vl != 0)return 0.9/vl;
		return 0;
	}

	@Override
	public List<City> initDistance(List<PointsCoordinate> po) {
		// TODO Auto-generated method stub
		//List<City> ct = new ArrayList<City>();
		lsct.clear();
		//lsct = new ArrayList<City>();
		for(PointsCoordinate pc:po){
			lsct.add(new City(pc.getPx()+"#"+pc.getPy(), pc));
		}
		return lsct;
	}

	@Override
	public double calculdistance(City a, City b) {
		// TODO Auto-generated method stub
		double x = Double.MAX_VALUE;
		double a1 = Math.pow(a.getLatlang().getPx()-b.getLatlang().getPx(), 2);
		double a2= Math.pow(a.getLatlang().getPy()-b.getLatlang().getPy(), 2);
		x = Math.sqrt(a1+a2);
		return x;
	}

	@Override
	public HashMap<Integer, List<Arc>> calculBestTour(
			HashMap<Integer, HashMap<Ant, List<Arc>>> in) {
		// TODO Auto-generated method stub
		/*
		for(Integer i:in.keySet()){
			HashMap<Ant, List<Arc>> d = new HashMap<Ant, List<Arc>>();
			d = in.get(i);
			for(Ant a:d.keySet()){
				System.out.println("ant hopa >> "+a.getName()+" >> "+d.get(a));
			}
			
		}
		*/
		//HashMap<Integer, List<Arc>> res = new HashMap<Integer, List<Arc>>();
		HashMap<Integer, List<Arc>> res2 = new HashMap<Integer, List<Arc>>();
	//	List<Double> b = new ArrayList<Double>();
		HashMap<Double, List<Arc>> tmp = new HashMap<Double, List<Arc>>();
		
		HashMap<Ant, List<Arc>> d = new HashMap<Ant, List<Arc>>();
		
		for(Integer i:in.keySet()){
			
			d.clear();
			//d = new HashMap<Ant, List<Arc>>();
			d = in.get(i);
		//	HashMap<Ant, Double> tr = new HashMap<Ant, Double>();
		//	b = new ArrayList<Double>();
			for(Ant a:d.keySet()){
				double x = CalclongthOfTour(d.get(a));
				//tr = new HashMap<Ant, Double>();
				//tr.put(a,x );
				//b.add(x);
				tmp.put(x, d.get(a));
			}
			/*
			Collections.sort(b);
			
			double me;
			for(Ant c:tr.keySet()){
				me = tr.get(c);
				if( me == b.get(0)){
					res.put(i, d.get(c));
				}
			}
			*/
		}
		
		SortedSet<Double> keys = new TreeSet<Double>(tmp.keySet());
		res2.put(0, tmp.get(keys.first()));
		
		
		
		return res2;
	}

	@Override
	public double CalclongthOfTour(List<Arc> ac) {
		// TODO Auto-generated method stub
		double vl =0;
		for(Arc a:ac){
			vl += a.getDistance();
		}
		
		return vl;
	}

	@Override
	public Ant CalculBestIteration(List<Ant> a) {
		// TODO Auto-generated method stub
		res.clear();
		//res = new HashMap<Double, Ant>();
		for (int i = 0; i < a.size(); i++) {
			if(a.get(i).getArcs().size() > 0){
				res.put(CalclongthOfTour(a.get(i).getArcs()), a.get(i));
			}
			
		}
		SortedSet<Double> keys = new TreeSet<Double>(res.keySet());
		
		//System.out.println(">>> best founded "+res.get(keys.first()).getName());
		return res.get(keys.first());
		
	}

	@Override
	public List<Arc> load_seccusseur_new(City src, Graph g,List<Arc> arc) {
		// TODO Auto-generated method stub
		//List<Arc> sec = new ArrayList<Arc>();
		//System.out.println("*************** begin "+src.getName()+"************************");
				//lsarc.clear();
				lsarc = new ArrayList<Arc>();
				for(Arc a:arc){
					if(a.getSrc().getName().equals(src.getName())){
						lsarc.add(a);
					}
				}
		//System.out.println("Load sec "+src.getName()+" >> "+lsarc.toString());
		//System.out.println("*****************************************************");
				return lsarc;
	}
}
