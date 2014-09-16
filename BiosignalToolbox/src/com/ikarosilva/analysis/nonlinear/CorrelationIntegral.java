package com.ikarosilva.analysis.nonlinear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



public class CorrelationIntegral {

	private static Options options = new Options();
	private int timeLag;
	private double[] threshold;
	private int dim;
	private int stepSize;
	private double[] data;
	private double[] err;
	static int defaultStep=1, errN;
	static int defaultLag=2;
	static double[] defaultTh={0.02, 0.01, 0.2, 0.3, 0.4,0.5};
	static int defaultDim=2;
    
	public CorrelationIntegral(int lag,double[] th,int d,int step,double[] data){
		timeLag=lag;
		threshold=th;
		dim=d;
		stepSize=step;
		this.data=data;
		int vectorSpan=dim*stepSize;
	    int lastPoint=data.length-vectorSpan;
	    errN=(lastPoint-lag)/step;
	    err=new double[errN];
	}

	public void getError(){
		int M=threshold.length;
		int N=data.length;
		int errInd=0;
		double[] y = new double[M];
			for(int i=0;i<N;i++){
				for(int k=i+timeLag;k<N;k++){
					double tmpErr=0;
					for(int z=0;z<(dim*stepSize);z+=stepSize){
						tmpErr+=Math.abs(data[i+z]-data[k+z]);
					}
					err[errInd]=tmpErr;
					errInd++;
				}
			}
	}


	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("ModelOne", options);
		System.exit(0);
	}

	private static void getOptions(){
		options = new Options();
		options.addOption("h",false, "Display help.");
	}

	public static void main(String[] args) throws IOException {

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		getOptions();
		int lag = defaultLag,step=defaultStep, d=defaultDim;
		double[] th=defaultTh;
		Double b=null,n=null,s=null;
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h")){
				help();
			}else if (cmd.hasOption("l")){
				lag=Integer.valueOf(cmd.getOptionValue("l"));
			}else if (cmd.hasOption("s")){
				step=Integer.valueOf(cmd.getOptionValue("s"));
			}else if (cmd.hasOption("b")){
				b=Double.valueOf(cmd.getOptionValue("b"));
			}else if (cmd.hasOption("n")){
				b=Double.valueOf(cmd.getOptionValue("n"));
			}else if (cmd.hasOption("s")){
				b=Double.valueOf(cmd.getOptionValue("s"));
			}else if (cmd.hasOption("d")){
				d=Integer.valueOf(cmd.getOptionValue("d"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}

		if(b!=null){
			int N=(int) ((b-n)/s);
			th=new double[N];
			for(int i=0;i<N;i++){
				th[i]=b+(i*s);
			}	
		}


		ArrayList<Double> YList = new ArrayList<Double>();
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		String line=is.readLine();
		while( line != null){
			YList.add((double) Double.parseDouble(line));
			line=is.readLine();
		}
		double[] data=new double[YList.size()];
		for(int i=0;i<YList.size();i++){
			data[i]=YList.get(i);
		}
		CorrelationIntegral corrInt=new CorrelationIntegral(lag,th,d,step,data);
	}

}
