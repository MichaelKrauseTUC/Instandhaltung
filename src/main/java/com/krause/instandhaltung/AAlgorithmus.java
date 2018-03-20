package com.krause.instandhaltung;

import java.util.ArrayList;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

public abstract class AAlgorithmus implements IAlgorithmus {

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
	private ArrayList<DoubleMatrix2D> lsgHistory;
	private ArrayList<DoubleMatrix2D> leistungHistory;
	private ArrayList<IKomponente> komponenten;

	public void verlaeufeUeberZeitPlotten() {
		// for (int i = 0; i < beststate.getComponents().size(); i++) {
		// Component c = beststate.getComponents().get(i);
		//
		// CWindow w = new CWindow("g" + i, "g", zeit, c.getgHistory());
		//
		// }
		// // visualisierung des verlaufs zfwOpt �ber die Zeit
		// CWindow w = new CWindow("f", "f", numPeriods, beststate.getValueHistory());
	}

}
