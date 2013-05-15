package com.ikarosilva.statistics;

public class Factorial {

	public static double factorial(int n) throws Exception{
		if(n<0){
			throw new Exception("Negative value not allowed:" + n);
		}
		if(n==0){
			return 1;
		}else if (n>99){
			//Use Stirling's approximation for large values
			return Math.sqrt(Math.PI*n*2)*
					Math.pow(n/Math.E,n);
		}
		else{
			return n*factorial(n-1);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
