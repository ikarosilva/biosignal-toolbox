package com.ikarosilva.analysis.nonlinear;

import java.util.Arrays;

import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.RecurrencePlot;
import com.ikarosilva.graphics.ScatterPlot;
import com.ikarosilva.statistics.General;

public class TransferEntropy {

	
	public static void main(String[] args) {
		int N=200;
		int mid=(int)Math.round(N/2);
		double[] data=  NonlinearProcess.LactateSimulation(N);
		double th=Double.MAX_VALUE;
		int M=3; //Embedding Dimension
		int[] neighborSize= {1};//;,2,3,4,5,10,20,40,80,150,300,mid-1};
		double [] v = new double[neighborSize.length];
		
		/*
		EmbeddedModeling model= new EmbeddedModeling(data[1],1,EmbeddedModeling.Norm.MAX);
		
		model.setApplyWeight(true);
		model.setLocalSlopeOrder(0);
		v=model.predictivePowerLeavePer(data[1],M,th,neighborSize);
		
		//Plot plt= new Plot("",neighborSize,v);
		Plot plt2= new Plot("",data[1]);
		*/
		Plot plt3= new Plot("",data);
		
		//double[][] p=model.prediction;
		//RecurrencePlot.plotDim(data[1],M);
		
		
		System.out.println("done!");
		
	}

}
