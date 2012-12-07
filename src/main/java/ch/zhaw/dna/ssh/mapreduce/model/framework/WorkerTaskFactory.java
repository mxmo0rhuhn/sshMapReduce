package ch.zhaw.dna.ssh.mapreduce.model.framework;

import com.google.inject.assistedinject.Assisted;

/**
 * Diese Factory wird zum erstellen von {@link MapWorkerTask}s und {@link ReduceWorkerTask}s benützt.
 * 
 * Für dieses Interface existiert keine Implemtnation, da sie über die Guice-Extension 'AssistedInject' verwendet wird.
 * 
 * @author Reto
 * 
 */
public interface WorkerTaskFactory {

	/**
	 * Erstellt eine neue Instanz vom MapWorkerTask mit den übergebenen Parametern. Wenn der Konstruktor der konkreten
	 * Implemtation mehr Parameter hat als hier angegeben, werden diese von Guice injected.
	 * 
	 * @param mapReduceTaskUUID
	 *            die ID der MapReduce Berechnung, zu der dieser Task gehört
	 * @param mapInstr
	 *            die zu verwendende MapInstruction
	 * @param combinerInstr
	 *            die zu verwendende CombinerInstruction
	 * @return eine neue Instanz eines MapWorkerTask
	 */
	MapWorkerTask createMapWorkerTask(@Assisted("uuid") String mapReduceTaskUUID, MapInstruction mapInstr,
			CombinerInstruction combinerInstr);

	/**
	 * Erstellt eine neue Instanz vom ReduceWorkerTask mit den übergebenen Parametern. Wenn der Konstruktor der
	 * konkreten Implemtation mehr Parameter hat als hier angegeben, werden diese von Guice injected.
	 * 
	 * Da zwei Parameter vom Typ String sind, müssen diese in der @Assisted Annotation näher beschrieben werden. So
	 * werden Verwechslungen vermieden.
	 * 
	 * @param mapReduceTaskUUID
	 *            die ID der MapReduce Berechnung, zu der dieser Task gehört
	 * @param key
	 *            der Key, für den reduziert wird
	 * @param reduceInstr
	 *            die zu verwendenden ReduceInstruction
	 * @return eine neue Instanz eines ReduceWorkerTask
	 */
	ReduceWorkerTask createReduceWorkerTask(@Assisted("uuid") String mapReduceTaskUUID, @Assisted("key") String key,
			ReduceInstruction reduceInstr);
}
