package com.krause.instandhaltung;

import java.util.ArrayList;

/**
 * Interface stellt sicher, dass man von jeder Komponente die Leistung auslesen
 * und einen Zeitschritt durchf�hren kann
 * 
 * @author mkrause
 */
public interface IKomponente {

	/**
	 * 
	 * @return aktuelle Leistung einer Komponente
	 */
	public double getLeistung();

	/**
	 * Leistung der Komponente ver�ndert sich in Abh�ngigkeit der Summe, die
	 * investiert wird, und dem Verschleiss
	 * 
	 * @param invest
	 *            Summe, die in Komponente investiert wird
	 */
	public void zeitschrittDurchfuehren(double invest);

	/**
	 * Leistung wird gesetzt
	 * 
	 * @param leistung
	 */
	public void setLeistung(Double leistung);
	
	public ArrayList<Double> getLeistungHistory();
}
