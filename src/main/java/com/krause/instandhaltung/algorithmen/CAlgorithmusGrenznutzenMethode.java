package com.krause.instandhaltung.algorithmen;

import java.util.ArrayList;

import com.krause.instandhaltung.*;
import com.krause.instandhaltung.leistungsentwicklung.*;
import com.krause.instandhaltung.systemkomponenten.*;


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
public class CAlgorithmusGrenznutzenMethode implements IAlgorithmus {

	/**
	 * GRANULARITAET beschreibt, wie kleinteilig die Lösung gesucht wird; bei einem Budget von 1.0 kann eine Granularität 
	 * von 0.01 als Prozentschritte interpretiert werden
	 */
	public static final double GRANULARITAET = 0.01;
	private CZustand zustand;
	private int zeit;
	private int anzahlIterationen;
	private double gesamtBudget;
	private Double[] anfangsLeistung;

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
	private int zweitMaxArgGrenznutzen;
	private double zweitMaxGrenznutzen;

	@Override
	public void initialisieren() {

		CKomponente c1 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonkaverInvestEinflussExponential(3.9),
				1.0);
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.4), new CKonkaverInvestEinflussExponential(4),
				1.0);
		CKomponente c3 = new CKomponente(new CKonstanterVerschleiss(0.3), new CKonkaverInvestEinflussExponential(4.1),
				1.0);
		komponenten = new ArrayList<>();
		komponenten.add(c1);
		komponenten.add(c2);
		komponenten.add(c3);

		gesamtBudget = 1.0;
		serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(gesamtBudget, serSys);

		anzKomponenten = zustand.getSystem().getKomponenten().size();
		anfangsLeistung = new Double[anzKomponenten];
		for (int i = 0; i < anzKomponenten; i++) {
			anfangsLeistung[i] = komponenten.get(i).getLeistung();
		}
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		invs.assign(0);
		lsgHistory = new ArrayList<>();
		grenznutzen = new Double[anzKomponenten];
		komponentenListeCPlus = new ArrayList<>();
	}

	@Override
	public void ausfuehren() {
		grenznutzenBerechnen(GRANULARITAET);
		int maxArgGrenznutzen = -1;
		double maxGrenznutzen = -Double.MAX_VALUE;
		for (int i = 0; i < grenznutzen.length; i++) {
			if (grenznutzen[i] > maxGrenznutzen) {
				maxGrenznutzen = grenznutzen[i];
				maxArgGrenznutzen = i;
			}
		}
		komponentenListeCPlus.add(maxArgGrenznutzen);
		while (gesamtBudget > 0) {
			double b = 0;
			if (komponentenListeCPlus.size() <= anzKomponenten) {
				zweitMaxArgGrenznutzen = -1;
				zweitMaxGrenznutzen = -Double.MAX_VALUE;
				for (int i = 0; i < grenznutzen.length; i++) {
					if (komponentenListeCPlus.contains(i))
						break;
					if (grenznutzen[i] > zweitMaxGrenznutzen) {
						zweitMaxGrenznutzen = grenznutzen[i];
						zweitMaxArgGrenznutzen = i;
					}
				}
				b = kleinstesBBestimmen(gesamtBudget);
			}
			if ((komponentenListeCPlus.size() == anzKomponenten) || (b > gesamtBudget)) {
				b = gesamtBudget;
			} else
				komponentenListeCPlus.add(zweitMaxArgGrenznutzen);
			gesamtBudget -= b;
		}
		System.out.println(maxGrenznutzen);
	}

	/**
	 * @return kleinstes b, dass die Ableitung des maximalen Grenznutzens nach unten
	 *         auf den zweitniedrigsten Wert drueckt
	 */
	private double kleinstesBBestimmen(double maxBudget) {
		double b = 0;
		for (int j = 0; j < komponentenListeCPlus.size(); j++) {
			int i = komponentenListeCPlus.get(j);
			while (true) {
				if (b + GRANULARITAET <= maxBudget) {
//					if (b >= 0.01)
//						System.out.println("jetzt gucken");
					invs.set(i, invs.get(i) + GRANULARITAET);
					b += GRANULARITAET;
					double grenzNutzenEinzel = grenznutzenBerechnenEinzel(komponenten.get(i), invs.get(i));
					if ((zweitMaxGrenznutzen <= grenzNutzenEinzel + CMain.FEHLERTOLERANZ)
							&& (zweitMaxGrenznutzen >= grenzNutzenEinzel - CMain.FEHLERTOLERANZ))
						break;
				} else
					break;
			}
		}
		return b;
	}

	private void grenznutzenBerechnen(double inv) {
		double zfwVorher = serSys.strukturfunktionBerechnen();
		for (int i = 0; i < anzKomponenten; i++) {
			komponenten.get(i).zeitschrittDurchfuehren(inv);
			double zfwNachher = serSys.strukturfunktionBerechnen();
			grenznutzen[i] = (zfwNachher - zfwVorher)/inv;
			komponenten.get(i).leistungSchrittZurueck();
		}
	}

	private double grenznutzenBerechnenEinzel(IKomponente komp, double inv) {
		double zfwVorher = serSys.strukturfunktionBerechnen();
		komp.zeitschrittDurchfuehren(inv);
		double zfwNachher = serSys.strukturfunktionBerechnen();
		komp.leistungSchrittZurueck();
		return ((zfwNachher - zfwVorher)/inv);
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
