package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateCorpora {
	JAlignFilePaths j = JAlignFilePaths.getInstance();
	private String l1OutputCorpora, l2OutputCorpora, l1InputCorpora, l2InputCorpora, classifierResults, outputDir;
	public CreateCorpora(String l1, String l2){
		l1OutputCorpora = j.getGeneratedCorporaOutputFile(l1, l1, l2);
		l2OutputCorpora = j.getGeneratedCorporaOutputFile(l2, l1, l2);
		classifierResults = j.getDevClassificationOutputPath(l1,l2);
		l1InputCorpora =j.getDevFile(l1, l1, l2);
		l2InputCorpora = j.getDevFile(l2, l1, l2);
		outputDir =  j.getCorporaPath(l1,l2) + "output/";
	}

	public  void create(){
		String parallelClass = "parallel";

		try{
			BufferedReader l1In  = new BufferedReader(new FileReader(new File(l1InputCorpora)));
			BufferedReader l2In  = new BufferedReader(new FileReader(new File(l2InputCorpora)));
			PrintWriter l1Out = new PrintWriter(new FileWriter(new File(l1OutputCorpora)));
			PrintWriter l2Out = new PrintWriter(new FileWriter(new File(l2OutputCorpora)));
			BufferedReader classifierReader  = new BufferedReader(new FileReader(new File(classifierResults)));
			String line = "";
			while ((line = classifierReader.readLine()) != null){
				String l1Line = l1In.readLine();
				String l2Line = l2In.readLine();
				String[] datum = line.split("\t");
				int lastIndex = datum.length - 1;
				String classifiedClass = datum[lastIndex].replaceAll("[^a-zA-Z]", "");
				if (classifiedClass.equals(parallelClass)){	
					l1Out.println(l1Line);
					l2Out.println(l2Line);
				}
			}
			l1Out.flush();
			l2Out.flush();
			l1Out.close();
			l2Out.close();
			System.out.println("Generated corpora available in"  + outputDir);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}