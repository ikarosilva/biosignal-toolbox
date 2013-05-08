package com.ikarosilva.graphics;
/* -------------------
 * Based on: HistogramDemo1.java by Object Refinery Limited.
 *
 */
import java.io.IOException;
import java.util.Random;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo of the {@link HistogramDataset} class.
 */
public class Plot1DPDF extends ApplicationFrame {

    public Plot1DPDF(String title,double[] x1, double[] x2, int bins) {
        super(title);
        
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.SCALE_AREA_TO_1);
        dataset.addSeries("x1",x1, bins);
        dataset.addSeries("x2",x2, bins);
        
        JPanel chartPanel = createDemoPanel(dataset);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public Plot1DPDF(String title,double[] x1, int bins) {
        super(title);
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.SCALE_AREA_TO_1);
        dataset.addSeries("x1",x1, bins);
        JPanel chartPanel = createDemoPanel(dataset);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    private static JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createHistogram(
            "Histogram Demo 1",
            null,
            null,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setForegroundAlpha(0.85f);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        // flat bars look best...
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setShadowVisible(false);
        return chart;
    }

    public static JPanel createDemoPanel( HistogramDataset dataset){
        JFreeChart chart = createChart(dataset);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    public static void main(String[] args) throws IOException {

    	 int N=10024;
    	 int bins=100;
    	 double[] x1= new double[N];
    	 double[] x2= new double[N];
    	 Random generator = new Random(12345678L);//Random(System.currentTimeMillis());
         for (int i = 0; i < N; i++) {
             x1[i] = generator.nextGaussian() + 5;
             x2[i] = x1[i]/5;
         }
        
        Plot1DPDF demo = new Plot1DPDF("Estimated PDF",x1,bins);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
