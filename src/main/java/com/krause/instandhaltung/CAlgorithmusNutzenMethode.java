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
	private int anzahlIterationenEinzelperiode;
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
	private Double[] zfwPeriode;

	public CAlgorithmusNutzenMethode(double anfangsbudget, int iter, int zeit) {
		this.anfangsbudget = anfangsbudget;
		this.anzahlIterationenEinzelperiode = iter;
		this.zeit = zeit;
		initialisieren();
	}

	@Override
	public void initialisieren() {

		CKomponente c1 = new CKomponente(new CVerschleissNormalverteilt(0.39, 0.01), new CKonkaverInvestEinflussExponential(4),
				1.0);
		CKomponente c2 = new CKomponente(new CVerschleissNormalverteilt(0.4, 0.01), new CKonkaverInvestEinflussExponential(4),
				1.0);
		CKomponente c3 = new CKomponente(new CVerschleissNormalverteilt(0.41, 0.01), new CKonkaverInvestEinflussExponential(4),
				1.0);
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
		zfwPeriode = new Double[zeit];
	}

	@Override
	public void ausfuehren() {
		double aufteilungBudget = GRANULARITAET;
		for (int t = 0; t < zeit; t++) {
			algorithmusEinzelperiode(0);
			zfwPeriode[t]=this.getZielfunktionswert();
			zustand.zustandsuebergang(invs);
		}
		int kritischePeriode = kritischePeriodeBestimmen(zfwPeriode);
		
	}

	private int kritischePeriodeBestimmen(Double[] zielfunktionswertePerioden) {
		double minZfw=Double.MAX_VALUE;
		int minArgZfw=-1;
		for (int t = 0; t < zielfunktionswertePerioden.length; t++) {
			if (zielfunktionswertePerioden[t]<minZfw) {
				minZfw=zielfunktionswertePerioden[t];
				minArgZfw=t;
			}
		}
		return minArgZfw;
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

	public void algorithmusEinzelperiode(double periodenbudget)
	{
		double zfwSumme=0;
		for (int j = 0; j < anzahlIterationenEinzelperiode; j++) {
			restbudget = periodenbudget;
			budgetVerteilen(restbudget);
			lsgHistory.add(invs);
			invs = new DenseDoubleMatrix1D(anzKomponenten);
			invs.assign(0);
			zfwSumme+=zfw;
		}
		DoubleMatrix1D mittelwertKomponenten = new DenseDoubleMatrix1D(anzKomponenten);
		mittelwertKomponenten.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			for (int j = 0; j < anzahlIterationenEinzelperiode; j++) {
				mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) + lsgHistory.get(j).get(i));
			}
			mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) / anzahlIterationenEinzelperiode);
		}
		lsg = new DenseDoubleMatrix2D(anzKomponenten, 1);
		lsg.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			lsg.set(i, 0, mittelwertKomponenten.get(i));
		}
		zfwOpt=zfwSumme/anzahlIterationenEinzelperiode;
	}
	private void nutzenBerechnen(double b) {
		for (int j = 0; j < anzKomponenten; j++) {
			for (int i = 0; i < anzKomponenten; i++) {
				if (i == j)
					komponenten.get(i).zeitschrittDurchfuehren(invs.get(i) + b);
				else
					komponenten.get(i).zeitschrittDurchfuehren(invs.get(i));
			}
			nutzen[j] = serSys.strukturfunktionBerechnen();
			for (int i = 0; i < anzKomponenten; i++) {
				komponenten.get(i).leistungSchrittZurueck();
			}
		}

	}

	/**
	 * @return kleinstes b, dass die Ableitung des maximalen Grenznutzens nach unten
	 *         auf den zweitniedrigsten Wert drueckt
	 */

	@Override
	public double getZielfunktionswert() {
		// TODO Auto-generated method stub
		return this.zfwOpt;
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
