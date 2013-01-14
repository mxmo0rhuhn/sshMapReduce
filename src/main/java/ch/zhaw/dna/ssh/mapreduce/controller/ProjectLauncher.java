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
		new ProjectLauncher();
	}
	
	/**
	 * Startet die Applikation mit 6 Workern.
	 */
	public ProjectLauncher() {
		Pool pool = Registry.getComponent(Pool.class);
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));
		pool.donateWorker(Registry.getComponent(Worker.class));

		OutputController out = new OutputController();
		WebCrawler currentWebCrawler = new WebCrawler();
		new MainFrame(out, currentWebCrawler);
	}

}
