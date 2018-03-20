package com.krause.instandhaltung;

import java.util.ArrayList;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
/**
 * Einfacher Testalgorithmus, um zu sehen, ob generelle Funktionsweise klappt
 * @author mkrause
 *
 */
public class CAlgorithmusTest implements IAlgorithmus {

	private CZustand zustand;
	private final int zeit = 1;
	private ArrayList<Double> invs;
	private double zfw;

	@Override
	public void initialisieren() {
		CKomponente c1 = new CKomponente(new CKonstanterVerschleiss(0.5), new CKonstanterInvestEinfluss());
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonstanterInvestEinfluss());
		ArrayList<IKomponente> komponenten = new ArrayList<>();
		ArrayList<Double> anfangsLeistung = new ArrayList<>();
		anfangsLeistung.add(1.0);
		anfangsLeistung.add(1.0);
		for (IKomponente komp : komponenten) {
			komp.setLeistung(anfangsLeistung.get(komponenten.indexOf(komp)));
		}
		komponenten.add(c1);
		komponenten.add(c2);
		CSerienSystem serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(1.0, serSys);
	}

	@Override
	public double getZielfunktionswert() {
		return zfw;
	}



	@Override
	public void ausfuehren() {
		double inv1 = 0.1;
		double inv2 = 0.3;
		invs = new ArrayList<>();
		invs.add(inv1);
		invs.add(inv2);
		DoubleMatrix1D invs1D=new DenseDoubleMatrix1D(2);
		invs1D.set(0, inv1);
		invs1D.set(1, inv2);
		zfw = zustand.getSystem().strukturfunktionBerechnen();
		zustand.zustandsuebergang(invs1D);
		zfw += zustand.getSystem().strukturfunktionBerechnen();
	}

	@Override
	public ArrayList<DoubleMatrix2D> getHistory() {
		return null;

	}

	@Override
	public DoubleMatrix2D getLoesung() {
		DoubleMatrix2D lsg = new DenseDoubleMatrix2D(2, 1);
		for (int i = 0; i < invs.size(); i++) {
			lsg.set(i, zeit-1, invs.get(i));
		}
		return lsg;
	}

	@Override
	public void verlaeufeUeberZeitPlotten() {
		// TODO Auto-generated method stub
		
	}

}
