package com.krause.instandhaltung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * 
 * 
 * @author mkrause
 *
 */
public class CAlgorithmusNutzenMethode extends AAlgorithmus {

	public static final double GRANULARITAET = 0.01;
	private CZustand zustand;
	private int zeit;
	private int anzahlIterationen;
	private double anfangsbudget;
	private double restbudget;
	private Double[] anfangsLeistung;

	private int anzKomponenten;
	private DoubleMatrix1D invs;
	private DoubleMatrix2D lsg;
	private DoubleMatrix2D lsgOpt;
	private DoubleFactory2D D = DoubleFactory2D.dense;
	private double zfw;
	private double zfwOpt;
	private Double[] nutzen;
	private ArrayList<DoubleMatrix1D> lsgHistory;
	private ArrayList<DoubleMatrix2D> leistungHistory;
	private ArrayList<IKomponente> komponenten;
	private CSerienSystem serSys;

	public CAlgorithmusNutzenMethode(double anfangsbudget, int iter, int zeit) {
		this.anfangsbudget = anfangsbudget;
		this.anzahlIterationen = iter;
		this.zeit = zeit;
	}

	@Override
	public void initialisieren() {

		CKomponente c1 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonkaverInvestEinflussExponential(3.9),
				1.0);
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonkaverInvestEinflussExponential(4),
				1.0);
		CKomponente c3 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonkaverInvestEinflussExponential(4.1),
				0.9);
		komponenten = new ArrayList<>();
		komponenten.add(c1);
		komponenten.add(c2);
		komponenten.add(c3);

		serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(anfangsbudget, serSys);

		anzKomponenten = zustand.getSystem().getKomponenten().size();
		anfangsLeistung = new Double[anzKomponenten];
		for (int i = 0; i < anzKomponenten; i++) {
			anfangsLeistung[i] = komponenten.get(i).getLeistung();
		}
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		invs.assign(0);
		lsgHistory = new ArrayList<>();
		nutzen = new Double[anzKomponenten];
		// komponentenListeCPlus = new ArrayList<>();
	}

	@Override
	public void ausfuehren() {
		for (int j = 0; j < anzahlIterationen; j++) {
			restbudget = anfangsbudget;
			budgetVerteilen(restbudget);
			lsgHistory.add(invs);
			invs.assign(0);
		}
		DoubleMatrix1D mittelwertKomponenten = new DenseDoubleMatrix1D(anzKomponenten);
		mittelwertKomponenten.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			for (int j = 0; j < anzahlIterationen; j++) {
				mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) + lsgHistory.get(j).get(i));
			}
			mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) / anzahlIterationen);
		}
		lsg = new DenseDoubleMatrix2D(anzKomponenten, 1);
		lsg.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			lsg.set(i, 0, mittelwertKomponenten.get(i));
		}
	}

	public void budgetVerteilen(double restbudget) {
		while (restbudget > 0) {
			double b = Math.min(GRANULARITAET, restbudget);
			nutzenBerechnen(b);
			int maxArgNutzen = -1;
			double maxNutzen = -Double.MAX_VALUE;
			for (int i = 0; i < nutzen.length; i++) {
				if (nutzen[i] > maxNutzen) {
					maxNutzen = nutzen[i];
					maxArgNutzen = i;
				}
			}
			invs.set(maxArgNutzen, invs.get(maxArgNutzen) + b);
			restbudget -= b;
			this.zfw = maxNutzen;
		}
	}

	/**
	 * @return kleinstes b, dass die Ableitung des maximalen Grenznutzens nach unten
	 *         auf den zweitniedrigsten Wert drueckt
	 */

	private void nutzenBerechnen(double inv) {
		for (int i = 0; i < anzKomponenten; i++) {
			komponenten.get(i).zeitschrittDurchfuehren(inv);
			double zfwNachher = serSys.strukturfunktionBerechnen();
			nutzen[i] = zfwNachher;
			komponenten.get(i).leistungSchrittZurueck();
		}
	}

	@Override
	public double getZielfunktionswert() {
		// TODO Auto-generated method stub
		return this.zfw;
	}

	public DoubleMatrix2D getLoesung() {
		return this.lsg;
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
