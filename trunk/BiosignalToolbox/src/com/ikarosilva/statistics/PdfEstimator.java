package com.ikarosilva.statistics;

import java.util.ArrayList;
import java.util.HashMap;
public class PdfEstimator {


	//px : 1 column= probability, 2nd column lower bound, 3rd column upper bound
	private double[][] pdf;
	private int Nbins;
	private int M;

	public PdfEstimator(){

	}

	private double[] minMax(double[] x){
		//Returns minmax for a vector
		double[] minMax=new double[2];
		minMax[0]=Double.MAX_VALUE;
		minMax[1]=Double.MIN_VALUE;
		for(int i=0;i<x.length;i++){
			minMax[0]=(minMax[0]>x[i]) ? x[i]:minMax[0];
			minMax[1]=(minMax[1]<x[i]) ? x[i]:minMax[1];
		}
		return minMax;
	}

	public HashMap<Integer, Double> equipartition(double[][] x, int N){
		Nbins=N;
		M=x.length;
		int samples=x[0].length;
		double wcount= (double) 1/samples;
		double[][] minmax=new double[M][2];
		double[] range=new double[M];
		double[] scale= new double[M];
		double[] step= new double[M];
		double[] picketFence= new double[M];
		double[] offset= new double[M];
		int key; 
		int[] rnd= new int[M];
		int[] mapDim=new int[M];
		Double tmp;
		HashMap<Integer,Double> pdf=new HashMap<Integer,Double>();

		System.out.println("M=" + M + " samps= " + samples);
		//Initialize parameters for the the pdf
		for(int i=0;i<M;i++)
		{
			minmax[i]=minMax(x[i]);
			range[i]= minmax[i][1]-minmax[i][0];
			scale[i]=(double)(Nbins-1)/range[i];
			step[i]=(double)range[i]/Nbins;
			picketFence[i]=(minmax[i][1]+step[i])/minmax[i][1];
			offset[i]=(double)step[i]/2;
			mapDim[i]=(int) Math.pow(Nbins,M-i-1);
		}
		//Create HashMap to store bin locations of X and Y
		//then use the HashMap to add to the count
		//The HashMap will be created by normalizing the variable and
		//multiplying by the number of bins. So keys are in the 
		// round(normalized*Nbins) domain //TODO: ensure capacity ??
		for(int i=0;i<samples;i++){
			//TODO: fix picket fence issues
			key=0;
			for(int j=0;j<M;j++){
				rnd[j]= (int) Math.round((((x[j][i]-minmax[j][0])*picketFence[j])-offset[j])*scale[j]);//maps a sample to a bin
				key+= (double) mapDim[j]*rnd[j]; //using integer division to have Nbins*rnd  on all except last dimension
				System.out.println("x[" + j + "][" + i +"]= " + x[j][i]+
						" rnd=" + rnd[j]+ " mapDim= " + mapDim[j]);
			}
			//Map subscript to absolute index
			System.out.println("key= "+ key);
			tmp=pdf.get(key);
			tmp=(tmp == null) ? wcount:tmp+wcount;
			pdf.put(key,tmp);
		}

		//Print HashMap
		System.out.println("map size=" + pdf.size());
		int[] dim=new int[M];
		for(Integer d : pdf.keySet()){
			System.out.print("pdf["+d+": ");
			for(int i=0;i<M;i++){
				//TODO: fix correction factor for higher dimensions
				dim[i]=d/mapDim[i] - (i*Nbins);
				System.out.print(dim[i]);
				if(i<(M-1))
					System.out.print(",");
			}
			System.out.println("]=" + pdf.get(d));
		}
		return pdf;

	}


	public static void main(String[] args) {

		//Test the histogram estimation
		ArrayList<Double> y= new ArrayList<Double>();
		HashMap<Integer,Double> hist;
		int samples=4, bins=4;
		int dim=2;
		double[][] x= new double[dim][samples];
		for(int n=0;n<samples;n++){
			//x[0][n]= Math.random();
			for(int d=0;d<dim;d++)
				x[d][n]=n;
			//x[1][n]=n;
			//y.add(x.get(n));
			//x[1][n]=Math.random();
		}
		PdfEstimator pdf=new PdfEstimator();
		hist=pdf.equipartition(x,bins);
	}

}
