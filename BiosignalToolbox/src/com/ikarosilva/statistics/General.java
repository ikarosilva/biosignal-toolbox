package com.ikarosilva.statistics;

public class General {

	public static double corrcoeff(double[] x, double[] y) throws Exception{
		if(x.length != y.length)
			throw new Exception("Array size do not match!");
		double p=Double.NaN;
		
		//Estimate mean from dataset
		int N=x.length;
		double mx=0, my=0;
		for(int n=0;n<N;n++){
			mx+=x[n];
			my+=y[n];
		}
		mx=mx/N;
		my=my/N;
		
		//Estimate corrcoeff from variance
		double Sxy=0, Sxx=0, Syy=0;
		for(int n=0;n<N;n++){
			Sxy+=(x[n]-mx)*(y[n]-my);
			Sxx+=(x[n]-mx)*(x[n]-mx);
			Syy+=(y[n]-my)*(y[n]-my);
		}
		p=Sxy/Math.sqrt(Sxx*Syy);
		return p;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
