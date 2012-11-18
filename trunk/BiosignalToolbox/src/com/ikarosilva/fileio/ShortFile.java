package com.ikarosilva.fileio;

//Converts all txt files in a directory in short binary files 
// so that the compressed data can be run from the Android APP
// of the PhysioNet 2011 Challenge

import java.awt.*;
import java.io.*;



public class ShortFile {
	
	public static void writeFile(String newFile, short[] data) throws IOException{
		
		FileOutputStream fos=new FileOutputStream(newFile);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(data);
		out.flush();
		out.close();
	}
	
	public static short[] readFile(String file) throws IOException, ClassNotFoundException{
		//Check that data is ok
		FileInputStream nFile = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(nFile);
		short[] data=null;
		data = (short[]) in.readObject();
		in.close();
		return data;
	}
	
	public static void main(String[] args) {
		// Convert the string to a File object, and check that the dir exists
		File dir = new File("/home/ikaro/workspace/BiosignalToolbox/src/com/ikarosilva/fileio/");
		int M=10;
		short [] data1= new short[M]; //Array for storing summed channel data
		short [] data2= null; //Array for storing summed channel data
		String path = dir.getAbsolutePath();
		
			//Simulate data and write to disk
			for(int i=0;i<M;i++)
				data1[i]=(short) i;
			try {
				writeFile(path + "/Shorttest.raw",data1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Read data
			try {
				data2= readFile(path + "/Shorttest.raw");
				for(int i=0;i<data2.length;i++)
					System.out.println(data2[i] + "    " + data1[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
}