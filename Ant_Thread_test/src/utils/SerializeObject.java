package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import models.BestFound;

import com.google.gson.Gson;

import entite.Ant;
import entite.Graph;

public class SerializeObject {


	public synchronized static int serializableObjectAnt(Ant obj,String fl){
		int x =-1;
		try {

			File st = new File(fl);



			if(!st.exists()){
				st.createNewFile();
			}

			//System.out.println(">>> ***************************** "+obj.getName());
			OutputStream file = new FileOutputStream(st,false);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(obj);
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in ser ant"+e.getMessage());
			x = -1;
		}
		return x;
	}

	public synchronized static Ant DeserializableObjectAnt(String fl){
		try {
			InputStream file = new FileInputStream(fl);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			return (Ant)input.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in des ant"+e.getMessage());
			return null;
		}
	}

	public synchronized static int serializableObjectGraph(Graph obj,String fl){
		int x =-1;
		try {
			File st = new File(fl);

			if(!st.exists()){
				st.createNewFile();
			}

			OutputStream file = new FileOutputStream(fl,false);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(obj);
			output.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in ser gr"+e.getMessage());
			x = -1;
		}
		return x;
	}

	public synchronized static Graph DeserializableObjectGraph(String fl){
		try {
			InputStream file = new FileInputStream(fl);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			return (Graph)input.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in des gr"+e.getMessage());
			return null;
		}
	}

	public synchronized static int serializableObjectBest(Ant obj,String fl){
		int x =-1;
		try {

			File st = new File(fl);



			if(!st.exists()){
				st.createNewFile();
			}
			if(obj != null){
				OutputStream file = new FileOutputStream(st,true);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);
				output.writeObject(obj);
				output.close();  
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in ser best ant"+e.getMessage());
			x = -1;
		}
		return x;
	}

	public synchronized static List<Ant> DeserializableObjectBest(String fl){
		try {
			InputStream file = new FileInputStream(fl);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			List<Ant> res = new ArrayList<Ant>();

			Ant ant = new Ant();
			while( (ant = (Ant)input.readObject()) != null){
				res.add(ant);
			}
			return res ; //(List<Ant>)input.readObject();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in des ant "+e.getMessage());
			return null;
		}
	}

	public synchronized static int serializableObjectBestAnt(BestFound obj,String fl){
		int x =-1;
		try {

			File st = new File(fl);



			if(!st.exists()){
				st.createNewFile();
			}

			Gson gson = new Gson();
			FileWriter fw = new FileWriter(fl, true);
			PrintWriter pout = new PrintWriter(fw);
			pout.println(gson.toJson(obj, BestFound.class));
			pout.close();

			//System.out.println("{"+gson.toJson(obj, BestFound.class)+"}");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error in ser best "+e.getMessage());
			x = -1;
		}
		return x;
	}

	public synchronized static Ant DeserializableObjectBestAnt(String fl){

		Ant ant = new Ant();
		try {

			Gson gson = new Gson();
			File secondInputFile = new File(fl);
			InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
			BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
			String line;

			while ((line = r.readLine()) != null) {

				BestFound bs = gson.fromJson(line, BestFound.class);
				//System.out.println(bs.toString());
				ant.setName(bs.getAnt());
				ant.setArcs(bs.getRes());
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ant;
	}


	public synchronized static boolean Queue(List<String> in){

		try{
			int n;
			List<String> tmp = new ArrayList<>();

			File file = new File("queue.txt");
			if(file.exists()){
				//Log.e("data loaded exist  ",file.getAbsolutePath());
				File secondInputFile = new File(file.getAbsolutePath());
				InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
				BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
				StringBuilder total = new StringBuilder();
				String line;


				while ((line = r.readLine()) != null) {
					tmp.add(line);
				}
			}

			if(tmp.containsAll(in))return true;


		}catch(Exception e){
			return false;
		}
		return false;
	}

	public synchronized static void PutQueue(String in,int n){

		try{
			File file = new File("queue.txt");
			FileOutputStream outputStream;

			if(!file.exists()){
				file.createNewFile();
				file.mkdir();
				//Log.e("file not exist ","wloo wloo");
			}


			switch (n) {
			case 0:
				if(file.exists()){
					FileWriter fw = new FileWriter(file, false);
					PrintWriter pout = new PrintWriter(fw);

					pout.print("");
					pout.close();
				}
				break;

			default:
				if(file.exists()){
					FileWriter fw = new FileWriter(file, true);
					PrintWriter pout = new PrintWriter(fw);

					pout.println(in);
					pout.close();
				}
				break;
			}

		}catch(Exception e){
		}
	}
}
