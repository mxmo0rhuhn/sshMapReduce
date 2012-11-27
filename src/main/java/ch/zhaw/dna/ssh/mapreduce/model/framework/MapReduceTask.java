package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.Registry;

/**
 * Dies ist ein neuer MapReduce Task. Er ist für seine Worker der Master.
 * 
 * @author Max, Reto, Desiree
 */
public final class MapReduceTask {

	private final MapTask mapTask;

	private final ReduceTask reduceTask;

	private final CombinerTask combinerTask;

	private final Master master;

	/**
	 * Erstellt einen neuen MapReduceTask mit den übergebenen map und reduce tasks.
	 * 
	 * @param mapTask
	 *            der Map-Task
	 * @param reduceTask
	 *            der Reduce-Task
	 */
	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask, CombinerTask combinerTask) {
		this.mapTask = mapTask;
		this.reduceTask = reduceTask;
		this.combinerTask = combinerTask;

		this.master = Registry.getComponent(Master.class);
	}

	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this(mapTask, reduceTask, null);
	}

	/**
	 * Wendet auf alle Elemente vom übergebenen Iterator (via next) den Map- und Reduce-Task an. Die Methode blockiert,
	 * bis alle Aufgaben erledigt sind. Es wird über den Iterator iteriert und für jeden Aufruf von
	 * {@link Iterator#next()} ein Map-Task abgesetzt (asynchron). Aus diesem Grund könnten im Iterator die Werte auf
	 * lazy generiert werden.
	 * 
	 * @param inputs
	 *            der gesamte input als Iterator
	 * @return das Resultat von dem ganzen MapReduceTask
	 */
	public Map<String, Collection<String>> compute(Iterator<String> input) {
		return this.master.runComputation(this.mapTask, this.combinerTask, this.reduceTask, input);
	}
}
