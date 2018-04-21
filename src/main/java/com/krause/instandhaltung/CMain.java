package com.krause.instandhaltung;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.FunctionsUtils;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.NewtonUnconstrained;
import com.joptimizer.optimizers.OptimizationRequest;
import com.krause.instandhaltung.algorithmen.*;

import cern.colt.matrix.tdouble.*;
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
	public static DoubleFactory1D F1 = DoubleFactory1D.dense;
	public static DoubleFactory2D F2 = DoubleFactory2D.dense;

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

		NewtonUnconstrained convexSolver = new NewtonUnconstrained();
		OptimizationRequest or = new OptimizationRequest();
		ConvexMultivariateRealFunction f0 = new ConvexMultivariateRealFunction() {

			@Override
			public DoubleMatrix2D hessian(DoubleMatrix1D X) {
				DoubleMatrix2D Z = new DenseDoubleMatrix2D(2, 2);

				Z.set(0, 0, 2);
				Z.set(0, 1, 0);
				Z.set(1, 0, 0);
				Z.set(1, 1, 2);
				return Z;
			}

			@Override
			public int getDim() {
				return 2;
			}

			@Override
			public double value(DoubleMatrix1D X) {
				double erg = -(Math.pow(X.get(0), 2) + Math.pow(X.get(1), 2));
				return erg;
			}

			public DoubleMatrix1D gradient(DoubleMatrix1D X) {
				DoubleMatrix1D Z = new DenseDoubleMatrix1D(2);
				Z.set(0, 2 * X.get(0));
				Z.set(1, 2 * X.get(1));
				return Z;
			}

		};

		ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[1];
		inequalities[0] = FunctionsUtils.createCircle(2, 3);// dim=2, radius=3, center=(0,0)
		or.setF0(f0);
		or.setFi(inequalities);
		double[] initPoint=new double[2];
		or.setInitialPoint(initPoint);
		or.setTolerance(1.e-8);
		convexSolver.setOptimizationRequest(or);
		convexSolver.optimize();

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
