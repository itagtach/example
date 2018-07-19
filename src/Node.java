import java.util.Map;
import java.util.Set;

public interface Node {
	Set<Node> getNeighbors();
	Map<Node, Integer> getTransitiveNeighbors(int distance);
	void addNeighbor(Node neighbor);
}
