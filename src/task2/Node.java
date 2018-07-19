package task2;
import java.util.Map;
import java.util.Set;
import java.io.*;

public interface Node extends Serializable{
	Set<Node> getNeighbors();
	Map<Node, Integer> getTransitiveNeighbors(int distance);
	void addNeighbor(Node neighbor);
}
