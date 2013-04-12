/* ===========================================================
 * Biosignal Toolbox 
 *              
 * ===========================================================
 *
 * (C) Copyright 2012, by Ikaro Silva
 *
 * Project Info:
 *    Biosignal Toolbox: http://code.google.com/p/biosignal-toolbox/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * (C) Copyright 2012, by Ikaro Silva
 *
 * Original Author:  Ikaro Silva
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * Check: http://code.google.com/p/biosignal-toolbox/
 * 
 * Most of the functions are based on the following references:
 * 
 * [1] "Elements of Information Theory", 2006 Second Edition,
 *		Cover & Thomas
 * [2] "Modeling and Reasoning with Bayesian Networks", Adnan Darwiche (2009)
 */ 
package com.ikarosilva.statistics;


public class InformationTheory {


	public static double DiscreteEntropy(double[] pdf){
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			H-= pdf[i]*( Math.log(pdf[i])/ Math.log(2) );
		}
		return H;
	}

	public static double NormalizedDiscreteEntropy(double[] pdf){
		double scale=Math.log(pdf.length)/ Math.log(2);
		return DiscreteEntropy(pdf)/scale;
	}

	public static double JointEntropy(double[][] pdf){
		//Assumes square matrix
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			for (int k=0; k<pdf[0].length;k++){
				H-= pdf[i][k]*( Math.log(pdf[i][k])/ Math.log(2) );
			}
		}
		return H;
	}

	public static double ConditionalEntropy(double[][] pdf, double[][] condpdf){
		//Assumes square matrix and joint of equal size
		double H = 0;
		for (int i=0; i<pdf.length;i++){
			for (int k=0; k<pdf[0].length;k++){
				H-= pdf[i][k]*( Math.log(condpdf[i][k])/ Math.log(2) );
			}
		}
		return H;
	}

	public static double RelativeEntropy(double[] pdfX,double[] pdfY){
		double H = 0;
		for (int i=0; i<pdfX.length;i++){
			H-= pdfX[i]*( Math.log(pdfX[i]/pdfY[i])/ Math.log(2) );
		}
		return H;
	}

	public static double ConditionalRelativeEntropy(double[] X,double[] Y, double[] Z){
		//D(p(y|x)||q(y|x)) = sum p(x) sum p(y|x)log p(y|x)/q(y|x) ->
		//D(X(Z) || Y(Z) ) = sum Z sum X(Z) log X(Z) / Y(Z)
		//Reference: "Elements of Information Theory", Cover & Thomas, pg 24
		double H = 0;
		for (int z=0; z<Z.length;z++){
			for (int i=0; i<X.length;i++){
				H-= Z[z]*X[i]*( Math.log(X[i]/Y[i])/ Math.log(2) );
			}
		}
		return H;
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

	public static double SanovProbability(double[] P, double[] Q, int n){
		return Math.pow(2,-(double)n*InformationTheory.RelativeEntropy(P,Q));
	}
	
	public static double IndependenceTest(double[] P, double[] Q, double[][] PQ, int n){
		return Math.pow(2,-(double)n*InformationTheory.MutualInformation(P, Q,PQ));
	}

	public static double MutualInformation(double[] x, double[] y, double[][] joint){
		//Assumes square matrix and joint of equal size
		double H = 0;
		for (int i=0; i<x.length;i++){
			for (int k=0; k<y.length;k++){
				H-= joint[i][k]*( Math.log(joint[i][k]/(x[i]*y[k]))/ Math.log(2) );
			}
		}
		return H;
	}

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
	
	public static double EntropyNormalDistribution(double variance){
		return 0.5*( Math.log(2*Math.PI*Math.E*variance)/ Math.log(2) );
	}
	
	public static double EntropyKNormalDistribution(double determinant, int n){
		//determinant = |K| of the covariance matrix
		double c=Math.pow(2*Math.PI*Math.E,(double) n);
		return 0.5*( Math.log(c*determinant)/ Math.log(2) );
	}
	
	
	public static double EstimationErrorBound(double[] X){
		//Assumes (pg 255):
		//1) Estimate is unbiased
		//2) Gaussian distribution on the bound
		double hx=InformationTheory.DiscreteEntropy(X);
		return Math.pow(Math.E,2*hx)/(2*Math.PI*Math.E);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
