package com.ikarosilva.statistics;

public class BinomialGaussianPdf {

	private double mx, my, stdx, stdy, corrcoef;
	
	public BinomialGaussianPdf(double mx, double my,
							double stdx, double stdy, double p){
	this.mx=mx;
	this.my=my;
	this.stdx=stdx;
	this.stdy=stdy;
	this.corrcoef=p;
	}
	
	public double[][] eval(int N){
		double[][] z= new double [N][N];
		double p2=corrcoef*corrcoef;
		double sigxy=stdx*stdy;
		double varx=stdx*stdx, vary=stdy*stdy;
		double den=stdx*stdy*2*Math.PI*Math.sqrt(1-p2);
		double c1=2*corrcoef/sigxy;
		double c2=(2*(1-p2));
		double exponent, tmpx,  tmpy;
		
		for (int i = 0; i < N; i++) {
			tmpx = (i-mx)*(i-mx)/varx;
			for (int k = 0; k < N; k++) {
				tmpy=(k-my)*(k-my)/vary;
				exponent= tmpx + tmpy -
						(c1*(i-mx)*(k-my));
				z[i][k]=Math.exp(-exponent/c2)/den;
			}
		}
		return z;
	}

}
