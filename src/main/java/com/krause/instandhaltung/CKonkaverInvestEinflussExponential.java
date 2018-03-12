package com.krause.instandhaltung;

/**
 * Klasse mit konkavem Investitionseinfluss (abnehmender Grenznutzen)
 * 
 * @author mkrause
 *
 */
public class CKonkaverInvestEinflussExponential implements IInvestEinfluss {

	private double skalierungsfaktor;

	/**
	 * 
	 * @param skalierungsfaktor
	 *            Skalierungsfaktor > 0 f�r den Einsatz des Geldes - umso h�her
	 *            desto mehr Anstieg der Leistung f�rs Geld
	 */
	public CKonkaverInvestEinflussExponential(double skalierungsfaktor) {
		this.skalierungsfaktor = skalierungsfaktor;
	}

	@Override
	/**
	 * berechnet den Einfluss des Geldes, sodass abnehmender Grenznutzen und
	 * Beschr�nkung auf ca. 100% bei der Komponente (im deterministischen Fall genau
	 * 100%)
	 */
	public double getInvestEinfluss(double inv, double leistung, double erwVerschleiss) {

		return (1 - Math.exp(-skalierungsfaktor * inv)) * (1 - leistung + erwVerschleiss);
	}

}
