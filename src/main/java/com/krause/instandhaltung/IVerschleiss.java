package com.krause.instandhaltung;

/**
 * Interface stellt sicher, dass der Verschleiss einer Komponente ausgelesen
 * werden kann
 * 
 * @todo pr�fen, ob Verschleiss abh�ngig von aktueller Leistung sein kann ->
 *       dann Erweiterung der Methode um Inputparameter double leistung
 * @author mkrause
 *
 */
public interface IVerschleiss {
	/**
	 * 
	 * @return Verschleiss der Komponente
	 */

	public double getErwVerschleiss(double leistung);

	/**
	 * 
	 * @param leistung
	 *            aktuelle Leistung
	 * @return aktueller Verschleiss in Abh�ngigkeit der Leistung
	 */
	public double getVerschleiss(double leistung);
}
