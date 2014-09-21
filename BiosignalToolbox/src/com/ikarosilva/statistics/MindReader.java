package com.ikarosilva.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MindReader {

	private static Options options = new Options();
	private static void help() {
		// Print out help for the function
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("PlotTimeSeries",options);
		System.exit(0);
	}


	private static void getOptions(){
		options = new Options();
		options.addOption("h",false, "Display help.");
	}

	private static int randomGuess(){
		int x=(Math.random()>0.5) ? 1:0;
		return x;
	}
	public static void main(String[] args) throws IOException {

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		getOptions();
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h"))
				help();
		} catch (ParseException e) {
			e.printStackTrace();
			help();
		}

		/*
		  Register map
		  1 - win, same, win
		  2 - win, same , loose
		  3 - win, different, win
		  4 - win, different, loose
		  5 - loose, same, win
		  6 - loose, same, loose
		  7 - loose, different, win
		  8 - loose, different, loose 

		 */
		Scanner sc= new Scanner(System.in);
		System.out.print("Guess 1 or 0 (enter \"2\" to quit): ");
		int[] register=new int[8]; //0-same, 1-different
		boolean[] isRepeat=new boolean[8];
		int[] guess=new int[8];
		int prediction=randomGuess(), answer1=0, answer2=0, result1=0, result2=0, ind=0, runs=0;
		int isSame=0;

		double score=0;
		while(true){
			//Update score and display to user
			runs++;
			System.out.print("Mind Reader: " + prediction +
					" , you: "+ answer2 + "\t,");
			System.out.println("Score: "+ (double) score/runs );
			System.out.print("Guess 1 or 0 (\"2\" to quit): ");


			//Update Model
			result2=(prediction==answer2) ? 1:0;
			isSame=(answer2==answer1) ? 1:0;
			answer1=answer2;
			ind=result1 + 2*isSame + 4*result2;
			if(register[ind]==isSame){
				isRepeat[ind]=true;
			}
			register[ind]=isSame;
			guess[ind]=answer2;

			//Get next prediction 
			if(isRepeat[ind]){
				if(register[ind]==1){
					prediction=guess[ind];
				}else{
					prediction=(guess[ind]==1) ? 0:1;
				}
			}else{
				prediction=randomGuess();
			}

			//Prompt for his guess
			answer2=sc.nextInt();
			if(answer2>1){
				break;
			}
		}
		System.out.print("Game ended! Final Score: "+
				(double) score/runs + "\t ( " + runs + " runs)");
		System.exit(0);
	}

}
