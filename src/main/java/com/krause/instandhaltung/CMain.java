package com.krause.instandhaltung;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.pmw.tinylog.Logger;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.FunctionsUtils;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.NewtonUnconstrained;
import com.joptimizer.optimizers.OptimizationRequest;
import com.joptimizer.optimizers.OptimizationResponse;
import com.krause.instandhaltung.algorithmen.*;

import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.impl.*;

/**
 * 
 * @author mkrause
 * @version 1.0
 * 
 */

public class CMain {
	/**
	 * 
	 * @param args
	 *            zur Zeit werden noch keine Parameter �bergeben
	 * @todo um grafische Funktionalit�t erweitern
	 * 
	 */

	public static final double FEHLERTOLERANZ = 0.0000001;
	private DenseDoubleAlgebra ALG = DenseDoubleAlgebra.DEFAULT;
	private DoubleFactory1D F1 = DoubleFactory1D.dense;
	private DoubleFactory2D F2 = DoubleFactory2D.dense;

	/**
	 * 
	 * @param args
	 *            keine Inputparameter definiert
	 * @throws JOptimizerException
	 */
	public static void main(String[] args) throws JOptimizerException {

		/**
		 * CAlgorithmusZufall algo2 = new CAlgorithmusZufall(100);
		 * algo2.initialisieren(); algo2.ausfuehren(); double zfw =
		 * algo2.getZielfunktionswert(); System.out.println(zfw); DoubleMatrix2D lsg =
		 * algo2.getLoesung(); System.out.println(lsg);
		 * algo2.verlaeufeUeberZeitPlotten(); ArrayList<DoubleMatrix2D> lsgHistory =
		 * algo2.getHistory(); Kommentar wieder wegmachen, wenn JOptimizer getestet!
		 **/

		CAlgorithmusGrenznutzenMethode algo1 = new CAlgorithmusGrenznutzenMethode();
		algo1.initialisieren();
		algo1.ausfuehren();
		double zfw = algo1.getZielfunktionswert();
		System.out.println(zfw);
		cern.colt.matrix.DoubleMatrix2D lsg = algo1.getLoesung();
		System.out.println(lsg);
	}

}
