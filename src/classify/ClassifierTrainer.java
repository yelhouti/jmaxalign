package classify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import utils.JAlignFilePaths;
import edu.stanford.nlp.classify.Classifier;
import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.objectbank.ObjectBank;

public class ClassifierTrainer {

	/**
	 * Runs the classifier
	 */
	public static void classify(String l1, String l2) {
		try{
			JAlignFilePaths j = JAlignFilePaths.getInstance();


			PrintWriter out = new PrintWriter(new FileWriter(new File(j.getOutputPath(l1, l2))));

			generateConfigFile(l1,l2);
			ColumnDataClassifier cdc = new ColumnDataClassifier(j.getClassifierPropertiesPath(l1, l2));
			Classifier<String,String> cl =
					cdc.makeClassifier(cdc.readTrainingExamples(j.getClassifierTrainPath(l1,l2)));
			for (String line : ObjectBank.getLineIterator(j.getClassifierTestPath(l1,l2))) {
				Datum<String,String> d = cdc.makeDatumFromLine(line, 0);
				out.println(line + "  ==>  " + cl.classOf(d));
			}
			out.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());}


	}
	
	public static void classifyDev(String l1, String l2) {
		try{
			JAlignFilePaths j = JAlignFilePaths.getInstance();
			PrintWriter out = new PrintWriter(new FileWriter(new File(j.getDevClassificationOutputPath(l1, l2))));

			generateConfigFile(l1,l2);
			ColumnDataClassifier cdc = new ColumnDataClassifier(j.getClassifierPropertiesPath(l1, l2));
			Classifier<String,String> cl =
					cdc.makeClassifier(cdc.readTrainingExamples(j.getClassifierTrainPath(l1,l2)));
			for (String line : ObjectBank.getLineIterator(j.getClassifierDevPath(l1,l2))) {
				Datum<String,String> d = cdc.makeDatumFromLine(line, 0);
				out.println(line + "  ==>  " + cl.classOf(d));
			}
			out.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());}


	}





	/**
	 * Generates the configuration file for the Stanford Maximum Entropy Classifier. The classification file
	 * can be used to train a parser deciding if a pair of sentences is parallel or not 
	 * @param l1 The first language of the classifier 
	 * @param l2 the second language of the classifier. 
	 */
	private static void generateConfigFile(String l1, String l2){
		try{
			JAlignFilePaths j = JAlignFilePaths.getInstance();
			File out = new File(j.getClassifierPropertiesPath(l1, l2));
			PrintWriter configOutput = new PrintWriter(new FileWriter(out));
			configOutput.println("#");
			configOutput.println("# Features");
			configOutput.println("#");
			configOutput.println("useClassFeature=true");
			configOutput.println("1.useNGrams=false");
			configOutput.println("1.usePrefixSuffixNGrams=true");
			configOutput.println("1.maxNGramLeng=4");
			configOutput.println("1.minNGramLeng=1");
			configOutput.println("1.binnedLengths=10,20,30");
			configOutput.println("#");
			configOutput.println("# Printing");
			configOutput.println("#");
			configOutput.println("# printClassifier=HighWeight");
			configOutput.println("printClassifierParam=200");
			configOutput.println("#");
			configOutput.println("# Mapping");
			configOutput.println("#");
			configOutput.println("goldAnswerColumn=0");
			configOutput.println("displayedColumn=1");
			configOutput.println("#");
			configOutput.println("# Optimization");
			configOutput.println("#");
			configOutput.println("intern=true");
			configOutput.println("sigma=3");
			configOutput.println("useQN=true");
			configOutput.println("QNsize=15");
			configOutput.println("tolerance=1e-4");
			configOutput.println("#");
			configOutput.println("# Training input");
			configOutput.println("#");
			configOutput.println("trainFile=" + j.getClassifierTrainPath(l1,l2));
			configOutput.flush();
			configOutput.close();
		}
		catch(IOException e){
			System.out.println("Can't write config file");
			System.out.println(e.getMessage());
		}
	}
	

}