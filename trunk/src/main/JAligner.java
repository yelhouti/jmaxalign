package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import lexicalsimilarity.Scorer;
import lexicalsimilarity.Trainer;
import utils.Fscore;
import utils.JAlignFilePaths;
import utils.JAlignFileUtils;
import classify.ClassifierWrapper;

public class JAligner{

	JAlignFilePaths j = JAlignFilePaths.getInstance();
	NewCommandLineArguments cmdArgs;
	//some Variables that govern the settings of an indvidual run 
	int numPositiveTrainingSentences;
	int numPositiveTestingSentences ;//how many sentences to train/test with 
	int numNegativeTrainingSentences;
	int numNegativeTestingSentences;
	boolean munteauTesting = false;
	boolean munteauTraining = false; 
	String  positiveL1TestingFile;
	String  positiveL2TestingFile;
	String  positiveL1TrainingFile;
	String  positiveL2TrainingFile;
	String  positiveL1EvalFile;
	String  positiveL2EvalFile;
	
	String negativeL1TestingFile;
	String negativeL2TestingFile;
	String negativeL1TrainingFile;
	String negativeL2TrainingFile;

	String negativeL1EvalFile;
	String negativeL2EvalFile;



	public static void main(String[] args){

		NewCommandLineArguments c = new NewCommandLineArguments(args[0]);

		//Get word alignetmsn
		if (args[1].equals("-setup")){
			Trainer wordAligner = new Trainer(c);
			wordAligner.generateConfigFile();
			wordAligner.generateWordAlignments();
		}
		JAligner jmaxalign = new JAligner(c);
		if (args[1].equals("-train")){
			jmaxalign.train();
		}
		if (args[1].equals("-test")){
			jmaxalign.test();
		}
		if (args[1].equals("-eval")){
			jmaxalign.eval();
		}




	}

	public JAligner(NewCommandLineArguments c){
		cmdArgs = c;
		numPositiveTrainingSentences	= 	c.getPositiveTrainingSentences();
		numPositiveTestingSentences		= 	c.getPositiveTestingSentences();
		numNegativeTrainingSentences 	= 	c.getNegativeTrainingSentences();
		numNegativeTestingSentences 	= 	c.getNegativeTestingSentences();

		positiveL1TestingFile			= 	c.getL1TestAbsoluteFile();
		negativeL1TestingFile			= 	c.getTestDir() +  "neg-" + c.getL1TestFileName();
		positiveL2TestingFile			= 	c.getL2TestAbsoluteFile();
		negativeL2TestingFile			= 	c.getTestDir() + "neg-" + c.getL2TestFileName();
		
		positiveL2TrainingFile			=	c.getL2TrainAbsoluteFile();
		negativeL2TrainingFile			= 	c.getTrainDir()  + "neg-" + c.getL2TrainFileName();
		positiveL1TrainingFile			= 	c.getL1TrainAbsoluteFile();
		negativeL1TrainingFile			= 	c.getTrainDir() + "neg-" + c.getL1TrainFileName();
		
		positiveL1EvalFile			=	c.getL2TrainAbsoluteFile();
		negativeL1EvalFile			= 	c.getEvalDir()  + "neg-" + c.getL2EvalFileName();
		positiveL2EvalFile			= 	c.getL1TrainAbsoluteFile();
		negativeL2EvalFile			= 	c.getEvalDir() + "neg-" + c.getL1EvalFileName();


	}

	public void train(){

		try {
			String l1 = cmdArgs.getL1();
			String l2 = cmdArgs.getL2();
			//Make Negative Examples
			JAlignFileUtils.executeBashCommand("mv " + cmdArgs.getBadLexweightsPath(l1) + " " + cmdArgs.getGoodLexweightsPath(l1));
			JAlignFileUtils.executeBashCommand("mv " + cmdArgs.getBadLexweightsPath(l2) + " " + cmdArgs.getGoodLexweightsPath(l2));
			System.out.println("Generating Negative training examples...");
			generateNegativeSentences(0);
			System.out.println("Generating Negative testing examples...");
			generateNegativeSentences(1);

			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.generateConfigFile();
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierTrainFile())));

			s.score("parallel", positiveL1TrainingFile, positiveL2TrainingFile, dataWriter);
			s.score("nonparallel", negativeL1TrainingFile, negativeL2TrainingFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}

	public void eval(){
		try{
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierEvalFile())));
			s.score("parallel", positiveL1TestingFile, positiveL2TestingFile, dataWriter);
			s.score("nonparallel", negativeL1TestingFile, negativeL2TestingFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.parseResults();
			System.out.println(cmdArgs.getRoot() + "results.txt");
			
			Fscore.getScores(cmdArgs);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test(){
		try{
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierTestFile())));
			s.score("unknown", positiveL1TestingFile, positiveL2TestingFile, dataWriter);
			s.score("unknown", negativeL1TestingFile, negativeL2TestingFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.parseResults();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	






	public  void generateNegativeSentences(int type) {
		try{


			String l1SentencesPath;
			String l2SentencesPath;
			String l1NegSentencesPath;
			String l2NegSentencesPath;
			int maxSentences;
			if(type ==0){ //training
				l1SentencesPath= positiveL1TrainingFile;
				l2SentencesPath= positiveL2TrainingFile;
				l1NegSentencesPath= negativeL1TrainingFile;
				l2NegSentencesPath= negativeL2TrainingFile;
				maxSentences = numNegativeTrainingSentences;
			}
			else if (type ==1){ //testing 
				l1SentencesPath= positiveL1TestingFile;
				l2SentencesPath= positiveL2TestingFile;
				l1NegSentencesPath= negativeL1TestingFile;
				l2NegSentencesPath= negativeL2TestingFile;
				maxSentences = numNegativeTestingSentences;
			}
			else if (type ==2){ //eval 
				l1SentencesPath= positiveL1TestingFile;
				l2SentencesPath= positiveL2TestingFile;
				l1NegSentencesPath= negativeL1TestingFile;
				l2NegSentencesPath= negativeL2TestingFile;
				maxSentences = numNegativeTestingSentences;
			}
			else{
				l1SentencesPath= null;
				l2SentencesPath= null;
				l1NegSentencesPath= null;
				l2NegSentencesPath= null;
				maxSentences = 0;
			}



			BufferedReader l1_input = new BufferedReader(new FileReader(new File(l1SentencesPath)));
			BufferedReader l2_input = new BufferedReader(new FileReader(new File(l2SentencesPath)));
			PrintWriter l1_writer = new PrintWriter(new FileWriter(new File(l1NegSentencesPath), true));
			PrintWriter l2_writer = new PrintWriter(new FileWriter(new File(l2NegSentencesPath), true));

			ArrayList<String> l1Sentences = new ArrayList<String>();
			ArrayList<String> l2Sentences = new ArrayList<String>();
			String line;
			while ((line = l1_input.readLine()) != null){
				l1Sentences.add(line);
				l2Sentences.add(l2_input.readLine());
			}

			int  currentSentences = 0;
			for (int i = 0; i < l1Sentences.size(); i++){
				String l1Sen = l1Sentences.get(i);
				for (String l2Sen : l2Sentences){
					if (currentSentences == maxSentences)
						break;
					else{


						l1_writer.println(l1Sen);
						l2_writer.println(l2Sen);
						currentSentences++;

					}
				}
			}
			l1_writer.flush();
			l2_writer.flush();
			l1_writer.close();
			l2_writer.close();
		}
		catch(Exception e){System.out.println(e.getMessage());}
	}
	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}

}
