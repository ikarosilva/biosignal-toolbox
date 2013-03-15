package com.ikarosilva.statistics;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot;

public class PdfEstimator {


	//px : 1 column= probability, 2nd column lower bound, 3rd column upper bound
	private double[][] pdf;
	private int Nbins;

	public PdfEstimator(){

	}

	public double[][] equipartition(ArrayList<Double> x, int N){
		Nbins=N;
		pdf = new double[Nbins][3];
		int LB=0, UB;
		Collections.sort(x);
		Double binWidth=(x.get(x.size()-1)-x.get(0))/Nbins;
		double index;
		int checkSum=0;
		for(int n=0;n<Nbins;n++){
			index = (n+1)*binWidth;
			if(n != Nbins){
				UB=Math.abs(Collections.binarySearch(x,index))-1;
				//Make sure its stable sorted
				while(UB>0 && UB < x.size() && 
				(x.get(UB) == x.get(UB-1) ))
					UB--;
			}else{
				UB=x.size()-1;
			}			
			pdf[n][0]=(double) (UB-LB+1)/x.size();
			checkSum+=(UB-LB+1);
			pdf[n][1]=x.get(LB);
			pdf[n][2]=x.get(UB);
			LB=UB+1;
		}
		if(checkSum  != x.size()){
			System.err.println("PDF estimation error: check Sum= " 
					+ checkSum + " expected: " 
					+ x.size() + " \n");
		}
		return pdf;
	}


	public double[][] equipartition(ArrayList<Double> x, 
			ArrayList<Double> y, int N){
		Nbins=N;
		//First is pdf, 2nd LB on x, 3rd UB on x
		//4th LB on y, 5th UB on y
		pdf = new double[Nbins*Nbins][5];
		Double minX = Collections.min(x);
		Double minY = Collections.min(y);
		Double maxX = Collections.max(x);
		Double maxY = Collections.max(y);
		Double rangeX= maxX-minX;
		Double rangeY= maxY-minY;
		int nX, nY;
		//Create HashMap to store bin locations of X and Y
		//then use the HashMap to add to the count
		//The HashMap will be created by normalizing the variable and
		//multiplying by the number of bins. So keys are in the 
		// round(normalized*N) domain //TODO: ensure capacity ??
		HashMap<Integer,Double> xy=new HashMap<Integer,Double>();	

		//Initialize HashMap,
		double scaleX=(N-1)/rangeX;
		double scaleY=(N-1)/rangeY;
		double stepX=(double)rangeX/N;
		double stepY=(double)rangeY/N;
        double picketFenceX=(maxX+stepX)/maxX;
        double picketFenceY=(maxY+stepY)/maxY;
        double offsetX=(double)stepX/2;
        double offsetY=(double)stepY/2;
        
		double wcount= (double) 1/x.size();
		int key; 
		Double tmp;
		System.out.println("scale="+scaleX + " range=" + rangeX +
				" min= " + minX + " max= "+ maxX);
		System.out.println("wcount= "+wcount);
		for(int i=0;i<x.size();i++){
			//TODO: fix picket fence issues
			nX= (int) Math.round((x.get(i)*picketFenceX-minX-offsetX)*scaleX  );
			nY= (int) Math.round((y.get(i)*picketFenceY-minY-offsetY)*scaleY  );
			//Map subscript to absolute index
			key=N*nX + nY;
			tmp=xy.get(key);
			
			System.out.println("x= "+ x.get(i) + " nX=" +nX 
					+ " y= "+ y.get(i)+ " nY=" + nY +
					" key= " + key + " tmp=" + tmp + "\n");

			
			if(tmp != null ){
				xy.put(key,tmp + wcount);
			}else{
				xy.put(key,wcount);
			}

		}


		System.out.println("map size=" + xy.size());
		int index=0;
		for(Integer d : xy.keySet()){
			nY=(int)( (double) d/N);
			nX=d-(nY*N);
			System.out.print("xy["+ nX +","+ nY +"]=" + xy.get(d) +"\t");
			System.out.println("d=" + d);
			index++;
		}

		return pdf;

	}


	public static void main(String[] args) {

		//Test the histogram estimation
		ArrayList<Double> x= new ArrayList<Double>();
		ArrayList<Double> y= new ArrayList<Double>();
		double[][] hist;
		int N=1000, bins=6;
		for(int n=0;n<N;n++){
			//x.add((double) Math.random());
			x.add((double) n);
			y.add((double) n);
			//y.add(x.get(n));
			//y.add((double) Math.random());
		}
		PdfEstimator pdf=new PdfEstimator();
		hist=pdf.equipartition(x,y,bins);
	}

}
