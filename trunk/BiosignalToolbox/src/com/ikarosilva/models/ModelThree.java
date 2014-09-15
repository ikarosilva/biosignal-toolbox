package com.ikarosilva.models;

import java.util.Random;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ModelThree {

	final double a;
	final double b;
	final double sigma;
	private double x=0; 
	private double y=0;
	private double v;
	private final static double defaulta=0.5;
	private final static double defaultb=3;
	private final static double defaultSigma=1;
	private final static double defaultv=3;
	private final static int Ndefault=10;
	private Random measurementNoise=null;
	private Random stateNoise=null;
	private static Options options = new Options();
	private static final double dt=0.1;
	private int N;

	public ModelThree(double a, double b, double sigma,double v){
		this.a=a;
		this.b=b;
		this.sigma=sigma;
		this.v=v;
	}

	public ModelThree(double a, double b){
		this.a=a;
		this.b=b;
		sigma=defaultSigma; //Default value
		v=defaultv;
	}

	public ModelThree(){
		a=defaulta;
		b=defaultb;
		sigma=defaultSigma;
		v=defaultv;
	}


	public void initializeState(double x, double y){
		this.x=x;
		this.y=y;
	}
	
	public double[] sim(){
		if(measurementNoise == null){
			measurementNoise= new java.util.Random();
			measurementNoise.setSeed(System.currentTimeMillis() + (long) (Math.random()*100));
		}
		if(stateNoise == null){
			stateNoise= new java.util.Random();
			stateNoise.setSeed(System.currentTimeMillis());
		}
		double[] data=new double[N];
		double dx, dy;
		for(int i=0;i<N;i++){
			dx=y;
			if(v !=0){
				dx+=stateNoise.nextGaussian()*v;
			}
			dy=-a*y-b*x;
			x= x + dx*dt;
			y= y + dy*dt;
			data[i]=x;
			if(sigma != 0){
				data[i]+=measurementNoise.nextGaussian()*sigma;
			}
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
		options.addOption("a",true, "Parameter a of model (default=  " + defaulta + " )");
		options.addOption("b",true, "Parameter p of model (default=  " + defaultb + " )");
		options.addOption("s",true,"Measurement Noise variance (default=  " + defaultSigma + " )");
		options.addOption("v",true,"State Noise variance (default=  " + defaultv + " )");
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		int N=Ndefault;
		double b=defaultb;
		double a=defaulta;
		double sigma=defaultSigma;
		double v=defaultv;
		
		getOptions();
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("n")) {
				N=Integer.valueOf(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("a")) {
				a=Double.valueOf(cmd.getOptionValue("a"));
			}
			if (cmd.hasOption("b")) {
				b=Double.valueOf(cmd.getOptionValue("b"));
			}
			if (cmd.hasOption("sigma")) {
				sigma=Double.valueOf(cmd.getOptionValue("sigma"));
			}
			if (cmd.hasOption("v")) {
				v=Double.valueOf(cmd.getOptionValue("v"));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}	
		ModelThree modelThree=new ModelThree(a,b,sigma,v);
		modelThree.N=N;
		double[] data=modelThree.sim();
		for(int n=0;n<N;n++){
			System.out.println(data[n]);
		}

	}

}
