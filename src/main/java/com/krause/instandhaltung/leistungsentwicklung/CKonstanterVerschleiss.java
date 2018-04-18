package com.krause.instandhaltung.leistungsentwicklung;

/**
 * Testklasse, die die Leistung einer Komponente genau in H�he des �bergebenen
 * Wertes mindert
 * 
 * @author mkrause
 *
 */
public class CKonstanterVerschleiss implements IVerschleiss {
	private double verschleiss;

	/**
	 * initialisiert konstanten Verschleiss
	 * 
	 * @param verschleiss
	 *            konstanter Verschleiss
	 */
	public CKonstanterVerschleiss(double verschleiss) {
		this.verschleiss = verschleiss;
	}

	@Override
	/**
	 * Erwarteten Verschleiss zur�ckgeben, ggf. in Abh�ngigkeit der Leistung
	 * 
	 * @return verschleiss
	 */
	public double getErwVerschleiss(double leistung) {
		return verschleiss;
	}

	@Override
	/**
	 * @return verschleiss Verschlei� f�r Komponente, ggf. in Abh�ngigkeit der
	 *         Leistung
	 */
	public double getVerschleiss(double leistung) {
		return verschleiss;
	}

}
