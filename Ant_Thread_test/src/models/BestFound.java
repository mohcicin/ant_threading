package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entite.Ant;
import entite.Arc;
import entite.City;

public class BestFound implements Serializable{

	private String ant;
	private int num;
	private List<Arc> res = new ArrayList();
	public BestFound() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAnt() {
		return ant;
	}
	public void setAnt(String ant) {
		this.ant = ant;
	}
	public List<Arc> getRes() {
		return res;
	}
	public void setRes(List<Arc> res) {
		this.res = res;
	}
	@Override
	public String toString() {
		return "BestFound [ant=" + ant + ", res=" + res + "]";
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public BestFound(String ant, int num, List<Arc> res) {
		super();
		this.ant = ant;
		this.num = num;
		this.res = prepa_arcs(res);
	}
	
	private List<Arc> prepa_arcs(List<Arc> in){
		
		for (int i = 0; i < in.size(); i++) {
			City src = in.get(i).getSrc();
			src.setSuccesseur(new ArrayList<Arc>());
			
			City dest = in.get(i).getDest();
			dest.setSuccesseur(new ArrayList<Arc>());
			
			in.get(i).setSrc(src);
			in.get(i).setDest(dest);
			
		}
		return in;
	}
}
