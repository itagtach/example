public interface Searcher {
	public static final int DISTANCE_INFINITE = -1;
	public int getDistance(Node from, Node to);
	public int getTransitiveDistance(int distance, Node from, Node to);
}
