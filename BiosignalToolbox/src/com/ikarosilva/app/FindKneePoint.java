package com.ikarosilva.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FindKneePoint {

	
	public static void main(String[] args) {
		
		//Store STDIN into data buffer
		ArrayList<Double> x= new ArrayList<Double>();
		ArrayList<Double> y= new ArrayList<Double>();
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		String[] parseLine;
		try {
			while( (inputLine =is.readLine()) !=null){
				parseLine=inputLine.split(" ");
				x.add(Double.valueOf(parseLine[0]));
				y.add(Double.valueOf(parseLine[0]));
			}
		} catch (IOException e) {
			System.err.println("Could not read data from input!!");
			e.printStackTrace();
		}
		

	}

}
