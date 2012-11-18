package utils;
import java.io.*;
import java.util.*;

import objects.NewSentencePair;

import lexicalsimilarity.Scorer;
public class NegativeTrainingExamples {

	private String l1, l2;
	private int maxSentences;

	public NegativeTrainingExamples(String lang1, String lang2, int maxsen){
		l1 = lang1;
		l2 = lang2;
		maxSentences = maxsen;
	}
	
	public  void generateNegativeTrainingExamples( Boolean munteau, 
			String l1SentencesPath, String l2SentencesPath, 
			String l1NegSentencesPath, String l2NegSentencesPath) {
		try{
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
