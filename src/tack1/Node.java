package tack1;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Node extends Serializable{
	Set<Node> getNeighbors();
	Map<Node, Integer> getTransitiveNeighbors(int distance);
	void addNeighbor(Node neighbor);
}
