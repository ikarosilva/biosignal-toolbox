package com.ikarosilva.analysis.nonlinear;

import java.util.Arrays;
import java.util.Random;
import com.ikarosilva.graphics.Plot;
import com.ikarosilva.statistics.General;

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

	public static double[] BrownianMotion(int N){
		Random rnd=new Random(System.currentTimeMillis());
		double[] x=new double[N];
		for(int n=1;n<N;n++)
			x[n]=x[n-1]+rnd.nextGaussian();
		return x;
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

	public static double[] modelOne(int N){
		double[] data=new double[N];
		double A=4, p=0.95, sigma=2, x=80;
		Random rnd= new Random(System.currentTimeMillis());
		for(int i=0;i<N;i++){
			x=A + p*x;
			data[i]= x+rnd.nextGaussian()*sigma;
		}
		return data;
		
	}
	
	public static double[] modelTwo(int N){
		double[] data=new double[N];
		double A=4, p=0.95, sig1=2,sig2=3, x=80;
		Random rnd1= new Random(System.currentTimeMillis());
		Random rnd2= new Random(System.currentTimeMillis()*3);
		for(int i=0;i<N;i++){
			x=A + p*x + rnd2.nextGaussian()*sig2;
			data[i]= x+rnd1.nextGaussian()*sig1;
		}
		return data;
		
	}
	
	public static double[] Conway(int N) {
		double[] output= new double[N];
		int[] a = new int[N];
		for(int n=0;n<N;n++){
			if(n<3){
				a[n]=1;
				output[n]=(double) 1;
			}else{
				a[n]=a[a[n-1]]+ a[n-a[n-1]];
				output[n]=(double) a[n] - ((double)n)/2;
			}
		}
		return output;

	}

	
	public static void main(String[] args) {
		int N=1000;
		double[] data= modelTwo(N);
		Plot plt= new Plot("Model One",data);
		double[] d0=Arrays.copyOfRange(data,0,N-2);
		double[] d1=Arrays.copyOfRange(data,1,N-1);
		try {
			System.out.println("p=" + General.corrcoeff(d0,d1));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}