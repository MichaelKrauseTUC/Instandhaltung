package com.krause.instandhaltung;

import java.util.Random;

public class CVerschleissNormalverteilt implements IVerschleiss {

	private double mu;
	private double sigma;

	public CVerschleissNormalverteilt(double mu, double sigma) {
		this.mu = mu;
		this.sigma = sigma;
	}

	@Override
	public double getErwVerschleiss(double leistung) {
		return mu;
	}

	@Override
	public double getVerschleiss(double leistung) {
		Random r = new Random();
		double erg = r.nextGaussian() * sigma + mu;
		if (erg > leistung)
			erg = leistung;
		return erg;
	}

}
