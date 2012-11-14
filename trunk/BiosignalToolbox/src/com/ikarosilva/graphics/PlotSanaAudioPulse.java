
    package com.ikarosilva.graphics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        	byte[] data = readBinary("/home/ikaro/AudioPulseData.raw");
        	System.out.println("Reading data");
        	for(int n=0;n<data.length;n++){
    			series.add(n*Ts, data[n]);
    			//System.out.println("Data is = " + data[n]);
    		}
        	result.addSeries(series);
            return result;
        }

        public static byte[] readBinary(String aInputFileName){
            
            File file = new File(aInputFileName);
            byte[] result = new byte[(int)file.length()];
            try {
              InputStream input = null;
              try {
                int totalBytesRead = 0;
                //TODO: modify to read short data
               
                input = new BufferedInputStream(new FileInputStream(file));
                while(totalBytesRead < result.length){
                  int bytesRemaining = result.length - totalBytesRead;
                  //input.read() returns -1, 0, or more :
                  int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
                  if (bytesRead > 0){
                    totalBytesRead = totalBytesRead + bytesRead;
                  }
                }
                /*
                 the above style is a bit tricky: it places bytes into the 'result' array; 
                 'result' is an output parameter;
                 the while loop usually has a single iteration only.
                */
              }
              finally {
                input.close();
              }
            }
            catch (FileNotFoundException ex) {
              System.err.println("File not found: ");
              ex.printStackTrace();
            }
            catch (IOException ex) {
              ex.printStackTrace();
            }
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
            PlotSanaAudioPulse demo = new PlotSanaAudioPulse(
                    "Audio Data");
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        }

    }
