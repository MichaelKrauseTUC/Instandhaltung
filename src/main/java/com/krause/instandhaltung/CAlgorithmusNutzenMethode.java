package com.krause.instandhaltung;

import java.util.ArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * 
 * 
 * @author mkrause
 * @todo es wird noch zu viel Gesamtbudget verteilt...
 */
public class CAlgorithmusNutzenMethode extends AAlgorithmus {

	public static final double GRANULARITAET = 0.5; // default: 0.01
	private CZustand zustand;
	private CZustand anfangszustand;
	private int zeit;
	private int anzahlIterationenEinzelperiode;
	private double anfangsbudget;
	private double restbudgetEinzel;
	private double restbudgetGesamt;
	private Double[] anfangsLeistung;

	private int anzKomponenten;
	private DoubleMatrix1D invs;
	private DoubleMatrix1D lsgPeriode;
	private DoubleMatrix2D lsgOpt;
	// private DoubleFactory2D D = DoubleFactory2D.dense;
	private double zfw;
	private double zfwOpt;
	private Double[] nutzen;
	private ArrayList<DoubleMatrix1D> lsgHistoryEinzel;
	private ArrayList<DoubleMatrix1D> lsgHistory;
	// private ArrayList<DoubleMatrix2D> leistungHistory;
	private ArrayList<IKomponente> komponenten;
	private CSerienSystem serSys;
	private Double[] zfwPeriode;
	private Double[] budgetPeriode;

	public CAlgorithmusNutzenMethode(double anfangsbudget, int iter, int zeit) {
		this.anfangsbudget = anfangsbudget;
		this.anzahlIterationenEinzelperiode = iter;
		this.zeit = zeit;
		initialisieren();
	}

	@Override
	public void initialisieren() {

		CKomponente c1 = new CKomponente(new CKonstanterVerschleiss(0.039), new CKonkaverInvestEinflussExponential(4.1),
				1.0);
		CKomponente c2 = new CKomponente(new CKonstanterVerschleiss(0.04), new CKonkaverInvestEinflussExponential(4),
				1.0);
		CKomponente c3 = new CKomponente(new CKonstanterVerschleiss(0.041), new CKonkaverInvestEinflussExponential(2),
				1.0);
		komponenten = new ArrayList<>();
		komponenten.add(c1);
		komponenten.add(c2);
		komponenten.add(c3);

		serSys = new CSerienSystem(komponenten);
		this.zustand = new CZustand(anfangsbudget, serSys);
		this.anfangszustand = new CZustand(anfangsbudget, serSys);

		anzKomponenten = zustand.getSystem().getKomponenten().size();
		// anfangsLeistung = new Double[anzKomponenten];
		// for (int i = 0; i < anzKomponenten; i++) {
		// anfangsLeistung[i] = komponenten.get(i).getLeistung();
		// }
		invs = new DenseDoubleMatrix1D(anzKomponenten);
		invs.assign(0);
		lsgOpt = new DenseDoubleMatrix2D(anzKomponenten, zeit);
		lsgOpt.assign(0);
		lsgHistoryEinzel = new ArrayList<>();
		lsgHistory = new ArrayList<>();
		nutzen = new Double[anzKomponenten];
		zfwPeriode = new Double[zeit];
		budgetPeriode = new Double[zeit];
		for (int t = 0; t < zeit; t++) {
			budgetPeriode[t] = 0.0;
		}
	}

	@Override
	public void ausfuehren() {
		zfwPeriode = zfwPeriodeInitialisieren();
		int kritischePeriode = kritischePeriodeBestimmen(zfwPeriode);
		double aufteilungBudget = GRANULARITAET;
		restbudgetGesamt = anfangsbudget;
		while (restbudgetGesamt > 0) {
			double periodenInvestition = Math.min(aufteilungBudget, restbudgetGesamt);
			kritischePeriode = hillClimbing(periodenInvestition, kritischePeriode);
			restbudgetGesamt -= periodenInvestition;
		}
	}

	private int hillClimbing(double budgetDelta, int kritischePeriode) {
		budgetPeriode[kritischePeriode] += budgetDelta;
		for (int t = 0; t < zeit; t++) {
			if (t == kritischePeriode) {
				algorithmusEinzelperiode(budgetPeriode[t]);
				zfwPeriode[t] = this.getZielfunktionswertPeriode();
				for (int i = 0; i < anzKomponenten; i++) {
					lsgOpt.set(i, t, this.getLoesungPeriode().get(i));
				}
				zustand.zustandsuebergang(invs);
			} else {
				for (int i = 0; i < anzKomponenten; i++) {
					invs.set(i, lsgOpt.get(i, t));
				}
				zustand.zustandsuebergang(invs);
				if (t > kritischePeriode) {
					zfwPeriode[t] = serSys.strukturfunktionBerechnen();
				}
			}
		}
		zustand.resetAnfangszustand();
		return kritischePeriodeBestimmen(zfwPeriode);
	}

	private Double[] zfwPeriodeInitialisieren() {
		for (int t = 0; t < zeit; t++) {
			zustand.zustandsuebergang(invs);
			zfwPeriode[t] = serSys.strukturfunktionBerechnen();
		}
		zustand.resetAnfangszustand();
		return zfwPeriode;
	}

	private int kritischePeriodeBestimmen(Double[] zielfunktionswertePerioden) {
		double minZfw = Double.MAX_VALUE;
		int minArgZfw = -1;
		for (int t = 0; t < zielfunktionswertePerioden.length; t++) {
			if (zielfunktionswertePerioden[t] < minZfw) {
				minZfw = zielfunktionswertePerioden[t];
				minArgZfw = t;
			}
		}
		return minArgZfw;
	}

	public void budgetVerteilen(double restbudget) {
		invs.assign(0);
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

	public void algorithmusEinzelperiode(double periodenbudget) {
		double zfwSumme = 0;
		for (int j = 0; j < anzahlIterationenEinzelperiode; j++) {
			restbudgetEinzel = periodenbudget;
			budgetVerteilen(restbudgetEinzel);
			lsgHistoryEinzel.add(invs);
			invs = new DenseDoubleMatrix1D(anzKomponenten);
			invs.assign(0);
			zfwSumme += zfw;
		}
		DoubleMatrix1D mittelwertKomponenten = new DenseDoubleMatrix1D(anzKomponenten);
		mittelwertKomponenten.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			for (int j = 0; j < anzahlIterationenEinzelperiode; j++) {
				mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) + lsgHistoryEinzel.get(j).get(i));
			}
			mittelwertKomponenten.set(i, mittelwertKomponenten.get(i) / anzahlIterationenEinzelperiode);
		}
		lsgPeriode = new DenseDoubleMatrix1D(anzKomponenten);
		lsgPeriode.assign(0);
		for (int i = 0; i < anzKomponenten; i++) {
			lsgPeriode.set(i, mittelwertKomponenten.get(i));
		}
		lsgHistory.add(lsgPeriode);
		zfwOpt = zfwSumme / anzahlIterationenEinzelperiode;
		lsgHistoryEinzel = new ArrayList<>();
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

	public double getZielfunktionswertPeriode() {
		return this.zfw;
	}

	@Override
	public double getZielfunktionswert() {
		return this.zfwPeriode[zeit - 1];
	}

	public DoubleMatrix1D getLoesungPeriode() {
		return this.lsgPeriode;
	}

	public DoubleMatrix2D getLoesung() {
		return this.lsgOpt;
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
