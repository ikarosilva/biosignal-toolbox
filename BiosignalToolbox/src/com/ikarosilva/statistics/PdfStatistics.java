package com.ikarosilva.statistics;

public class PdfStatistics {

	public static double CDDistance(double[] pdf1,double[] pdf2){
		double D=Double.NaN;
		double RMAX=Double.MIN_VALUE;
		double RMIN= Double.MAX_VALUE;
		double R;
		//Calculates CD distance 
		//Acording to 
		//Modeling and Reasoning with Bayesian Networks, Adnan Darwiche (2009)
		//pg 418 (D=  ln max(x) LR - ln min(x) LR  where LR= pdf1(x)/pdf2(x)
		
		//Assumes: pdf1 and pdf2 are ranked and same size
		for(int i=0;i<pdf2.length;i++){
			R=pdf1[i]/pdf2[i];
			RMAX=(R>RMAX) ? R:RMAX;
			RMIN=(R<RMIN) ? R:RMIN;
		}
		D=Math.log(RMAX/RMIN);
		return D;	
	}
	
	public static void main(String[] args) {

	}

}
