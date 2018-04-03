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

		this.zustand = new CZustand(gesamtBudget, serSys);
		anzKomponenten = zustand.getSystem().getKomponenten().size();
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		lsgHistory = new ArrayList<>();
		grenznutzen = new ArrayList<>();
		komponentenListeCPlus = new ArrayList<>();
	}

	@Override
	public void ausfuehren() {
		grenznutzen = grenznutzenBerechnen();
	}

	private ArrayList<Double> grenznutzenBerechnen() {
		double zfwVorher = serSys.strukturfunktionBerechnen();
		for (int i = 0; i < anzKomponenten; i++) {
			IKomponente hilfskomp = komponenten.get(i).clone();
			IKomponente originalkomp = komponenten.get(i);
			hilfskomp.zeitschrittDurchfuehren(0.01);
			serSys.komponenten.remove(i);
			serSys.komponenten.add(i, hilfskomp);
			double zfwNachher = serSys.strukturfunktionBerechnen();
			grenznutzen.add(zfwNachher - zfwVorher);
			serSys.komponenten.remove(i);
			serSys.komponenten.add(i,originalkomp);
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
