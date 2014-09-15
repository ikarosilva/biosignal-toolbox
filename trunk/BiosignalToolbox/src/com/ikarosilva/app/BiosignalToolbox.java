package com.ikarosilva.app;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ikarosilva.graphics.Plot;
import com.ikarosilva.models.Conway;
import com.ikarosilva.models.ModelFour;
import com.ikarosilva.models.ModelOne;
import com.ikarosilva.models.ModelThree;

public class BiosignalToolbox {

	private static Options options = new Options();

	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Biosignal Toolbox",options);
		System.exit(0);
	}

	private static void getOptions(){
		options = new Options();
		options.addOption("h",false, "Display help.");
		options.addOption("ModelOne",false, "Simulates ModelOne");
		options.addOption("ModelThree",false, "Simulates ModelThree");
		options.addOption("ModelFour",false, "Simulates ModelFour (logistic model)");
		options.addOption("Conway",false, "Generates the Conway fractal Series");
		options.addOption("Plot",false, "Plots streaming data");
	}

	public static void main(String[] args) throws IOException {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		getOptions();
		String[] toolboxArgs={args[0]};
		try {
			cmd = parser.parse(options, toolboxArgs);
			if (cmd.hasOption("ModelOne")) {
				ModelOne.main(Arrays.copyOfRange(args,1,args.length));
			}else if (cmd.hasOption("ModelThree")) {
				ModelThree.main(Arrays.copyOfRange(args,1,args.length));
			}else if (cmd.hasOption("ModelFour")) {
				ModelFour.main(Arrays.copyOfRange(args,1,args.length));
			}else if (cmd.hasOption("Conway")) {
				Conway.main(Arrays.copyOfRange(args,1,args.length));
			}else if (cmd.hasOption("Plot")) {
				Plot.main(Arrays.copyOfRange(args,1,args.length));
			}else{
				help();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}	
	}

}
