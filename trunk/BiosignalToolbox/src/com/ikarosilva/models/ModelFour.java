package com.ikarosilva.models;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ModelFour {

	final double u;
	private double x=0.2;
	private final static double defaultu=4.0;
	private final static int Ndefault=10;
	private static Options options = new Options();
	private int N;

	public ModelFour(double u){
		this.u=u;
	}

	public ModelFour(){
		u=defaultu;
	}

	public void initializeState(double x){
		this.x=x;
	}
	
	public double[] sim(){
		double[] data=new double[N];
		for(int i=0;i<N;i++){
			x=u*x*(1-x);
			data[i]=x;
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
		options.addOption("u",true, "Parameter u of model (default=  " + defaultu + " )");
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		int N=Ndefault;
		double u=defaultu;
		getOptions();
		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("n")) {
				N=Integer.valueOf(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("u")) {
				u=Double.valueOf(cmd.getOptionValue("u"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}	
		ModelFour modelFour=new ModelFour(u);
		modelFour.N=N;
		double[] data=modelFour.sim();
		for(int n=0;n<N;n++){
			System.out.println(data[n]);
		}

	}

}
