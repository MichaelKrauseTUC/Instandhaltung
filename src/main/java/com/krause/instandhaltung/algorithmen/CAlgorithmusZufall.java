package com.krause.instandhaltung.algorithmen;

import java.util.ArrayList;
import java.util.LinkedList;

import com.krause.instandhaltung.*;
import com.krause.instandhaltung.leistungsentwicklung.*;
import com.krause.instandhaltung.systemkomponenten.*;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

/**
 * 
 * 
 * @author mkrause
 *
 */
public class CAlgorithmusZufall implements IAlgorithmus {

	private CZustand m_zustand;
	private final int m_zeit = 3;
	private final int m_anzahlIterationen;
	private final double m_gesamtBudget = 1.0;
	private final ArrayList<Double> m_anfangsLeistung = new ArrayList<>();

	private CSerienSystem m_serSys;
	private int m_anzKomponenten;
	private DoubleMatrix1D m_invs;
	private DoubleMatrix2D m_lsg;
	private DoubleMatrix2D m_lsgOpt;
	private final DoubleFactory2D m_D = DoubleFactory2D.dense;
	private double m_zfw;
	private double m_zfwOpt;
	private final ArrayList<DoubleMatrix2D> m_lsgHistory = new ArrayList<>();
	private final ArrayList<IKomponente> m_komponenten = new ArrayList<>();
	private LinkedList<LinkedList<Double>> m_leistungsHistorien = new LinkedList<>();
	private LinkedList<Double> m_leistungsHistorieC1 = new LinkedList<>();
	private LinkedList<Double> m_leistungsHistorieC2 = new LinkedList<>();
	private LinkedList<Double> m_leistungsHistorieC3 = new LinkedList<>();
	private LinkedList<Double> m_leistungsHistorieSystem = new LinkedList<>();

	/**
	 * Konstruktor fuer Zufallsalgorithmus (3-Komponenten-Seriensystem)
	 * 
	 * @param p_anzahlIterationen
	 *            Anzahl der Iterationen, die der Zufallsalgorithmus laufen soll
	 */
	public CAlgorithmusZufall(int p_anzahlIterationen) {
		m_anzahlIterationen = p_anzahlIterationen;
	}

	@Override
	public void initialisieren() {
		m_komponenten.add(new CKomponente(new CKonstanterVerschleiss(0.4), new CKonstanterInvestEinfluss(), 1.0));
		m_komponenten.add(new CKomponente(new CKonstanterVerschleiss(0.3), new CKonstanterInvestEinfluss(), 0.9));
		m_komponenten.add(new CKomponente(new CVerschleissNormalverteilt(0.4, 0.4),
				new CKonkaverInvestEinflussExponential(5), 1.0));
		m_serSys = new CSerienSystem(m_komponenten);
		m_zustand = new CZustand(m_gesamtBudget, m_serSys);
		m_anzKomponenten = m_zustand.getSystem().getKomponenten().size();
		for (int i = 0; i < m_anzKomponenten; i++) {
			m_anfangsLeistung.add(m_komponenten.get(i).getLeistung());
		}
		m_invs = new DenseDoubleMatrix1D(m_anzKomponenten);
		for (int i = 0; i < m_anzKomponenten; i++) {
			m_leistungsHistorien.add(new LinkedList<Double>());
		}
	}

	@Override
	public double getZielfunktionswert() {
		return m_zfwOpt;
	}

	@Override
	public void ausfuehren() {
		m_zfwOpt = 0;
		for (int iter = 0; iter < m_anzahlIterationen; iter++) {
			innererAlgorithmus();
			if (m_zfwOpt < m_zfw) {
				m_zfwOpt = m_zfw;
				m_lsgOpt = (DoubleMatrix2D) m_lsg.clone();
				m_lsgHistory.add(this.getLoesung());
				for (int i = 0; i < m_anzKomponenten; i++) {
					m_leistungsHistorien.set(i, m_komponenten.get(i).getLeistungHistory()) ;
				}
				m_leistungsHistorieSystem = m_serSys.getSystemleistungHistorie();

			}
			m_zustand.setBudget(m_gesamtBudget);
			for (IKomponente komp : m_komponenten) {
				komp.setLeistung(m_anfangsLeistung.get(m_komponenten.indexOf(komp)));
			}
		}
	}

	private void innererAlgorithmus() {
		m_zfw = 0;
		m_lsg = m_D.random(m_anzKomponenten, m_zeit);
		// Loesungsmatrix normieren, damit Gesamtbudget von 1 nicht ueberschritten wird
		double sum = m_lsg.zSum();
		for (int i = 0; i < m_anzKomponenten; i++) {
			for (int t = 0; t < m_zeit; t++) {
				m_lsg.set(i, t, m_lsg.get(i, t) / sum);
			}
		}

		for (int t = 0; t < m_zeit; t++) {
			for (int i = 0; i < m_anzKomponenten; i++) {
				m_invs.set(i, m_lsg.get(i, t));
			}
			m_zustand.zustandsuebergang(m_invs);
			m_zfw += m_zustand.getSystem().strukturfunktionBerechnen();
		}
	}

	@Override
	public ArrayList<DoubleMatrix2D> getHistory() {
		return m_lsgHistory;
	}

	@Override
	public DoubleMatrix2D getLoesung() {
		return m_lsgOpt;
	}

	@Override
	public void verlaeufeUeberZeitPlotten() {
		for (int i = 0; i < m_anzKomponenten; i++) {
			new CWindow("g" + (i + 1), "g", m_zeit, m_leistungsHistorien.get(i));
		}

		new CWindow("f", "f", m_zeit, m_leistungsHistorieSystem);

	}

}
