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
package com.krause.instandhaltung;

import junit.framework.TestCase;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.BasicConfigurator;
import org.pmw.tinylog.Logger;

import cern.colt.matrix.tdouble.DoubleFactory1D;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.jet.math.tdouble.DoubleFunctions;

import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.*;

/**
 * @author alberto trivellato (alberto.trivellato@gmail.com)
 */
public class NewtonLEConstrainedFSPTest extends TestCase {
	
	private DenseDoubleAlgebra ALG = DenseDoubleAlgebra.DEFAULT;
	private DoubleFactory1D F1 = DoubleFactory1D.dense;
	private DoubleFactory2D F2 = DoubleFactory2D.dense;

	public void testOptimize() throws Exception {
		BasicConfigurator.configure();
		Logger.info("testOptimize");
		DoubleMatrix2D pMatrix = F2.make(new double[][] { 
				{ 1.68, 0.34, 0.38 },
				{ 0.34, 3.09, -1.59 }, 
				{ 0.38, -1.59, 1.54 } });
		DoubleMatrix1D qVector = F1.make(new double[] { 0.018, 0.025, 0.01 });

		// Objective function (Risk-Aversion).
		double theta = 0.01522;
		double[][] P = pMatrix.assign(DoubleFunctions.mult(theta)).toArray();
		double[] q = qVector.assign(DoubleFunctions.mult(-1)).toArray();
		PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(P, q, 0);

		OptimizationRequest or = new OptimizationRequest();
		or.setF0(objectiveFunction);
		or.setInitialPoint(new double[] { 0.8, 0.1, 0.1 });
		or.setA(new double[][] { { 1, 1, 1 } });
		or.setB(new double[] { 1 });

		// optimization
		NewtonLEConstrainedFSP opt = new NewtonLEConstrainedFSP();
		opt.setOptimizationRequest(or);
		opt.optimize();
		
		OptimizationResponse response = opt.getOptimizationResponse();
		double[] sol = response.getSolution();
		Logger.info("sol   : " + ArrayUtils.toString(sol));
		Logger.info("value : " + objectiveFunction.value(F1.make(sol)));
		assertEquals(0.04632311555988555, sol[0], 0.00000000000001);
		assertEquals(0.5086308460954377,  sol[1], 0.00000000000001);
		assertEquals(0.44504603834467693, sol[2], 0.00000000000001);
	}
	
	
	/**
	 * Minimize x - Log[-x^2 + 1], 
	 * dom f ={x | x^2<1}
	 * N.B.: this simulate a centering step of the barrier method 
	 * applied to the problem:
	 * Minimize x
	 * s.t. x^2<1
	 * when t=1.
	 */
	public void testOptimize2() throws Exception {
		BasicConfigurator.configure();
		Logger.info("testOptimize2");
		
		// START SNIPPET: NewtonLEConstrainedFSP-1

		// Objective function
		ConvexMultivariateRealFunction objectiveFunction = new ConvexMultivariateRealFunction() {
			
			public double value(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				return x - Math.log(1-x*x);
			}
			
			public DoubleMatrix1D gradient(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				return F1.make(new double[]{1+2*x/(1-x*x)});
			}
			
			public DoubleMatrix2D hessian(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				return F2.make(new double[][]{{4*Math.pow(x, 2)/Math.pow(1-x*x, 2)+2/(1-x*x)}});
			}
			
			public int getDim() {
				return 1;
			}
		};

		OptimizationRequest or = new OptimizationRequest();
		or.setCheckKKTSolutionAccuracy(true);
		or.setF0(objectiveFunction);
		or.setInitialPoint(new double[] {0});//must be feasible
		
		// optimization
		NewtonLEConstrainedFSP opt = new NewtonLEConstrainedFSP();
		opt.setOptimizationRequest(or);
		opt.optimize();
		
		// END SNIPPET: NewtonLEConstrainedFSP-1
		
		OptimizationResponse response = opt.getOptimizationResponse();
		double[] sol = response.getSolution();
		double value = objectiveFunction.value(F1.make(sol));
		Logger.info("sol   : " + ArrayUtils.toString(sol));
		Logger.info("value : " + value);
		assertEquals(-0.41421356, sol[0], 0.0000001);//=1-Math.sqrt(2)
		assertEquals(-0.22598716, value , 0.0000001);
	}
	
	/**
	 * Minimize 100(2x+y) - Log[x] - Log[y], 
	 * s.t. x+y=1
	 * N.B.: this simulate a centering step of the barrier method 
	 * applied to the problem:
	 * Minimize 2x + y
	 * s.t. -x<0, 
	 *      -y<0
	 *      x+y=1
	 * when t=100; 
	 */
	public void testOptimize3() throws Exception {
		BasicConfigurator.configure();
		Logger.info("testOptimize3");
		
			// Objective function (linear)
		ConvexMultivariateRealFunction objectiveFunction = new ConvexMultivariateRealFunction() {
			
			public double value(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				double y = X.getQuick(1);
				return 100 * (2*x + y) - Math.log(x)- Math.log(y);
			}
			
			public DoubleMatrix1D gradient(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				double y = X.getQuick(1);
				return F1.make(new double[]{200-1./x, 100-1./y});
			}
			
			public DoubleMatrix2D hessian(DoubleMatrix1D X) {
				double x = X.getQuick(0);
				double y = X.getQuick(1);
				return F2.make(new double[][]{{1./Math.pow(x,2), 0},{0,1./Math.pow(y,2)}});
			}
			
			public int getDim() {
				return 2;
			}
		};

		OptimizationRequest or = new OptimizationRequest();
		or.setF0(objectiveFunction);
		or.setInitialPoint(new double[] {0.0900980486377967, 0.9099019513622053});
		or.setA(new double[][] { { 1, 1} });
		or.setB(new double[] { 1 });
		
		// optimization
		NewtonLEConstrainedFSP opt = new NewtonLEConstrainedFSP(true);
		opt.setOptimizationRequest(or);
		opt.optimize();
		
		OptimizationResponse response = opt.getOptimizationResponse();
		double[] sol = response.getSolution();
		double value = objectiveFunction.value(F1.make(sol));
		Logger.info("sol   : " + ArrayUtils.toString(sol));
		Logger.info("value : " + value);
		assertEquals(0., sol[0], 0.01);
		assertEquals(1., sol[1], 0.01);
		assertEquals(1., sol[0]+sol[1],   0.000000000001);//check constraint
	}
}
