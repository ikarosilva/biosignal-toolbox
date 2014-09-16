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
	private int dim;
	private int stepSize;
	private double[] data;
	private ArrayList<Double> err;
	static int defaultStep=1, errN;
	static int defaultLag=2;
	static double[] defaultTh={0.02, 0.01, 0.2, 0.3, 0.4,0.5};
	static int defaultDim=2;

	public CorrelationIntegral(int lag,int d,int step,double[] data){
		timeLag=lag;
		dim=d;
		stepSize=step;
		this.data=data;
		int vectorSpan=dim*stepSize;
		int lastPoint=data.length-vectorSpan;
		errN=(lastPoint-lag)/step;
		err=new ArrayList<Double>();
		err.ensureCapacity(errN);
	}

	public void getDistance(){
		int N=data.length;
		int windowN=dim*stepSize;
		for(int i=N-1;i>=0;i--){
			for(int k=i-windowN-timeLag;k>=0;k--){
				double tmpErr=0;
				for(int z=0;z<windowN;z+=stepSize){
					System.out.println("[" + i + "," +k+ "," + z +" ]= " + data[i-z]+ " - " + data[k-z]);
					tmpErr+=Math.abs(data[i-z]-data[k-z]);
				}
				err.add(tmpErr);
			}
		}
	}

	public int countRemoveNeighbors(double th){
		int count=0;
		for(int i=0;i<err.size();i++){
			if(err.get(i)<th){
				count++;
				err.remove(i);
			}
		}
		return count;
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

		int N=5;
		int lag=0, d=2, step=1;
		double[] data=new double[N];
		for(int i=0;i<N;i++){
			System.out.print(i + " , ");
			data[i]=i;
		}
		System.out.println(" ");
		CorrelationIntegral corrInt=new CorrelationIntegral(lag,d,step,data);
		corrInt.getDistance();
		
		/*
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
		CorrelationIntegral corrInt=new CorrelationIntegral(lag,d,step,data);
		*/
	}

}
