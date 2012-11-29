package com.ikarosilva.oaeanalysis;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.optimization.fitting.CurveFitter;
import org.apache.commons.math3.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.graphics.Plot;
import com.ikarosilva.graphics.ScatterPlot;
import com.ikarosilva.nonlinear.Shuffler;

public class SoaeFractalAnalysis {

	double Ftrack=0;
	double Pmax=0;
	double dc=0;
	int winSize=128;
	double Fs=8000;
	int Findex=0;
	double Fsearch=winSize;
	int N=0;
	double fres=Fs/N;
	double[] data=new double[N];
	FastFourierTransformer FFT;
	double[] Pxx=null;
	double[] Fxx=null;
	double[] timeSeries=null;
	double[] timeSeriesLag=null;
	BufferedWriter out_log =null;
	private static String data_dir="/home/ikaro/oae_data/";

	public void getData(String inFileName){
		//Read Short file from disk	
		short[] tmpdata=null;
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(inFileName);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(inFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			tmpdata = (short[]) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		N=(int) Math.pow(2,Math.floor(Math.log((int) tmpdata.length)/Math.log(2)));
		data=new double[N];
		for(int i=0;i<N;i++){
			data[i]=(double)tmpdata[i];
		}

	}

	public void writeData(String outFile, double[][] data){

		File log_file = new File(outFile);

		try {
			//FileWriter out_log_writer=new FileWriter(log_file);
			out_log= new BufferedWriter(new FileWriter(log_file));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not generate log file:" + log_file);
		}

		int k=0;
		Double tmpdouble=(double) 0;

		for(int i=0;i<data[0].length;i++){
			for(k=0;k<data.length;k++){
				try {
					tmpdouble=data[k][i];
					out_log.write(tmpdouble.toString());
					out_log.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getSpectrum(){

		double FtrackUB=1200;
		double FtrackLB=800;
		//Get powerspectrum of data and calculate maximum peak around Ftrack
		//Based on Epstein and Silva, 2010, optimal time window size lenght is between 10-20ms. 
		//So that for 8kHz we have about 160 samples (set to 128 so its a power of 2), yielding a spectral resolution of about
		// 8kHz/160 = 50 Hz. So that we look for  a maximum peak at 1kHz +- 200 Hz 
		FFT= new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] A= FFT.transform(data, TransformType.FORWARD);
		Pxx= new double[A.length];
		Fxx= new double[A.length];

		for(int i=0;i<Pxx.length;i++){
			Pxx[i]=A[i].abs();
			Fxx[i]=fres*(double)i;
			if(fres*(double)i >= FtrackLB && fres*(double)i <= FtrackUB){
				if(Pxx[i]>Pmax){
					Pmax=Pxx[i];
					Ftrack=fres*(double)i;
				}	
			}
		}

		//Find the Frequency index based on new window size
		for(int i=0;i<winSize;i++){
			if(Math.abs(Ftrack-i*Fs/winSize) < Fsearch){
				Fsearch=Math.abs(Ftrack-i*Fs/winSize);
				Findex=i;
			}
		}
	}

	public double[][] getDFA(String tmpFile){
		ProcessBuilder launcher = new ProcessBuilder();
		launcher.redirectErrorStream(true);
		List<Double>results = new ArrayList<Double>();
		double[][] out=null;
		List<String> inputs = new ArrayList<String>();
		inputs.add(0,"/home/ikaro/cdfa.sh");
		inputs.add(1,tmpFile);
		launcher.redirectErrorStream(true);
		launcher.command(inputs);
		int n=0;

		try {
			Process p = launcher.start();
			BufferedReader output = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			String[] tmpStr;

			while ((line = output.readLine()) != null){
				tmpStr=line.split("\\s+");
				results.add(n*2,Double.valueOf(tmpStr[0]));
				results.add(n*2+1,Double.valueOf(tmpStr[1]));
				n++;
			}
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
		out=new double[2][n-1];
		for(int i=0;i<(n-1);i++){
			out[0][i]=results.get(2*i);
			out[1][i]=results.get(2*i+1);
		}
		return out;
	}

	public void trackSpectrum(){
		//Now that the frequency has been selected. Track its power spectrum value as a function of time
		//over the selected window size.
		int M=(int) Math.floor(N/winSize);
		double[] tmpTimeSeries= new double[winSize];
		timeSeries=new double[M];
		timeSeriesLag=new double[timeSeries.length];
		timeSeriesLag[0]=(double) 0;

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
		//Subtract DC component
		for(int i=0;i<timeSeries.length;i++){
			timeSeries[i]-=dc;
			if(i>0){
				timeSeriesLag[i]=timeSeries[i-1];
			}
		}
	}

	private void writeData(String outFile, double[] data) {

		File log_file = new File(outFile);
		try {
			log_file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Could not create cached data file: " + e1.getLocalizedMessage());
		}

		try {
			out_log= new BufferedWriter(new FileWriter(log_file));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not generate log file:" + log_file);
		}

		for(int i=0;i<data.length;i++){
			try {
				out_log.write(Double.toString(data[i])+"\n");
				//
				out_log.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static File[] finder( String dirName){
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() { 
			public boolean accept(File dir, String filename)
			{ return filename.endsWith(".raw"); }
		} );

	}


	public static void main(String[] args) throws IOException, ClassNotFoundException {

		//Get directory listing 
		File[] oaeFiles=finder(data_dir);
		String tmpFile="/tmp/TMPsoae.txt"; 
		double[][] slope=new double[2][oaeFiles.length];
		
		for(int i=0;i<oaeFiles.length;i++){

			System.out.println("Analzing: " + oaeFiles[i].getName());
			SoaeFractalAnalysis analysis= new SoaeFractalAnalysis();
			analysis.getData(oaeFiles[i].getAbsolutePath());
			final CurveFitter fitter = new CurveFitter(new GaussNewtonOptimizer());
			analysis.getSpectrum();
			analysis.trackSpectrum();

			//Shuffler shuffler= new Shuffler(analysis.timeSeries);
			//double [] noise = shuffler.fftShuffle();
			analysis.writeData(tmpFile,analysis.timeSeries);

		//Plot results
			
		Plot demo = new Plot("SOAE Time Series: "+ oaeFiles[i].getName(),analysis.timeSeries);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
			 
		Plot demo0 = new Plot("Waveform: "+ oaeFiles[i].getName(),analysis.data);
		demo0.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo0.setVisible(true);

		/*
		//Generate Scatter of phase 
		ScatterPlot demo2 = new ScatterPlot("Scatter Plot: " + oaeFiles[i].getName(),analysis.timeSeries,analysis.timeSeriesLag);
		demo2.pack();
		RefineryUtilities.centerFrameOnScreen(demo2);
		demo2.setVisible(true);
			*/ 

			//Calculate DFA values
			double[][] results= analysis.getDFA(tmpFile);
			for(int k=0;k<results[0].length;k++){
				fitter.addObservedPoint(results[0][k],results[1][k]);
			}
			final double[] init = { 1, 1}; // a - bx

			// Compute optimal coefficients.
			final double[] best = fitter.fit(new PolynomialFunction.Parametric(), init);
			// Construct the polynomial that best fits the data.
			final PolynomialFunction fitted = new PolynomialFunction(best);
			double[] xhat=new double[results[0].length];
		
			//Store slope values
			slope[0][i]=fitted.getCoefficients()[0];
			slope[1][i]=fitted.getCoefficients()[1];
			for(int k=0;k<results[0].length;k++){
				xhat[k]=fitted.value(results[0][k]);
			}
			//Calculate DFA values
			ScatterPlot demo3 = new ScatterPlot(oaeFiles[i].getName() +":" + fitted.toString(),results[0],results[1],xhat);
			demo3.pack();
			RefineryUtilities.centerFrameOnScreen(demo3);
			demo3.setVisible(true);
	
		}
		//Calculate DFA values
		ScatterPlot demo4 = new ScatterPlot("Coeff",slope[0],slope[1]);
		demo4.pack();
		RefineryUtilities.centerFrameOnScreen(demo4);
		demo4.setVisible(true);


	}


}


















