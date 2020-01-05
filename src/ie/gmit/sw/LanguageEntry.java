package ie.gmit.sw;

/**
 * @author Conor Shortt
 * @version 1.0
 * @since 1.8
 * 
 * The class {@link LanguageEntry} has three instance variables,
 * kmer, frequency, and rank. The class implements Comparable
 * to compare one {@link LanguageEntry} to another.
 * 
 */
public class LanguageEntry implements Comparable<LanguageEntry> {
	private int kmer;
	private int frequency;
	private int rank;

	/**
	 * A constructor for LanguageEntry.
	 * 
	 * @param kmer for substrings of a line.
	 * @param frequency to calculate recurring kmers.
	 */
	public LanguageEntry(int kmer, int frequency) {
		super();
		this.kmer = kmer;
		this.frequency = frequency;
	}
	/**
	 * @return kmer for LanguageEntry.
	 */
	public int getKmer() {
		return kmer;
	}
	/**
	 * @param kmer sets kmer.
	 */
	public void setKmer(int kmer) {
		this.kmer = kmer;
	}
	/**
	 * @return frequency returns frequency of kmer
	 */
	public int getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency sets frequency of kmer
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return rank returns rank of kmer with respect to frequency
	 */
	public int getRank() {
		return rank;
	}
	/**
	 * @param rank sets rank of kmer with respect to frequency
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * @param next LanguageEntry to compare frequency of LanguageEntries
	 * @return int returns rank of frequency
	 */
	@Override
	public int compareTo(LanguageEntry next) {
		return - Integer.compare(frequency, next.getFrequency());
	}
	/**
	 * @return String prints out kmer, frequency, and rank of {@link LanguageEntry}
	 */
	@Override
	public String toString() {
		return "[" + kmer + "/" + frequency + "/" + rank + "]";
	}
}