
package tack1;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

public class TCPClient {
	// How many nodes and how many edges to create.
	private static final int GRAPH_NODES = 5;
	private static final int GRAPH_EDGES = 12;

	// How many searches to perform
	private static Node[] nodes;

	private static Random random = new Random();


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

	public static void main(String[] args) {
		// Create a randomly connected graph and do a quick measurement.
		// Consider replacing connectSomeNodes with connectAllNodes to verify that all distances are equal to one.
		createNodes(GRAPH_NODES);
		connectSomeNodes(GRAPH_EDGES);
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		 
		try {
			
			Socket clientSocket = new Socket("localhost", 8024);			
			ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());	
			outToServer.writeObject(nodes);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			printBuffer(inFromServer);
			clientSocket.close();			

		} catch (Exception e) {
		
		} 		
	}
	
	public static void printBuffer(BufferedReader buff) throws IOException{
		String s = null;
		while ((s=buff.readLine())!=null) {
			System.out.println(s);
		}
	}
	
}
