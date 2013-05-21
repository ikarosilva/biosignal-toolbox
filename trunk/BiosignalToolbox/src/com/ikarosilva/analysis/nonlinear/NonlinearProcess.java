package com.ikarosilva.analysis.nonlinear;


import java.util.Arrays;
import java.util.Random;
import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.ScatterPlot;
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
	
	public static double[] modelThree(int N){
		double[] data=new double[N];
		double a=0.5, b=3, T=0.1, sig1=1,sig2=3;
		double x=0, y=0, dx=0, dy=0;
		Random rnd1= new Random(System.currentTimeMillis());
		Random rnd2= new Random(System.currentTimeMillis()*3);
		for(int i=0;i<N;i++){
			dx=y + rnd2.nextGaussian()*sig2;
			dy=-a*y - b*x;
			x=dx*T +x;
			y=dy*T +y;
			data[i]=x + rnd1.nextGaussian()*sig1; 
		}
		return data;
	}
	
	public static double[] modelFour(int N){
		double[] data=new double[N];
		double u=4;
		data[0]=0.2;
		for(int n=1;n<N;n++)
			data[n]=u*data[n-1]*(1-data[n-1]);
		return data;
	}
	
	public static double[] whiteNoise(int N){
		double[] x=new double[N];
		Random rnd=new Random();
		for(int i=0;i<N;i++)
			x[i]=rnd.nextGaussian();
		return x;
	}
	public static double[] bloodCell(int N){
		double[] data=new double[N];
		double dx=0,x=10,y=0.1, dy=0,dt=0.001;
		for(int n=0;n<N;n++){
			dx=(2*y/(1+ y*y) ) - x;
			dy= x- y;
			y=dy*dt + y;
			x=dx*dt + x;
			data[n]=x;
		}
		return data;
	}
	
	public static double[] modelFive(int N){
		double[] data=new double[N];
		double xd=0,x=0,y=0, yd=0, 
				z=0, sig1=0.1, m=0, 
				u=0.7, b=0.3, sig2=0.05;
		Random rnd1= new Random(System.currentTimeMillis());
		Random rnd2= new Random(System.currentTimeMillis());
		for(int n=0;n<N;n++){
			m=0.4 - (6/(1+ x*x + y*y));
			yd=y;
			xd=x;
			y=u*(xd*Math.sin(m) + yd*Math.cos(m));
			x=1 + u*(xd*Math.cos(m) - yd*Math.sin(m)) + 0.2*z;
			z=1.4 + 0.3*rnd1.nextGaussian()*sig1-z*z;
			data[n]=x + b*z + rnd2.nextGaussian()*sig2;
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
		double[] data= modelFour(N);
		double th=Double.MAX_VALUE;
		int M=1; //Embedding Dimension
		int[] neighborSize= {1,2,3,4,5,10,20,40,62,80};
		double [] v = new double[neighborSize.length];
		EmbeddedModeling model= new EmbeddedModeling(data,1,EmbeddedModeling.Norm.EUCLIDEAN);
		
		General.demean(data);
		v=model.predictivePowerLeaveHalf(data,M,th,neighborSize);
		
		
		Plot plt= new Plot("",neighborSize,v);
		//Plot plt2= new Plot("",data);
		double[][] p=model.prediction;
		int mid=(int)Math.round(N/2);
		
		
		/*
		ScatterPlot sct = new
                  ScatterPlot("",Arrays.copyOfRange(data,0,mid-2),Arrays.copyOfRange(data,1,mid-1)
                		  ,p[0],p[1],
                		  Arrays.copyOfRange(data,mid,N-2),Arrays.copyOfRange(data,mid+1,N-1));
		*/
		System.out.println("done!");
		
	}

}