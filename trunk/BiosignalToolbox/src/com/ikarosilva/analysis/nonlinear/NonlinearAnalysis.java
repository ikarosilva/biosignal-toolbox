package com.ikarosilva.analysis.nonlinear;


import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot;

public class NonlinearAnalysis {

	
	public NonlinearAnalysis(double data){
		
	}
	//Nonlinear analysis methods from :
    //Understanding Nonlinear Dynamics, Kaplan and Glass, 1995
	public static void returnPlot(double x0,double R, int N){
		//TODO: implement return Map
	}
	
	public static void corrIntegral(){
		//TODO: implement method in pg 317 and 353
		//Assumes that the time series came from a dynamical system that is on
		//a attractor!!
	}
	
	public static void corrDimension(){
		//TODO implment method in 320 and 354
	}
	
	public static void deterministicError(){
		//TODO implment method in 325
	}
	
	public static void linearModel(){
		//TODO implment method in 337
	}
	
	public static void lyapunov(){
		//TODO implmente method in 335
		//Assumes data is well described by deterministic 
		//finite difference equation
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		//Get directory listing 

		int N=1000;
	    double[] data= new double[N];
	    //Simulate with a rnadom noise signal
	    for(int n=0;n<N;n++){
	    	data[n]=Math.random();
	    }
		

	}

	
}
