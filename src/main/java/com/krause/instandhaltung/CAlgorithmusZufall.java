package com.krause.instandhaltung;

import java.util.ArrayList;
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
public class CAlgorithmusZufall extends AAlgorithmus {

	private CZustand zustand;
	private final int zeit = 3;
	private int anzahlIterationen;
	private final double gesamtBudget = 1.0;
	private ArrayList<Double> anfangsLeistung;

	private int anzKomponenten;
	private DoubleMatrix1D invs;
	private DoubleMatrix2D lsg;
	private DoubleMatrix2D lsgOpt;
	private DoubleFactory2D D = DoubleFactory2D.dense;
	private double zfw;
	private double zfwOpt;
	private ArrayList<DoubleMatrix2D> lsgHistory;
	private ArrayList<IKomponente> komponenten;

	/**
	 * Konstruktur f�r Zufallsalgorithmus (3-Komponenten-Seriensystem)
	 * 
	 * @param anzahlIterationen
	 *            Anzahl der Iterationen, die der Zufallsalgorithmus laufen soll
	 */
	public CAlgorithmusZufall(int anzahlIterationen) {
		this.anzahlIterationen = anzahlIterationen;
	}

	@Override
	public void initialisieren() {
		anfangsLeistung = new ArrayList<>();
		anfangsLeistung.add(1.0);
		anfangsLeistung.add(1.0);
		anfangsLeistung.add(0.9);
		CKomponente c1 = new CKomponente(new CVerschleissNormalverteilt(0.4, 0.4),
				new CKonkaverInvestEinflussExponential(5));
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonstanterInvestEinfluss());
		CKomponente c3 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonstanterInvestEinfluss());
		komponenten = new ArrayList<>();
		komponenten.add(c1);
		komponenten.add(c2);
		komponenten.add(c3);
		for (IKomponente komp : komponenten) {
			komp.setLeistung(anfangsLeistung.get(komponenten.indexOf(komp)));
		}
		CSerienSystem serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(gesamtBudget, serSys);
		anzKomponenten = zustand.getSystem().getKomponenten().size();
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		lsgHistory = new ArrayList<>();
	}

	@Override
	public double getZielfunktionswert() {
		return zfwOpt;
	}

	@Override
	public void ausfuehren() {
		zfwOpt = 0;
		for (int iter = 0; iter < anzahlIterationen; iter++) {
			innererAlgorithmus();
			if (zfwOpt < zfw) {
				zfwOpt = zfw;
				lsgOpt = (DoubleMatrix2D) lsg.clone();
				lsgHistory.add(this.getLoesung());
			}
			zustand.setBudget(gesamtBudget);
			for (IKomponente komp : komponenten) {
				komp.setLeistung(anfangsLeistung.get(komponenten.indexOf(komp)));
			}
		}
	}

	private void innererAlgorithmus() {
		zfw = 0;
		lsg = D.random(anzKomponenten, zeit);
		// L�sungsmatrix normieren, damit Gesamtbudget von 1 nicht �berschritten wird
		double sum = lsg.zSum();
		for (int i = 0; i < anzKomponenten; i++) {
			for (int t = 0; t < zeit; t++) {
				lsg.set(i, t, lsg.get(i, t) / sum);
			}
		}

		for (int t = 0; t < zeit; t++) {
			for (int i = 0; i < anzKomponenten; i++) {
				invs.set(i, lsg.get(i, t));
			}
			zustand.zustandsuebergang(invs);
			zfw += zustand.getSystem().strukturfunktionBerechnen();
		}
	}

	@Override
	public ArrayList<DoubleMatrix2D> getHistory() {
		return lsgHistory;
	}

	@Override
	public DoubleMatrix2D getLoesung() {
		return lsgOpt;
	}

}
