package ie.gmit.sw;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Conor Shortt
 * @version 1.0
 * @since 1.8
 * 
 * The parser class <b>parses</b> the data of the subject file and query file.
 * It implements {@link Runnable} which is an interface requiring the class to contain the run method.
 *
 */
public class Parser implements Runnable {

	private Database db = null;
	private String file;
	private int k;
	Map<Integer, LanguageEntry> query = new TreeMap<Integer, LanguageEntry>();

	/**
	 * A constructor for the Parser class.
	 * 
	 * @param file name of file to be parsed.
	 * @param k size of kmers.
	 */
	public Parser(String file, int k) {
		this.file = file;
		this.k = k;
	}
	/**
	 * @param db to set the current database.
	 */
	public void setDb(Database db) {
		this.db = db;
	}
	
	/**
	 * Run method which contains a buffered reader, which reads the file.
	 * Read in line by line, each line is trimmed into two strings, and passed to the parse method.
	 */
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;

			while ((line = br.readLine()) != null) {
				String[] record = line.trim().split("@");
				if (record.length != 2)
					continue;
				parse(record[0], record[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Parse method, which recieves 2 parameters.
	 * The {@link String} text is looped through and broken into kmers.
	 * The language {@link String} is set to a language object.
	 * These are both added to the {@link Database} using <code>db.add</code>
	 * 
	 * @param text the line of text to be parsed to kmers.
	 * @param lang the language of the text.
	 */
	private void parse(String text, String lang) {
		Language language = Language.valueOf(lang);

		for (int i = 0; i <= text.length() - k; i++) {
			CharSequence kmer = text.substring(i, i + k);
			db.add(kmer, language);
		}
	}
	/**
	 * The readQueryFile method takes in a String as a parameter which
	 * is used as a file location by the {@link BufferedReader}. This
	 * file is read in line by line and sends Strings to analyseQuery.
	 * After finishing the while loop and populating the {@link Database},
	 * The language is pulled from the {@link Database} using <code>db.getLanguage</code>
	 * 
	 * @param queryFile the query file to be parsed.
	 * @throws FileNotFoundException if file is not found at queryFile location.
	 */
	public void readQueryFile(String queryFile) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile)));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				analyseQuery(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Language language = db.getLanguage(query);
		System.out.println("Language = " + language);
	}
	/**
	 * The analyseQuery method recieves a {@link String} as a parameter.
	 * This {@link String} is parsed into kmers using <code>s.substring</code>
	 * and subsequently converted to an {@link Integer} using <code>kmer.hashCode();</code>
	 * The hashKmer and frequency are now put into the query database.
	 * @param s the {@link String} to be parsed.
	 */
	public void analyseQuery(String s) {
		
		for (int i = 0; i <= s.length() - k; i++) { 
			CharSequence kmer = s.substring(i, i + k);
			int hashKmer = kmer.hashCode();
			//Using the below method to get frequency was less accurate than simply using i.
			/*if(query.containsKey(hashKmer)) {
				frequency += query.get(hashKmer).getFrequency();
			}*/
			query.put(hashKmer, new LanguageEntry(hashKmer, (i+1)));
		}
	}

}
