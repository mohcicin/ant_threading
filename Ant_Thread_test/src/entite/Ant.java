package entite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import metier.imetier;
import metier.metierimpl;

import utils.probability;

public class Ant implements Serializable{

	private String name;
	private City start;
	private City actuel;
	private Arc tmp;
	private List<Arc> arcs = new ArrayList<Arc>();
	private List<City> memories = new ArrayList<City>();
	private List<probability> proba = new ArrayList<probability>();
	private imetier dao ;
	private Graph g;
	
	private File file ;
    private BufferedWriter output;
	
	private double longeur =0;
	
	private double phoromonel= 0.1;
	private double phoromoneg=0.1;
	
	private List<String> lstmp = new ArrayList<String>();
	private List<String> lstmpmemo = new ArrayList<String>();
	
	public Ant() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ant(String name, Graph g,City s,imetier mx) {
		super();
		this.name = name;
		this.g = g;
		this.start = s;
		this.actuel = this.start;
		this.dao = mx;
		file = new File("ants.txt");
		
		for(City c:this.g.getCities()){
			this.lstmp.add(c.getName());
		}
		lstmpmemo = new ArrayList<String>();
	}

	public void findTour(){
		try {
			//output.write("Start construction "+this.getName());
			longeur = 0;
			while(!endsearch()){
				System.out.println("actuel "+this.actuel.getName() +" =start== "+new Date());
				this.memories.add(this.actuel);
				this.lstmpmemo.add(this.actuel.getName());
				tmp = dao.calculbestmove(this.actuel, this.g, this.memories, this.g.getAlpha(), this.g.getBeta());
				//System.out.println("temp "+tmp);
				if(tmp != null){
					this.arcs.add(tmp);
					this.actuel = tmp.getDest();
				}else{
					break;
				}
				System.out.println("actuel "+this.actuel.getName() +" =end== "+new Date());
			}
				/*
			File file = new File(this.getName()+".txt");
			FileWriter fw = new FileWriter(file, true);
			
	        PrintWriter pw = new PrintWriter(fw);
			pw.println("vegin construction ********************************************* \n");
				for (int i = 0; i < this.arcs.size(); i++) {
					this.arcs.get(i).setPheromone(dao.longthOfTour(arcs));
					longeur += this.arcs.get(i).getDistance();
					pw.println("Arc end Construction "+this.arcs.get(i).getSrc().getName() +" <===> "+this.arcs.get(i).getDest().getName()+" : "+this.arcs.get(i).getPheromone()+"\n");
				}
				pw.println("fin construction *********************************************  with length = "+longeur+"\n");
				pw.close();
				*/
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception in find best tour "+this.name+" ## "+e.getMessage());
		}
	}

	public boolean endsearch(){
		/*
		List<String> m = new ArrayList<String>();
		List<String> cm = new ArrayList<String>();


		for(City c:this.memories){
			m.add(c.getName());
		}
		
		for(City c:this.g.getCities()){
			cm.add(c.getName());
		}
		*/
		if(lstmpmemo.containsAll(lstmp)) return true;
		
		return false;
	}
	
	public void initAnt(){
		this.memories = new ArrayList<City>();
		this.arcs = new ArrayList<Arc>();
		this.lstmpmemo = new ArrayList<String>();
	}
	public probability calcproba(){
		probability p =new probability();
		double[] vl = new double[this.proba.size()];
		int k =0;
		for(probability px :this.proba){
			vl[k++] = px.getProba();
		}
		Arrays.sort(vl);
		for(probability px :this.proba){
			if(px.getProba() == vl[vl.length-1]) {
				p = px;
			}
		}
		return p;
		
	}
	
	public double calctotal(){
		double vl =0;
		for(Arc a:dao.load_seccusseur(actuel, this.g, this.memories)){
			probability p = new probability(actuel, a.getDest(),g.getAlpha(), g.getBeta(), this.phoromoneg, this.g, a.getDistance());
			vl = vl+p.getProba();
		}
		return vl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public City getStart() {
		return start;
	}
	public void setStart(City start) {
		this.start = start;
	}
	public City getActuel() {
		return actuel;
	}
	public void setActuel(City actuel) {
		this.actuel = actuel;
	}
	public List<Arc> getArcs() {
		return arcs;
	}
	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}
	public List<City> getMemories() {
		return memories;
	}
	public void setMemories(List<City> memories) {
		this.memories = memories;
	}
	public List<probability> getProba() {
		return proba;
	}
	public void setProba(List<probability> proba) {
		this.proba = proba;
	}
	
	public imetier getDao() {
		return dao;
	}
	public void setDao(imetier dao) {
		this.dao = dao;
	}
	public Graph getG() {
		return g;
	}
	public void setG(Graph g) {
		this.g = g;
	}
	public double getPhoromoneg() {
		return phoromoneg;
	}
	public void setPhoromoneg(double phoromone) {
		this.phoromoneg = phoromone;
	}
	@Override
	public String toString() {
		return "Ant [name=" + name + ", start=" + start + ", actuel=" + actuel
				+ ", arcs=" + arcs + ", memories=" + memories + ", proba="
				+ proba + ", dao=" + dao + ", g=" + g + ", phoromone="
				+ phoromoneg + "]";
	}
	public double getLongeur() {
		return longeur;
	}
	public void setLongeur(double longeur) {
		this.longeur = longeur;
	}
	public double getPhoromonel() {
		return phoromonel;
	}
	public void setPhoromonel(double phoromonel) {
		this.phoromonel = phoromonel;
	}
}
