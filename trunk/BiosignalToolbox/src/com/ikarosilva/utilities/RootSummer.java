package com.ikarosilva.utilities;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Compute implements Callable<Double> {
	private int min;
	private int max;
	public Compute(int min, int max){
		this.min=min;
		this.max=max;
	}
	public Double call(){
		double sum=0.0;
		for(int i=min;i<max;++i)
			sum+=Math.sqrt(i);
		return sum;
	}

}

public class RootSummer {
	private static final int THREADS=4;
	private static final int N=2000000000;
	private static final int FUTURES=1000;

	public static void main(String[] args){
		ArrayList<Future<Double>> futures=
				new ArrayList<Future<Double>>(FUTURES);
		ExecutorService executor= 
				Executors.newFixedThreadPool(THREADS);
		int work = N/ FUTURES;

		System.out.println("Creating futures...");
		long start=System.currentTimeMillis();
		for(int i=0;i<FUTURES;i++){
			Callable<Double> summer = 
					new Compute(1+i*work,1+ (i+1)*work);
			Future<Double> future= executor.submit(summer);
			futures.add(future);
		}

		
		System.out.println("Getting results from futures....");
		double sum=0;
		for( Future<Double> future: futures){
			try{
				sum+=future.get();
			}catch (InterruptedException e){
				e.printStackTrace();
			}catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		long end=System.currentTimeMillis();
		executor.shutdown();
		System.out.println("sum is = " + sum + " time= "+
		(end-start)/1000.0);
	}

}
