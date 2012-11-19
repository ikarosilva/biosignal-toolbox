package com.ikarosilva.fileio;

//Converts all txt files in a directory in short binary files 
// so that the compressed data can be run from the Android APP
// of the PhysioNet 2011 Challenge

import java.awt.*;
import java.io.*;



public class ShortFile {

	public static void writeFile(File outFile, short[] samples) throws IOException{
		//Write Short file to disk
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		fos = new FileOutputStream(outFile);
		out = new ObjectOutputStream(fos);
		out.writeObject(samples);
		out.flush();
		out.close();
	}

	public static short[] readFile(String file) throws IOException, ClassNotFoundException{
		//Read Short file from disk	
		FileInputStream nFile = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(nFile);
		short[] data=null;
		data = (short[]) in.readObject();
		in.close();
		return data;
	}
	
	public static double[] readFile2Double(String file) throws IOException, ClassNotFoundException{
		//Read Short file from disk	
		FileInputStream nFile = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(nFile);
		short[] tmpdata=null;
		
		tmpdata = (short[]) in.readObject();
		in.close();
		double[] data=new double[tmpdata.length];
		for(int i=0;i<tmpdata.length;i++)
			data[i]=(double)tmpdata[i];
		
		return data;
		
	}


}