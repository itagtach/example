package tack1;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class TCPServer {
	private static Searcher searcher = new SearcherImpl();

	public static void main(String argv[]) throws Exception {

		ServerSocket helloSocket = new ServerSocket(8024);

		try {

			Socket connectSocket = helloSocket.accept();
		
			ObjectInputStream recievedObject = new ObjectInputStream(connectSocket.getInputStream());

			Node[] arr = (Node[]) recievedObject.readObject();
			//System.out.println(arr);
			Map<Node, Map<Node, Integer>> answer = searchBenchMark(arr.length,arr);
			String answerString = mapToString(answer,arr);

			DataOutputStream outgoing = new DataOutputStream(connectSocket.getOutputStream());

			outgoing.writeBytes(answerString + "\n");
			
		} catch(Exception e){
			
		}
	}
	
	public static Map<Node, Map<Node, Integer>> searchBenchMark(int howMany,Node[] nodes){
		System.out.println("Running search");
		Map<Node, Map<Node, Integer>> theMap = new HashMap<Node, Map<Node, Integer>>();

		for(int h = 0; h < howMany; h++){
			Map<Node, Integer> tempMap = new HashMap<Node, Integer>();
			for (int i = 0; i < howMany; i++) {
				
				// Select two random nodes.
				final int idxFrom = h;
				final int idxTo = i;
				final int distance = searcher.getDistance(nodes[idxFrom], nodes[idxTo]);
				final int transitiveDistance = searcher.getTransitiveDistance(4, nodes[idxFrom], nodes[idxTo]);

				if (distance != transitiveDistance) {
					System.out.printf("Standard and transitive algorithms inconsistent (%d != %d)\n", distance,
							transitiveDistance);
				} else {

					tempMap.put(nodes[i],distance);
				}
			}
			theMap.put(nodes[h], tempMap);
			System.out.println(tempMap);
		}
		return theMap;
		
	}
	
	public static String mapToString(Map<Node, Map<Node, Integer>> answer,Node[] nodes){
		int size = answer.size();
		String mapString = new String();
		for(int i = 0;i<size;i++){
			mapString = mapString + "Distances from Node: " + i + "\n";
			for(int j = 0; j < size; j++){
				mapString = mapString + "To Node: " + j + " is " + answer.get(nodes[i]).get(nodes[j]) + "\n";	
			}			
		}
		return mapString;
	}
	
	
}