package com.krause.instandhaltung;

import java.util.ArrayList;

import cern.colt.matrix.DoubleMatrix1D;
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
	 * generiert L�sung
	 * 
	 * @todo evtl. R�ckgabewert Matrix2D von colt (CERN)
	 */
	public DoubleMatrix2D getLoesung();

	/**
	 * soll L�sungshistorie zur�ckgeben, also ein Array<Matrix2D> oder so �hnlich
	 * @return 
	 * @todo coden 
	 */
	public ArrayList<DoubleMatrix2D> getHistory();
	
	public void verlaeufeUeberZeitPlotten();
}
