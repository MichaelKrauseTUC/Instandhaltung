package com.krause.instandhaltung.leistungsentwicklung;

/**
 * Testklasse, die die Leistung einer Komponente um genau den
 * Investitionseinsatz anhebt
 * 
 * @author mkrause
 *
 */
public class CKonstanterInvestEinfluss implements IInvestEinfluss {
	/**
	 * berechnet Investitionseinfluss (z. B. konkave Nutzenfunktion)
	 * 
	 * @return Einfluss der Investition auf Komponente
	 */


	@Override
	public double getInvestEinfluss(double inv, double leistung, double erwVerschleiss) {
		// TODO Auto-generated method stub
		return inv;
	}

}
