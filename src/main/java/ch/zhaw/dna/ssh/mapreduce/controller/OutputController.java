package ch.zhaw.dna.ssh.mapreduce.controller;

import java.io.IOException;

import ch.zhaw.dna.ssh.mapreduce.view.util.ConsoleOutput;
import ch.zhaw.dna.ssh.mapreduce.view.util.OutputInterface;
import ch.zhaw.dna.ssh.mapreduce.view.util.FileOutput;

/**
 * Dieses Interface regelt den Output des Projektes. Es ist möglich Logfiles auf einer Konsole auszugeben oder in ein File zu schreiben.
 * 
 * @author Max
 * 
 */
public class OutputController {

	private OutputInterface curOutputStrategy = new ConsoleOutput();

	public static enum OUTPUT_STRATEGY {
		TEXTFILE, CONSOLE
	}

	/**
	 * Stellt das derzeitige Output Interface ein.
	 * 
	 * @param outMethod
	 *            das gewünschte User-Interface
	 */
	public void setOutput(OUTPUT_STRATEGY outMethod) {
		switch (outMethod) {
		case TEXTFILE:
			curOutputStrategy = new FileOutput();
			break;
		case CONSOLE:
		default:
			curOutputStrategy = new ConsoleOutput();
			break;
		}
	}

	/**
	 * Schreibt eine Zeile in das derzeitige Output Interface
	 * 
	 * @param outString
	 *            die Zeile die ausgegeben werden soll
	 * @throws IOException
	 */
	public void println(String outString) throws IOException {
		curOutputStrategy.println(outString);
	}
}
