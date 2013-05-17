package com.ikarosilva.analysis.nonlinear;

import java.util.Arrays;


public class EmbeddedModeling {

	 private int M; //state size of vector
	 private double r; //Neighbor threshold
	 private int tau; //delay
	 private double[] data;
	 private double[][] distance; //Square matrix of state distances
	 
	public double[][] getMaxNormDistance(int tau, int M){
		//Calculates distance between vectors
		double[][] dist =new double[M][];
		return dist;
	}
	public double falseNeighbour(double threshold,double sigma){
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
		double[][] distanceM1= getMaxNormDistance(tau,M+1);
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
