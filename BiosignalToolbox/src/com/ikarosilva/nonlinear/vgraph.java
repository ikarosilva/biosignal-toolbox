package com.ikarosilva.nonlinear;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Visibility Graph 
//source: "From time series to complex networks: The visibility graph
//Lacasa, Luque, Ballesteros, Luque, Nuno, PNAS, April,1 ,2008, 105(13)

//Written by Ikaro Silva, 2011
public class vgraph {

	public static  ArrayList<HashSet> run(double[] x){
		
		ArrayList<HashSet> y = new ArrayList<HashSet>();
		HashSet tmp;
		y.ensureCapacity(x.length-1);
		for(int i=0;i<(x.length-1);i++){
			tmp=new HashSet();
			int cur_point=i;
			int vis_point=i+1;
			//int df=0
			//while(  )
			
			
		}
		
		return y;
	}
	
	
	public static void main(Double[] x) {
		// TODO Auto-generated method stub

	}

}
