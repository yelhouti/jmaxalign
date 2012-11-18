package lexicalsimilarity;

import java.util.*;
import java.io.*;
import utils.*;
/**
 * 
 * @author Max Kaufmann
 * This class builds a Language Model (lm) using the SRILM toolkit.
 * @Precondition The SRILM toolkit must have been installed first. 
 */
public class LanguageModel {
	JAlignFilePaths j = JAlignFilePaths.getInstance();

	private String lang;
	private double THRESHOLD; 

	/**
	 * 
	 * @param l The language of the lm
	 * @param thresh The threshold used in detecting stop words. Words that occur 
	 * more frequently than the treshold will be identified as stop words. 
	 */
	public LanguageModel(String l, double thresh){
		lang = l;
		THRESHOLD = thresh; 
	}

	/**
	 * This builds the LM by running SRILMs -get-ngram command. 
	 * @see http://www-speech.sri.com/projects/srilm/manpages/ngram-count.1.html
	 */
	public void build(){
		String lm_output = j.getLMPath(lang);
		String text_input = j.getCleanedWikiPath(lang);
		
		File file=new File(lm_output);
		boolean exists = file.exists();
		if (exists){
			System.out.println("LM already generated, skipping this step.");
		}
		else{
			
		
		String command = j.getSRILMPath();
		JAlignFileUtils.executeBashCommand(command + " -order 1" 
				+ " -interpolate "
				+ " -kndiscount "
				+ " -unk "
				+ " -text " + text_input
				+ " -lm " + lm_output
		);
		}

	}

	/**
	 * Reads the n-gram counts from a lm, and sorts them by probability. Currently only supports unigrams
	 * @return A map containing the probabilities of various n-grams. 
	 */
	public SortedMap<Double,String > readNgrams(){
		SortedMap<Double,String > probabilities = new TreeMap<Double,String>();
		JAlignFilePaths j = JAlignFilePaths.getInstance();

		try{
			//"/home/kaufmann/thesis/tools/SRILM/bin/i686/file"
			BufferedReader b = new BufferedReader(new FileReader(
					new File(j.getLMPath(lang))));
			String line;
			while ((line = b.readLine()) != null){
				//Only look at lines with n-gram counts.
				if (line.contains("\t")){
					String[] temp = line.split("\t");
					//A sample line might be something like "-2.53313\tCat"
					probabilities.put(Double.parseDouble(temp[0]), temp[1]);
				}
			}
			return probabilities; 
		}
		catch(IOException e){System.out.println(e.getMessage());}
		return probabilities; 
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * This method looks at the n- grams in the lm, and picks out the ones that are above 
	 * the threshold and identifies them as stop words.
	 * @return A list of stop words.
	 */
	public ArrayList<String> detectStopWords(){
		SortedMap<Double,String> allNgrams = readNgrams();
		ArrayList<String>  stopWords =  new ArrayList<String>();
		for (Double d : allNgrams.keySet()){
			if (d > THRESHOLD){ //check if over threshhold
				stopWords.add(allNgrams.get(d));
			}
		}
		return stopWords;
	}
	public static void main(String[] args){
		LanguageModel l = new LanguageModel("hu", -2);
		l.build();
		l.detectStopWords();
	}


}
