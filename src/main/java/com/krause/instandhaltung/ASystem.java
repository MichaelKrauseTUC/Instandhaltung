package com.krause.instandhaltung;

import java.util.ArrayList;

/**
 * Abstrakte Klasse, um Feld "komponenten" nicht neu anlegen zu m�ssen
 * 
 * @author mkrause
 * @brief Kurzbeschreibung
 * 
 * 
 */
public abstract class ASystem implements ISystem {
	/**
	 * @param komponenten
	 *            Liste von Komponenten, die sich im System befinden
	 */
	protected ArrayList<IKomponente> komponenten;

	protected ArrayList<Double> systemleistungHistorie;

	public ArrayList<Double> getSystemleistungHistorie() {
		return systemleistungHistorie;
	}

	/**
	 * initialisiert ein System mit dessen Komponenten
	 * 
	 * @param komponenten
	 *            Liste von Komponenten des Systems
	 */
	public ASystem(ArrayList<IKomponente> komponenten) {
		this.komponenten = komponenten;
		systemleistungHistorie = new ArrayList<>();
	}

	/**
	 * @return gibt die Liste der Komponenten zur�ck
	 */
	public ArrayList<IKomponente> getKomponenten() {
		return this.komponenten;
	}
}
