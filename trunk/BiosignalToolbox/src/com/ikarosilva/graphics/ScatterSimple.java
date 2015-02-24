package com.ikarosilva.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.ikarosilva.models.Conway;
import com.ikarosilva.models.ModelFour;
import com.ikarosilva.models.ModelOne;
import com.ikarosilva.models.ModelThree;
import com.ikarosilva.statistics.MindReader;

public class ScatterSimple extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static double[] x, y;
	private static Options options = new Options();

	public ScatterSimple(String s, double[] X, double[] Y) {
		super(s);
		final ChartPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new Dimension(640, 480));
		this.add(chartPanel);
		x=X;
		y=Y;
	}

	public ChartPanel createDemoPanel() {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(
				"Scatter Plot Demo", "X", "Y", samplexydataset(),
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setVerticalTickLabels(true);
		return new ChartPanel(jfreechart);
	}

	private XYDataset samplexydataset() {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries series = new XYSeries("Data");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y[i]);
		}
		xySeriesCollection.addSeries(series);
		return xySeriesCollection;
	}

	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("Scatter",options);
		System.exit(0);
	}

	private static void getOptions(){
		options = new Options();
		options.addOption("h",false, "Display help.");
		options.addOption("r",true, "Sample delay for recurrence plot");
	}

	public static void main(String args[]) {

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		getOptions();
		int delay=0;
		try {
			cmd = parser.parse(options, args);
			
			if (cmd.hasOption("r")) {
				delay=Integer.valueOf(cmd.getOptionValue("r"));
			}else{
				help();
			}
		} catch (org.apache.commons.cli.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		ArrayList<Float> XList = new ArrayList<Float>();
		ArrayList<Float> YList = new ArrayList<Float>();
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try {
			line = is.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] tmp;
		LinkedList<Float> oldValue=new LinkedList<Float>();
		Float currentValue=Float.NaN;
		int counter =0;
		while( line != null){
			tmp=line.split("\\s+");
			if(delay ==0 ){
				//Standard Scatter plot
			XList.add((float) Float.parseFloat(tmp[0]));
			YList.add((float) Float.parseFloat(tmp[1]));
			}else{
				//Generate Scatter plot based on a single delayed time series
				if(counter<delay){
					oldValue.addLast(Float.parseFloat(tmp[0]));
				}else{
					currentValue=Float.parseFloat(tmp[0]);
					YList.add((float) currentValue);
					XList.add((float) oldValue.pop());
					oldValue.addLast(currentValue);
				}
				counter++;
			}
			try {
				line=is.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		x=new double[XList.size()];
		y=new double[YList.size()];
		for(int n=0;n<YList.size();n++){
			x[n]=XList.get(n);
			y[n]=YList.get(n);
		}

		ScatterSimple demo = new ScatterSimple("Scatter Plot Demo",x,y);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}