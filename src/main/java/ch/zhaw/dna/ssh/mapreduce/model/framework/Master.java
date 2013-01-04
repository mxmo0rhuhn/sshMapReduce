package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.MapReduceTaskUUID;

public final class Master {

	private final Map<String, Collection<String>> globalResultStructure = new HashMap<String, Collection<String>>();

	private final String mapReduceTaskUUID;

	private final WorkerTaskFactory runnerFactory;

	@Inject
	public Master(WorkerTaskFactory runnerFactory, @MapReduceTaskUUID String mapReduceTaskUUID) {
		this.runnerFactory = runnerFactory;
		this.mapReduceTaskUUID = mapReduceTaskUUID;
	}

	public Map<String, Collection<String>> runComputation(final MapInstruction mapInstruction,
			final CombinerInstruction combinerInstruction,
			final ReduceInstruction reduceInstruction, Iterator<String> input) {

		// Die übersetzung welche ID welchen Input hat
		Map<String, String> taskIDMapping = new HashMap<String, String>();

		// Eine Sammelung aus IDs von ausstehenden Tasks und wer sie erledigen will.
		// TODO das ist noch falsch: später soll dies keine 1-1 Relation sein
		Map<String, MapWorkerTask> undoneTasks = new HashMap<String, MapWorkerTask>();

		// Alle Worker, die Ergebnisse besitzen
		Set<Worker> mapResults = new HashSet<Worker>();

		// reiht für jeden Input - Teil einen MapWorkerTask in den Pool ein
		while (input.hasNext()) {
			MapWorkerTask mapTask = runnerFactory.createMapWorkerTask(mapReduceTaskUUID,
					mapInstruction, combinerInstruction);

			String inputUID = UUID.randomUUID().toString();
			String todo = input.next();

			undoneTasks.put(inputUID, mapTask);
			taskIDMapping.put(inputUID, todo);
			mapTask.runMapInstruction(inputUID, todo);
		}
		// Eine Sammelung aus IDs von erledigten Tasks
		List<String> doneTasks = new ArrayList<String>(undoneTasks.size());

		// Fragt alle MapWorker Tasks an ob sie bereits erledigt sind - bis sie erledigt sind ...
		do {
			updateDoneMappers(doneTasks, mapResults, undoneTasks, taskIDMapping);
			// Wartet eine Sekunde abzüglich der Prozentzahl an bereits erledigten Aufgaben
			try {
				wait(1000 - 1000 * (doneTasks.size() / (taskIDMapping.size() + 1)));
			} catch (InterruptedException e) {
				// ... das war nicht gut irgendwas war
				System.err.println("Exiting ... interupted");
				return Collections.emptyMap();
			}

		} while (undoneTasks.size() > 0);

		// INSERT SOME SHUFFELING HERE!
		Map<String,List<KeyValuePair>> magicShuffleStructure
		
		 for(Worker curMapResult : mapResults) {
		 }
		
		for(Map.Entry<String, List<String>> keyValuePairs : magicShuffleStructure.entrySet()) {
			 ReduceWorkerTask reduceTask = runnerFactory.createReduceWorkerTask(mapReduceTaskUUID, keyValuePairs.getKey(), reduceInstruction);
			reduceTask.runReduceTask(keyValuePairs.getValue());
		}

		return globalResultStructure;
	}

	// /**
	// * Fuegt das resultat fuer den Key dem globalen Resultat hinzu (thread-safe).
	// *
	// * @param key
	// * Key fuer das Resultat. Es kann mehere Resultate fuer einen Key geben
	// * @param result
	// * das Resultate fuer den Key
	// */
	// public void globalResultStructureAppend(String key, String result) {
	// if (!this.globalResultStructure.containsKey(key)) {
	// this.globalResultStructure.putIfAbsent(key, new ConcurrentLinkedQueue<String>());
	// }
	// Collection<String> res = this.globalResultStructure.get(key);
	// res.add(result);
	// }

	/**
	 * Retourniert die globale Resultat-Struktur, wo alle Resultate gespeichert werden.
	 * 
	 * @return Map mit allen Resultaten
	 */
	public Map<String, Collection<String>> getGlobalResultStructure() {
		return Collections.unmodifiableMap(this.globalResultStructure);
	}

	// /**
	// * Iteriert ueber alle worker und prueft, ob sie fertig sind.
	// *
	// * @param workers
	// * @return true, wenn alle fertig sind, sonst false.
	// */
	// private boolean allWorkerTasksCompleted(Collection<? extends WorkerTask> workers) {
	// for (WorkerTask worker : workers) {
	// if (worker.getCurrentState() != State.COMPLETED) {
	// return false;
	// }
	// }
	// return true;
	// }

	private void updateDoneMappers(List<String> doneTasks, Set<Worker> mapResults,
			Map<String, MapWorkerTask> undoneTasks, Map<String, String> taskIDMapping) {
		// Schauen welche Tasks noch ausstehend sind
		for (String todoID : taskIDMapping.keySet()) {
			if (undoneTasks.containsKey(todoID)) {
				switch (undoneTasks.get(todoID).getCurrentState()) {
				case COMPLETED:
					doneTasks.add(todoID);
					mapResults.add(undoneTasks.get(todoID).getWorker());
					undoneTasks.remove(todoID);

				case FAILED:

					// Falls es diesen Status überhaupt gibt

				}
			}
		}
	}

	public String getMapReduceTaskUUID() {
		return this.mapReduceTaskUUID;
	}
}
