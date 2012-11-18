package utils;
//
import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;
/**
 * This filter turns an XML Wiki article into 
 *
 * The number of processed articles is limited by a maximum counter
 *
 */
public class Wiki2Txt implements IArticleFilter {

	int max = 20000;
	int i = 0;
	static PrintWriter wrt;
	static Pattern p;



	/* (non-Javadoc)
	 * @see info.bliki.wiki.dump.IArticleFilter#process(info.bliki.wiki.dump.WikiArticle, info.bliki.wiki.dump.Siteinfo)
	 */
	public void process(WikiArticle article, Siteinfo siteinfo) throws SAXException {
		if (i > max)
			throw new SAXException("Next article");
		if ( (i % 5000) == 0)
			System.out.println(++i);
		//pull text from article
		String plainStr, fullText;
		WikiModel wikiModel = new WikiModel("${image}", "${title}");
		wikiModel.setUp();
		fullText = article.getText();


		//run converter to put in plain text
		plainStr = wikiModel.render(new PlainTextConverter(), fullText);

		//replace all {{ }} junk that doesn't get removed for some reason?
		Matcher m = p.matcher(plainStr);
		String clean =  m.replaceAll("");

		boolean printed = false;
		//only print lines that have enough data
		String[] lines = clean.split("\n");
		for (String s : lines){
			String [] words = s.split(" ");
			if (words.length > 10){
				wrt.println(s);
				printed = true;
			}
		}
		if (printed){
			i++;
		}
		wikiModel.tearDown();
	}




	/**
	 * Takes an XML representation of Wikipedia for a specificl anguage as input, and parses it into plain text. 
	 */
	public static void Wiki2Lm(String lang) {
		try{
			JAlignFilePaths j = JAlignFilePaths.getInstance();

			File LM = new File(j.getLMPath(lang));
			File Wiki = new File(j.getWikiPath(lang));
			if (!Wiki.exists()){
				throw new IOException ("No Wiki");
			}
			if (LM .exists()){
				throw new IOException ("LM Already Made");
			}
			IArticleFilter filter = new Wiki2Txt();
			System.out.println("Creating LM");
			WikiXMLParser parser;
			wrt = new PrintWriter(new FileWriter(LM));
			p = Pattern.compile("\\{\\{.*\\}\\}");

			parser = new WikiXMLParser(j.getWikiPath(lang), filter);
			parser.parse();
		}
		catch(SAXException e){
			System.out.println("Wiki creation failed");
			System.out.println(e.getMessage());
		}
		catch(IOException e){
			System.out.println("Wiki creation failed");
			System.out.println(e.getMessage());
		}
	}


}
