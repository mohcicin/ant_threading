package utils;

import metier.metierimpl;
import entite.City;
import entite.Graph;

public class probability {

	private City src;
	private City dest;
	private double proba;
	
	public probability(City src, City dest,double alpha,double beta,double ph,Graph g,double dis) {
		super();
		this.src = src;
		this.dest = dest;
		this.calc(alpha, beta, ph,g,dis);
	}
	
	public double calc(double alpha,double beta,double ph,Graph g,double dis){
		//System.out.println("ph probax "+ph);
		this.proba = Math.pow(ph,alpha)*Math.pow(1/dis, beta);
		return proba;
	}
	public probability() {
		super();
		// TODO Auto-generated constructor stub
	}
	public City getSrc() {
		return src;
	}
	public void setSrc(City src) {
		this.src = src;
	}
	public City getDest() {
		return dest;
	}
	public void setDest(City dest) {
		this.dest = dest;
	}
	public double getProba() {
		return proba;
	}
	public void setProba(double proba) {
		this.proba = proba;
	}
	@Override
	public String toString() {
		return "Arc [src=" + src + ", dest=" + dest + ", proba=" + proba
				+ "]";
	}
}
