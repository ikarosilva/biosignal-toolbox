package com.ikarosilva.statistics;

public class Entropy {

	
	public static double DiscreteEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where 2nd column is x lower bound indices and 1st
		//column is probabilities
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			H-= pdf[i][0]*( Math.log(pdf[i][1])/ Math.log(2) );
		}
		return H;
	}
	
	public static double NormalizedDiscreteEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where 2nd column is x lower bound indices and 1st
		//column is probabilities
		double scale=Math.log(pdf.length)/ Math.log(2);
		return DiscreteEntropy(pdf)/scale;
	}
	
	public static double DifferentialEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where 2nd column is x lower bound indices,
		//and 3rd is upper bound index and 1st column is
		//probability density
		double H = 0;
		double width;
		for (int i=0; i<pdf.length;i++){
			width=pdf[i][2]-pdf[i][1];
			H-= width*pdf[i][0]*( Math.log(pdf[i][0])/ Math.log(2) );
		}
		return H;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
