package com.ikarosilva.utilities;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class Compute implements Callable<Double> {
	private final int min;
	private final int max;
	public Compute(int min, int max){
		this.min=min;
		this.max=max;
	}
	public Double call(){
		double sum=0.0;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=min;i<max;++i)
			sum+=Math.sqrt(i);
		return sum;
	}

}

public class RootSummer {
	private static final int THREADS=1;//Runtime.getRuntime().availableProcessors();
	private static final int N=200000000;
	private static final int FUTURES=THREADS;
	private static final int  quotient=N/FUTURES;
	private static final int  remainder= N%FUTURES;

	public static void main(String[] args){
		ArrayList<Future<Double>> futures=
				new ArrayList<Future<Double>>(FUTURES);
		ExecutorService executor= 
				Executors.newFixedThreadPool(THREADS);
		int work = quotient;

		System.out.println("Creating futures...threads= " + THREADS);
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
