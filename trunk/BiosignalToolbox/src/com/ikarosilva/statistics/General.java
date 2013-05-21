package com.ikarosilva.statistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
	
	public static double[][] sortRows(double[][] x){
		Arrays.sort(x, new Comparator<double[]>() {
	        public int compare(double[] o1, double[] o2) {
	            return Double.compare(o1[1], o2[1]);
	        }
	    });
        return x;
	}
	
	public static double var(ArrayList<Double> x){
		double[] y=new double[x.size()];
		for(int n=0;n<x.size();n++)
			y[n]=x.get(n);
		return General.var(y);
	}
	
	public static double var(double[] x){
		double sigma=0;
		double mx=General.mean(x);
		for(int n=0;n<x.length;n++)
			sigma+=(x[n]-mx)*(x[n]-mx);
		return (double) sigma/(x.length-1);
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
	
	public static double mean(ArrayList<Double> x){
		double[] y=new double[x.size()];
		for(int n=0;n<x.size();n++)
			y[n]=x.get(n);
		return General.mean(y);
	}
	
	public static double mean(double[] x){
		double mx=0;
		double count=0;
		for(int n=0;n<x.length;n++){
			if(Double.valueOf(x[n])!= Double.NaN){
				mx=(count*mx + x[n])/(count+1);
				count++;
			}
		}
		return mx;
	}
	
	public static void main(String[] args) {
	    double[][] arr = { { 1, 10, 2.0 }, { 1, 11, 1.2 }, { 1, 12, 1.4 }, };
	    ;
	    Arrays.sort(arr, new Comparator<double[]>() {
	        public int compare(double[] o1, double[] o2) {
	            return Double.compare(o1[1], o2[1]);
	        }
	    });

	    for (int i = 0; i < arr.length; i++) {
	        for (int j = 0; j < arr.length; j++) {
	            System.out.print(arr[i][j]);
	            System.out.print("\t");
	        }
	        System.out.println();
	    }

	}

}
