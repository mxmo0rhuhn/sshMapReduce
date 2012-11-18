package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

/**
 * Am Anfang steht jeweils ein (potenziell) sehr langer String. Dieser muss in verschiedene kuerzere Strings aufgeteilt
 * werden. Hierzu kann ein InputSplitter verwendet werden. Der InputerSplitter funktioniert nach dem Iterator-Pattern.
 * 
 * @author Reto
 * 
 */
public interface InputSplitter extends Iterator<String> {

}
