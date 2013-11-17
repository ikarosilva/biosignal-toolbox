package com.ikarosilva.bistat;

import org.apache.commons.math3.distribution.BetaDistribution;

public class MortalityPosterior {

	public static double likelihood(double theta, int N, int z){
		return (Math.pow(theta,z)*Math.pow(1-theta,N-z));
	}

	public static double[] prior(double a, double b, double[] theta){
		double[] prior=new double[theta.length];
		BetaDistribution beta = new BetaDistribution(a,b);	
		for(int i=0; i<theta.length;i++)
			prior[i]=beta.density(theta[i]);
		return prior;
	}
	
	public static double[] posterior(double a, double b, int N, int z, double[] theta){
		double[] prior=new double[theta.length];
		BetaDistribution beta = new BetaDistribution(a + (double) z,b + (double) (N-z));	
		for(int i=0; i<theta.length;i++)
			prior[i]=beta.density(theta[i]);
		return prior;
	}

	public static double posteriorMean(double a, double b, int N, int z, double[] theta){
		double data= (double) z/N;
		double prior= a/(a+b);
		double w1= (double) N/((double) N+a+b);
		double w2= (double) (a+b)/((double) N+a+b);
		return (w1*data + w2*prior);
	}
	
	public static void main(String[] args) {


	}

}
