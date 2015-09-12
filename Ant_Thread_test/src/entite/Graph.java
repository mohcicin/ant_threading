package entite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph implements Serializable{

	private String name;
	private List<City> cities = new ArrayList<City>();
	private List<Arc> arcs = new ArrayList<Arc>();
	private List<Ant> ants = new ArrayList<Ant>();
	private HashMap<Integer, List<List<Arc>>> bestTours = new HashMap<Integer, List<List<Arc>>>();
	
	private double alpha;
	private double beta;
	private double ru;
	private double pheromone0;
	
	public double getPheromone0() {
		return pheromone0;
	}
	public void setPheromone0(double pheromone0) {
		this.pheromone0 = pheromone0;
	}
	public Graph(String name, double alpha, double beta) {
		super();
		this.name = name;
		this.alpha = alpha;
		this.beta = beta;
		this.bestTours = new HashMap<Integer, List<List<Arc>>>();
	}
	
	public HashMap<Integer, List<List<Arc>>> getBestTours() {
		return bestTours;
	}
	public void setBestTours(HashMap<Integer, List<List<Arc>>> bestTours) {
		this.bestTours = bestTours;
	}
	public Graph() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public List<Arc> getArcs() {
		return arcs;
	}
	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getBeta() {
		return beta;
	}
	public void setBeta(double beta) {
		this.beta = beta;
	}
	@Override
	public String toString() {
		return "Graph [name=" + name + ", cities=" + cities + ", arcs=" + arcs
				+ ", alpha=" + alpha + ", beta=" + beta + "]";
	}
	public List<Ant> getAnts() {
		return ants;
	}
	public void setAnts(List<Ant> ants) {
		this.ants = ants;
	}
	public double getRu() {
		return ru;
	}
	public void setRu(double ru) {
		this.ru = ru;
	}
	
}
