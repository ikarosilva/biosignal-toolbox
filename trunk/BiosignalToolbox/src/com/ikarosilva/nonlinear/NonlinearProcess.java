package com.ikarosilva.nonlinear;

public class NonlinearProcess {

	//Nonlinear processes from :
	//Understanding Nonlinear Dynamics, Kaplan and Glass, 1995
	public static double[] quadriacticMap(double x0,double R, int N){
		double[] y=new double[N];
		y[0]=x0;
		for(int i=1;i<N;i++)
			y[i]=R*y[i-1]*(1-y[i-1]);
		return y;

	}

	public static double[] sineMap(double x0, double b, int N){
		double[] y=new double[N];
		y[0]=x0;
		for(int i=1;i<N;i++)
			y[i]=y[i-1] + b*Math.sin(2*Math.PI*y[i-1]);
		return y;
	}

	public static double[] gompertzGrowth(double x0, double k,double a, double Ts, int N){
		double[] y=new double[N];
		y[0]=x0;
		for(int i=1;i<N;i++)
			y[i]=x0*Math.exp((k/a)*(1-Math.exp(-a*i*Ts)));
		return y;
	}
	
	public static double[] wbcControl(double x0, double Ts, int N){
		double[] y=new double[N];
		double tau=20; //time delay in days
		double k=0.1; // rate of normal WBC elimination
		double a=0.2; //Parameter for the control function of WBC
		double b=10;  //Parameter for the control function of WBC
		double g=0; //Control function of production of WBC
		double xt=0;
		double dx=0;
		y[0]=x0;
		for(int i=1;i<N;i++){
		    xt = (i*Ts<tau) ? 0 : y[(int) (i-Math.round(tau*Ts))];
		    g=(a*xt)/(1-Math.pow(xt,b));
			dx=g - 0.1*y[i-1];
			y[i]=dx*Ts + y[i-1];
		}
			
		return y;
	}
	
	
}