package com.krause.instandhaltung.algorithmen;

import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * 
 * @author mkrause
 *
 */
public interface IAlgorithmus {
	/**
	 * Komponenten werden mit Werten Anfangswerten belegt, System wird aufgebaut
	 * (welche Strukturfunktion?) und Budget wird initialisiert
	 */
	public void initialisieren();

	/**
	 * Algorithmus l�uft durch und erzeugt L�sung und Zielfunktionswert
	 * 
	 * @todo in Abh�ngigkeit von Zeit t?
	 * @todo mit parallelen Threads arbeiten (Interface Runnable...), insbesondere
	 *       bei Approximativer Dynamischer Programmierung
	 */
	public void ausfuehren();

	/**
	 * 
	 * @return Zielfunktionswert nach Durchlauf
	 */
	public double getZielfunktionswert();

	/**
	 * @return Loesung x[i,t] des Algorithmus
	 * 
	 */
	public DoubleMatrix2D getLoesung();

	/**
	 * @return Loesungshistorie x[i,t] für jede Iteration
	 */
	public List<DoubleMatrix2D> getHistory();
	
	public void verlaeufeUeberZeitPlotten();
}
