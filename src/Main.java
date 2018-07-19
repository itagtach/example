import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 1000;
	private static final int GRAPH_EDGES = 2000;

	// How many searches to perform
	private static final int SEARCHES = 50;

	private static Node[] nodes;

	private static Random random = new Random();
	private static Searcher searcher = new SearcherImpl();

	/**
	 * Creates nodes of a graph.
	 * 
	 * @param howMany
	 */
	public static void createNodes(int howMany) {
		nodes = new Node[howMany];

		for (int i = 0; i < howMany; i++) {
			nodes[i] = new NodeImpl();
		}
	}

	/**
	 * Creates a fully connected graph.
	 */
	public void connectAllNodes() {
		for (int idxFrom = 0; idxFrom < nodes.length; idxFrom++) {
			for (int idxTo = idxFrom + 1; idxTo < nodes.length; idxTo++) {
				nodes[idxFrom].addNeighbor(nodes[idxTo]);
				nodes[idxTo].addNeighbor(nodes[idxFrom]);
			}
		}
	}

	/**
	 * Creates a randomly connected graph.
	 * 
	 * @param howMany
	 */
	public static void connectSomeNodes(int howMany) {
		for (int i = 0; i < howMany; i++) {
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			nodes[idxFrom].addNeighbor(nodes[idxTo]);
		}
	}

	/**
	 * Runs a quick measurement on the graph.
	 * 
	 * @param howMany
	 */
	//
	public static Map<Node, Map<Node, Integer>> searchBenchMark(int howMany,Node[] nodes){
		// Display measurement header.
		//System.out.printf("%7s %8s %13s %13s\n", "Attempt", "Distance", "Time", "TTime");
		Map<Node, Map<Node, Integer>> theMap = new HashMap<Node, Map<Node, Integer>>();

		for(int h = 0; h < howMany; h++){
			Map<Node, Integer> tempMap = new HashMap<Node, Integer>();
			for (int i = 0; i < howMany; i++) {
				
				// Select two random nodes.
				final int idxFrom = h;
				final int idxTo = i;

				// Calculate distance, measure operation time
				//final long startTimeNs = System.nanoTime();
				final int distance = searcher.getDistance(nodes[idxFrom], nodes[idxTo]);
				//final long durationNs = System.nanoTime() - startTimeNs;

				// Calculate transitive distance, measure operation time
				//final long startTimeTransitiveNs = System.nanoTime();
				final int transitiveDistance = searcher.getTransitiveDistance(4, nodes[idxFrom], nodes[idxTo]);
				//final long transitiveDurationNs = System.nanoTime() - startTimeTransitiveNs;

				if (distance != transitiveDistance) {
					System.out.printf("Standard and transitive algorithms inconsistent (%d != %d)\n", distance,
							transitiveDistance);
				} else {
					// Print the measurement result.
					//System.out.printf("%7d %8d %13d %13d\n", i, distance, durationNs / 1000, transitiveDurationNs / 1000);
					tempMap.put(nodes[i],distance);
				}
			}
			theMap.put(nodes[h], tempMap);
		}
		return theMap;
		
	}

	public static void main(String[] args) {
		// Create a randomly connected graph and do a quick measurement.
		// Consider replacing connectSomeNodes with connectAllNodes to verify that all distances are equal to one.
		createNodes(GRAPH_NODES);
		connectSomeNodes(GRAPH_EDGES);
		System.out.println(searchBenchMark(SEARCHES,nodes));
		
	}
}
