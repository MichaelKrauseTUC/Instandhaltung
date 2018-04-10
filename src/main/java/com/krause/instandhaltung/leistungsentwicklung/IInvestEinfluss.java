package com.krause.instandhaltung.leistungsentwicklung;

import com.krause.instandhaltung.*;

/**
 * Interface stellt sicher, dass der Einfluss von Geld auf eine Komponente
 * gew�hrleistet ist
 * 
 * @author mkrause
 *
 */
public interface IInvestEinfluss {
	/**
	 * berechnet, um wieviel die Leistung in Abh�ngigkeit des Geldeinsatzes
	 * angehoben wird (konkave Funktion, abnehmender Grenznutzen)
	 * 
	 * @param inv
	 *            Geld, das in Komponente investiert wird
	 * @param leistung
	 *            aktuelle Leistung
	 * @param erwVerschleiss
	 *            aktueller erwarteter Verschleiss
	 * @return Wert der Nutzenfunktion
	 */
	public double getInvestEinfluss(double inv, double leistung, double erwVerschleiss);
}
