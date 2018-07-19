package task3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GetSearcher extends Remote {
	GraphSearcher getSearcher() throws RemoteException; 
}