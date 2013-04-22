package ch.zhaw.dna.ssh.mapreduce.controller;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ch.zhaw.dna.ssh.mapreduce.model.WebCrawler;
import ch.zhaw.dna.ssh.mapreduce.view.MainFrame;
import ch.zhaw.dna.ssh.mapreduce.view.util.SysoFrame;
import ch.zhaw.mapreduce.Pool;
import ch.zhaw.mapreduce.Worker;
import ch.zhaw.mapreduce.plugins.thread.ThreadWorker;
import ch.zhaw.mapreduce.registry.MapReduceConfig;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

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
	public ProjectLauncher() {
	}

	/**
	 * Startet das GUI und gibt dem Pool eine bestimmte Anzahl Worker
	 * 
	 * @param nworkers
	 */
	public void launch(int nworkers) {
		Injector injector = Guice.createInjector(new MapReduceConfig(), new AbstractModule() {
			@Override
			protected void configure() {
				bind(Worker.class).to(ThreadWorker.class);
				bind(Executor.class).toInstance(Executors.newCachedThreadPool());
			}
		});

		// TODO do not use registry directly
		new SysoFrame();
		System.out.println("Worker: " + nworkers);
		Pool pool = injector.getInstance(Pool.class);
		for (int i = 1; i < nworkers; i++) {
			pool.donateWorker(injector.getInstance(Worker.class));
		}
		OutputController out = new OutputController();
		WebCrawler currentWebCrawler = new WebCrawler();
		MainFrame main = new MainFrame(out, currentWebCrawler, nworkers);
		currentWebCrawler.addObserver(main);
	}

}
