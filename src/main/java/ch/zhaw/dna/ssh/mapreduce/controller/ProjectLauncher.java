package ch.zhaw.dna.ssh.mapreduce.controller;

import ch.zhaw.dna.ssh.mapreduce.model.WebCrawler;
import ch.zhaw.dna.ssh.mapreduce.view.MainFrame;
import ch.zhaw.mapreduce.Pool;
import ch.zhaw.mapreduce.Worker;
import ch.zhaw.mapreduce.registry.Registry;

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
		int nworker = args.length == 1 ? Integer.parseInt(args[0]) : Runtime.getRuntime().availableProcessors() + 1;
		ProjectLauncher launcher = new ProjectLauncher();
		launcher.launch(nworker);
	}
	
	/**
	 * default konstruktor
	 */
	public ProjectLauncher() { }
	
	/**
	 * Startet das GUI und gibt dem Pool eine bestimmte Anzahl Worker
	 * @param nworkers
	 */
	public void launch(int nworkers) {
		// TODO do not use registry directly
		Pool pool = Registry.getComponent(Pool.class);
		for (int i = 0; i < nworkers; i++) {
			pool.donateWorker(Registry.getComponent(Worker.class));
		}
		OutputController out = new OutputController();
		WebCrawler currentWebCrawler = new WebCrawler();
		MainFrame main = new MainFrame(out, currentWebCrawler);
		currentWebCrawler.addObserver(main);
	}

}
