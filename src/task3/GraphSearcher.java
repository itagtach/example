package task3;

import java.io.Serializable;
import java.util.Map;

public interface GraphSearcher extends Serializable {
	
	Map<Node, Map<Node,Integer>> searchBenchmark(int howMany,Node[] nodes);

	
}