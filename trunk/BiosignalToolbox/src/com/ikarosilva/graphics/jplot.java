package com.ikarosilva.graphics;

import ptolemy.plot.Plot;
import ptolemy.plot.PlotFrame;

public class jplot {
	static public void plot(double[] output){
		Plot plot = new Plot();             // Make a default plot
		for(int i=0;i<output.length;i++){
			plot.addPoint(0,i,output[i],true);  // Cos to data 0
		}
		//      Make a frame to display the plot in
		PlotFrame frame = new PlotFrame("Title",plot);
		frame.setSize(600,400);              // Set size of frame (in pixels)
		frame.setVisible(true);              // Make frame visible
	}
}
