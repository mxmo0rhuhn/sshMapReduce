package 	;

public class WordFrequencyReduceTask implements Reducer{
	
	public void reduce(MapRunner mapRunner, String[] todo) {
		//Iterate over all entries with the same key and add the values
		long value = 0;
		
		for(String word : todo) {
			value +=  Long.parseLong(word);
		}
		System.out.println(Long.toString(value));
		
		Pool.registerReduce(mapRunner);
	}
	
	

}
