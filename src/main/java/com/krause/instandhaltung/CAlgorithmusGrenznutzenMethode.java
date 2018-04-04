package com.krause.instandhaltung;

import java.util.ArrayList;
import java.util.Collections;

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
	private ArrayList<Double> grenznutzen;
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
		grenznutzen = new ArrayList<>();
		komponentenListeCPlus = new ArrayList<>();
	}

	@Override
	public void ausfuehren() {
		grenznutzen = grenznutzenBerechnen();
		double maxGrenznutzen = Collections.max(grenznutzen);
		System.out.println(maxGrenznutzen);
	}

	private ArrayList<Double> grenznutzenBerechnen() {
		double zfwVorher = serSys.strukturfunktionBerechnen();
		for (int i = 0; i < anzKomponenten; i++) {
			komponenten.get(i).zeitschrittDurchfuehren(0.01);
			double zfwNachher = serSys.strukturfunktionBerechnen();
			grenznutzen.add(zfwNachher - zfwVorher);
			komponenten.get(i).leistungSchrittZurueck();
		}
		return grenznutzen;
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
