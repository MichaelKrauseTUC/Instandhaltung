package com.krause.instandhaltung;

import java.util.ArrayList;
import java.util.Arrays;

public final class CMain {

    public static void main(final String[] p_args) {
        new CAlgorithmus(5, new CSystem2()).run();
        System.out.println();
        new CAlgorithmus(10, new CSystem()).run();
        int numComponents = 2;
        int numPeriods = 10;

        double maximum = Double.MIN_VALUE;
        int numIterations = 100000;
        CState beststate = null;        // zum speichern des besten Zustands

        for (int k = 0; k < numIterations; k++) {
            //Init:
            double B = 1.0;
            double[][] xit = generateXit(numComponents, numPeriods, B);    //x[i][t], i Komponentenindex, t Periodenindex
            //Parameter einer Komponente: a,g,ca,cg,ct, x
            Component c0 = new Component(0, 1, 0.1, 0.2, 0.3, xit[0]);    //x Werte f�r Komponente 0 �bergeben
            Component c1 = new Component(0, 1, 0.2, 0.3, 0.4, xit[1]);    ////x Werte f�r Komponente 1 �bergeben
//			Component c2 = new Component(0, 1, 0.1, 0.2, 0.3, xit[2]);
//			Component c3 = new Component(0, 1, 0.2, 0.3, 0.4, xit[3]);
            ArrayList<Component> components = new ArrayList<>();
            components.addAll(Arrays.asList(c0, c1));        //alle Komponenten hier einf�gen!

            CState state = new CState(components, B);
            double tmpMax = calc(state, numPeriods);
            //beste L�sung beibehalten
            if (tmpMax > maximum) {
                //Maximum und zugeh�rigen Zustand merken
                maximum = tmpMax;
                beststate = state;
            }


        }

        System.out.println("Maximum:" + maximum);        //Maximum ausgeben

        //Visualisierung der besten gi und ai
        for (int i = 0; i < beststate.getComponents().size(); i++) {
            Component c = beststate.getComponents().get(i);

            //Fenster f�r g erstellen, Parameter: Fenstertitel, Y-Achsenbeschriftung, X-Achsenbeschriftung, Anzahl Perioden, zu zeichnende Werte
            CWindow w = new CWindow("g" + i, "g", "t", numPeriods, c.getgHistory());
            //folgende Funktionen sind f�r die korrekte Darstellung des Fensters n�tig
            w.pack();
            RefineryUtilities.centerFrameOnScreen(w);
            w.setVisible(true);

            //Fenster f�r a
            CWindow w2 = new CWindow("a" + i, "a", "t", numPeriods, c.getaHistory());
            w2.pack();
            RefineryUtilities.centerFrameOnScreen(w2);
            w2.setVisible(true);
        }
        //Visualisierung der Zielfunktion
        CWindow w = new CWindow("f", "f", "t", numPeriods, beststate.getValueHistory());
        w.pack();
        RefineryUtilities.centerFrameOnScreen(w);
        w.setVisible(true);

        /**
         * Zufallszahlen mit Seed erzeugen
         */
//        Random r = new Random(21);
//        System.out.println(r.nextDouble());

    }

    public static double calc(CState state, int T) {
        double funcValue = 0;
        double minGi = Double.MAX_VALUE;
        for (Component c : state.getComponents()) {
            minGi = Math.min(minGi, c.getG());
        }
        state.addValue(minGi);
        for (int t = 1; t <= T; t++) {
            //Zustand updaten
            for (Component c : state.getComponents()) {
                double nextA = (1 + c.getA()) * Math.exp((-1 / c.getCa() * c.getXt()[t - 1]));
                double nextG = c.getG() - c.getG() * (c.getA() + 1) / (1 / c.getCt() * T) + (1 - c.getG() * (1 - (c.getA() + 1) / (1 / c.getCt() * T))) * (1 - Math.exp(-1 / c.getCg() * c.getXt()[t - 1]));
                c.setA(nextA);
                c.setG(nextG);
                state.setB(state.getB() - c.getXt()[t - 1]);
                minGi = Math.min(minGi, c.getG());
            }
            funcValue += minGi;
            //state.addValue(funcValue);
            state.addValue(minGi);    // Liste von Werten f�r die Visualisierung der Zielfunktion/minimalen gi
        }
        return funcValue;
    }

    public static double[][] generateXit(int maxI, int maxT, double B) {
        double[][] xit = new double[maxI][maxT];
        double sum = 0.0;
        //zuf�llig generieren und aufsummieren
        for (int i = 0; i < maxI; i++) {
            for (int t = 0; t < maxT; t++) {
                xit[i][t] = Math.random();
                sum += xit[i][t];
            }
        }

        //normieren
        for (int i = 0; i < maxI; i++) {
            for (int t = 0; t < maxT; t++) {
                xit[i][t] *= (B / sum);
            }
        }
        return xit;
    }
}