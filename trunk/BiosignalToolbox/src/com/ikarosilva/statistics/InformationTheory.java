package com.ikarosilva.statistics;

public class InformationTheory {

	
	public static double Entropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where first column is x uppper bound indices and second
		//column is probabilities
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			H-= pdf[i][1]*( Math.log(pdf[i][1])/ Math.log(2) );
		}
		return H;
	}
	
	public static double NormalizedEntropy(double[][] pdf){
		//input is [][] from PdfEstimator
		//where first column is x uppper bound indices and second
		//column is probabilities
		double scale=Math.log(pdf.length)/ Math.log(2);
		return Entropy(pdf)/scale;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
