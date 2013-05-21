package com.ikarosilva.analysis.nonlinear;

import java.util.ArrayList;
import java.util.Arrays;
import com.ikarosilva.statistics.General;

public class EmbeddedModeling {

	public enum Norm{
		MAX,
		EUCLIDEAN
	}

	private int tau; //delay between state samples
	private double[][] distance; //Square matrix of state distances
	private Norm norm; //Stores the type of norm used to test distance
	private int step;
	private double[] data;
	private int N;
	public double[][] prediction;
	public boolean localLinearPrediction;
	public double lypunovExponent;
	public boolean applyWeight;
	
	public EmbeddedModeling(double[] data,int tau, Norm norm){
		this.tau=tau;
		this.norm=norm;
		this.data=data;
		N=data.length;
		step=1;//Step size between state vectors (in sample) in order to avoid local similarities
		localLinearPrediction=true;
		applyWeight=false;
	}
	
	public void setApplyWeight(boolean set){
		applyWeight=set;
	}
	
	public void setlocalLinearPrediction(boolean set){
		localLinearPrediction=set;
	}
	public void setData(double[] data){
		this.data=data;
		N=data.length;
	}
	public double[][] getNormDistance(int tau, int M){
		//Calculates distance between vectors
		double[][] dist =new double[M][];
		return dist;
	}

	public double[] correlationDimension(int[] M, int iterations){
		/*
		Assumptions: One of the hardest task is choosing the appropiate range 
		for the threshold value (ie, the "scaling region"). A value too small
		will yield very high slope variability between the embedded dimension due to measurement noise. A
		value of th too high will have embedded dimensions slopes to converge to 0.

		  Moreover, the slope estimation should be really done manually for segmenting the initial transition
		  from the steady state behaviour (ie, determining the knee-point of the correlation curve).

		  We use the suggestions on Kaplan (pg 354) to deal with the scaling region issue.
		 */
		iterations++; //First loop is for th1
		double[] v= new double[M.length];
		double th1 = Math.sqrt(General.var(data))/4;
		double step = 1.5;
		double[] corr= new double[iterations];
		
		//According to Kaplan first threshold should be proportional to noise and the second
		//one 5x the first
		double[] best=new double[2];
		for(int m=0;m<M.length;m++){	
			for(int i=0;i<iterations;i++)
				corr[i]=correlationIntegral(th1+th1*i*step,M[m]);
			
			//Search for value closest to C(th1)/C(th2) ~ 5
			best[0]=Double.MAX_VALUE;
			best[1]=0;
				for(int i=1;i<iterations;i++){
					if(Math.abs(corr[i]-corr[0]/5)<best[0]){
						best[0]=corr[i]-corr[0]/5;
						best[1]=i;
					}
				}
					
			v[m]=(Math.log(corr[0])-Math.log(corr[(int)best[1]])) / 
					(Math.log(th1)-Math.log(th1+th1*step*best[1]));
		}
		return v;
	}

	public double[] correlationIntegral(double[] threshold, int M){
		double[] v= new double[threshold.length];
		for(int i=0;i<threshold.length;i++)
			v[i]=correlationIntegral(threshold[i],M);
		return v;
	}

	public double correlationIntegral(double threshold, int M){
		//Calculate the correlation integral of the time series
		//based on equation 6.34 (pg 317) of Kaplan
		int n, m, k, count=0;
		int endPoint=(N-1) - (M-1)*tau+step;
		double Corr=0, dist;
		double[] v1= new double[M];
		double[] v2= new double[M];
		for(n=0;n<(endPoint-(M-1)*tau-step);n=n+step){
			for(k=n+(M-1)*tau+step;k<endPoint;k++){
				for(m=0;m<M;m++){
					v1[m]=data[n+m*tau];
					v2[m]=data[k+m*tau];
				}
				dist=getDistance(v1,v2);	
				count++;
				//If within tolerance, increment neighbor count
				if(dist<threshold)
					Corr++;
			}
		}
		return Corr/count;
	}


	public double getDistance(double[] v1, double[] v2){
		double dist=0;
		for(int m=0;m<v1.length;m++){
			switch (norm) {
			case MAX:
				dist+=Math.abs(v1[m]-v2[m]);
				break;                
			case EUCLIDEAN:
				dist+=Math.sqrt((v1[m]-v2[m])*(v1[m]-v2[m]));
				break;                     
			default:
				System.out.println("Unknown norm");			
			}
		}
		return dist;
	}

	public double[] predictivePowerLeaveHalf(double[] timeSeries,int M, double th, int[] neighborSize){
		double[] err=new double[neighborSize.length];
		for(int i=0;i<neighborSize.length;i++){
			err[i]=predictivePowerLeaveHalf(timeSeries,M,th,neighborSize[i]);
			//System.out.println("Dim= " +neighborSize[i] + "-> err= " + err[i]);
		}
		return err;
	}

	public double predictivePowerLeaveHalf(double[] timeSeries,int M, double th,int neighborSize){
		/*
		 * Estimates the predictive power of the time series by calculating the 
		 * error ratio between the embedded model of size M and variance of the time series 
		 */
		int n, m;
		double[] v1= new double[M];
		lypunovExponent=1;
		//Use the beginning as the training data
		int N0=Math.round(timeSeries.length/2);
		setData(Arrays.copyOfRange(timeSeries,0,N0));
		ArrayList<Double> err= new ArrayList<Double>();
		err.ensureCapacity(N-N0);
		prediction = new double[2][timeSeries.length-1];

		for(n=(N0+(M-1)*tau-1);n<timeSeries.length-1;n=n+step){
			//Get the vector to match and its future value
			for(m=0;m<M;m++){
				v1[m]=timeSeries[n-(M-1-m)*tau];
			}
			//Get the prediction
			try {
				prediction[0][n]=v1[M-1];
				prediction[1][n]=predict(v1,th,neighborSize);
				err.add((timeSeries[n+1]-prediction[1][n])
						*(timeSeries[n+1]-prediction[1][n]));
			} catch (Exception e) {
				prediction[0][n]=Double.NaN;
				prediction[1][n]=Double.NaN;
				e.printStackTrace();
			}
		}
		lypunovExponent=Math.pow(lypunovExponent,1/err.size());
		return General.mean(err)/General.var(timeSeries);
	}

	public double[] predictivePowerLeaveOne(double[] timeSeries,int M, double th, int[] neighborSize){
		double[] err=new double[neighborSize.length];
		for(int i=0;i<neighborSize.length;i++){
			err[i]=predictivePowerLeaveOne(timeSeries,M,th,neighborSize[i]);
		}
		return err;
	}

	public double predictivePowerLeaveOne(double[] timeSeries,int M, double th,	int neighborSize){
		/*
		 * Estimates the predictive power of the time series by calculating the 
		 * error ratio between the embedded model of size M and variance of the time series 
		 */
		int n, k, m;
		double[] tmpData=new double[timeSeries.length-1];
		double[] v1= new double[M];
		double future=0, futureHat;
		ArrayList<Double> err= new ArrayList<Double>();
		err.ensureCapacity(timeSeries.length);

		for(n=(M-1)*tau;n<timeSeries.length-1;n=n+step){
			//Generate truncate time-series with the value to predict removed
			for(k=0;k<tmpData.length;k++){
				if(k != n)
					tmpData[k]=timeSeries[k];
			}
			//Get the vector to match and its future value
			for(m=n;m>(n-M+1);m--)
				v1[m]=timeSeries[n-(n-m)*tau];
			future=timeSeries[n+1];

			//Reset the data
			this.setData(tmpData);

			//Get the prediction
			try {
				futureHat=predict(v1,th,neighborSize);
				//System.out.println(future +" , "+ futureHat);
				err.add((future-futureHat)*(future-futureHat));
			} catch (Exception e) {
				System.err.println("Non-valid prediction...skipping sample");
			}
		}

		//return ratio of mse to signal variance
		//System.out.println("err= " + General.mean(err));
		//System.out.println("std= " + General.var(timeSeries));
		return General.mean(err)/General.var(timeSeries);

	}

	public double predict(double[] x, double th, int neighborSize) throws Exception{
		//Find history that matches current state and average them to find the future
		//with neighborhood limit of neighborSize
		double y= 0, dist;
		int n, m,k, count=0; 
		int M=x.length;
		double[] v1=new double[M];
		int bufferSize=Math.round(N/4)+1;
		bufferSize=(bufferSize > neighborSize) ? bufferSize:neighborSize;
		double[][] neighboor= new double[bufferSize][2]; //First column is dist, second is future index
		double max=Double.MIN_VALUE;
		int maxInd=0, index;
		int endPoint=(N-1) - (M-1)*tau;
		double intercept=0, slope=0, center=0;
		
		
		if(neighborSize >= N)
			throw new Exception("Neighboor size =" + neighborSize +" but data size is only = " + N);

		for(n=0;n<endPoint;n=n+step){
			for(m=0;m<M;m++){
				v1[m]=data[n+m*tau];
				//System.out.println("v1= " + data[n+m*tau]);
			}
			dist=getDistance(x,v1);	
			//If within tolerance add to the neighboor hood
			if(dist<th){
				if(count<bufferSize){
					neighboor[count][0]=dist;
					neighboor[count][1]=n+(M-1)*tau +1; //future index
					if(count ==(bufferSize-1)){
						for(k=0;k<bufferSize;k++){
							if(neighboor[k][0] > max){
								max=neighboor[k][0];
								maxInd=k;	
							}
						}
					}
					count++;
				}else if(dist<max){
					//Buffer is full but current value is better than what is on the buffer
					neighboor[maxInd][0]=dist;
					neighboor[maxInd][1]=n+(M-1)*tau+1;//future index
					//Recalculate the max
					max=Double.MIN_VALUE;
					maxInd=0;
					for(k=0;k<bufferSize;k++){
						if(neighboor[k][0] > max){
							max=neighboor[k][0];
							maxInd=k;	
						}
					}
				}
			}//End of if(dist<th)
		}

		if(count < (neighborSize/10)){
			System.err.println("Neighboorhood size is too small for prediction, size= " + count);
			return Double.NaN;
		}else if(count < neighborSize)
			System.err.println("Neighboorhood size is: " + count +" , expected :" + neighborSize);

		if(count>neighborSize){
			count=neighborSize;
			//Sort the array and the the closest neighbors
			neighboor=General.sortRows(neighboor);
		}

		if(applyWeight){
			//Apply weight inversely proportional to distance
			double[] weights = new double[count];
			double sumWeight=0;
			for(n=0;n<count;n++){
				weights[n]=1/neighboor[n][0];
				sumWeight+=weights[n];
			}
			//Scale so that weights sum to one apply the weighted averaging 
			for(n=0;n<count;n++){
				weights[n]=weights[n]/sumWeight;
				index=(int) neighboor[n][1];
				center+= weights[n]*data[index-1];
				intercept+=weights[n]*data[index];
			}			
		}else{
			//Get average over the neighborhood
			//System.out.println("\tAveraging: " + count + " points");
			for(n=0;n<count;n++){
				index=(int) neighboor[n][1];
				center=(n*center + data[index-1])/(n+1);
				intercept=(n*intercept + data[index])/(n+1);
				
			}
		}
		slope=intercept-center;
		lypunovExponent=lypunovExponent*(slope);
		if(localLinearPrediction){
			y=intercept+slope*(x[M-1]-center);
		}else{
			y=intercept;
		}
			
		return y;
	}

	
	public double falseNeighbour(double threshold,double sigma, int M, double th){
		/*
		Calculates the number of false neighbors for this model
		given a series of threshold values. Equation from
		pg 37 "Nonlinear Time Series Analysis", Kantz et al.
		sigma is  and estimate of the standard deviation of
		the noise in the time series.
		Assumptions:
			1. The distance matrix between the vectors 
			is measured with the maximum norm.
		 */
		double[][] distanceM1= getNormDistance(tau,M+1);
		int num=0, den=0, minIndex=0;
		double th1= sigma/threshold;
		for(int i=0;i<N;i++){
			//Perform search for minimum distance
			minIndex=i+1;
			for(int k=0;k<N;k++){
				if(k != i)
					minIndex=(distance[i][k]<distance[i][minIndex]) ?
							k:minIndex;
			}
			if(distance[i][minIndex]< th1){
				//The smallest distance is within the accepted noise threshold
				//now proceed to see wether at higher dimension the
				//distance is still within the input threshold.
				if((distanceM1[i][minIndex]/distance[i][minIndex])
						< threshold){
					num++;
				}
				den++;
			}
		}
		return (double) num/den;	
	}
	
}
