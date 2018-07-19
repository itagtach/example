package task2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface GraphSearcher extends Remote {
	
	Map<Node, Map<Node,Integer>> searchBenchmark(int howMany,Node[] nodes) throws RemoteException;

}