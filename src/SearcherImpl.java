import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class SearcherImpl implements Searcher {
	/** A trivial distance measurement algorithm
	 * 
	 * Starting from the source node, then a set of visited nodes
	 * is always extended by immediate neighbors of all visited nodes,
	 * until the target node is visited or no node is left.
	 */
	@Override
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

	/**
	 * A transitive distance measurement algorithm.
	 * 
	 * Starting from the source node, a set of visited nodes
	 * is always extended by transitive neighbors of all visited
	 * nodes, until the target node is visited or no node is left.
	 */
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
}
