package com.krause.instandhaltung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

/**
 * 
 * 
 * @author mkrause
 *
 */
public class CAlgorithmusGrenznutzenMethode extends AAlgorithmus {

	private double granularitaet = 0.01;
	private CZustand zustand;
	private int zeit;
	private int anzahlIterationen;
	private double gesamtBudget;
	private ArrayList<Double> anfangsLeistung;

	private int anzKomponenten;
	private DoubleMatrix1D invs;
	private DoubleMatrix2D lsg;
	private DoubleMatrix2D lsgOpt;
	private DoubleFactory2D D = DoubleFactory2D.dense;
	private double zfw;
	private double zfwOpt;
	private Double[] grenznutzen;
	private ArrayList<DoubleMatrix2D> lsgHistory;
	private ArrayList<DoubleMatrix2D> leistungHistory;
	private ArrayList<IKomponente> komponenten;
	private CSerienSystem serSys;
	private ArrayList<Integer> komponentenListeCPlus;

	@Override
	public void initialisieren() {
		anfangsLeistung = new ArrayList<>();
		CKomponente c1 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonkaverInvestEinflussExponential(5),1.0);
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonkaverInvestEinflussExponential(4),1.0);
		CKomponente c3 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonkaverInvestEinflussExponential(6),0.9);
		komponenten = new ArrayList<>();
		komponenten.add(c1);
		komponenten.add(c2);
		komponenten.add(c3);
		for (IKomponente komp : komponenten) {
			komp.setLeistung(anfangsLeistung.get(komponenten.indexOf(komp)));
		}
		gesamtBudget = 1.0;
		serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(gesamtBudget, serSys);
		anzKomponenten = zustand.getSystem().getKomponenten().size();
		for (int i = 0; i < anzKomponenten; i++) {
			anfangsLeistung.add(komponenten.get(i).getLeistung());
		}
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		lsgHistory = new ArrayList<>();
		grenznutzen = new Double[anzKomponenten];
		komponentenListeCPlus = new ArrayList<>();
	}

	@Override
	public void ausfuehren() {
		grenznutzenBerechnen(granularitaet);
		int maxArgGrenznutzen=-1;
		double maxGrenznutzen=Double.MIN_VALUE;
		for (int i = 0; i < grenznutzen.length; i++) {
			if (grenznutzen[i]>maxGrenznutzen) {
				maxGrenznutzen=grenznutzen[i];
				maxArgGrenznutzen=i;
			}
		}
		komponentenListeCPlus.add(maxArgGrenznutzen);
		while (gesamtBudget>0) {
			if (komponentenListeCPlus.size()<anzKomponenten)
			{
				int zweitMaxArgGrenznutzen=-1;
				double zweitMaxGrenznutzen=Double.MIN_VALUE;
				for (int i = 0; i < grenznutzen.length; i++) {
					if (komponentenListeCPlus.contains(i))
						break;
					if (grenznutzen[i]>zweitMaxGrenznutzen) {
						zweitMaxGrenznutzen=grenznutzen[i];
						zweitMaxArgGrenznutzen=i;
					}
				}
				double b = kleinstesBBestimmen();
			}
		}
		System.out.println(maxGrenznutzen);
	}
/**
 * @todo Methode implementieren
 * @return
 */
	private double kleinstesBBestimmen() {
		double b=0;
		for (int i = 0; i < komponentenListeCPlus.size(); i++) {
			
		}
		return 0;
	}

	private void grenznutzenBerechnen(double inv) {
		double zfwVorher = serSys.strukturfunktionBerechnen();
		for (int i = 0; i < anzKomponenten; i++) {
			komponenten.get(i).zeitschrittDurchfuehren(inv);
			double zfwNachher = serSys.strukturfunktionBerechnen();
			grenznutzen[i]= zfwNachher - zfwVorher;
			komponenten.get(i).leistungSchrittZurueck();
		}
	}

	@Override
	public double getZielfunktionswert() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DoubleMatrix2D getLoesung() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<DoubleMatrix2D> getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verlaeufeUeberZeitPlotten() {
		// TODO Auto-generated method stub

	}

}
