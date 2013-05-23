package com.ikarosilva.graphics;

import java.util.Arrays;

public class RecurrencePlot {

	public static void plotDim(double[] data, int dim){
		ScatterPlot sct = new
                ScatterPlot("",Arrays.copyOfRange(data,0,data.length-1-dim),
              		  Arrays.copyOfRange(data,dim,data.length-1));
	}
}
