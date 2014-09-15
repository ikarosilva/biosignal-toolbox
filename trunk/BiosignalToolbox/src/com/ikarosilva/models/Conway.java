package com.ikarosilva.models;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Conway {

	private final static int Ndefault=10;
	private static Options options = new Options();
	private int N;

	public double[] sim(){
		double[] output= new double[N];
		int[] a = new int[N];
		for(int n=0;n<N;n++){
			if(n<3){
				a[n]=1;
				output[n]=(double) 1;
			}else{
				a[n]=a[a[n-1]]+ a[n-a[n-1]];
				output[n]=(double) a[n] - ((double)n)/2;
			}
		}
		return output;
	}

	public void setN(int N){
		this.N=N;
	}

	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Conway",options);
		System.exit(0);
	}

	private static void getOptions(){
		options = new Options();
		options.addOption("h",false, "Display help.");
		options.addOption("n",true, "Number of samples to generate");
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		int N=Ndefault;
		getOptions();
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("n")) {
				N=Integer.valueOf(cmd.getOptionValue("n"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}	
		Conway conway=new Conway();
		conway.N=N;
		double[] data=conway.sim();
		for(int n=0;n<N;n++){
			System.out.println(data[n]);
		}
	}

}
