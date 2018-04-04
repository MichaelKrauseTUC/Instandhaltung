package com.krause.instandhaltung;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * 
 * @author mkrause
 * @version 1.0
 * 
 */

public class CMain {
	/**
	 * 
	 * @param args
	 *            zur Zeit werden noch keine Parameter �bergeben
	 * @todo um grafische Funktionalit�t erweitern
	 * 
	 */

	public static final double FEHLERTOLERANZ = 0.0000001;

	/**
	 * 
	 * @param args
	 *            keine Inputparameter definiert
	 */
	public static void main(String[] args) {

		CAlgorithmusGrenznutzenMethode algo3 = new CAlgorithmusGrenznutzenMethode();
		algo3.initialisieren();
		algo3.ausfuehren();
//		CAlgorithmusZufall algo2 = new CAlgorithmusZufall(100);
//		algo2.initialisieren();
//		algo2.ausfuehren();
//		double zfw = algo2.getZielfunktionswert();
//		System.out.println(zfw);
//		DoubleMatrix2D lsg = algo2.getLoesung();
//		System.out.println(lsg);
//		algo2.verlaeufeUeberZeitPlotten();
		// ArrayList<DoubleMatrix2D> lsgHistory = algo2.getHistory();

		// CAlgorithmusTest algo1 = new CAlgorithmusTest();
		// algo1.initialisieren();
		// algo1.ausfuehren();
		// double zfw = algo1.getZielfunktionswert();
		// System.out.println(zfw);
		// DoubleMatrix2D lsg = algo1.getLoesung();
		// System.out.println(lsg);
	}
}
