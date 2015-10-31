import java.util.*;

class Graph implements Iterable{

    public final Map<Object, List<Edge>> graph;

    public Graph() {
        graph = new HashMap<Object, List<Edge>>();
    }

    public boolean addNode(Object node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null.");
        }
	
        if (graph.containsKey(node)){
	    return false;
	}

        graph.put(node, new ArrayList<Edge>());
	
        return true;
    }

    public void addEdge (Object source, Object destination) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source/Destination, cannot be null.");
        }
	
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source/Destination, both should be part of graph");
        }
	
        Edge edge1 = new Edge(source, destination);
	Edge edge2 = new Edge(destination, source);

        graph.get(source).add(edge1);//////////////////////////
        graph.get(destination).add(edge2);/////////////////////
    }

    public void removeEdge (Object source, Object destination) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source/Destination, cannot be null.");
        }
	
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            throw new NoSuchElementException("Source/Destination, both should be part of graph");
        }
	
        graph.get(source).remove(destination);
        graph.get(destination).remove(source);
    }

    public List<Edge> edgesFrom(Object node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null.");
        }
	
        List<Edge> edges = graph.get(node);

	if (edges == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }

	return edges;//Collections.unmodifiableList(edges);
    }

    @Override public Iterator iterator() {
        return graph.keySet().iterator();
    }
}
