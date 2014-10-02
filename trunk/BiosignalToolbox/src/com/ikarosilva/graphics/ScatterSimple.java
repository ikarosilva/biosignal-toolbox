package com.ikarosilva.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

public class ScatterSimple extends ApplicationFrame {

	private double[] x, y;
	
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

    public static void main(String args[]) {
    	
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
		double[] x=new double[YList.size()];
		double[] y=new double[YList.size()];
		for(int n=0;n<YList.size();n++){
			x[n]=n;
			y[n]=YList.get(n);
		}
		
        ScatterSimple demo = new ScatterSimple("Scatter Plot Demo",x,y);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}