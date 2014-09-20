package com.ikarosilva.kalman;

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
		
		Scanner sc= new Scanner(System.in);
		System.out.print("Guess 1 or 0 (enter \"2\" to quit): ");
		int answer=sc.nextInt();
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
		int[] behavior=new int[8]; //0-same, 1-different
		int[] isRepeat=new int[8];
		int[] oldAnswer=new int[4];
		int prediction=randomGuess(), playerWon, ind=0, oldResult=0, runs=0, isDifferent;
		int isSame;
		double score=0;
		while(true){
			runs++;
			System.out.print("Mind Reader: " + prediction +
					" , you: "+ answer + "\t,");
			playerWon=(prediction==answer) ? 1:0;
			ind=oldResult + 4*playerWon;
			isSame=(oldAnswer[ind]==answer) ? 1:0;
			
			isDifferent=(behavior[ind]==ind) ? 1:0;
			behavior[ind]=(behavior[ind]==answer)
					
			System.out.println("Score: "+ (double) score/runs );
			System.out.print("Guess 1 or 0 (\"2\" to quit): ");
			answer=sc.nextInt();
			prediction=randomGuess();
			if(answer>1){
				break;
			}
		}
		System.out.print("Game ended! Final Score: "+
				(double) score/runs + "\t ( " + runs + " runs)");
		System.exit(0);
	}
	
}
