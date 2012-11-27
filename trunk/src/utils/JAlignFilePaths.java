package utils;

import main.NewCommandLineArguments;

public class JAlignFilePaths {


	public NewCommandLineArguments c;
	public NewCommandLineArguments getC() {
		return c;
	}

	public void setC(NewCommandLineArguments c) {
		this.c = c;
	}


	private static JAlignFilePaths instance = new JAlignFilePaths();
	private  JAlignFilePaths() {

	}

	public static JAlignFilePaths getInstance() {
		return instance;
	}



	public static void setRoot(String r){
		root = r;
		dbpedia_root = root + "dbpedia/";
		corpora_root = root + "corpora/";
		tools_root = root + "tools/";
		wiki_root = root + "wikipedia/";
		lm_root = root + "lm/";
		segmentation_root = root + "segmentation/";
		hunalign_path = tools_root + "hunalign/src/hunalign/hunalign";
		log_path = root + "log/";
	}


	public static String root = "/home/kaufmann/newcomp/";
	//	public static String root;
	private static String dbpedia_root;
	private static String tools_root;
	private static String corpora_root;
	private static String wiki_root ;
	private static String lm_root ;
	private static String segmentation_root;
	private static String hunalign_path;
	private static String log_path ;


	//Takes two languages, and computes the directory path for the parallel corpora
	//EX: corporaPath("de", "es) returns "/home/kaufmann/thesis/coprora/de-es
	public static String getCorporaPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/";
	}


	public static String getCleanSriptPath(String l1, String l2){
		return root + "clean.sh " + l1 + " " + l2;
	}

	//Training 
	public static String getTrainingAlignmentFile(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "training_alingment.config";
	}
	public static String getTrainingAlignDirectoryPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/";
	}

	public static String getTrainingPrasesPath(String l1, String l2){
		return getTrainingAlignDirectoryPath(l1,l2)+  "phrases"; 
	}


	public static String getOptionsPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/options.map";
	}
	public static String getGeneratedPhrasePath(String l1, String l2){
		return getTrainingAlignDirectoryPath(l1,l2)+  "phrases"; 
	}

	public static String getGeneratedLexWeightsPath(String oneOfTheLanguages, String l1, String l2){
		return getTrainingAlignDirectoryPath(l1,l2)+ oneOfTheLanguages +  ".lexweights"; 
	}


	//Parallel and Comporable file paths 
	public static String getParallelDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "parallel" + "/parallel." + oneOfTheLanguages; 
	}

	public static String getPositiveTestDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "test" + "/positive-test." + oneOfTheLanguages; 
	}


	public static String getPositiveTrainDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "train" + "/positive-train." + oneOfTheLanguages; 
	}


	public static String getMunteauNegativeTestDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "test" + "/negative-test." + oneOfTheLanguages; 
	}


	public static String getMunteauNegativeTrainDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "train" + "/negative-train." + oneOfTheLanguages; 
	}

	public static String getNegativeTestDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "test" + "/negative-test-random." + oneOfTheLanguages; 
	}

	public static String getNegativeDataFile(String oneOfTheLanguages, String l1, String l2, String evaluationType, boolean munteau, String corpus, int numSentences){
		if (munteau && corpus.equals("KDE"))
			return getCorporaPath(l1,l2)+ evaluationType + "/negative-"+ evaluationType+ "-munteau-KDE." +  numSentences +  "." + oneOfTheLanguages; 
		else if ((!munteau) && corpus.equals("KDE"))
			return getCorporaPath(l1,l2)+ evaluationType + "/negative-"+ evaluationType+ "-random-KDE." +  numSentences + "." +  oneOfTheLanguages; 
		else if (munteau && corpus.equals("Subtitles"))
			return getCorporaPath(l1,l2)+ evaluationType + "/negative-"+ evaluationType+ "-munteau-Subtitles." +  numSentences +  "." + oneOfTheLanguages; 
		else if ((!munteau) && corpus.equals("Subtitles"))
			return getCorporaPath(l1,l2)+ evaluationType + "/negative-"+ evaluationType+ "-random-Subtitles-." +  numSentences +  "." + oneOfTheLanguages; 
		else
			return "";
	}
	

	public static String getPositiveDataFile(String oneOfTheLanguages, String l1, String l2, String evaluationType, String corpus, int numSentences){
		if (corpus.equals("KDE"))
			return getCorporaPath(l1,l2)+ evaluationType + "/positive-"+ evaluationType+ "-KDE." +  numSentences +  "." + oneOfTheLanguages; 
		else if ( corpus.equals("Subtitles"))
			return getCorporaPath(l1,l2)+ evaluationType + "/positive-"+ evaluationType+ "-Subtitles-." +  numSentences +  "." + oneOfTheLanguages; 
		else
			return "";
	}

	public static String getlogPath(String log){
		return log_path + log + ".txt";
	}

	public static String getDevFile(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2) + "dev/dev." + oneOfTheLanguages;
	}


	public static String getGeneratedCorporaOutputFile(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2) + "output/parallel." + oneOfTheLanguages;
	}



	public static String getNegativeTrainDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "train" + "/negative-train-random." + oneOfTheLanguages; 
	}

	public static String getOutputPath( String l1, String l2){
		return getCorporaPath(l1,l2)+ "output.txt";
	}
	
	public static String getDevClassificationOutputPath( String l1, String l2){
		return getCorporaPath(l1, l2) + "classify/dev.output";

	}


	public static String getAllTestDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "test" + "/test." + oneOfTheLanguages; 
	}


	public static String getAllTestTrainDataPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "train" + "/train." + oneOfTheLanguages; 
	}

	public static String getComporableCorporaPath(String oneOfTheLanguages, String l1, String l2){
		return getCorporaPath(l1,l2)+ "/comporable/comporable_corpora_segmented_prepared." + oneOfTheLanguages; 
	}


	public static String getParallelDirectory(String l1, String l2){
		return getCorporaPath(l1,l2)+  "parallel"; 
	}

	public static String getComporableDirectory(String l1, String l2){
		return getCorporaPath(l1,l2)+ "comporable"; 
	}



	public static String getTrainDirectory(String l1, String l2){
		return getCorporaPath(l1,l2)+  "train"; 
	}

	public static String getTestirectory(String l1, String l2){
		return getCorporaPath(l1,l2)+ "test"; 
	}




	public static String getBadLexweightsPath(String oneOfTheLanguages, String l1, String l2){
		if (oneOfTheLanguages.equals(l1))
			return corpora_root + l1 + "-" + l2 + "/align/2.lexweights";
		else if (oneOfTheLanguages.equals(l2))
			return corpora_root + l1 + "-" + l2 + "/align/1.lexweights";
		else 
			return null;
	}

	public static String getGoodLexweightsPath(String oneOfTheLanguages, String l1, String l2){
		if (oneOfTheLanguages.equals(l1))
			return corpora_root + l1 + "-" + l2 + "/align/"+ l1 + ".lexweights";
		else if (oneOfTheLanguages.equals(l2))
			return corpora_root + l1 + "-" + l2 + "/align/"+ l2 + ".lexweights";
		else 
			return null;
	}


	//Fixes for alowing the stanford parser to interact with berkley
	public static String getBadTrainingFilePath(String oneOfTheLanguages, String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/training." + oneOfTheLanguages + "Input.txt";
	}

	public static String getGoodTrainingFilePath(String oneOfTheLanguages, String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/training." + oneOfTheLanguages;
	}

	public static String getBadAlignPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/training." + l2 + "-" + l1  + ".align";
	}

	public static String getGoodAlignPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/align/training.align";
	}

	public static String getTestFilePath(String oneOfTheLanguages, String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/test/test." + oneOfTheLanguages;
	}

	public static String getTMXPath(String l1, String l2, String corpus){
		if (corpus.equals("KDE"))
			return corpora_root + l1 + "-" + l2 + "/KDE4" + l1 + "-" + l2 + ".tmx";
		else if (corpus.equals("Subtitles"))
			return corpora_root + l1 + "-" + l2 + "/OpenSubtitles2011" + l1 + "-" + l2 + ".tmx";
		else 
			return "";

	}

	public static String getKDETMXPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/"  + "KDE4" + l1 + "-" + l2 + ".tmx";
	}

	public static String getSubtitlesTMXPath(String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/" + "OpenSubtitles2011" + l1 + "-" + l2 + ".tmx";
	}
	public static String getHunAlignPath(){
		return hunalign_path;
	}

	//wiki path

	public static String getUncleanWikiPath(String lang){
		return  wiki_root + lang + ".xml";
	}

	public static String getCleanedWikiPath(String lang){
		return  lm_root + "corpus." + lang;
	}


	public static String getNgramPath(String lang){
		return  lm_root + "ngramCounts." + lang;
	}

	public static String getExtractedPhrasesPath(String l1, String l2){
		return getCorporaPath(l1, l2) + "align/phrases.gz";
	}

	//SRILM

	public static String getSRILMPath(){
		return  tools_root + "SRILM/bin/i686/ngram-count";
	}

	//Classify
	public static String getClassifierTrainPath(String l1, String l2){
		return getCorporaPath(l1, l2) + "classify/train.prop";
	}

	public static String getClassifierTestPath(String l1, String l2){
		return getCorporaPath(l1, l2) + "classify/test.prop";
	}
	
	
	public static String getClassifierDevPath(String l1, String l2){
		return getCorporaPath(l1, l2) + "classify/dev.prop";
	}
	public static String getClassifierPropertiesPath(String l1, String l2){
		return getCorporaPath(l1, l2) + "classify/data.prop";
	}
	public static String getClassifierResults(String l1, String l2){
		return getCorporaPath(l1, l2) + "output.txt";
	}

	public static String getHunalignPath(String oneOfTheLanguages, String l1, String l2){
		return corpora_root + l1 + "-" + l2 + "/hunalign." + oneOfTheLanguages;
	}

	public static String getWikiPath(String lang){
		return "/home/kaufmann/thesis/wikipedia/" + lang + ".xml";
	}

	public static String getLMPath(String lang){
		return "/home/kaufmann/thesis/lm/." + lang + ".lm";
	}


	/*
	 * 	wrt = new PrintWriter(new FileWriter(new File("/home/kaufmann/thesis/lm/corpus.es")));
			p = Pattern.compile("\\{\\{.*\\}\\}");

			parser = new WikiXMLParser("/home/kaufmann/thesis/wikipedia/es.xml", filter);
	 */
}
