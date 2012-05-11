package com.ikarosilva.graphics;

import java.awt.Color;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

@SuppressWarnings("serial")
public class File2ScatterPlot extends ApplicationFrame {

	public File2ScatterPlot(String title, File datafile, int x, int y) {

		super(title);
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Series 1");
		Double number = null;
		try {
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			BufferedReader input =  new BufferedReader(new FileReader(datafile));
			try {
				String line = null; //not declared within while loop
				int row = 0;
				while (( line = input.readLine()) != null){
					StringTokenizer st = new StringTokenizer(line,",");
					int col = 0;
					double[] xy = new double[2];
					while (st.hasMoreTokens())
					{
						try{
						number = Double.valueOf(st.nextToken());
						} catch (NumberFormatException e){
							col++;
							continue;
						}				
						if(col==x)
						{
							xy[0]=number;
						}else if (col == y) {
							xy[1]=number;
						}
						col++;
					}
					series1.add(xy[0],xy[1]);
					row++;
				}
			}
			finally {
				input.close();
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		dataset.addSeries(series1);
		
		JFreeChart chart = ChartFactory.createScatterPlot(
				"Scatter Plot", // title
				"X", "Y", // axis labels
				dataset, // dataset
				PlotOrientation.VERTICAL,
				true, // legend? yes
				true, // tooltips? yes
				false // URLs? no
		);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		
		float thickness=(float) 0.25;
		float length=3;
		Shape cross = ShapeUtilities.createDiagonalCross(length, thickness);
		XYItemRenderer renderer = chart.getXYPlot().getRenderer();
		renderer.setSeriesPaint(0, Color.blue);
		renderer.setSeriesShape(0, cross);
		
	}

	public static void main(String[] args) {
		if (args.length !=3) {
			System.err.println("Usage: java File2ScatterPlot filename ind1 ind2");
			System.exit(0);
		}

		File datafile= new File(args[0]);
		int x= Integer.valueOf(args[1]);
		int y= Integer.valueOf(args[2]);
		File2ScatterPlot demo = new File2ScatterPlot(datafile.toString(), datafile, x, y);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}