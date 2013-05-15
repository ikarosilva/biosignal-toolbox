package com.ikarosilva.statistics;

public class BernoulliBayesEstimation {
	//From pg 255 Prob. Random Variables and Random Processes (Schaum's Outline)
	private double p; //assumes the prior of p is uniform over 0-1
	private int n;
	private double sumX;

	public BernoulliBayesEstimation(){
		sumX=0;
		n=0;
	}
	public double getP(){
		return p;
	}
	public double estimateP(double[] data){
		for(int i=0;i<data.length;i++)
			sumX+=data[i];
		n+=data.length;
		p=(sumX+1)/(n+2);
		return p;
	}

	public void resetP(){
		p=0;
		sumX=0;
		n=0;
	}

	public double[][] estimatePosterior(int N){
		double[][] posterior=new double[N][2];
		double numerator=0, denominator=0;
		double pvals=0;
		for(int i=0;i<N;i++){
			pvals=(double) i/N;
			try {
				numerator=Factorial.factorial(n-1)*Math.pow(pvals,sumX)
						*Math.pow(1-pvals,n-sumX);	
				denominator=Factorial.factorial((int) sumX)*
						Factorial.factorial((int) (n-sumX));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			posterior[i][0]=pvals;
			posterior[i][1]=numerator/denominator;
		}
		return posterior;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
