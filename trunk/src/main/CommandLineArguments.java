package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommandLineArguments {


	public static void main(String[] args){
		ArrayList<String> o = alphabeticalOrder("gg", "ll");
		System.out.println(o.get(0));
		System.out.println(o.get(1));
	}

	public static ArrayList<String> alphabeticalOrder(String l1, String l2){
		ArrayList<String> order = new ArrayList<String>();
		if (l1.compareTo(l2) < 0){
			order.add(0, l1);
			order.add(1, l2);
		}
		else{
			order.add(0, l2);
			order.add(1, l1);
		}
		return order;

	}

	public void alphabeticalOrder(){
		if (l1.compareTo(l2)  > 0){
			String temp = l1;
			l1 = l2;
			l2 = temp;
		} 
	}



	private String root;
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getL1() {
		return l1;
	}
	public void setL1(String l1) {
		this.l1 = l1;
	}
	public String getL2() {
		return l2;
	}
	public void setL2(String l2) {
		this.l2 = l2;
	}
	public Integer getPositiveTrainingSentences() {
		return positiveTrainingSentences;
	}
	public void setPositiveTrainingSentences(Integer positiveTrainingSentences) {
		this.positiveTrainingSentences = positiveTrainingSentences;
	}
	public Integer getNegativeTrainingSentences() {
		return negativeTrainingSentences;
	}
	public void setNegativeTrainingSentences(Integer negativeTrainingSentences) {
		this.negativeTrainingSentences = negativeTrainingSentences;
	}
	public Integer getPositiveTestingSentences() {
		return positiveTestingSentences;
	}
	public void setPositiveTestingSentences(Integer positiveTestingSentences) {
		this.positiveTestingSentences = positiveTestingSentences;
	}
	public Integer getNegativeTestingSentences() {
		return negativeTestingSentences;
	}
	public void setNegativeTestingSentences(Integer negativeTestingSentences) {
		this.negativeTestingSentences = negativeTestingSentences;
	}
	public Boolean getMunteauTesting() {
		return munteauTesting;
	}
	public void setMunteauTesting(Boolean munteauTesting) {
		this.munteauTesting = munteauTesting;
	}
	public Boolean getMunteauTraining() {
		return munteauTraining;
	}
	public void setMunteauTraining(Boolean munteauTraining) {
		this.munteauTraining = munteauTraining;
	}
	private String l1;
	private String l2;
	private Integer positiveTrainingSentences; 
	private Integer negativeTrainingSentences;
	private Integer positiveTestingSentences;
	private Integer negativeTestingSentences;
	private Boolean munteauTesting;
	private Boolean munteauTraining;
	private String testingCorpus;
	private String trainingCorpus;
	private Boolean devCorpus;

	public String toString(){
		StringBuilder sb = new StringBuilder();
		DateFormat dateFormat = new SimpleDateFormat("MM.dd-HH:mm:ss-");
		Date date = new Date();
		sb.append(dateFormat.format(date));
		sb.append("-");
		sb.append(l1);
		sb.append("-");
		sb.append(l2);
		sb.append("-");
		sb.append(positiveTrainingSentences);
		sb.append("-");
		sb.append(negativeTrainingSentences);
		sb.append("-");
		sb.append(positiveTestingSentences);
		sb.append("-");
		sb.append(negativeTestingSentences);
		sb.append("-");
		sb.append(testingCorpus);
		sb.append("-");
		sb.append(trainingCorpus);
		sb.append("-");
		sb.append(devCorpus);	
		
		return sb.toString();
	}
	public String getTestingCorpus() {
		return testingCorpus;
	}
	public void setTestingCorpus(String testingCorpus) {
		this.testingCorpus = testingCorpus;
	}
	public String getTrainigCorpus() {
		return trainingCorpus;
	}
	public void setTrainigCorpus(String trainigCorpus) {
		this.trainingCorpus = trainigCorpus;
	}

	public boolean parseLine(String line){
		boolean parsed = false;
		String[] temp = line.split("=");
		if (temp.length != 2){
			System.out.println("Invalid line" + line);
			return false;
		}
		String field = temp[0];
		String value = temp[1].trim();
		if (field.equals("root")) {
			root = value;
			parsed = true;
		}
		else if (field.equals("l1")) {
			l1 = value;
			parsed = true;
		}
		else if (field.equals("l2")) {
			l2 = value;
			parsed = true;
		}
		else if (field.equals("positiveTrainingSentences")) {
			positiveTrainingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("negativeTrainingSentences")) {
			negativeTrainingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("positiveTestingSentences")) {
			positiveTestingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("negativeTestingSentences")) {
			negativeTestingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("munteauTesting")) {
			munteauTesting = Boolean.parseBoolean(value);
			parsed = true;
		}
		else if (field.equals("munteauTraining")) {
			munteauTraining = Boolean.parseBoolean(value);
			parsed = true;
		}
		else if (field.equals("testingCorpus")) {
			testingCorpus = value;
			parsed = true;
		}
		else if (field.equals("trainingCorpus")) {
			trainingCorpus = value;
			parsed = true;
		}
		else if (field.equals("devCorpus")) {
			devCorpus = Boolean.parseBoolean(value);
			parsed = true;
		}
		else if (field.equals("\n")) {
			parsed=true;
		}
		else if (field.startsWith("#")){
			parsed=true;
		}

		return parsed;

	}

	public String getTrainingCorpus() {
		return trainingCorpus;
	}

	public void setTrainingCorpus(String trainingCorpus) {
		this.trainingCorpus = trainingCorpus;
	}

	public Boolean getDevCorpus() {
		return devCorpus;
	}

	public void setDevCorpus(Boolean devCorpus) {
		this.devCorpus = devCorpus;
	}

}
