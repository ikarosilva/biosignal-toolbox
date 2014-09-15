package com.ikarosilva.app;
import java.util.Arrays;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ikarosilva.models.ModelOne;
public class Main {

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
	}

	public static void main(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		getOptions();
		String[] toolboxArgs={args[0]};
		System.err.println(toolboxArgs[0]);
		try {
			cmd = parser.parse(options, toolboxArgs);
			if (cmd.hasOption("h"))
				help();
			if (cmd.hasOption("ModelOne")) {
				ModelOne.main(args);
			}
		} catch (ParseException e) {
			System.err.println("Error:");
			e.printStackTrace();
			help();
		}	
	}

}
