package ch.zhaw.dna.ssh.mapreduce.controller;

import ch.zhaw.dna.ssh.mapreduce.model.WebCrawler;
import ch.zhaw.dna.ssh.mapreduce.view.MainFrame;
import ch.zhaw.mapreduce.MapReduceFactory;

/**
 * Diese Klasse startet die sshMapReduce Applikation * @author Max
 * 
 */
public class ProjectLauncher {

	/**
	 * Startet die Applikation und ruft die benötigten Aufgaben
	 * 
	 * @param args
	 *            die nicht beachteten Übergabeparameter
	 */
	public static void main(String[] args) {
		new ProjectLauncher();
	}

	/**
	 * default konstruktor
	 */
	public ProjectLauncher() {
		MapReduceFactory.getMapReduce().start();

		OutputController out = new OutputController();
		WebCrawler currentWebCrawler = new WebCrawler();
		MainFrame main = new MainFrame(out, currentWebCrawler);
		currentWebCrawler.addObserver(main);

		MapReduceFactory.getMapReduce().stop();
	}
}