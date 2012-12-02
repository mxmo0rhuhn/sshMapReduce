package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Die Registry ist der zentrale Zugriffspunkt zum Instanzieren von Bindings/Objekten, die von Guice verwaltet werden.
 * Da die Registry über statische Methoden aufgerufen wird, wird keine Instanz benötigt.
 * 
 * Diese zentrale Registry wird verwendet, da wir keine Abhängigkeit auf das Dependency Injection (DI) -Framework haben
 * wollen. Somit braucht die Applikation nur diese Registry zu kennen während unter der Haube das DI-Framework
 * transparent ausgewechselt werden kann. Um unabhängig vom verwendeten DI-Framework zu sein, sollten nur Standard
 * Java-Annotation verwendet werden. Diese befinden sich typischerweise in einem Package javax.*
 * 
 * @author Reto
 * 
 */
public final class Registry {

	private final static Registry INSTANCE = new Registry();

	/**
	 * Referenz zum Guice Injector
	 */
	private final Injector injector;

	/**
	 * Zugriff nur über statische Methoden
	 */
	private Registry() {
		// Erstellt einen neuen Guice Injector mit der Standard Config.
		this.injector = Guice.createInjector(new MapReduceConfig());
	}

	/**
	 * Gibt eine Instanz für den angeforderten Typen zurück. Der Scope der Instanz (z.B. Singleton, Prototype, usw) wird
	 * vom DI-Framework verwaltet.
	 * 
	 * @param Interface
	 *            oder Klasse, für die ein Binding existiert.
	 * @return eine Instanz vom Typ
	 */
	public static <T> T getComponent(Class<T> type) {
		return INSTANCE.injector.getInstance(type);
	}
}