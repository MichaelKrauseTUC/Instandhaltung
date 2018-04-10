package com.krause.instandhaltung.systemkomponenten;

import com.krause.instandhaltung.*;

import java.util.ArrayList;

/**
 * Klasse bildet konkretes System mit zwei und mehr Komponenten ab
 * 
 * @author mkrause
 * 
 */
public class CSerienSystem extends ASystem {
	/**
	 * Konstruktur, um Seriensystem zu initialisieren; genauso wie bei jedem anderen
	 * System
	 * 
	 * @param komponenten
	 *            Komponenten des Systems
	 */
	public CSerienSystem(ArrayList<IKomponente> komponenten) {
		super(komponenten);
	}

	/**
	 * berechnet aus den Leistungen den Wert der Strukturfunktion; bei einem
	 * Seriensystem ist das das Minimum �ber alle Komponentenleistungen
	 */
	@Override
	public double strukturfunktionBerechnen() {
		double min = Double.MAX_VALUE;
		for (IKomponente iKomponente : komponenten) {
			if (iKomponente.getLeistung() < min)
				min = iKomponente.getLeistung();
		}
		systemleistungHistorie.add(min);
		return min;
	}

}
