package com.krause.instandhaltung;

import java.util.ArrayList;

public class CKomponente implements IKomponente{

	private double g;
	private double cg;
	private ArrayList<Double> gHistory = new ArrayList<>();	//Liste f�r die g Werte (f�r die visualisierung)
	public CKomponente(double g) {
		super();
		this.g = g;
		gHistory.add(g);
	}
	public ArrayList<Double> getgHistory() {
		return gHistory;
	}


	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
		gHistory.add(g);
	}

}
