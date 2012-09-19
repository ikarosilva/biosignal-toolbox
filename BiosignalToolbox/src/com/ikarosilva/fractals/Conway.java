package com.ikarosilva.fractals;

public class Conway {

	static public Double[] sim(int N){
		Double[] output= new Double[N];
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
	
	public static Double[] main(int N){
		Double[] output = Conway.sim(N);
		return output;
	}


}

