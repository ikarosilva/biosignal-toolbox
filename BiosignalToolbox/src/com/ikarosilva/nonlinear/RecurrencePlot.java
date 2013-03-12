package com.ikarosilva.nonlinear;
//Nonlinear analysis methods from :
//Understanding Nonlinear Dynamics, Kaplan and Glass, 1995

//Standard recurence plot derived from a single time-series
//where the dimensional space is given by the lag variables

public class RecurrencePlot {

	private int p;  //Number of lags (equivalent to the dimensional space).
	private int h; 	//Sample delay (default 1)
	private double r; //Distance tolerance for the neighborrhood 
	
	public RecurrencePlot(int p,int h,double r){
		this.p=p;
		this.h=h;
		this.r=r;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Example
		
		

	}

}
