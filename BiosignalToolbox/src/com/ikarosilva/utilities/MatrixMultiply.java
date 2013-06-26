package com.ikarosilva.utilities;

class compute extends Thread {
	private double[][] a;
	private double[][] b;
	private double[][] c;
	private int lower;
	private int upper;
	
	public compute(double[][] a, double[][] b, double[][]c,
			int lower, int upper){
		this.a=a;
		this.b=b;
		this.c=c;
		this.upper=upper;
		this.lower=lower;
	}
	public void run(){
		for(int i= lower;i<upper;i++)
			for(int j=0;j<c[i].length;j++)
				for(int k=0;k<b.length;k++)
					c[i][j]+=a[i][k]*b[k][j];
	}
	
}

public class MatrixMultiply {
	
	public void main(String[] args){
		int N=3;
		double[][] a,b,c;
		compute[] threads= new compute[N];
		int quotient=c.length/N;
		int remainder= c.length%N;
		int start=0;
		for(int i=0;i<N;i++){
			int rows= quotient;
			if(i< remainder)
				rows++;
			threads[i]= new compute(a,b,c,start,start+rows);
			threads[i].start();
			start+=rows;
		}	
	}
	
}
