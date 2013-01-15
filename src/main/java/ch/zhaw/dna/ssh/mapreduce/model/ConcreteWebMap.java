package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.IOException;

import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;


public class ConcreteWebMap implements MapInstruction {
	
	private boolean h1IsSet = false;
	private boolean h2IsSet = false;
	private boolean h3IsSet = false;
	private boolean pIsSet = false;
	private boolean aIsSet = false;
	
	public static final String URLKEY = "Urls";


	public void map(MapEmitter arg0, String url) {
		
		//1.get URL
			try {
				getURL(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    //2. Parse content
			
			
			

		
	}
	
	public String getURL(String url) throws IOException{
		URLInputReader input = new URLInputReader();

		return input.readURL(url);
		
	}


	public boolean isH1IsSet() {
		return h1IsSet;
	}


	public void setH1IsSet(boolean h1IsSet) {
		this.h1IsSet = h1IsSet;
	}


	public boolean isH2IsSet() {
		return h2IsSet;
	}


	public void setH2IsSet(boolean h2IsSet) {
		this.h2IsSet = h2IsSet;
	}


	public boolean isH3IsSet() {
		return h3IsSet;
	}


	public void setH3IsSet(boolean h3IsSet) {
		this.h3IsSet = h3IsSet;
	}


	public boolean ispIsSet() {
		return pIsSet;
	}


	public void setpIsSet(boolean pIsSet) {
		this.pIsSet = pIsSet;
	}


	public boolean isaIsSet() {
		return aIsSet;
	}


	public void setaIsSet(boolean aIsSet) {
		this.aIsSet = aIsSet;
	}
	
	
	

}
