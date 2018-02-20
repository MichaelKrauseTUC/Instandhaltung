package com.krause.instandhaltung;

import java.util.ArrayList;

public class CKomponente2 {
    private double a;
    private double g;
    private double ca,cg,ct;
    private double[] xt;		//x-Werte der Komponente f�r jedes t
    private ArrayList<Double> gHistory = new ArrayList<>();	//Liste f�r die g Werte (f�r die visualisierung)
    private ArrayList<Double> aHistory = new ArrayList<>(); //Liste f�r die a Werte (f�r die visualisierung)
    public CKomponente2(double a, double g, double ca, double cg, double ct,double[] x) {
        super();
        this.a = a;
        this.g = g;
        this.ca = ca;
        this.cg = cg;
        this.ct = ct;
        this.xt = x;
        gHistory.add(g);
        aHistory.add(a);
    }
    public ArrayList<Double> getgHistory() {
        return gHistory;
    }
    public ArrayList<Double> getaHistory() {
        return aHistory;
    }
    public double[] getXt() {
        return xt;
    }
    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
        aHistory.add(a);
    }
    public double getG() {
        return g;
    }
    public void setG(double g) {
        this.g = g;
        gHistory.add(g);
    }
    public double getCa() {
        return ca;
    }

    public double getCg() {
        return cg;
    }

    public double getCt() {
        return ct;
    }



}
