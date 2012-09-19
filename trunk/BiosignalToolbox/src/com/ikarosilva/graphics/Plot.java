
    package com.ikarosilva.graphics;

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

    public class Plot extends ApplicationFrame {

    	static String title;
        public Plot(String title, Double[] data) {
            super(title);
            this.title=title;
            JPanel chartPanel = createDemoPanel(data);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);
        }
       
      
        private static JFreeChart createChart(XYDataset dataset) {
            // create the chart...
            JFreeChart chart = ChartFactory.createXYLineChart(
                title,       // chart title
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
       
        private static XYDataset createDataset(Double[] data) {
        	XYSeriesCollection result = new XYSeriesCollection();
        	XYSeries series = new XYSeries(1);
    		for(int n=0;n<data.length-1;n++){
    			//System.out.println("n=" +n + " data[n]=" + data[n]);
    			if(data[n] !=0)
    			series.add(n,Math.log(data[n]));
    			//series.add(n,data[n]);
    		}
        	result.addSeries(series);
            return result;
        }

        /**
         * Creates a panel for the demo (used by SuperDemo.java).
         *
         * @return A panel.
         */
        private static JPanel createDemoPanel(Double[] data) {
            JFreeChart chart = createChart(createDataset(data));
            return new ChartPanel(chart);
        }
       
              
        /**
         * Starting point for the demonstration application.
         *
         * @param args  ignored.
         */
        public static void main(String[] args) {
        	
        	int N =5000;
    		Double[] a = new Double[N];
    		for(int n=0;n<N;n++){
    			if(n<3){
    				a[n]=(double) 1;
    			}else{
    				a[n]=a[a[n-1].intValue()]+ a[(n-a[n-1].intValue())];
    			}
    		}
        	
            Plot demo = new Plot(
                    "Simple",a);
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        }

    }
