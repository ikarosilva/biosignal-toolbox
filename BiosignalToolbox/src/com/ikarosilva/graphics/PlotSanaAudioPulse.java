
package com.ikarosilva.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotSanaAudioPulse extends ApplicationFrame {

	public PlotSanaAudioPulse(String title) {
		super(title);
		JPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}


	private static JFreeChart createChart(XYDataset dataset) {
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Audio Data ",       // chart title
				"X",                      // x axis label
				"Y",                      // y axis label
				dataset,                  // data
				PlotOrientation.VERTICAL, 
				true,                     // include legend
				true,                     // tooltips
				false                     // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);
		return chart;
	}

	public static XYDataset createDataset() {
		XYSeriesCollection result = new XYSeriesCollection();
		XYSeries series = new XYSeries(1);
		double Ts = 1;
		short[] data=null;
		try {
			data = readBinary2("/home/ikaro/oae_data/AP_1353247872.raw");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Reading data");
		for(int n=0;n<data.length;n++){
			series.add(n*Ts, data[n]);
			//System.out.println("Data is = " + data[n]);
		}
		result.addSeries(series);
		return result;
	}

	public static short[] readBinary2(String aInputFileName) throws IOException{
		
		//Check that data is ok
		FileInputStream nFile = null;
		
		try {
			nFile = new FileInputStream(aInputFileName);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}	
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(nFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		short[] data=null;
		try {
			try {
				data = (short[]) in.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.print("Could not open data file.");
		}	
		in.close();
		return data;
		
	}
	
	
	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart(createDataset());
		return new ChartPanel(chart);
	}


	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		PlotSanaAudioPulse demo = new PlotSanaAudioPulse(
				"Audio Data");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
