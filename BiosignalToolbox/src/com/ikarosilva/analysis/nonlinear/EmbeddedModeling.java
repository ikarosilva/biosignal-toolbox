package com.ikarosilva.analysis.nonlinear;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.optimization.fitting.CurveFitter;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

public class EmbeddedModeling {

	public enum Norm{
		MAX,
		EUCLIDEAN
	}

	private int tau; //delay between state samples
	private double[][] distance; //Square matrix of state distances
	private Norm norm; //Stores the type of norm used to test distance
	private int step;

	public EmbeddedModeling(int tau, Norm norm){
		this.tau=tau;
		this.norm=norm;
		step=1;//Step size between state vectors (in sample) in order to avoid local similarities
	}

	public double[][] getNormDistance(int tau, int M){
		//Calculates distance between vectors
		double[][] dist =new double[M][];
		return dist;
	}

	public double[] estimateDimension(double[] data, double[] threshold, int[] M){
		double[] v= new double[M.length];
		double[] best;
		double[] init = { 1, 1}; // a - bx
		PolynomialFunction fitted;
		CurveFitter fitter = new CurveFitter(new GaussNewtonOptimizer());				
		for(int i=0;i<threshold.length;i++){
			for(int m=0;m<M.length;m++){
				fitter.addObservedPoint(Math.log(threshold[i]),
						Math.log(correlationIntegral(data,threshold[i],M[m])));
				System.out.println(Math.log(threshold[i]) + " " +
						Math.log(correlationIntegral(data,threshold[i],M[m])));
			}
			// Compute optimal coefficients.
			best = fitter.fit(new PolynomialFunction.Parametric(), init);
			// Construct the polynomial that best fits the data.
			fitted = new PolynomialFunction(best);
			v[i]=fitted.getCoefficients()[1];
		}
		return v;
	}


	public double correlationIntegral(double[] data, double threshold, int M){
		//Calculate the correlation integral of the time series
		//based on equation 6.34 (pg 317) of Kaplan
		int N=data.length, n, m, k, dist;
		int endPoint=(N-1) - (M-1)*tau+step;
		double Corr=0;
		for(n=0;n<(endPoint-(M-1)*tau-step);n=n+step){
			dist=0;
			//System.out.println("tau= " + tau + "\tdata[ " + n +" ]=" + data[n]);
			for(k=n+(M-1)*tau+step;k<endPoint;k++){
				for(m=0;m<M;m++){
					//System.out.println( "k= " + k + " m= " + m +
					//		"\tdata[ " + (n+m*tau) + " ]= " + data[n+m*tau] +
					//		"\tdata[ " + (k+m*tau) + " ]= " + data[k+m*tau]); 
					switch (norm) {
					case MAX:
						dist+=Math.abs(data[n+m*tau]-data[k+m*tau]);
						break;                
					case EUCLIDEAN:
						dist+=Math.sqrt(data[n+m*tau]*data[n+m*tau]-data[k+m*tau]*data[k+m*tau]);
						break;                     
					default:
						System.out.println("Unknown norm");			
					}
				}
				//If within tolerance, average future value
				if(dist<threshold)
					Corr++;
			}
		}
		return Corr/(N*N-N);
	}



	public double predict(double[] data, double[] x, int M, double th) throws Exception{
		//Find history that matches current state and average them to find the future
		
		//TODO: Fix so that this part is similar to that of the Correlation Integral 
		double y= 0;
		int n, m, dist, aveN=1, N= data.length; 
		if(x.length != M)
			throw new Exception("Prediction vector must be of size " + M);

		for(n=0;n<N-1;n=n+step){
			//Estimate distance of current state vector and input
			dist=0;
			for(m=0;m<M;m++){
				switch (norm) {
				case MAX:
					dist+=Math.abs(x[m]-data[n+m*tau]);
					break;                
				case EUCLIDEAN:
					dist+=Math.sqrt(x[m]*x[m]-data[n+m*tau]*data[n+m*tau]);
					break;                     
				default:
					System.out.println("Unknown norm");			
				}
			}
			//If within tolerance, average future value
			if(dist<th)
				y=((aveN-1)*y + data[n+M*tau+1])/aveN;
		}

		return y;
	}
	public double falseNeighbour(double threshold,double sigma, int M, double th){
		/*
		Calculates the number of false neighbors for this model
		given a series of threshold values. Equation from
		pg 37 "Nonlinear Time Series Analysis", Kantz et al.
		sigma is  and estimate of the standard deviation of
		the noise in the time series.
		Assumptions:
			1. The distance matrix between the vectors 
			is measured with the maximum norm.
		 */
		double[][] distanceM1= getNormDistance(tau,M+1);
		int num=0, den=0, minIndex=0;
		int N=distance[0].length;
		double th1= sigma/threshold;
		for(int i=0;i<N;i++){
			//Perform search for minimum distance
			minIndex=i+1;
			for(int k=0;k<N;k++){
				if(k != i)
					minIndex=(distance[i][k]<distance[i][minIndex]) ?
							k:minIndex;
			}
			if(distance[i][minIndex]< th1){
				//The smallest distance is within the accepted noise threshold
				//now proceed to see wether at higher dimension the
				//distance is still within the input threshold.
				if((distanceM1[i][minIndex]/distance[i][minIndex])
						< threshold){
					num++;
				}
				den++;
			}
		}
		return (double) num/den;	
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
