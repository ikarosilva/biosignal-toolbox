/**
 Copyright (c) 2011 by Ikaro Silva, All Rights Reserved
 Contact Ikaro (ikaro@mit.edu)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
    02111-1307  USA 
 */
package com.ikarosilva.models;

import java.util.Random;
import java.util.Vector;

public class int_fire {

	private double threshold=1;
	private double sigma=0.3;
	private double beta=1;
	private Vector<Double> output;
	private double dt=10/1000; //10 ms
	private int N;
	private double int_sum;
		
	public int_fire(int N){
		output=new Vector<Double>(N/1000);

	}
	public Vector<Double> simulate(int N){

		int_sum=0;
		Random generator = new Random();
		double r;
		int k=0;
		
		for(double n=1;n<N;n=dt*N){
			r=generator.nextGaussian();
			int_sum += (beta+sigma*r)*dt;
			if(int_sum > threshold){
				k++;
				output.set(k,n);
			}
		}
		return output;
	}

	public static void main() {

		//Test the model
		int T=10*60*1000;
		int_fire mod = new int_fire(T);



	}

}
