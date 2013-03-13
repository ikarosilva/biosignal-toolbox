package com.ikarosilva.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot;

public class PdfEstimator {




	public static double[][] equipartition(ArrayList<Double> data, int Nbins){
		double[][] y = new double[Nbins][3];
		Collections.sort(data);
		Double binWidth=(data.get(data.size()-1)-data.get(0))/Nbins;
		int LB=0,UB;
		double index;
		for(int n=0;n<Nbins;n++){
			index = (n+1)*binWidth;
			if(n != Nbins){
				UB=Math.abs(Collections.binarySearch(data,index))-1;
				//Make sure its stable sorted
				while(UB >0 && UB < data.size() && (data.get(UB) == data.get(UB-1) ))
					UB--;
			}else{
				UB=data.size()-1;
			}		
			y[n][0]=data.get(LB);
			y[n][1]=data.get(UB);
			y[n][2]=(double) (UB-LB)/data.size();
			LB=UB+1;
			System.out.println(y[n][0] +"," + y[n][1] +","+ y[n][2] + "\n");
		}
		return y;
	}


	public static void main(String[] args) {

		//Test the histogram estimation
		ArrayList<Double> x= new ArrayList<Double>();
		double[][] hist;
		int N=10000;
		for(int n=0;n<N;n++){
			x.add((double) Math.random());
		}
		hist=equipartition(x,10);
		//System.out.println("Entropy= " + Entropy.NormalizedEntropy(hist));
	}

}
