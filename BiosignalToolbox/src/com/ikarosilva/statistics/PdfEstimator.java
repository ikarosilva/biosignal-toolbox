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
		double rndX, rndY;
		Double tmp;
		System.out.print("scale="+scaleX + " range=" + rangeX +
				" min= " + minX + " max= "+ maxX);
		System.out.println(" wcount= "+wcount);
		for(int i=0;i<x.size();i++){
			//TODO: fix picket fence issues
			rndX= Math.round((((x.get(i)-minX)*picketFenceX)-offsetX)*scaleX);
			rndY= Math.round((((y.get(i)-minY)*picketFenceY)-offsetY)*scaleY);
			nX= (int) rndX;
			nY= (int) rndY;
			//Map subscript to absolute index
			key=N*nX + nY;
			tmp=xy.get(key);
			
			/*
			System.out.println("x= "+ x.get(i) + " rX=" +rndX 
					+ " nX= " + nX + " y= "+ y.get(i)+
					" rY=" + " key= " + key + " tmp=" + tmp);
			*/
			
			if(tmp != null ){
				xy.put(key,tmp + wcount);
			}else{
				xy.put(key,wcount);
			}

		}


		System.out.println("map size=" + xy.size());
		for(Integer d : xy.keySet()){
			nY=(int)( (double) d/N);
			nX=d-(nY*N);
			System.out.print("xy["+ nX +","+ nY +"]=" + xy.get(d) +"\t");
			System.out.println("d=" + d);
		}

		return pdf;

	}


	public static void main(String[] args) {

		//Test the histogram estimation
		ArrayList<Double> x= new ArrayList<Double>();
		ArrayList<Double> y= new ArrayList<Double>();
		double[][] hist;
		int N=1000, bins=2;
		for(int n=0;n<N;n++){
			x.add((double) Math.random());
			//x.add((double) n);
			//y.add((double) n);
			//y.add(x.get(n));
			y.add((double) Math.random());
		}
		PdfEstimator pdf=new PdfEstimator();
		hist=pdf.equipartition(x,y,bins);
	}

}
