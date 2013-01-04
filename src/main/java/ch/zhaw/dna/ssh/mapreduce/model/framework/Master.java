package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
		// MAP
		// reiht für jeden Input - Teil einen MapWorkerTask in den Pool ein
		while (input.hasNext()) {
			MapWorkerTask mapTask = runnerFactory.createMapWorkerTask(mapReduceTaskUUID,
					mapInstruction, combinerInstruction);

			String inputUID = UUID.randomUUID().toString();
			String todo = input.next();

			undoneTasks.put(inputUID, mapTask);
			taskIDMapping.put(inputUID, todo);
			mapTask.runMapInstruction();
		}
		List<String> doneTasks = new ArrayList<String>(undoneTasks.size());

		try {
			waitForWorkers(doneTasks, mapResults, undoneTasks, taskIDMapping);
		} catch (InterruptedException e) {
			// ... das war nicht gut irgendwas war
			System.err.println("Exiting ... interupted");
			return Collections.emptyMap();
		}

		Map<String, List<KeyValuePair>> magicShuffleStructure = new HashMap<String, List<KeyValuePair>>();

		// SHUFFLE
		for (Worker curMapResult : mapResults) {
			for (KeyValuePair curKeyValuePair : curMapResult
					.getStoredKeyValuePairs(mapReduceTaskUUID)) {
				if (magicShuffleStructure.containsKey(curKeyValuePair.getKey())) {
					magicShuffleStructure.get(curKeyValuePair.getKey()).add(curKeyValuePair);
				} else {
					List<KeyValuePair> newKeyValueList = new LinkedList<KeyValuePair>();
					newKeyValueList.add(curKeyValuePair);
					magicShuffleStructure.put(curKeyValuePair.getKey(), newKeyValueList);
				}

				runnerFactory.createMapWorkerTask(mapReduceTaskUUID, mapInstruction, combinerInstruction);
				runnerFactory.createReduceWorkerTask(mapReduceTaskUUID, keyValuePairs.getKey(), reduceInstruction, keyValuePairs.getValue());
			}
		}

		// REDUCE
		for (Map.Entry<String, List<KeyValuePair>> keyValuePairs : magicShuffleStructure.entrySet()) {
			ReduceWorkerTask reduceTask = runnerFactory.createReduceWorkerTask(mapReduceTaskUUID,
					keyValuePairs.getKey(), reduceInstruction, keyValuePairs.getValue());
			reduceTask.runReduceTask();
		}

		return globalResultStructure;
	}

	/**
	 * Retourniert die globale Resultat-Struktur, wo alle Resultate gespeichert werden.
	 * 
	 * @return Map mit allen Resultaten
	 */
	public Map<String, Collection<String>> getGlobalResultStructure() {
		return Collections.unmodifiableMap(this.globalResultStructure);
	}

	private void updateDoneWorkers(List<String> doneTasks, Set<Worker> results,
			Map<String, MapWorkerTask> undoneTasks, Map<String, String> taskIDMapping) {
		// Schauen welche Tasks noch ausstehend sind
		for (String todoID : taskIDMapping.keySet()) {
			if (undoneTasks.containsKey(todoID)) {
				switch (undoneTasks.get(todoID).getCurrentState()) {
				case COMPLETED:
					doneTasks.add(todoID);
					results.add(undoneTasks.get(todoID).getWorker());
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

	private void waitForWorkers(List<String> doneTasks, Set<Worker> results,
			Map<String, MapWorkerTask> undoneTasks, Map<String, String> taskIDMapping)
			throws InterruptedException {
		// Fragt alle MapWorker Tasks an ob sie bereits erledigt sind - bis sie erledigt sind ...
		do {
			updateDoneWorkers(doneTasks, results, undoneTasks, taskIDMapping);
			// Wartet eine Sekunde abzüglich der Prozentzahl an bereits erledigten Aufgaben
			// "+1" um die DIV/0 zu verhindern
			wait(1000 - 1000 * (doneTasks.size() / (taskIDMapping.size() + 1)));

		} while (undoneTasks.size() > 0);
	}
}
