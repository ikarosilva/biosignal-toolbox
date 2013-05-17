package com.ikarosilva.statistics;

import java.util.HashMap;

import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot2DPDF;
public class PmfEstimator {


	//We assume first dimension is data series and second dimension (columns) are the different categories
	private HashMap<Integer,Double> pmfHashMap;
	private int Nbins;
	private int M;
	private double[][] minmax; //minimum and maximum values for bins (ie, range)
	private int[] mapDim;
	private double[] range;

	public PmfEstimator(){
		Nbins=0;
	}

	public PmfEstimator(int Nbins){
		this.Nbins=Nbins;
	}
	public PmfEstimator(int Nbins, double[][] minmax){
		this.Nbins=Nbins;
		this.minmax=minmax;
	}

	public int getDim(){
		return M;
	}
	private double[] minMax(double[] x){
		//Returns minmax (ie, range ) for a vector
		double[] minMax=new double[2];
		minMax[0]=Double.MAX_VALUE;
		minMax[1]=Double.MIN_VALUE;
		for(int i=0;i<x.length;i++){
			minMax[0]=(minMax[0]>x[i]) ? x[i]:minMax[0];
			minMax[1]=(minMax[1]<x[i]) ? x[i]:minMax[1];
		}
		return minMax;
	}

	private void initializeStates(int M){
		range=new double[M];
		pmfHashMap=new HashMap<Integer,Double>();
		mapDim=new int[M];
		for(int i=0;i<M;i++)
		{
			range[i]= minmax[i][1]-minmax[i][0];
			mapDim[i]=(int) Math.pow(Nbins,M-i-1);
		}
	}

	public void equipartition(double[][] x){
		int samples=x[0].length;
		double wcount= (double) 1/samples;
		int key; 
		int binIndex=0;
		Double tmp;

		//Initialize parameters for the the pmf based on the data
		M=x.length;
		if(minmax == null){
			minmax=new double[M][2];
			for(int i=0;i<M;i++)
				minmax[i]=minMax(x[i]);
		}

		initializeStates(M);	
		/*
		Create HashMap to store bin locations of X1 , X2,...XM and use the HashMap to add to the count. The HashMap is created 
		by normalizing the respective variables and multiplying by the number of bins. The keys to the location in them map are generated by:
		round(normalized*Nbins)
		 */
		for(int i=0;i<samples;i++){
			key=0;
			for(int j=0;j<M;j++){
				//Find the bin location of the sample
				binIndex= (int) Math.floor(
						Nbins*( x[j][i] - minmax[j][0] )/range[j]				
						);
				//System.out.print(x[j][i] + " === " );
				//System.out.print(Nbins*( x[j][i] - minmax[j][0] )/range[j]);
				//System.out.println(" -->"+ binIndex );
				if(binIndex==Nbins)
					binIndex--; //In cases where the value is maximum, it should map to the N-1 bin because of flooring
				key+= (double) mapDim[j]*binIndex; 
			}
			//Map subscript to absolute index
			tmp=pmfHashMap.get(key);
			tmp=(tmp == null) ? wcount:(tmp+wcount);
			pmfHashMap.put(key,tmp);
		}
	}



	public int[] sub2Indices(Integer sub){
		int[] indices=new int[M];
		for(int i=0;i<M;i++){
			indices[i]=sub;
			for(int k=0;k<i;k++){
				indices[i]-=indices[k]*mapDim[k];
			}
			indices[i]=(int) Math.floor(indices[i]/mapDim[i]);
		}
		return indices;
	}
	
	public double[][] toDouble2D(){
		double[][] pmf = new double[Nbins][Nbins];
		int[] ind;
		for(Integer thisKey : pmfHashMap.keySet()){
			ind = sub2Indices(thisKey);
			pmf[ind[0]][ind[1]]=pmfHashMap.get(thisKey);
		}
		return pmf;
	}


	public void printHashMap(){
		int[] indices;
		for(Integer thisKey : pmfHashMap.keySet()){
			indices=sub2Indices(thisKey);
			System.out.print("pmf[ " + thisKey + " : ");
			for(int j=0;j<M;j++){
				System.out.print(indices[j] + " ");
			}
			System.out.println(" ]=" + pmfHashMap.get(thisKey));
		}		
	}


	public static void main(String[] args) {
		//Test the histogram estimation
		int bins=6;
		int N=1000;
		double[][] data=new double[2][N];
		
		for(int i=0;i<N;i++){
			data[0][i]=Math.random();
			data[1][i]=Math.random();	
		}
		
		PmfEstimator pmf=new PmfEstimator(bins);
		pmf.equipartition(data);
		pmf.printHashMap();
		//Plot pdf
		double[][] pmf2D=pmf.toDouble2D();
		Plot2DPDF demo = new Plot2DPDF(
				"PDF",pmf2D,1/10);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	
	}

}
