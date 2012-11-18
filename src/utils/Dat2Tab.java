package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.TreeSet;

public class Dat2Tab {

	public static void main(String[] args) throws Exception {
		TreeSet<String> tr = new TreeSet<String>();
		File dir = new File("/home/kaufmann/thesis/logs");
		for (File child : dir.listFiles()) {
			if (".".equals(child.getName()) || "..".equals(child.getName())) {
				continue;  // Ignore the self and parent aliases.
			}
			if (child.isFile()){
				BufferedReader b = new BufferedReader(new FileReader(child));
				String line = "";
				String[] lang = child.getName().split("-");
				String trainCorp =  lang[2] + " & ";
				String testCorp = lang[4] + " & ";
				String val = "";
				val += lang[0] + "-" + lang[1] + " & ";

				while ((line = b.readLine()) != null){
					//					System.out.println(line);
					if (line.contains("Percision")){
						String[] temp = line.split(":");
						val += temp[1]  + " &" ;
					}
					if (line.contains("Recall")){
						String[] temp = line.split(":");
						val += temp[1] + " & "  ;
					}
					if (line.contains("F-Score")){
						String[] temp = line.split(":");
						val += temp[1] + " & "  ;
					}
				}
				tr.add(val + trainCorp + testCorp) ;

			}
		}  

		//obtain an Iterator for Collection
		Iterator itr = tr.iterator();

		//iterate through TreeMap values iterator
		while(itr.hasNext())
			System.out.println(itr.next());

	}
}