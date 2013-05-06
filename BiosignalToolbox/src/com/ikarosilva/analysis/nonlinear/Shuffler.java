package com.ikarosilva.analysis.nonlinear;

import java.io.IOException;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.ui.RefineryUtilities;
import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.SpectralPlot;

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
		//This generates shuffled data with the same autocorrelation as the original dataset
		//Both the original data and this surrogate data should have optimal linear models
		double phase=0;
		double amp=0;
		int midFFT=((int) NFFT/2);
		double[] y= new double[NFFT];
		Complex[] YFFT=new  Complex[NFFT];
		Complex[] TMPFFT=new  Complex[NFFT];
		YFFT[0]=XFFT[0];
		YFFT[midFFT]=XFFT[midFFT];
		//Shuffle the FFT spectrum
		for(int i=1;i<midFFT;i++){
			amp = XFFT[i].abs();
			phase = Math.random()*2*Math.PI;
			//Got to make sure that the FFT is symetrical!
			YFFT[i]=new Complex(amp*Math.cos(phase), amp*Math.sin(phase));
			YFFT[NFFT-i]=YFFT[i].conjugate();
		}
		//Reconstruc in the time-domain
		YFFT=FFT.transform(YFFT,TransformType.INVERSE);
		for(int i=0;i<NFFT;i++)
			y[i]=YFFT[i].getReal();
		return y;
	}

	public static void AmpAdjustedFFTShuffle(){
		//TODO: implement method in pg 356
	}


	public static void main(String[] args) throws IOException, ClassNotFoundException {


		int N=1024;	
		double[] timeSeries= NonlinearProcess.Conway(N);
		Shuffler shuffled = new Shuffler(timeSeries);
		double[] surrogate=shuffled.fftShuffle();

		Plot demo = new Plot("Orginal Time Series",timeSeries);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

		Plot demo0 = new Plot("Surrogate Time Series",surrogate);
		demo0.pack();
		RefineryUtilities.centerFrameOnScreen(demo0);
		demo0.setVisible(true);


		//Plot Spectrum
		SpectralPlot demo2 = new SpectralPlot(
				"Original Spectrum",timeSeries,1);
		demo2.pack();
		RefineryUtilities.centerFrameOnScreen(demo2);
		demo2.setVisible(true);

		//Plot Spectrum
		SpectralPlot demo3 = new SpectralPlot(
				"Surrogate Spectrum",surrogate,1);
		demo3.pack();
		RefineryUtilities.centerFrameOnScreen(demo3);
		demo3.setVisible(true);

	}

}
