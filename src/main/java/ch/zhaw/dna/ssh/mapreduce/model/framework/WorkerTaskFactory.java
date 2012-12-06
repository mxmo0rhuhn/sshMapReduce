package ch.zhaw.dna.ssh.mapreduce.model.framework;

import com.google.inject.assistedinject.Assisted;

public interface WorkerTaskFactory {

	MapWorkerTask createMapWorkerTask(@Assisted("uuid") String mapReduceTaskUUID, MapInstruction mapInstr,
			CombinerInstruction combinerInstr);

	ReduceWorkerTask createReduceWorkerTask(@Assisted("uuid") String uuid, @Assisted("key") String key,
			ReduceInstruction reduceInstr);

}
