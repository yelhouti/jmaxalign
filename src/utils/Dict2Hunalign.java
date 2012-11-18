package utils;

import java.io.*;
import java.util.HashMap;

import lexicalsimilarity.Scorer;

public class Dict2Hunalign {

	public static void main(String[] args) throws Exception{
		JAlignFilePaths j = JAlignFilePaths.getInstance();

		String l1 = "en";
		String l2 = "hu";
		Scorer s = new Scorer(l1, l2);
		HashMap<String, HashMap<String, Double>> L1toL2LexWeights = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, HashMap<String, Double>> L2toL1LexWeights = new HashMap<String, HashMap<String, Double>>();

		String path = "/home/kaufmann/thesis/corpora/" + l1 + "-" + l2 + "/hunalign.dic";

		PrintWriter p = new PrintWriter(new FileWriter(new File(path)));
		L1toL2LexWeights = s.loadWeightsIntoMemory(l1, .3);
		L2toL1LexWeights= s.loadWeightsIntoMemory(l2, .3); 
		//
		for (String word : L1toL2LexWeights.keySet()){
			HashMap<String, Double> candidateTranslations = L1toL2LexWeights.get(word);
			if (candidateTranslations != null){
				//If we have candidate translations, check if they appear in the other sentence
				for (String translation : candidateTranslations.keySet()){
					p.println(translation + " @ " + word);
				}
			}
		}


		for (String word : L2toL1LexWeights.keySet()){
			HashMap<String, Double> candidateTranslations = L2toL1LexWeights.get(word);
			if (candidateTranslations != null){
				//If we have candidate translations, check if they appear in the other sentence
				for (String translation : candidateTranslations.keySet()){
					p.println(translation + " @ " + word);
				}
			}
		}
		p.flush();
		p.close();

		int numSentences = 5000;	String trainingCorpus = "Subtitles";
		HashMap<Integer, String> vals = new HashMap<Integer,String>();
		String positiveL1TrainingPath= 	j.getPositiveDataFile(l1, l1, l2, "train",  trainingCorpus, numSentences);
		String positiveL2TrainingPath=		j.getPositiveDataFile(l2, l1, l2, "train",  trainingCorpus, numSentences);
		String negativeL1TrainingPath= 		j.getNegativeDataFile(l1, l1, l2, "train", false, trainingCorpus, numSentences);
		String negativeL2TrainingPath= 		j.getNegativeDataFile(l2, l1, l2, "train", false, trainingCorpus, numSentences);

		BufferedReader reader;
		PrintWriter l1Hun = new PrintWriter(new FileWriter(new File (j.getHunalignPath(l1,l1,l2))));
		PrintWriter l2Hun = new PrintWriter(new FileWriter(new File (j.getHunalignPath(l2,l1,l2))));

		//0 = parallel, 1 = nonparallle 
		reader = new BufferedReader(new FileReader(new File(positiveL1TrainingPath)));
		String line ="";
		while ((line = reader.readLine()) != null){
			vals.put(0, line);
			l1Hun.println(line);
		}
		reader = new BufferedReader(new FileReader(new File(negativeL1TrainingPath)));
		while ((line = reader.readLine()) != null){
			vals.put(1, line);
			l1Hun.println(line);
		}

		reader = new BufferedReader(new FileReader(new File(positiveL2TrainingPath)));
		while ((line = reader.readLine()) != null){
			vals.put(0, line);
			l2Hun.println(line);
		}
		reader = new BufferedReader(new FileReader(new File(negativeL2TrainingPath)));
		while ((line = reader.readLine()) != null){
			vals.put(1, line);
			l2Hun.println(line);
		}
		l2Hun.flush();
		l1Hun.flush();

		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;

		reader = new BufferedReader(new FileReader(new File("")));
		int i = 0; 
		while ((line = reader.readLine()) != null){
			double thresh = .3;
			String[] temp= line.split("\t");
			if (temp[0].equals(temp[1])){
				Double val = Double.parseDouble(temp[3]);
				if ((Integer.parseInt(temp[0]) < 5000) && (val > thresh))
					truePositive++;
				else if (((Integer.parseInt(temp[0]) < 5000) && (val < thresh)))
					falseNegative++;
				if ((Integer.parseInt(temp[0]) > 5000) && (val < thresh))
					trueNegative++;
				if ((Integer.parseInt(temp[0]) > 5000) && (val > thresh))
					falsePositive++;
				
			}
			else{
				falsePositive++;
			}
		}

		float  percision = divideIntegers(truePositive, (truePositive + falsePositive));

		float  recall = divideIntegers(truePositive, (truePositive + falseNegative));
		System.out.println("Percision:" + percision);
		System.out.println("Recall: " + recall);

		double f = 0;
		f = ((2 * percision * recall)) / (percision + recall);

		//		System.out.println(L1toL2LexWeights.keySet().size());
		//		System.out.println(L1toL2LexWeights.values().size());
		//		System.out.println(L2toL1LexWeights.keySet().size());
		//		System.out.println(L2toL1LexWeights.values().size());
	}

	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}

	public void runHunAlign(){
		String l1 = "en";
		String l2 = "hu";



	}
}
