
package com.ikarosilva.graphics;

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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;


public class Plot {

	private static Options options = new Options();
	private DefaultXYDataset dataset;
	
	public Plot(double[][] data){
		dataset = new DefaultXYDataset();
		dataset.addSeries("data",data);

		// create a chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Plot",
				"X",
				"Y",
				dataset
				);
		// create and display a frame...
		ChartFrame frame = new ChartFrame("PlotChart", chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("PlotTimeSeries",options);
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
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h"))
				help();
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}
		
		ArrayList<Double> YList = new ArrayList<Double>();
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		String line=is.readLine();
		while( line != null){
			YList.add((double) Double.parseDouble(line));
			line=is.readLine();
		}
		double[][] data=new double[2][YList.size()];
		for(int n=0;n<YList.size();n++){
			data[0][n]=n;
			data[1][n]=YList.get(n);
		}
		Plot myPlot=new Plot(data);
	}


}
