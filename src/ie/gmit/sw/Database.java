package ie.gmit.sw;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Conor Shortt
 * @version 1.0
 * @since 1.8
 * 
 * The database class is the blueprint for databases used
 * for the subject Wili text file and also the query text
 * file. When these files are parsed to ngrams the ngrams
 * are added to the database. This class contains multiple
 * methods for manipulating the map to show the top 300
 * language entries and most frequent ngrams.
 */
public class Database {
	private Map<Language, Map<Integer, LanguageEntry>> db = new ConcurrentHashMap<>();
	
	/**
	 * The add method takes in a {@link CharSequence} and a {@link Language} variable.
	 * The {@link CharSequence} is converted to a hashCode and then
	 * added to the database after acquiring the current frequency of the kmer.
	 * 
	 * @param s {@link CharSequence} for converting to hashcode to add to db
	 * @param lang language used to acquire {@link LanguageEntry}
	 */
	public void add(CharSequence s, Language lang) {
		int kmer = s.hashCode();
		Map<Integer, LanguageEntry> langDb = getLanguageEntries(lang);
		
		int frequency = 1;
		if (langDb.containsKey(kmer)) {
			frequency += langDb.get(kmer).getFrequency();
		}
		langDb.put(kmer, new LanguageEntry(kmer, frequency));
		
	}
	/**
	 * The getLanguageEntries method takes in a language parameter,
	 * and returns the map of all language entries relating to that
	 * language.
	 * 
	 * @param lang the language you want to acquire entries for.
	 * @return returns the map of LanguageEntries in relation to the language.
	 */
	private Map<Integer, LanguageEntry> getLanguageEntries(Language lang){
		Map<Integer, LanguageEntry> langDb = null; 
		if (db.containsKey(lang)) {
			langDb = db.get(lang);
		}else {
			langDb = new ConcurrentHashMap<Integer, LanguageEntry>();
			db.put(lang, langDb);
		}
		return langDb;
	}
	/**
	 * Resizes the database of top entries acquired from the getTop method.
	 * 
	 * @param max to amount of entries to keep.
	 */
	public void resize(int max) {
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			Map<Integer, LanguageEntry> top = getTop(max, lang);
			db.put(lang, top);
		}
	}
	
	/**
	 * Gets the top entries in the database. Makes a temp map and arraylist,
	 * Then sorts the arraylist and puts each entry in the temp map.
	 * Temp is then returned to the caller of the method.
	 * 
	 * @param max the amount of entires to keep
	 * @param lang the language of the database.
	 * @return returns the map of top entries.
	 */
	public Map<Integer, LanguageEntry> getTop(int max, Language lang) {
		Map<Integer, LanguageEntry> temp = new ConcurrentHashMap<>();
		List<LanguageEntry> les = new ArrayList<>(db.get(lang).values());
		Collections.sort(les);
		
		int rank = 1;
		for (LanguageEntry le : les) {
			le.setRank(rank);
			temp.put(le.getKmer(), le);			
			if (rank == max) break;
			rank++;
		}
		
		return temp;
	}
	/**
	 * Gets the language associated with the query.
	 * 
	 * @param query map of kmers associated with the query text file.
	 * @return the language of the query file.
	 */
	public Language getLanguage(Map<Integer, LanguageEntry> query) {
		TreeSet<OutOfPlaceMetric> oopm = new TreeSet<>();
		
		Set<Language> langs = db.keySet();
		for (Language lang : langs) {
			oopm.add(new OutOfPlaceMetric(lang, getOutOfPlaceDistance(query, db.get(lang))));
		}
		return oopm.first().getLanguage();
	}
	
	/**
	 * Gets the out of place distance of the query kmers and the subject kmers.
	 * 
	 * @param query the query map.
	 * @param subject the subject map.
	 * @return returns and integer of the distance between the two maps.
	 */
	private int getOutOfPlaceDistance(Map<Integer, LanguageEntry> query, Map<Integer, LanguageEntry> subject) {
		int distance = 0;
		
		Set<LanguageEntry> les = new TreeSet<>(query.values());		
		for (LanguageEntry q : les) {
			LanguageEntry s = subject.get(q.getKmer());
			if (s == null) {
				distance += subject.size() + 1;
			}else {
				distance += s.getRank() - q.getRank();
			}
		}
		return distance;
	}
	
	/**
	 * @author Conor Shortt
	 * @version 1.0
	 * @since 1.8
	 * 
	 * The metric class for calculating out of place distance.
	 * Implements {@link Comparable} to compare distances.
	 */
	private class OutOfPlaceMetric implements Comparable<OutOfPlaceMetric>{
		private Language lang;
		private int distance;
		
		/**
		 * Constructor for the out of place metric.
		 * 
		 * @param lang language of the map.
		 * @param distance the distance between entries.
		 */
		public OutOfPlaceMetric(Language lang, int distance) {
			super();
			this.lang = lang;
			this.distance = distance;
		}
		/**
		 * @return returns the language.
		 */
		public Language getLanguage() {
			return lang;
		}
		/**
		 * @return returns absolute distance.
		 */
		public int getAbsoluteDistance() {
			return Math.abs(distance);
		}
		/**
		 * Compares distances
		 * @param o {@link OutOfPlaceMetric} to compare another metric with the current one.
		 */
		@Override
		public int compareTo(OutOfPlaceMetric o) {
			return Integer.compare(this.getAbsoluteDistance(), o.getAbsoluteDistance());
		}
		/**
		 * Outputs a formatted version of the distance.
		 */
		@Override
		public String toString() {
			return "[lang=" + lang + ", distance=" + getAbsoluteDistance() + "]";
		}
		
	}
	
	/**
	 * Outputs a formatted of the database.
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		int langCount = 0;
		int kmerCount = 0;
		Set<Language> keys = db.keySet();
		for (Language lang : keys) {
			langCount++;
			sb.append(lang.name() + "->\n");
			 
			 Collection<LanguageEntry> m = new TreeSet<>(db.get(lang).values());
			 kmerCount += m.size();
			 for (LanguageEntry le : m) {
				 sb.append("\t" + le + "\n");
			 }
		}
		sb.append(kmerCount + " total k-mers in " + langCount + " languages"); 
		return sb.toString();
	}
}