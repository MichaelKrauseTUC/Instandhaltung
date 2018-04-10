package com.krause.instandhaltung.systemkomponenten;

import com.krause.instandhaltung.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * stellt sicher, dass System aus Komponenten gebildet werden und ein
 * Strukturfunktionswert gebildet werden kann
 * 
 * @author mkrause
 *
 */
public interface ISystem {
	
	
	/**
	 * berechnet den Wert der Strukturfunktion; Kern dessen, was ein System ausmacht
	 * 
	 * @return Wert der Strukturfunktion
	 */
	public double strukturfunktionBerechnen();
/**
 * 
 * @return Liste von Komponenten
 */
	public ArrayList<IKomponente> getKomponenten();
	
	public LinkedList<Double> getSystemleistungHistorie();
}
