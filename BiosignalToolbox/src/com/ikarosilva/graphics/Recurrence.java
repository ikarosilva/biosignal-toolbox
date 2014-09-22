
package com.ikarosilva.graphics;

import java.awt.RenderingHints;
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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo of the fast scatter plot.
 *
 */
public class Recurrence extends ApplicationFrame {


    private static Options options = new Options();

    public Recurrence(float[][] data) {

        super("");
        final NumberAxis domainAxis = new NumberAxis("X");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Y");
        rangeAxis.setAutoRangeIncludesZero(false);
        final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
        final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);


        chart.getRenderingHints().put
            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final ChartPanel panel = new ChartPanel(chart, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        panel.setMinimumDrawHeight(10);
        panel.setMaximumDrawHeight(2000);
        panel.setMinimumDrawWidth(20);
        panel.setMaximumDrawWidth(2000);
        
        setContentPane(panel);

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
	
    public static void main(final String[] args) {
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
    		
    		ArrayList<Float> YList = new ArrayList<Float>();
    		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
    		String line = null;
			try {
				line = is.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		while( line != null){
    			YList.add((float) Float.parseFloat(line));
    			try {
					line=is.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		float[][] data=new float[2][YList.size()];
    		for(int n=0;n<YList.size();n++){
    			data[0][n]=n;
    			data[1][n]=YList.get(n);
    		}
    		
        final Recurrence demo = new Recurrence(data);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}