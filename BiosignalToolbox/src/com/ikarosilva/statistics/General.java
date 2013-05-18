package com.ikarosilva.statistics;

import java.util.Arrays;

public class General {

	public static double[] autocorr(double[] x, int M) {
		int N=x.length;
		if(M==0)
			M=(int)N/20;
		double[] R=new double[M];
		for(int m=0;m<M;m++){
			try {
				R[m]=corrcoeff(
						Arrays.copyOfRange(x,0,N-1-m),
						Arrays.copyOfRange(x,m,N-1))/(N-m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return R;
	}
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
