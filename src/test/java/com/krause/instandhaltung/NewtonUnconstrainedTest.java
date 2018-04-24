package com.krause.instandhaltung;

/*
 * Copyright 2011-2017 joptimizer.com
 *
 * This work is licensed under the Creative Commons Attribution-NoDerivatives 4.0 
 * International License. To view a copy of this license, visit 
 *
 *        http://creativecommons.org/licenses/by-nd/4.0/ 
 *
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.pmw.tinylog.Logger;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.BasicConfigurator;

import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.NewtonUnconstrained;
import com.joptimizer.optimizers.OptimizationRequest;
import com.joptimizer.optimizers.OptimizationResponse;
import cern.colt.matrix.tdouble.DoubleFactory1D;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import junit.framework.TestCase;

public class NewtonUnconstrainedTest extends TestCase {
	private DenseDoubleAlgebra ALG = DenseDoubleAlgebra.DEFAULT;
	private DoubleFactory1D F1 = DoubleFactory1D.dense;
	private DoubleFactory2D F2 = DoubleFactory2D.dense;
	// private static Log log =
	// LogFactory.getLog(NewtonUnconstrainedTest.class.getName());

	/**
	 * Quadratic objective.
	 */
	public void testOptimize() throws Exception {
		BasicConfigurator.configure();
		Logger.info("testOptimize");
		// START SNIPPET: newtonUnconstrained-1

		RealMatrix P = new Array2DRowRealMatrix(
				new double[][] { { 1., 0 }, { 0, 1. } });
		double[] q = new double[] { 0, 0};

		// Objective function.
		PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(P.getData(),
				q, 0);

		OptimizationRequest or = new OptimizationRequest();
		or.setF0(objectiveFunction);
		or.setInitialPoint(new double[] { 0.04, 0.50 });
		or.setTolerance(1.e-8);

		// optimization
		JOptimizer opt = new JOptimizer();
		opt.setOptimizationRequest(or);
		opt.optimize();

		// END SNIPPET: newtonUnconstrained-1

		OptimizationResponse response = opt.getOptimizationResponse();
		double[] sol = response.getSolution();
		Logger.info("sol   : " + ArrayUtils.toString(sol));
		Logger.info("value : " + objectiveFunction.value(F1.make(sol)));
	}

}
