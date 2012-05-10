
    package com.ikarosilva.graphics;

import java.util.Random;
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

    public class PlottingDemo1 extends ApplicationFrame {

        public PlottingDemo1(String title) {
            super(title);
            JPanel chartPanel = createDemoPanel();
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);
        }
       
        /**
         * Creates a chart.
         *
         * @param dataset  the dataset.
         *
         * @return A chart instance.
         */
        private static JFreeChart createChart(XYDataset dataset) {
            // create the chart...
            JFreeChart chart = ChartFactory.createXYLineChart(
                "Conway ",       // chart title
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
            //XYDataset result = DatasetUtilities.sampleFunction2D(new X2(),
            //        -4.0, 4.0, 40, "f(x)");
        	XYSeriesCollection result = new XYSeriesCollection();
        	XYSeries series = new XYSeries(1);
        	XYSeries series2 = new XYSeries(2);
        	Random generator = new Random();
        	double output=0;
        	int N =5000;
    		int[] a = new int[N];
    		for(int n=0;n<N;n++){
    			if(n<3){
    				a[n]=1;
    				output=1;
    			}else{
    				a[n]=a[a[n-1]]+ a[n-a[n-1]];
    				output=(double) a[n] - ((double)n)/2;
    			}
    			series.add(n, output);
    			series2.add(n, generator.nextGaussian()-output);
    		}
        	
        	result.addSeries(series);
        	result.addSeries(series2);
            return result;
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
            PlottingDemo1 demo = new PlottingDemo1(
                    "Test");
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        }

    }
