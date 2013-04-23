package ch.zhaw.dna.ssh.mapreduce.controller;

import ch.zhaw.dna.ssh.mapreduce.model.WebCrawler;
import ch.zhaw.dna.ssh.mapreduce.view.MainFrame;
import ch.zhaw.mapreduce.ServerStarter;
import ch.zhaw.mapreduce.plugins.thread.ThreadConfig;

/**
 * Diese Klasse startet die Applikation * @author Max
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
		ProjectLauncher launcher = new ProjectLauncher();
		launcher.launch(ThreadConfig.NWORKERS);
	}

	/**
	 * default konstruktor
	 */
	public ProjectLauncher() {
	}

	/**
	 * Startet das GUI und gibt dem Pool eine bestimmte Anzahl Worker
	 * 
	 * @param nworkers
	 */
	public void launch(int nworkers) {
		System.setProperty("mrplugins", "Thread");
		new ServerStarter().start();
		System.out.println("Worker: " + nworkers);

		OutputController out = new OutputController();
		WebCrawler currentWebCrawler = new WebCrawler();
		MainFrame main = new MainFrame(out, currentWebCrawler, nworkers);
		currentWebCrawler.addObserver(main);
	}

}
