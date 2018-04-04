package com.krause.instandhaltung;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * Klasse leitet sich von JFreeChart Application Frame ab und erzeugt Fenster
 * mit Koordinatensystemen, um L�sungsverlauf nachzuvollziehen
 * 
 * @author mkrause
 *
 */
public class CWindow extends ApplicationFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param title
	 *            Diagrammtitel
	 * @param titleYAxis
	 *            Beschriftung der Y-Achse
	 * @param numPeriods
	 *            Anzahl der Perioden (X-Achse)
	 * @param data
	 *            Liste von Double-Datenpunkten, die zu den Perioden geh�ren
	 */
	public CWindow(String title, String titleYAxis, int numPeriods, LinkedList<Double> data) {
		super(title);
		JFreeChart xylineChart = ChartFactory.createXYLineChart(title, "Time", titleYAxis,
				createDataset(numPeriods, data), PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(xylineChart);
		chartPanel.setPreferredSize(new Dimension(560, 367));
		setContentPane(chartPanel);
		this.fensterAufrufen();
	}

	/**
	 * 
	 * @param numPeriods
	 *            Anzahl Perioden
	 * @param data
	 *            zu den Perioden geh�rige Y-Werte
	 * @return Punktepaare, wobei nun X-Wert vom Typ double
	 * @todo get(i) von data später mit Iterator?
	 */
	private XYDataset createDataset(int numPeriods, List<Double> data) {
		final XYSeries series = new XYSeries("");
		for (int i = 0; i < numPeriods; i++) {
			series.add((double) (i), data.get(i));
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

	public void fensterAufrufen() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

	}
}
