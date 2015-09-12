package entite;

import java.io.Serializable;

public class Arc implements Serializable{

	private City src;
	private City dest;
	private double distance;
	private double pheromone;
	public Arc(City src, City dest, double distance,double pheromone) {
		super();
		this.src = src;
		this.dest = dest;
		this.distance = distance;
		this.pheromone = pheromone;
	}
	public double getPheromone() {
		return pheromone;
	}
	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
	}
	public Arc() {
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "Arc [dest=" + dest + ", distance=" + distance + ", pheromone="
				+ pheromone + ", src=" + src + "]";
	}
}
