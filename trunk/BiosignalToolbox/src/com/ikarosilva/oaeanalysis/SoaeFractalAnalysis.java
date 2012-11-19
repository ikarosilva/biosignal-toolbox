package com.ikarosilva.oaeanalysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.fileio.ShortFile;
import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.SpectralPlottingDemo1;

public class SoaeFractalAnalysis {


	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//Read Short file from disk	
		FileInputStream nFile = new FileInputStream("/home/ikaro/oae_data/cc1.raw");
		ObjectInputStream in = new ObjectInputStream(nFile);
		short[] tmpdata=null;
		double Fs=8000;
		
		//Based on Epstein and Silva, 2010, optimal time window size lenght is between 10-20ms. 
		//So that for 8kHz we have about 160 samples (set to 128 so its a power of 2), yielding a spectral resolution of about
		// 8kHz/160 = 50 Hz. So that we look for  a maximum peak at 1kHz +- 200 Hz 
		double Ftrack=0;
		double FtrackUB=1200;
		double FtrackLB=800;
		double Pmax=0;
		double dc=0;
		int winSize=128;

		tmpdata = (short[]) in.readObject();
		in.close();
		int N=(int) Math.pow(2,Math.floor(Math.log((int) tmpdata.length)/Math.log(2)));
		double fres=Fs/N;
		double[] data=new double[N];
		for(int i=0;i<N;i++)
			data[i]=(double)tmpdata[i];

		//Get powerspectrum of data and calculate maximum peak around Ftrack
		FastFourierTransformer FFT = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] A= FFT.transform(data, TransformType.FORWARD);
		double[] Pxx= new double[A.length];
		
		for(int i=0;i<Pxx.length;i++){
			Pxx[i]=A[i].abs();
			if(fres*(double)i >= FtrackLB && fres*(double)i <= FtrackUB){
				if(Pxx[i]>Pmax){
					Pmax=Pxx[i];
					Ftrack=fres*(double)i;
				}	
			}
		}
		
		//Find the Frequency index based on new window size
		int Findex=0;
		double Fsearch=winSize;
		for(int i=0;i<winSize;i++){
			if(Math.abs(Ftrack-i*Fs/winSize) < Fsearch){
				Fsearch=Math.abs(Ftrack-i*Fs/winSize);
				Findex=i;
			}
		}
		
		//Now that the frequency has been selected. Track its power spectrum value as a function of time
		//over the selected window size.
		int M=(int) Math.floor(N/winSize);
		double[] tmpTimeSeries= new double[winSize];
		Double[] timeSeries=new Double[M];
		
		for(int m=0;m<M;m++){
			for(int i=0;i<winSize;i++){
				tmpTimeSeries[i]=data[m*winSize+i];
			}
			Complex[] A1= FFT.transform(tmpTimeSeries, TransformType.FORWARD);
			timeSeries[m]=Double.valueOf((double) A1[Findex].abs());
			dc+=timeSeries[m];
			//System.out.println(A1[Findex].abs()+ " F=" + Findex);
		}
		dc/=M;
		//Subtract mean from time series
		for(int i=0;i<M;i++)
			timeSeries[i]-=dc;
		
		//Plot results
		Plot demo = new Plot("SEPOAE",timeSeries);
		demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
		
	}
}
