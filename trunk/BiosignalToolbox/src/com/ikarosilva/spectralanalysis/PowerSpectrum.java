package com.ikarosilva.spectralanalysis;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class PowerSpectrum {

	public static double[][] PowerSpectrum(double[] x, double Fs){
		int N=(int) Math.pow(2,Math.floor(Math.log((int) x.length)/Math.log(2)));
		double fres=Fs/N;
        double[] tmp=new double[N];
        
		//Truncate data so its a power of two
		if(x.length != N){
			for(int i=0;i<N;i++)
				tmp[i]=x[i];
		}
		
		//Get powerspectrum of data and calculate maximum peak around Ftrack
		FastFourierTransformer FFT = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] A= FFT.transform(tmp, TransformType.FORWARD);
		double[][] Pxx= new double[A.length][2];

		for(int i=0;i<Pxx.length;i++){
			Pxx[i][0]=fres*(double)i;
			Pxx[i][1]=A[i].abs();
		}
		
		return Pxx;
	}


}
