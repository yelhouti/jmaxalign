package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author kaufmann
 * Various utilities to make interacting with Files from within Java an easier task. 
 */
public class JAlignFileUtils {	



	
	
	/**
	 * Executes a bash command on the command line, and waits for it to finish
	 * 
	 * @param command The command to be executed
	 */
	public static void executeBashCommand(String command){
		System.out.println("Running command " + command + "...");
		try{
			 Runtime.getRuntime().exec(command).waitFor();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("Finished!");

	}
	//combines the aligned versions of short and long abstracts as well as the parallel definitions to create a paralle corproa 
	public static void catFiles(List<String> whatToCat, String whereToOutput){
		try{

			PrintWriter out = new PrintWriter(new FileWriter(new File(whereToOutput)));
			int i = 0;
			for (String input : whatToCat){
				BufferedReader buf = new BufferedReader(new FileReader(input));
				String line;

				while ( (line = buf.readLine() ) != null ) 
				{
					i++;
					out.println(line);
				}
				buf.close();
				out.flush();

			}
			out.flush();
			System.out.println("\t\tWordcout for " + whereToOutput +  ": [" + i + "]");
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}

	
	
	/**
	 * @param filename, The file to be counted
	 * @return An <code>int</code> that represents the number of lines in the file.
	 * @throws IOException The file cannot be found 
	 */
	private static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        while ((readChars = is.read(c)) != -1) {
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n')
	                    ++count;
	            }
	        }
	        return count;
	    } finally {
	        is.close();
	    }
	}

}
