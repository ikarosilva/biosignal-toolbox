
    package com.ikarosilva.graphics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JPanel;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
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

import com.ikarosilva.analysis.nonlinear.NonlinearProcess;

    public class SpectralPlot extends ApplicationFrame {

        public SpectralPlot(String title,double[] data, double Fs) {
            super(title);
            JPanel chartPanel = createDemoPanel(data,Fs);
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            setContentPane(chartPanel);
        }
       
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
       
        public static XYDataset createDataset(double[] data, double Fs) {
        	XYSeriesCollection result = new XYSeriesCollection();
        	XYSeries series = new XYSeries(1);
        	int N = data.length;	
    		//Calculate spectrum 
    		FastFourierTransformer FFT = new FastFourierTransformer(DftNormalization.STANDARD);
    		Complex[] A= FFT.transform(data, TransformType.FORWARD);
    		double fres= (double) Fs/N;
    		//System.out.println("res is " + fres);
    		for(int n=0;n<(N/2);n++){
    			series.add(((double) 0) + n*fres, A[n].abs());
    		}
    		
        	result.addSeries(series);
        	//result.addSeries(series2);
            return result;
        }
        public static JPanel createDemoPanel(double[] data, double Fs) {
            JFreeChart chart = createChart(createDataset(data,Fs));
            return new ChartPanel(chart);
        }      
        
        public static double[] readBinary2(String aInputFileName) throws IOException{
    		
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

    		short[] tmpdata=null;
    		try {
    			try {
    				tmpdata = (short[]) in.readObject();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    			System.out.print("Could not open data file.");
    		}	
    		in.close();
    		int N=(int) Math.pow(2,Math.floor(Math.log((int) tmpdata.length)/Math.log(2)));
    		double[] data=new double[N];
    		for(int i=0;i<N;i++){
    			data[i]=(double) tmpdata[i];
    		}
    		return data;
    		
    	}
        
        public static void main(String[] args) {
        	double[] data= NonlinearProcess.Conway(1024);
        	double Fs=1;
            SpectralPlot demo = new SpectralPlot(
                    "test",data,Fs);
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        }

    }
