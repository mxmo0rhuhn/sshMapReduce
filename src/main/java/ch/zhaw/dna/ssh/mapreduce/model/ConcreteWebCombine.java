package ch.zhaw.dna.ssh.mapreduce.model;

public interface ConcreteWebCombine {
	
	/**
	 * Methode um Informationen zu kombinieren, verhält sich unterschiedlich je nach Input (prüft auf was erhalten wurde)
	 * @param combinableText
	 * @return String mit kombiniertenInfos
	 */
	public String goCombineInformation(String combinableText);

}
