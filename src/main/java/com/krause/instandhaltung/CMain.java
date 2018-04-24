package com.krause.instandhaltung;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

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
	private static Log log = LogFactory.getLog(CMain.class.getName());
	
	
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
		 * algo2.verlaeufeUeberZeitPlotten();
		 * 
		 * Kommentar wieder wegmachen, wenn JOptimizer getestet!
		 **/

		log.debug("test");
		RealMatrix P = new Array2DRowRealMatrix(new double[][] { { 1., 0 }, { 0, 1. } });
		PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(P.getData(),
				new double[] { 0, 0 }, 0);

		OptimizationRequest or = new OptimizationRequest();
		or.setF0(objectiveFunction);

		ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[1];
		inequalities[0] = FunctionsUtils.createCircle(2, 3);// dim=2, radius=3, center=(0,0)

		NewtonUnconstrained convexSolver = new NewtonUnconstrained();
//		or.setFi(inequalities);
		double[] initPoint = new double[] { 0, 0 };
		or.setInitialPoint(initPoint);
		or.setTolerance(1.e-8);
		or.setToleranceFeas(1.E-8);
		convexSolver.setOptimizationRequest(or);
		convexSolver.optimize();

		OptimizationResponse response = convexSolver.getOptimizationResponse();
		double[] sol = response.getSolution();
		log.debug("sol   : " + ArrayUtils.toString(sol));
//		log.debug("value : "	+ objectiveFunction.value(F1.make(sol)));
		
		// ArrayList<DoubleMatrix2D> lsgHistory = algo2.getHistory();

		// CAlgorithmusTest algo1 = new CAlgorithmusTest();
		// algo1.initialisieren();
		// algo1.ausfuehren();
		// double zfw = algo1.getZielfunktionswert();
		// System.out.println(zfw);
		// DoubleMatrix2D lsg = algo1.getLoesung();
		// System.out.println(lsg);
	}

}
