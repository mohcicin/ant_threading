package metier;

import java.util.HashMap;
import java.util.List;

import view.PointsCoordinate;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

public interface imetier {

	public List<Arc> load_seccusseur(City src,Graph g,List<City> m);
	public List<Arc> load_seccusseur_new(City src,Graph g,List<Arc> m);
	public Arc linkcity(City s,City d,Graph g);
	public boolean checkcity(City f,List<City> m);
	public double CalcAllPheromone(Graph g);
	
	public Arc calculbestmove(City i,Graph g,List<City> m,double alpha,double beta);
		public double calcprobamove(Arc i,double alpha,double beta);
		public double calcprobainvsmove(City i,List<Arc> ar,double alpha,double beta);
		public Graph updateGlobalPheromone(Graph g)throws Exception;
	public double longthOfTour(List<Arc> ac);
	
	public Graph initGraph(double alpha,double beta,double ph0,double ru,List<PointsCoordinate> pc);
	
	public List<City> initDistance(List<PointsCoordinate> po);
	public double calculdistance(City a,City b);
	
	public HashMap<Integer, List<Arc>> calculBestTour(HashMap<Integer, HashMap<Ant, List<Arc>>> in);
	public double CalclongthOfTour(List<Arc> ac);
	
	public Ant CalculBestIteration(List<Ant> a);
	
	
}
