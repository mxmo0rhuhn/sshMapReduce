package ch.zhaw.dna.ssh.mapreduce.model;

public interface ConcreteWebMap{
	

	/**
	 * Durchsucht Webseite nach <p> Tags
	 * Info: eventuell nicht nötig, bei nicht Bedarf entfernen
	 * @param searchableText übergibt zu durchsuchenden Text
	 * @return String mit gefundenen Tags
	 */
	public String getContentPTags(String searchableText);
	/**
	 * Druchsucht Webseite nach <q> Tags
	 * Info: eventuell nicht nötig, bei nicht Bedarf entfernen
	 * @param searchableText übergibt zu durchsuchenden Text
	 * @return String mit gefundenen Tags
	 */
	public String getContentQTags(String searchableText);
	
	/**
	 * Durchsucht Webseite nach Strukturmerkmalen und erkennt dadurch die Tiefe
	 * hint: http://www.java2s.com/Code/Java/XML/TraversetheDOMtreeusingTreeWalker.htm
	 * @param searchableText
	 * @return int mit Anzahl Tiefen
	 */
	public int getContentDepth(String searchableText);
	
	/**
	 * Durchsucht Webseite nach <a> Tags (Links auf andere Webseiten)
	 * @return Link auf andere Seite
	 * @return String mit gefundenen Tags
	 */
	public String getContentATags(String searchableText);
	

}
