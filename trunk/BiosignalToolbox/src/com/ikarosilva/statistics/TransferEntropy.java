/*
 * ===========================================================
 * Transfer Entropy estimation 2013
 *              
 * ===========================================================
 *
 * (C) Copyleft 2013, by Ikaro Silva
 *
 * Project Info:
 *    Code: http://code.google.com/p/wfdb-app-toolbox/
 *    
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *
 * Author:  Ikaro Silva,
 * 
 * Last Modified:	 November 23, 2013
 * 
 *
 */
package com.ikarosilva.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TransferEntropy {
	private final int Q, P;
	private final double[] x, y;
	private final int[] tau;
	private double[] TE;
	private double[] Tz;
	private double Ixy;
	private double stepX, stepY;
	private double[] Mb;
	double[] Rx;
	double[] Ry;

	public TransferEntropy(double[] x,double[] y, int[] tau,int Q){
		if(y.length != x.length)
			new Exception("Vector x and y must be same length");		
		this.Q=Q;
		this.tau=tau;
		this.x=x;
		this.y=y;
		P=x.length;
		Rx=General.minmax(x);
		Ry=General.minmax(y);
		TE = new double[tau.length];
		Tz= new double[tau.length];
		//Get step size closest to equipartition size (for a total of Q-1 bins)
		stepX= (Rx[1]-Rx[0])/Q;
		stepY= (Ry[1]-Ry[0])/Q;
		Mb=new double[Q];
	}

	public double[] estimateTE(){
		HashMap<Integer,Double> mabc;
		double[][] Mbc;
		double[] Mc= new double[P];

		int[] indices=new int[3];
		double abc_count;
		Double mapCheck;
		int cIndex, aIndex, bIndex, oldbIndex, mapIndex;
		double[][] Mab=new double[Q][Q];
		Mbc=new double[Q][Q];

		//Get counts that don't depend on tau, which will be varying
		//So calculate counts for Pv and Pv-1
		bIndex=(int) Math.floor((x[0]-Rx[0])/stepX);
		oldbIndex=bIndex;
		Mb[bIndex]++;
		cIndex=(int) Math.floor((y[0]-Ry[0])/stepY);
		Mc[cIndex]++;
		Mbc[bIndex][cIndex]++;
		for(int i=1;i<P;i++){
			cIndex=(int) Math.floor((y[i]-Ry[0])/stepY);
			cIndex= (cIndex > (Q-1)) ? (Q-1):cIndex;
			Mc[cIndex]++;
			bIndex=(int) Math.floor((x[i]-Rx[0])/stepX);
			bIndex= (bIndex > (Q-1)) ? (Q-1):bIndex;
			Mb[bIndex]++;
			Mab[bIndex][oldbIndex]++;
			oldbIndex=bIndex;
			//FIXME: shouuld be doiuble summation on Mab and Mbc...
			for(int c=1;c<P;c++){
				cIndex=(int) Math.floor((y[i]-Ry[0])/stepY);
				cIndex= (cIndex > (Q-1)) ? (Q-1):cIndex;
				Mbc[bIndex][cIndex]++;
			}
		}
		//Compute mutual Information at zero lag for normalization
		for(int k=0;k<Q;k++){
			for(int j=0;j<Q;j++){
				if((Mbc[k][j] != 0) && (Mc[j] != 0)){
					Ixy+= (Mbc[k][j]/(P*P))*Math.log(Mbc[k][j]/(Mb[k]*Mc[j]));
					System.out.println("i= " + k+ " Ixy=" + Ixy + " Mbc[k][j]= "
							+ Mbc[k][j] + "Mb[k]*Mc[j]" + Mb[k]*Mc[j] 
									+ " -> "+ Mbc[k][j]/Mb[k]*Mc[j]);
					System.out.println("i= " + " Mbc[k][j]= "+ Mbc[k][j] +
							" Mb[k=]" + Mb[k] + " Mc[j]= "+ Mc[j]
									+ " log= " + Math.log(Mbc[k][j]/(Mb[k]*Mc[j])));
				}
			}
		}
		//Calculate TE as a function of tau
		for(int i=0;i<tau.length;i++){
			abc_count=0;
			mapCheck=(double) 0;
			mabc=new HashMap<Integer, Double>();
			bIndex=(int) Math.floor((x[tau[i]-1]-Rx[0])/stepX);
			//Compile the joint pdf counts and Mutual information at zero lag
			for(int k=tau[i];k<P;k++){
				aIndex=(int) Math.floor((x[k]-Rx[0])/stepX);
				cIndex=(int) Math.floor((y[k-tau[i]]-Ry[0])/stepY);
				aIndex= (aIndex > (Q-1)) ? (Q-1):aIndex;
				cIndex= (cIndex > (Q-1)) ? (Q-1):cIndex;
				//Map subscripts to absolute index in the HashMap
				//With dimensions AxBxC
				mapIndex=General.sub2ind(aIndex,bIndex,cIndex,Q);
				mapCheck = mabc.get(mapIndex);
				mapCheck=(mapCheck == null) ? 1:mapCheck++;
				mabc.put(mapIndex,mapCheck);
				Mbc[bIndex][cIndex]++;
				bIndex=aIndex;
			}		
			//Loop throught the Joint calculating the transfer entropy
			for(Integer thisKey : mabc.keySet()){
				abc_count=mabc.get(thisKey);
				//Revert Map subscripts from absolute index in the HashMap
				//to dimensions AxBxC
				indices=General.ind2sub(thisKey,Q);

				TE[i]+= (abc_count/(Q*Q*Q))*Math.log(abc_count*Mb[indices[1]]/
						( Mab[indices[0]][indices[1]]*Mbc[indices[1]][indices[2]] ) );
			}		
			//Get normalized Transfer Entropy
			System.out.println(TE.length+ " i=" + i  + " Ixy=" + Ixy + " lengt=" + TE.length
					+ " lenth tz " + Tz.length);
			Tz[i]=TE[i]/Ixy;
			System.out.println("Ixy= " + Ixy + " tau= "+ tau[i] + " TE=" 
					+ TE[i] + " Tz= " + Tz[i]);
		}
		return TE;
	}

	public static void main(String[] args) throws Exception {

		ArrayList<String> text= new ArrayList<String>();
		double[][] data=null;
		String[] tmpStr=null;
		int colInd;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(args[0])));
			String line = null;
			while ((line = reader.readLine()) != null) {
				text.add(line);
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}

		tmpStr=text.get(0).trim().split("\\s+");
		data=new double[tmpStr.length][text.size()];
		for (int i=1;i<text.size();i++){
			for(colInd=0;colInd<tmpStr.length;colInd++){
				try{    
					data[colInd][i-1]= Double.valueOf(tmpStr[colInd]);
				}catch (NumberFormatException e){
					throw new Exception("Data not in the expected format");
				}
			}
			tmpStr=text.get(i).trim().split("\\s+");
		}
		System.out.println("Calculating Transfer entropy: " + data.length);
		int[]  tau= {2,3,4,5};

		TransferEntropy myTE=new TransferEntropy(data[0],data[0],tau,4);
		myTE.estimateTE();


	}

}
