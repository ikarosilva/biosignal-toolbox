package com.ikarosilva.analysis.nonlinear;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class Shuffler {

	public double[] x;
	public Complex[] XFFT;
	public int NFFT;
	public FastFourierTransformer FFT;

	//Methods for generating surrogate data based on :
	//Understanding Nonlinear Dynamics, Kaplan and Glass, 1995
	public Shuffler(double[] xin){

		//Generate FFT from original time series and store
		NFFT=(int) Math.pow(2,Math.floor(Math.log((int) xin.length)/Math.log(2)));		
		x = new double[NFFT];
		for(int i=0;i<NFFT;i++){
			x[i]=xin[i];	
		}	
		XFFT=new Complex[NFFT];
		//Get powerspectrum of data and calculate maximum peak around Ftrack
		FFT = new FastFourierTransformer(DftNormalization.STANDARD);
		XFFT= FFT.transform(x, TransformType.FORWARD);	
	}

	public double[] fftShuffle(){
		//Implement surrogate generation using FFT method in pg 343 and 354

		double phase=0;
		double amp=0;
		int midFFT=(int) NFFT/2;
		double[] y= new double[NFFT];
		Complex[] YFFT=new  Complex[NFFT];
		Complex[] TMPFFT=new  Complex[NFFT];
		System.out.println("midFFT:" + midFFT);
		YFFT[0]=XFFT[0];
		YFFT[midFFT]=XFFT[midFFT];
		for(int i=1;i<midFFT;i++){
			amp = XFFT[i].abs();
			phase = Math.random()*2*Math.PI;
			//Got to make sure that the FFT is symetrical!
			YFFT[i]=new Complex(amp*Math.cos(2*Math.PI*phase), amp*Math.sin(2*Math.PI*phase));
			YFFT[i+midFFT]=new Complex(amp*Math.cos(2*Math.PI*phase), -1*amp*Math.sin(2*Math.PI*phase));
		}
		TMPFFT=FFT.transform(YFFT,TransformType.INVERSE);
		double maxImag=0;
		for(int i=0;i<NFFT;i++){
			y[i]=TMPFFT[i].getReal();
			if(Math.abs(TMPFFT[i].getImaginary())>maxImag){
				maxImag=Math.abs(TMPFFT[i].getImaginary());
			}	
		}
		System.out.println("Maximum imaginary is:" + maxImag);
		return y;
	}

	public static void AmpAdjustedFFTShuffle(){
		//TODO: implement method in pg 356
	}
}
