package com.ikarosilva.analysis.nonlinear;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.SpectralPlot;

public class Shuffler {

	public static FastFourierTransformer FFT;

	//Methods for generating surrogate data based on :
	//Understanding Nonlinear Dynamics, Kaplan and Glass, 1995

	public static double[] phaseShuffle(final double[] xin){
		//Implement surrogate generation using FFT method in pg 343 and 354
		//This generates shuffled data with the same autocorrelation as the original dataset
		//Both the original data and this surrogate data should have optimal linear models

		//NOTE: The amplitude in the surrogate data will have a Gaussian distributtion, which
		// may not be optimal because this could be used by a test that check for non-gaussian 
		//distributions on the real set (ie, surrogate data may not cluster around
		// peaks as in the case of arctangent transformation). Thus, this method assumes
		// linear dynamics with linear measurements as the Null Hypothesis as an 
		// implicit assumption.
		//Generate FFT from original time series and store
		int NFFT=(int) Math.pow(2,Math.ceil(Math.log((int) xin.length)/Math.log(2)));		
		double[] x = Arrays.copyOf(xin,NFFT);
		double[] y= new double[xin.length];
		
		Complex[] XFFT=new Complex[NFFT];
		//Get powerspectrum of data and calculate maximum peak around Ftrack
		FFT = new FastFourierTransformer(DftNormalization.STANDARD);
		XFFT= FFT.transform(x, TransformType.FORWARD);
		double phase=0;
		double amp=0;
		int midFFT=((int) NFFT/2);
		Complex[] YFFT=new  Complex[NFFT];
		YFFT[0]=XFFT[0];
		YFFT[midFFT]=XFFT[midFFT];
		Random rd = new Random();
		rd.setSeed(System.currentTimeMillis());
		//Shuffle the FFT spectrum
		for(int i=1;i<midFFT;i++){
			amp = XFFT[i].abs();
			phase =2*Math.PI*rd.nextDouble();
			//Got to make sure that the FFT is symetrical!
			YFFT[i]=new Complex(amp*Math.cos(phase), amp*Math.sin(phase));
			YFFT[NFFT-i]=YFFT[i].conjugate();
		}
		//Reconstruct in the time-domain
		YFFT=FFT.transform(YFFT,TransformType.INVERSE);
		for(int i=0;i<xin.length;i++)
			y[i]=YFFT[i].getReal();
		return y;
	}

	public static double[] amplitudeTransform(double[] source){
		double[] target=new double[source.length];
		Random rd = new Random();
		rd.setSeed(System.currentTimeMillis());
		for(int i=0;i<target.length;i++)
			target[i]=rd.nextGaussian();	
		return amplitudeTransform(source,target);

	}

	public static double[] amplitudeTransform(final double[] source, final double[] target){
		//Steps:
		//1. Sort the source by increasing amp
		//2. Sort target as #1
		//3. Swap source amp by target amp
		//4. Sort #3 by increasing time index of #1
		int ampIndex=0;
		double [] sortedSource=Arrays.copyOf(source, source.length);
		double [] sortedTarget=Arrays.copyOf(target, target.length);
		double [] results=new double[target.length];
		Arrays.sort(sortedSource);
		Arrays.sort(sortedTarget);

		for(int timeIndex=0;timeIndex<source.length;timeIndex++){
			ampIndex=Arrays.binarySearch(sortedSource,source[timeIndex]);
			results[timeIndex]=sortedTarget[ampIndex];
		}
		return results;

	}
	public static double[] AmplitudeAdjustedPhaseShuffle(final double[] source){
		//Underlying assumption is that the Null Hypothesis consists
		//of linear dynamics with possibly non-linear, monotonically increasing,
		//measurement function.

		/*Steps:
		 * 1. Amp transform original data to Gaussian distribution
		 * 2. Phase randomize #1
		 * 3. Amp transform #2 to original
		 * 
		 ***Auto-correlation function should be similar but not exact!
		 */
		double[] target = amplitudeTransform(source);
		target=phaseShuffle(target);
		return amplitudeTransform(target,source);

	}


	public static void main(String[] args) throws IOException, ClassNotFoundException {


		int N=600;	
		double[] timeSeries= NonlinearProcess.Conway(N);
		double[] surrogate=AmplitudeAdjustedPhaseShuffle(timeSeries);

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
