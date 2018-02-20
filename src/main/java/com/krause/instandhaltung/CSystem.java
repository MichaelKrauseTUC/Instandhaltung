package com.krause.instandhaltung;

import java.util.ArrayList;

public final class CSystem implements ISystem
{

    private ArrayList<Component> components = new ArrayList<>();	//Komponentenliste
    private double b;
    private ArrayList<Double> valueHistory = new ArrayList<>();		//Liste f�r die Funktionswerte
    public CSystem(ArrayList<Component> components, double b) {
        super();
        this.b = b;
        this.components = components;
    }
    public ArrayList<Component> getComponents() {
        return components;
    }
    public double getB() {
        return b;
    }
    public void setB(double b) {
        this.b = b;
    }
    public void addValue(double value){
        valueHistory.add(value);
    }
    public ArrayList<Double> getValueHistory(){
        return valueHistory;
    }
    public final void run()
    {
        System.out.println( "system run" );
    }


}