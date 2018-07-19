package task3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Random;



public class RMIClient {

	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 10;
	private static final int GRAPH_EDGES = 12;

	// How many searches to perform

	private static Node[] nodes;
	private static Random random = new Random();

	public static void createNodes(int howMany) {
		nodes = new Node[howMany];

		for (int i = 0; i < howMany; i++) {
			nodes[i] = new NodeImpl();
		}
	}

	public void connectAllNodes() {
		for (int idxFrom = 0; idxFrom < nodes.length; idxFrom++) {
			for (int idxTo = idxFrom + 1; idxTo < nodes.length; idxTo++) {
				nodes[idxFrom].addNeighbor(nodes[idxTo]);
				nodes[idxTo].addNeighbor(nodes[idxFrom]);
			}
		}
	}

	public static void connectSomeNodes(int howMany) {
		for (int i = 0; i < howMany; i++) {
			final int idxFrom = random.nextInt(nodes.length);
			final int idxTo = random.nextInt(nodes.length);

			nodes[idxFrom].addNeighbor(nodes[idxTo]);
		}
	}


	public static void main(String[] args) {
		// Create a randomly connected graph and do a quick measurement.
		// Consider replacing connectSomeNodes with connectAllNodes to verify that all distances are equal to one.
		createNodes(GRAPH_NODES);
		connectSomeNodes(GRAPH_EDGES);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
        
        try {
            String name = "GetSeacher";
            Registry registry = LocateRegistry.getRegistry("localhost");
            GetSearcher gs = (GetSearcher) registry.lookup(name);
            GraphSearcher grs= gs.getSearcher();
            Map<Node, Map<Node, Integer>> answer = grs.searchBenchmark(nodes.length, nodes);
            printMap(answer);

        } catch (Exception e) {
            System.err.println("GraphSearcher exception:");
            e.printStackTrace();
        }
	}	
	public static void printMap(Map<Node, Map<Node, Integer>> answer){
		for(int i = 0;i<GRAPH_NODES;i++){
			System.out.println("Distances from Node: " + i);
			for(int j = 0; j < GRAPH_NODES; j++){
				System.out.println("To Node: " + j + " is " + answer.get(nodes[i]).get(nodes[j]));	
			}			
		}
	}
	
}
