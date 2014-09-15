package com.ikarosilva.models;

import java.util.Random;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ModelOne {

	final double A;
	final double p;
	final double sigma;
	private double x=0; 
	private final static double defaultA=4;
	private final static double defaultp=0.95;
	private final static double defaultSigma=2;
	private final static int Ndefault=10;
	private Random noise=null;
	private static Options options = new Options();
	private int N;

	public ModelOne(double A, double p, double sigma){
		this.A=A;
		this.p=p;
		this.sigma=sigma;
	}

	public ModelOne(double A, double p){
		this.A=A;
		this.p=p;
		sigma=defaultSigma; //Default value
	}

	public ModelOne(){
		A=defaultp;
		p=defaultA;
		sigma=defaultSigma;
	}

	public double getSteadyState(){
		return ( A / (1-p) );
	}

	public void initializeState(double x){
		this.x=x;
	}
	public double[] sim(){
		if(noise == null){
			noise= new java.util.Random();
		}
		double[] data=new double[N];
		for(int i=0;i<N;i++){
			x=A + p*x;
			data[i]=x + noise.nextGaussian()*sigma;
		}
		return data;
	}

	public void setN(int N){
		this.N=N;
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
		options.addOption("n",true, "Number of samples to generate");
		options.addOption("A",true, "Parameter A of model (default=  " + defaultA + " )");
		options.addOption("p",true, "Parameter A of model (default=  " + defaultp + " )");
		options.addOption("s",true,"Noise variance (default=  " + defaultSigma + " )");
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		int N=Ndefault;
		double p=defaultp;
		double A=defaultA;
		double sigma=defaultSigma;
		
		getOptions();
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("n")) {
				N=Integer.valueOf(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("A")) {
				N=Integer.valueOf(cmd.getOptionValue("A"));
			}
			if (cmd.hasOption("p")) {
				N=Integer.valueOf(cmd.getOptionValue("p"));
			}
			if (cmd.hasOption("sigma")) {
				N=Integer.valueOf(cmd.getOptionValue("sigma"));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}	
		ModelOne modelOne=new ModelOne(A,p,sigma);
		modelOne.N=N;
		double[] data=modelOne.sim();
		for(int n=0;n<N;n++){
			System.out.println(data[n]);
		}

	}

}
