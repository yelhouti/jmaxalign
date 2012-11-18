package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lexicalsimilarity.Scorer;
import lexicalsimilarity.Trainer;
import objects.NewSentencePair;
import utils.CreateCorpora;
import utils.Fscore;
import utils.JAlignFilePaths;
import utils.JAlignFileUtils;
import classify.ClassifierTrainer;

public class JAligner{

	JAlignFilePaths j = JAlignFilePaths.getInstance();
	CommandLineArguments cmdArgs;
	String l1;
	String l2;
	//some Variables that govern the settings of an indvidual run 
	int numPositiveTrainingSentences;
	int numPositiveTestingSentences ;//how many sentences to train/test with 
	int numNegativeTrainingSentences;
	int numNegativeTestingSentences;
	String testingCorpus, trainingCorpus;
	boolean munteauTesting;
	boolean munteauTraining;
	String  positiveL1TestingPath;
	String  positiveL2TestingPath;
	String  positiveL1TrainingPath;
	String  positiveL2TrainingPath;

	String negativeL1TestingPath;
	String negativeL2TestingPath;
	String negativeL1TrainingPath;
	String negativeL2TrainingPath;

	String devl1Path, devl2Path;

	//	public static void main(String[] args){
	//
	//		JAlignFilePaths.setRoot(args[0]);
	//		int nPositiveTrainingSentences = Integer.parseInt(args[1]);
	//		int nPositiveTestingSentences =  Integer.parseInt(args[2]);
	//		int nNegativeTrainingSentences =  Integer.parseInt(args[3]);
	//		int nNegativeTestingSentences =  Integer.parseInt(args[4]);
	//		String teCorpus = args[5];
	//		String trCorpus = args[6];
	//		boolean munteauTesting = Boolean.parseBoolean(args[7]);
	//		boolean munteauTraining = Boolean.parseBoolean(args[8]);
	//
	//
	//		JAligner jal = new JAligner(args[9], args[10]);
	//		jal.configureFeatures(nPositiveTrainingSentences, nPositiveTestingSentences, 
	//				nNegativeTrainingSentences, nNegativeTestingSentences,
	//				munteauTesting, munteauTraining, teCorpus, trCorpus);
	//		if (args[11].equals("-setup"))
	//			jal.run(true);
	//		else if (args[11].equals("-full"))
	//			jal.run(false);
	//		else{
	//			System.err.println("Last option must be either -setup or -full");
	//			System.exit(0);
	//		}
	//		//score it
	//		
	//		DateFormat dateFormat = new SimpleDateFormat("MM.dd-HH:mm:ss-");
	//		Date date = new Date();
	//		System.out.println();
	//		String logpath = dateFormat.format(date);
	//		for (int i = 1; i <args.length;i++){
	//			logpath+=args[i] + "-";
	//		}
	//
	//		Fscore f = new Fscore(jal.l1 + "-" + jal.l2  +logpath) ;
	//		f.getScores(jal.l1, jal.l2);
	//
	//
	//	}

	public static void main(String[] args){

		try{
			BufferedReader b = new BufferedReader(new FileReader(new File(args[0])));
			String line ="";
			CommandLineArguments c = new CommandLineArguments();
			while ((line=b.readLine()) != null){
				if (c.parseLine(line) == false)
					System.out.println("Could not parse line: " + line);
			}

			c.alphabeticalOrder();
			JAlignFilePaths.setRoot(c.getRoot());
			JAligner jal = new JAligner(c);

			if (args[1].equals("-train"))
				jal.train();
			else if (args[1].equals("-test"))
				jal.test();
			else if (args[1].equals("-create")){
				if (c.getDevCorpus())
					jal.evaluate();
				else{
					System.out.println("devcorpus must be set to true in the config file, and there must be a parallel."
							+ c.getL1() + " and a parallel. " + c.getL2() + "in the dev/ folder");
					System.exit(0);
				}
			}
			else{
				System.err.println("Last option must be either -train, -test -create");
				System.exit(0);
			}

		}
		catch(IOException e){
			e.printStackTrace();
		}


	}

	public JAligner(CommandLineArguments c){
		cmdArgs = c;
		l1=c.getL1();
		l2=c.getL2();
		numPositiveTrainingSentences = c.getPositiveTrainingSentences();
		numPositiveTestingSentences =  c.getPositiveTestingSentences();
		numNegativeTrainingSentences = c.getNegativeTrainingSentences();
		numNegativeTestingSentences =   c.getNegativeTestingSentences();
		testingCorpus= c.getTestingCorpus();
		trainingCorpus= c.getTrainigCorpus();
		munteauTesting= c.getMunteauTesting();
		munteauTraining = c.getMunteauTraining();
		positiveL1TestingPath= 		j.getPositiveDataFile(l1, l1, l2, "test",  testingCorpus, numPositiveTestingSentences);
		positiveL2TestingPath= 		j.getPositiveDataFile(l2, l1, l2, "test",  testingCorpus, numPositiveTestingSentences);
		positiveL1TrainingPath= 	j.getPositiveDataFile(l1, l1, l2, "train",  trainingCorpus, numPositiveTrainingSentences);
		positiveL2TrainingPath=		j.getPositiveDataFile(l2, l1, l2, "train",  trainingCorpus, numPositiveTrainingSentences);

		negativeL1TestingPath= 		j.getNegativeDataFile(l1, l1, l2, "test", munteauTesting, testingCorpus, numNegativeTestingSentences);
		negativeL2TestingPath= 		j.getNegativeDataFile(l2, l1, l2, "test", munteauTesting, testingCorpus, numNegativeTestingSentences);
		negativeL1TrainingPath= 		j.getNegativeDataFile(l1, l1, l2, "train", munteauTraining, trainingCorpus, numNegativeTrainingSentences);
		negativeL2TrainingPath= 		j.getNegativeDataFile(l2, l1, l2, "train", munteauTraining, trainingCorpus, numNegativeTrainingSentences);
		devl1Path  = j.getDevFile(l1, l1, l2);
		devl2Path = j.getDevFile(l2, l1, l2);

	}



	public  void train(){

		String cleanpath = j.getCleanSriptPath(l1, l2);
		JAlignFileUtils.executeBashCommand(cleanpath);

		//Generate data for training
		generatePositiveTestingAndTrainingData();

		//Train with Berkeley aligner
		Trainer LX  = new Trainer(l1, l2);

	}
	public void test(){

		//Make Negative Examples
		JAlignFileUtils.executeBashCommand("mv " + j.getBadLexweightsPath(l1, l1, l2) + " " + j.getGoodLexweightsPath(l1, l1, l2));
		JAlignFileUtils.executeBashCommand("mv " + j.getBadLexweightsPath(l2, l1, l2) + " " + j.getGoodLexweightsPath(l2, l1, l2));
		System.out.println("Generating Negative training examples...");
		generateNegativeSentences(true);
		System.out.println("Generating Negative testing examples...");
		generateNegativeSentences(false);

		Scorer S = new Scorer(l1, l2);
		System.out.println("Scoring positive training data...");
		S.score("parallel", positiveL1TrainingPath, positiveL2TrainingPath, j.getClassifierTrainPath(l1, l2) );
		System.out.println("Scoring positive testing data...");
		S.score("parallel", positiveL1TestingPath,positiveL2TestingPath,  j.getClassifierTestPath(l1, l2) );
		System.out.println("Scoring negative testing  data...");
		S.score("nonparallel", negativeL1TestingPath, negativeL2TestingPath,  j.getClassifierTestPath(l1, l2) );
		System.out.println("Scoring negative training data...");
		S.score("nonparallel",	negativeL1TrainingPath, negativeL2TrainingPath,  j.getClassifierTrainPath(l1, l2) );

		//Train the Stanford classifier
		ClassifierTrainer.classify(l1, l2);

		Fscore f = new Fscore(l1 + "-" + l2  + "-" + cmdArgs.toString()) ;
		f.getScores(l1, l2);

	}

	public  void evaluate(){
		Scorer S = new Scorer(l1, l2);
		System.out.println("Scoring Evaluation Data");
		S.score("unknown", devl1Path, devl2Path, j.getClassifierDevPath(l1, l2));
		ClassifierTrainer.classifyDev(l1, l2);
		CreateCorpora maker = new CreateCorpora(l1,l2);
		maker.create();
	}




	public void generatePositiveTestingAndTrainingData(){

		boolean switchedToTest = false;
		try{
			BufferedReader tmxReader = new BufferedReader(new FileReader(new File(j.getTMXPath(l1, l2, trainingCorpus))));

			//			
			PrintWriter l1_output = new PrintWriter(new FileWriter(new File(positiveL1TrainingPath), true));
			PrintWriter l2_output = new PrintWriter(new FileWriter(new File(positiveL2TrainingPath), true));
			//			
			String line;

			int i = 0;
			while ((line = tmxReader.readLine()) != null){

				//We parsed all the testing data we want
				if (i>= numPositiveTrainingSentences && (!switchedToTest) ){
					switchedToTest = true;

					l1_output.flush();
					l2_output.flush();
					l1_output.close();
					l2_output.close();
					tmxReader = new BufferedReader(new FileReader(new File(j.getTMXPath(l1, l2, testingCorpus))));
					l1_output = new PrintWriter(new FileWriter(new File(positiveL1TestingPath), true));
					l2_output = new PrintWriter(new FileWriter(new File(positiveL2TestingPath), true));

				}
				//we also parsed all the training data
				if (i>= (numPositiveTestingSentences + numPositiveTrainingSentences)  && (switchedToTest) ){
					break;
				}
				//				if(line.contains("<tu>")){
				//
				//					String doc = xmldoc.toString();
				//					l1_output.println(getSentences(doc));
				//					//					l2_output.println(getSentences(l2));
				//					xmldoc = new StringBuilder();
				//					xmldoc.append(line);
				//				}
				//				else{
				//					xmldoc.append(line + "\n");
				//				}
				if ( (!(line.contains("srclang"))) && (!(line.contains("adminlang"))) ){
					if(line.contains("\"" + l1 + "\"")){				

						line = line.replaceAll("<tuv xml:lang=\"" + l1 + "\">", "")
								.replaceAll("</seg>","")
								.replaceAll("<seg>", "").
								replaceAll("</tuv>", "");
						l1_output.println(line.trim());
					}
					if(line.contains("\"" + l2 + "\"")){
						i++;
						line = line.replaceAll("<tuv xml:lang=\"" + l2 + "\">", "")
								.replaceAll("</seg>","")
								.replaceAll("<seg>", "").
								replaceAll("</tuv>", "");
						l2_output.println(line.trim());
					}
				}
			}
			l1_output.flush();
			l2_output.flush();
			l1_output.close();
			l2_output.close();
		}
		catch(IOException e){System.out.println(e.getMessage());}
	}


	public  void generateNegativeSentences(boolean training) {
		try{

			Boolean munteau;
			String l1SentencesPath;
			String l2SentencesPath;
			String l1NegSentencesPath;
			String l2NegSentencesPath;
			int maxSentences;
			if(training){
				munteau = munteauTraining;
				l1SentencesPath= positiveL1TrainingPath;
				l2SentencesPath= positiveL2TrainingPath;
				l1NegSentencesPath= negativeL1TrainingPath;
				l2NegSentencesPath= negativeL2TrainingPath;
				maxSentences = numNegativeTrainingSentences;
			}
			else{
				munteau = munteauTesting;
				l1SentencesPath= positiveL1TestingPath;
				l2SentencesPath= positiveL2TestingPath;
				l1NegSentencesPath= negativeL1TestingPath;
				l2NegSentencesPath= negativeL2TestingPath;
				maxSentences = numNegativeTestingSentences;

			}

			Scorer scorer = new Scorer(l1, l2);
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
						if (munteau){
							int l1Len = l1Sen.split(" ").length;
							int l2Len = l2Sen.split(" ").length;
							float ratio = divideIntegers(l1Len, l2Len);
							if ( ratio < 200  ){
								NewSentencePair sp = scorer.computeLexicalSimilarity(l1Sen, l2Sen);
								if (sp.percentageUnAlignedWords() < 50){
									l1_writer.println(l1Sen);
									l2_writer.println(l2Sen);
									currentSentences++;
								}
							}
						}
						else{
							l1_writer.println(l1Sen);
							l2_writer.println(l2Sen);
							currentSentences++;
						}
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
