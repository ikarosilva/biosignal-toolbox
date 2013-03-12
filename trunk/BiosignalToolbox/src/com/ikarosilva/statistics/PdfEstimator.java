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
		
		double[][] y = new double[Nbins][2];
		Collections.sort(data);
		Double binWidth=(data.get(data.size()-1)-data.get(0))/Nbins;
		int LB,UB;
		double index;
		
		for(int n=0;n<Nbins-1;n++){
			index = (n+1)*binWidth;
			LB=Collections.binarySearch(data,index-binWidth);
			UB=Collections.binarySearch(data,index);
			//Make sure its stable sorted
			while(LB >0 && (data.get(LB) == data.get(LB-1) ))
					LB--;
			while(UB >0 && (data.get(UB) == data.get(UB-1) ))
				UB--;
			
			y[n][0]=index-(binWidth/2);
			y[n][1]=UB-LB;
			System.out.println("y[n][0]= " +y[n][0] +
					" y[n][1]= " + y[n][1] + "\n");
		}
		
		return y;
	}
	
	
	public static void main(String[] args) {
		
		//Test the histogram estimation
		ArrayList<Double> x= new ArrayList<Double>();
		double[][] hist;
		int N=100;
		for(int n=0;n<N;n++){
			x.add((double) Math.round(Math.random()*10));
		}
		hist=equipartition(x,5);
		Plot plt= new Plot("histogram",hist);
		plt.pack();
		RefineryUtilities.centerFrameOnScreen(plt);
		plt.setVisible(true);
	}

}
