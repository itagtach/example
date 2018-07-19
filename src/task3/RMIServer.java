package task3;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class RMIServer extends UnicastRemoteObject implements Searcher,GetSearcher,GraphSearcher{
	protected RMIServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	private static Searcher searcher = new SearcherImpl();
	
	public static void main(String args[]) throws Exception{
		try{
			Registry r = LocateRegistry.getRegistry();
			r.bind("RMIServer", new RMIServer());
			System.out.println("Server running");
		} catch (Exception e){
			
		}
		
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "GetSeacher";
            GetSearcher gs = new RMIServer();
            GetSearcher stub = (GetSearcher) UnicastRemoteObject.exportObject(gs, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("RMIServer bound");
        } catch (Exception e) {
            System.err.println("RMIServer exception:");
            e.printStackTrace();
        }
	}

	public int getDistance(Node from, Node to) {
		// visited keeps the nodes visited in past steps.
		Set<Node> visited = new HashSet<Node>();
		// boundary keeps the nodes visited in current step.
		Set<Node> boundary = new HashSet<Node>();

		int distance = 0;

		// We start from the source node.
		boundary.add(from);

		// Traverse the graph until finding the target node.
		while (!boundary.contains(to)) {
			// Not having anything to visit means the target node cannot be reached.
			if (boundary.isEmpty())
				return (Searcher.DISTANCE_INFINITE);

			Set<Node> traversing = new HashSet<Node>();

			// Nodes visited in current step become nodes visited in past steps.
			visited.addAll(boundary);

			// Collect a set of immediate neighbors of nodes visited in current step.
			for (Node node : boundary)
				traversing.addAll(node.getNeighbors());

			// Out of immediate neighbors, consider only those not yet visited.
			for (Iterator<Node> node = traversing.iterator(); node.hasNext();) {
				if (visited.contains(node.next()))
					node.remove();
			}

			// Make these nodes the new nodes to be visited in current step.
			boundary = traversing;

			distance++;
		}

		return (distance);
	}

	@Override
	public int getTransitiveDistance(int distance, Node from, Node to) {
		// visited keeps the nodes visited in past steps.
		Set<Node> visited = new HashSet<Node>();
		// boundary keeps the nodes visited in current step.
		Map<Node, Integer> boundary = new HashMap<Node, Integer>();

		// We start from the source node.
		boundary.put(from, 0);

		// Traverse the graph until finding the target node.
		while (true) {
			// Not having anything to visit means the target node cannot be reached.
			if (boundary.isEmpty()) {
				return (Searcher.DISTANCE_INFINITE);
			}

			Map<Node, Integer> traversing = new HashMap<Node, Integer>();

			// Collect transitive neighbors of nodes visited in current step
			for (Entry<Node, Integer> currentTuple : boundary.entrySet()) {
				final Node currentNode = currentTuple.getKey();
				final int currentDistance = currentTuple.getValue();
				if (visited.contains(currentNode)) {
					continue;
				}

				Map<Node, Integer> partialGraph = currentNode.getTransitiveNeighbors(distance);

				for (Entry<Node, Integer> searchedTuple : partialGraph.entrySet()) {
					// Check whether the node is already traversed
					final Node searchedNode = searchedTuple.getKey();
					final int dist = currentDistance + searchedTuple.getValue();

					if (traversing.containsKey(searchedNode)) {
						if (dist < traversing.get(searchedNode))
							traversing.put(searchedNode, dist);
					} else {
						traversing.put(searchedNode, dist);
					}
				}

				// Nodes visited in current step become nodes visited in past steps
				visited.add(currentNode);
			}

			for (Entry<Node, Integer> entry : traversing.entrySet()) {
				if (entry.getKey().equals(to)) {
					return entry.getValue();
				}
			}

			boundary = traversing;
		}
	}

	public Map<Node, Map<Node, Integer>> searchBenchmark(int howMany,Node[] nodes){
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

	@Override
	public GraphSearcher getSearcher() throws RemoteException {
		GraphSearcher gs = new RMIServer();
		return gs;
	}

}
