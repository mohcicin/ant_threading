package view;

import java.io.Serializable;

public class PointsCoordinate implements Serializable{
	private int px;
	private int py;
	@Override
	public String toString() {
		return "PointsCoordinate [px=" + px + ", py=" + py + "]";
	}
	public int getPx() {
		return px;
	}
	public void setPx(int px) {
		this.px = px;
	}
	public int getPy() {
		return py;
	}
	public void setPy(int py) {
		this.py = py;
	}
	public PointsCoordinate(int px, int py) {
		super();
		this.px = px;
		this.py = py;
	}
	public PointsCoordinate() {
		super();
		// TODO Auto-generated constructor stub
	}
}
