package com.krause.instandhaltung.systemkomponenten;

import com.krause.instandhaltung.*;

import cern.colt.matrix.DoubleMatrix1D;

/**
 * Klasse bildet den Zustand (also System und Restbudget) ab
 * 
 * @author mkrause
 * 
 */
public class CZustand {
	private ISystem system;

	private double budget;

	private int anzKomp;

	private double anfangsbudget;

	private double[] anfangsleistungen;

	/**
	 * Funktion wird ben�tigt, um Anfangszustand wiederherzustellen
	 * 
	 * @param budget
	 *            Budget wird f�r Zustand festgelegt
	 */
	public void setBudget(double budget) {
		this.budget = budget;
	}

	/**
	 * gibt System zur�ck (noch nicht die Komponenten selbst)
	 * 
	 * @return Objekt "ISystem"
	 */
	public ISystem getSystem() {
		return system;
	}

	/**
	 * 
	 * @return aktuelles Budget des Zustands
	 */
	public double getBudget() {
		return budget;
	}

	/**
	 * f�hrt einen Zustands�bergang des Systems durch
	 * 
	 * @param invs
	 *            Liste von Investitionen, die f�r den Zustands�bergang eingesetzt
	 *            werden
	 */
	public void zustandsuebergang(DoubleMatrix1D invs) {
		if (invs.size() == system.getKomponenten().size()) {
			double sum = invs.zSum();
			if (sum <= this.budget + CMain.FEHLERTOLERANZ) {
				for (int i = 0; i < invs.size(); i++) {
					system.getKomponenten().get(i).zeitschrittDurchfuehren(invs.get(i));
				}
				budget -= sum;
			}
		}
	}

	/**
	 * Konstruktur der Klasse; Zustand setzt sich zusammen aus einem System von
	 * Komponenten und einem (Rest-)Budget
	 * 
	 * @param budget
	 *            (Rest-)Budget
	 * @param system
	 *            Komponenten des Systems
	 */
	public CZustand(double budget, ISystem system) {
		this.system = system;
		this.budget = budget;
		this.anfangsbudget = budget;
		anzKomp = system.getKomponenten().size();
		anfangsleistungen = new double[anzKomp];
		for (int i = 0; i < anzKomp; i++) {
			anfangsleistungen[i] = system.getKomponenten().get(i).getLeistung();
		}
	}

	public void resetAnfangszustand() {
		budget = anfangsbudget;
		for (int i = 0; i < anzKomp; i++) {
			system.getKomponenten().get(i).setLeistung(anfangsleistungen[i]);
		}
	}

}
