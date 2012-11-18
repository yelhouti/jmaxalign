package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Fscore {
	JAlignFilePaths j = JAlignFilePaths.getInstance();

	public static void main(String[] args){

		System.exit(0);
		int truePositive = 8105; //is paralle, found parallel 
		int falsePositive = 1995; //is parallel, found not parallel 
		int trueNegative = 9578; //is nonparallel, found non parallel
		int falseNegative = 422;  //is nonparallel, found parallel
		float  percision = divideIntegers(truePositive, (truePositive + falsePositive));
		
		float  recall = divideIntegers(truePositive, (truePositive + falseNegative));
		System.out.println("Precision:" + percision);
		System.out.println("Recall: " + recall);
		
		double f = 0;
		f = ((2 * percision * recall)) / (percision + recall);
		System.out.println("F-Score:" + f);
	}
	String path;
	
	public Fscore(String p){
		path = p;
	}
	public  void getScores(String l1, String l2){

		int truePositive = 0; //is paralle, found parallel 
		int falsePositive = 0; //is parallel, found not parallel 
		int trueNegative = 0; //is nonparallel, found non parallel
		int falseNegative = 0;  //is nonparallel, found parallel
		String parallelClass = "parallel";
		String nonParallelClass = "nonparallel";
		
		try{
			BufferedReader b  = new BufferedReader(new FileReader(new File(j.getClassifierResults(l1,l2))));
			PrintWriter w = new PrintWriter(new FileWriter(new File(j.getlogPath(path)), true));
			String line = "";
			while ((line = b.readLine()) != null){
				String[] datum = line.split("\t");
				int lastIndex = datum.length - 1;
				String actualClass = datum[0];
				String classifiedClass = datum[lastIndex].replaceAll("[^a-zA-Z]", "");
				if ( (actualClass.equals(parallelClass)) && classifiedClass.equals(parallelClass))
					truePositive++;
				else if ( (actualClass.equals(parallelClass)) && classifiedClass.equals(nonParallelClass))
					falsePositive++;
				else if ( (actualClass.equals(nonParallelClass)) && classifiedClass.equals(nonParallelClass))
					trueNegative++;
				else if ( (actualClass.equals(nonParallelClass)) && classifiedClass.equals(parallelClass))
					falseNegative++;				
			}
		
			
/*
 * Precision = TP/(TP+FP) = What portion of what you found was ground truth?

Recall = TP/(TP+FN) = What portion of the ground truth did you recover?
 */
			float  percision = divideIntegers(truePositive, (truePositive + falsePositive));
			float  recall = divideIntegers(truePositive, (truePositive + falseNegative));
			int total = truePositive + trueNegative + falsePositive + falseNegative;
			float  accuracy = divideIntegers((truePositive + trueNegative), total);

		
			
		
			double  f = 0;
			f = ((2 * percision * recall)) / (percision + recall);
	
			w.println("------------------");
			w.println("True Positive: " + truePositive);
			w.println("False Positive: " + falsePositive);
			w.println("True Negative: " + trueNegative);
			w.println("False Negative: " + falseNegative);
			w.println("------------------");
			w.println("Precision:" + percision);
			w.println("Recall: " + recall);			
			w.println("Accuracy " + accuracy);
			w.println("F-Score:" + f);
			w.println("------------------");
			
			System.out.println("------------------");
			System.out.println("True Positive: " + truePositive);
			System.out.println("False Positive: " + falsePositive);
			System.out.println("True Negative: " + trueNegative);
			System.out.println("False Negative: " + falseNegative);
			System.out.println("------------------");
			System.out.println("Precision:" + percision);
			System.out.println("Recall: " + recall);			
			System.out.println("Accuracy " + accuracy);
			System.out.println("F-Score:" + f);
			System.out.println("------------------");
			System.out.println("Scoring data written to: " + j.getlogPath(path));
			
			System.out.flush();
			
		}
		catch(IOException e){System.out.println(e.getMessage());}
	}
	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}
	
	
}
