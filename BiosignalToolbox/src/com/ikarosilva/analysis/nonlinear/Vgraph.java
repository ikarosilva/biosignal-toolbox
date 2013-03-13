package com.ikarosilva.analysis.nonlinear;
import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.fractals.Conway;
import com.ikarosilva.graphics.Plot;

//Visibility Graph 
//source: "From time series to complex networks: The visibility graph
//Lacasa, Luque, Ballesteros, Luque, Nuno, PNAS, April,1 ,2008, 105(13)

//Written by Ikaro Silva, 2011
public class Vgraph {

	public static  Double[] run(Double[] data){

		Double[] pk = new Double[data.length];
		int[] backward_links=new int[data.length];
		double yab= 0, slope=0, intercept=0;
		int links = 1;
		boolean islink;
		int a,b,c;
		for(a=1;a<data.length-1;a++){
			pk[a]=(double) 0;
			backward_links[a]= 1;
		}
		backward_links[0]= 0;
		for(a=0;a<(data.length-1);a++){
			//Add link due to the first forward neighbor
			links=1;		
			for (b=a+2;b<data.length-1;b++){	
				islink=true;
				slope= ( data[b]-data[a])/(b-a);
				intercept=data[b] - slope*b;
				for(c=a+1;c<b;c++){						
					//yab= data[b] + ( (data[a] -data[b])*( (b-c)/(b-a)) ); This equation from the paper may not be right
					yab=intercept + slope*c;
					/*		
					if(b==8)
					 System.out.println(" yab= " + yab + 
							 			" (a)data[" + a + "]=" + data[a] +
							 			" (b)data[" + b + "]=" + data[b] +
							 			" (c)data[" + c + "]=" + data[c] +
							 			" islink= " + (data[c] < yab) );
						*/	
					if(data[c] >= yab){
						islink=false;
						break;
					}
				} //c
				if(islink){
					links++;
					backward_links[b]++;
				}
			}//b	
			pk[links + backward_links[a]]++;
			//System.out.println("bk_pk[" + a + "]=" + backward_links[a]);
		}//a
		//Convert pk array into probability values
		double test=0;
		for(a=1;a<data.length-1;a++){
			//if(pk[a] >2 )
			System.out.println("pk[" + a + "]=" + pk[a]);
			pk[a]/=(data.length-1);
			System.out.println("probability: pk[" + a + "]=" + pk[a]);
			test +=pk[a];
		}
		System.out.println("sum= " + test);
		return pk;
	}


	public static void main(String[] args) {
		// Test the visibility graph with a sample
		// fractal signal
		int N=11;
		double[] data= new double[N];
		//data = Conway.sim(N);
		for(int n=0;n<N;n++)
			data[n]=(double) 1;
		//	data[n]=Math.random();
	/*
		data[5]=(double) 2;
		double[] pk = Vgraph.run(data);	
		Plot plot = new Plot(
				"P(k)",pk);
		plot.pack();
		RefineryUtilities.centerFrameOnScreen(plot);
		plot.setVisible(true);
		*/
	}

}
