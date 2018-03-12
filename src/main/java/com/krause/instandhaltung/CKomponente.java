package com.krause.instandhaltung;

/**
 * 
 * @author mkrause
 * 
 */

public class CKomponente implements IKomponente {

	private double leistung;

	/**
	 * 
	 * @param leistung
	 *            aktuelle Leistung der Komponente wird gesetzt
	 */
	public void setLeistung(double leistung) {
		this.leistung = leistung;
	}

	private IVerschleiss verschleiss;
	private IInvestEinfluss invEinfluss;

	/**
	 * 
	 * @param verschleiss
	 *            Verschleiss / Abnutzung einer Komponente bei einem Zeitschritt;
	 *            hier k�nnen auch sp�ter stochastische Einfl�sse eingearbeitet
	 *            werden; ggf. noch Abh�ngigkeit von Leistung integrieren?
	 * @param invEinfluss
	 *            Investitionseinfluss - hier kann sp�ter noch ein abnehmender
	 *            Grenznutzen eingearbeitet werden; ggf. noch Abh�ngigkeit von
	 *            Leistung integrieren?
	 */
	public CKomponente(IVerschleiss verschleiss, IInvestEinfluss invEinfluss) {
		this.verschleiss = verschleiss;
		this.invEinfluss = invEinfluss;
	}

	/**
	 * @return aktuelle Leistung der Komponente
	 */
	public double getLeistung() {
		return leistung;
	}

	/**
	 * Komponente f�hrt einen Zeitschritt durch
	 * 
	 * @todo erwarteten Verschleiss nicht durch 0 abfangen
	 */
	public void zeitschrittDurchfuehren(double invest) {
		leistung = leistung - verschleiss.getVerschleiss(leistung)
				+ invEinfluss.getInvestEinfluss(invest, leistung, verschleiss.getErwVerschleiss(leistung));
		if (leistung < 0)
			leistung = 0;
	}

	@Override
	/**
	 * aktuelle Leistung wird gesetzt
	 * 
	 * @param leistung
	 *            Leistung wird gesetzt (Double statt double einziger Unterschied zu
	 *            anderer Funktion
	 */
	public void setLeistung(Double leistung) {
		this.leistung = leistung;

	}
}
