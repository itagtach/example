package task2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Random;

public class RMIClient {
	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 10;
	private static final int GRAPH_EDGES = 12;
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
		createNodes(GRAPH_NODES);
		connectSomeNodes(GRAPH_EDGES);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "GraphSeacher";
            Registry registry = LocateRegistry.getRegistry("localhost");
            GraphSearcher gs = (GraphSearcher) registry.lookup(name);
            Map<Node, Map<Node, Integer>> answer = gs.searchBenchmark(nodes.length, nodes);
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
