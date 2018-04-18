////Komponenten-Art wird ggf. nicht gebraucht
//
//package com.krause.instandhaltung.systemkomponenten;
//
//import com.krause.instandhaltung.*;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//
//public class CKomponenteAlter implements IKomponente{
//	private double a;
//	private double g;
//	private double ca,cg,ct;
//	private double[] xt;
//	private ArrayList<Double> gHistory = new ArrayList<>();
//	private ArrayList<Double> aHistory = new ArrayList<>();
//	public CKomponenteAlter(double a, double g, double ca, double cg, double ct,double[] x) {
//		super();
//		this.a = a;
//		this.g = g;
//		this.ca = ca;
//		this.cg = cg;
//		this.ct = ct;
//		this.xt = x;
//		gHistory.add(g);
//		aHistory.add(a);
//	}
//	public ArrayList<Double> getgHistory() {
//		return gHistory;
//	}
//	public ArrayList<Double> getaHistory() {
//		return aHistory;
//	}
//	public double[] getXt() {
//		return xt;
//	}
//	public double getA() {
//		return a;
//	}
//	public void setA(double a) {
//		this.a = a;
//		aHistory.add(a);
//	}
//	public double getG() {
//		return g;
//	}
//	public void setG(double g) {
//		this.g = g;
//		gHistory.add(g);
//	}
//	public double getCa() {
//		return ca;
//	}
//
//	public double getCg() {
//		return cg;
//	}
//
//	public double getCt() {
//		return ct;
//	}
//	@Override
//	public double getLeistung() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	@Override
//	public void zeitschrittDurchfuehren(double invest) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void setLeistung(Double leistung) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public LinkedList<Double> getLeistungHistory() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public CKomponenteAlter clone() {
//		return this;
//	}
//	@Override
//	public void leistungSchrittZurueck() {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void leistungNSchritteZurueck(int n) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	
//	
//}
