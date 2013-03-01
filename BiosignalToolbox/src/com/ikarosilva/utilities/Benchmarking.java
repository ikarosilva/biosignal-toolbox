package com.ikarosilva.utilities;

public class Benchmarking {

	public static int sizeThreadPool(int Ncpu, double Ucpu,
					            double Rwc){
		
		//Calculates an optimal pool size for keeping the processors
		//at the desired utilization level
		//From: "Java Concurrency in Practice", B. Goetz, p 171
		
		if(Ncpu == 0)
			Ncpu= Runtime.getRuntime().availableProcessors();
		int Nthreads=(int) (Ncpu * Ucpu * (1+ Rwc)); 
		return Nthreads;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
