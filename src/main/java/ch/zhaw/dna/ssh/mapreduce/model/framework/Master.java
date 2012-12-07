package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

	// Die übersetzung welche ID welchen Input hat
	private Map<String, String> taskIDMapping;

	// Eine Sammelung aus IDs von ausstehenden Tasks und wer sie erledigen will.
	// TODO das ist noch falsch: später soll dies keine 1-1 Relation sein
	private Map<String, MapWorkerTask> undoneTasks;

	// Eine Sammelung aus IDs von erledigten Tasks
	private List<String> doneTasks;

	// Alle Worker, die Ergebnisse besitzen
	private Set<Worker> mapResults;

	@Inject
	public Master(WorkerTaskFactory runnerFactory, @MapReduceTaskUUID String mapReduceTaskUUID) {
		this.runnerFactory = runnerFactory;
		this.mapReduceTaskUUID = mapReduceTaskUUID;
	}

	public Map<String, Collection<String>> runComputation(final MapInstruction mapInstruction,
			final CombinerInstruction combinerInstruction, final ReduceInstruction reduceInstruction,
			Iterator<String> input) {

		while (input.hasNext()) {
			MapWorkerTask mapRunner = runnerFactory.createMapWorkerTask(mapReduceTaskUUID, mapInstruction,
					combinerInstruction);

			String inputUID = UUID.randomUUID().toString();
			String todo = input.next();

			undoneTasks.put(inputUID, mapRunner);
			taskIDMapping.put(inputUID, todo);
			mapRunner.runMapInstruction(inputUID, todo);
		}

		do {
			updateDoneMappers();
			// Wartet eine Sekunde abzüglich der Prozentzahl an bereits erledigten Aufgaben
			try {
				wait(1000 - 1000 * (doneTasks.size() / taskIDMapping.size()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (undoneTasks.size() > 0);

		// INSERT SOME SHUFFELING HERE!
		// }
		//
		// while (!allWorkerTasksCompleted(reduceRunners.values())) {
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// Thread.currentThread().interrupt();
		// break;
		// }
		// }
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

	private void updateDoneMappers() {
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
