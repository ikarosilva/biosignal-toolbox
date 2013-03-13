package com.ikarosilva.statistics;

public class Entropy {

	
	public static double DiscreteEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where first column is x lower bound indices and second
		//column is probabilities
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			H-= pdf[i][1]*( Math.log(pdf[i][1])/ Math.log(2) );
		}
		return H;
	}
	
	public static double NormalizedDiscreteEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where first column is x lower bound indices and second
		//column is probabilities
		double scale=Math.log(pdf.length)/ Math.log(2);
		return DiscreteEntropy(pdf)/scale;
	}
	
	public static double DifferentialEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where first column is x lower bound indices,
		//and second is upper bound index and last column is
		//probability density
		double H = 0;
		double width, area;
		for (int i=0; i<pdf.length;i++){
			width=pdf[i][1]-pdf[i][0];
			H-= width*pdf[i][1]*( Math.log(pdf[i][1])/ Math.log(2) );
		}
		return H;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
