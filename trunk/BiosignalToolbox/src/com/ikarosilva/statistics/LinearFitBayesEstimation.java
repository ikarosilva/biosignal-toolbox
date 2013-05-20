package com.ikarosilva.statistics;

public class LinearFitBayesEstimation {

	//Based on pg 103: "Fundamentals of Kallman Filtering: A Practical Approach"
	//Assumes uniformly sampled data (Ts=1) and fits the data according to:
	// yhat = a + b*x; (where x = 1 2 3... N)
	int N;
	double a, b, K1, K2, res;
	
	public LinearFitBayesEstimation(double a, double b){
		this.a=a;
		this.b=b;
		K1=0;
		K2=0;
		res=0;
		N=1;
	}
	
	public void fit(double[] data){
		for(int n=0;n<data.length;n++){
			K1=2*(2*N-1)/(N*(N+1));
			K2=6/(N*N +N);
			res=data[n]- (a+b);
			a= a + b + K1*res;
			b= b + K2*res;
			N++;
		}
			
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
