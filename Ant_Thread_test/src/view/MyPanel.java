package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import entite.*;

public class MyPanel extends JPanel implements MouseListener,Serializable{

	private List<PointsCoordinate> centre;
	private Graphics gr;
	private MyViewer myview;
	
	private HashMap<Integer, List<Arc>> data = new HashMap<Integer, List<Arc>>();
	
	public HashMap<Integer, List<Arc>> getData() {
		return data;
	}

	public MyPanel(){
		super();
		centre = new ArrayList<PointsCoordinate>();
		this.setBackground(Color.RED);
		this.addMouseListener(this);
	}
	public void setData(HashMap<Integer, List<Arc>> data) {
		
		this.data = data;
	}

	public MyViewer getMyview() {
		return myview;
	}

	public void setMyview(MyViewer myview) {
		this.myview = myview;
	}

	public MyPanel(MyViewer vh){
		centre = new ArrayList<PointsCoordinate>();
		this.setBackground(Color.white);
		this.addMouseListener(this);
		this.myview = vh;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		this.dessiner(g);
		this.dessinerLine(g);
		
		this.dessinerTri9(this.data, g);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		try {
			int x = me.getX();
			int y = me.getY();
			
			centre.add(new PointsCoordinate(x, y));
			repaint();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}

	public void dessiner(Graphics g){
		for(PointsCoordinate px:centre){
			g.drawOval(px.getPx() - 10, px.getPy() - 10, 2 * 10, 2 * 10);
	        g.setColor(Color.BLUE);
	        g.fillOval(px.getPx() - 10, px.getPy() - 10, 2 * 10, 2 * 10);
	        
	        g.setColor(Color.RED);
	        g.drawString("("+px.getPx()+"|"+ px.getPy()+")", px.getPx() - 10, px.getPy() - 10);
		}
	}
	public void cleanPanel(Graphics g){
		this.cleanPanel(g);
	}
	
	public void dessinerLine(Graphics g){
		for (int i = 0; i < centre.size(); i++) {
			for (int j = i+1; j < centre.size(); j++) {
				g.setColor(Color.BLACK);
				g.drawLine(centre.get(i).getPx(), centre.get(i).getPy(), centre.get(j).getPx(), centre.get(j).getPy());
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	public List<PointsCoordinate> getCentre() {
		return centre;
	}

	public void setCentre(List<PointsCoordinate> centre) {
		this.centre = centre;
	}

	public Graphics getGr() {
		return gr;
	}

	public void setGr(Graphics gr) {
		this.gr = gr;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	
	public void dessinerTri9(HashMap<Integer, List<Arc>> dr,Graphics g){
		Random r = new Random();
		try {
			Graphics2D gg = (Graphics2D)g;
			File file = new File("bestour.txt");
	        FileWriter fw = new FileWriter(file,false);
	        PrintWriter pw = new PrintWriter(fw);
	        Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
			for(Integer i:dr.keySet()){
				System.out.println("iteration "+i+" dis "+dr.get(i));
				pw.println("Iteration "+i+" >> "+dr.get(i));
				for(Arc a:dr.get(i)){
					BasicStroke bs2 = new BasicStroke(5, BasicStroke.CAP_ROUND,
			                BasicStroke.JOIN_BEVEL);
					gg.setStroke(bs2);
					gg.setColor(c);
					gg.drawLine(a.getSrc().getLatlang().getPx(), a.getSrc().getLatlang().getPy(), a.getDest().getLatlang().getPx(), a.getDest().getLatlang().getPy());
				}
			}
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
