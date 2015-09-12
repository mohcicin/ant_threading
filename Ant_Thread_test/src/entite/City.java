package entite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import view.PointsCoordinate;

public class City implements Serializable{

	private String name;
	private PointsCoordinate latlang;

	private List<Arc> successeur = new ArrayList<Arc>();
	

	public PointsCoordinate getLatlang() {
		return latlang;
	}

	public void setLatlang(PointsCoordinate latlang) {
		this.latlang = latlang;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", latlang=" + latlang  + "]";
	}

	public City(String name, PointsCoordinate latlang) {
		super();
		this.name = name;
		this.latlang = latlang;
	}

	public List<Arc> getSuccesseur() {
		return successeur;
	}

	public void setSuccesseur(List<Arc> successeur) {
		this.successeur = successeur;
	}

	
}
