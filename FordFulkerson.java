import java.util.*;

public class FordFulkerson {

    private Graph graph;

    public FordFulkerson (Graph graph) {
        if (graph == null) {
            throw new NullPointerException("Graph cannot be null");
        }
	
        this.graph = graph;
    }


    private void validate(Object source, Object destination) {
        if (source == null || destination == null) {
            throw new NullPointerException("Source/Destination cannot be null");
        }

        if (source.equals(destination)) {
            throw new IllegalArgumentException("Source cannot be the same as destination");
        }
    }

    public double maxFlow(Object source, Object destination) {
        validate(source, destination);

        double max = 0;
	
        List<Object> nodes = getPath(source, destination);

	System.out.println(nodes.size());
	
	while (nodes.size() > 0) {
            double maxCapacity = maxCapacity(nodes);
            max = max + maxCapacity;
            drainCapacity(nodes, maxCapacity);
            nodes = getPath(source, destination);

	    System.out.println("loop nodes "+nodes.size());
        }
	
        return max;
    }

    private List<Object> getPath(Object source, Object destination) {
        synchronized (graph) {
            final ArrayList<Object> path = new ArrayList<Object>();

	    depthFind(source, destination, path);

	    return new ArrayList<Object>(path);
        }
    }

    private boolean depthFind(Object current, Object destination, ArrayList<Object> path) {
        path.add(current);

        if (current.equals(destination)) {
            return true;
        }

        for (Edge edge : graph.edgesFrom(current)) {
            // if not cycle and if capacity exists.
            if (!path.contains(edge.to)) {
                // if end has been reached.
                if (depthFind(edge.to, destination, path)) {
                    return true;
                }
            }
        }

        path.remove(current);
	
        return false;
    }

    private double maxCapacity(List<Object> nodes) {
	/*
	double maxCapacity = Double.MAX_VALUE;
	double capacity = 1;
	
        for (int i = 0; i < nodes.size() - 1; i++) {
            Object source = nodes.get(i);
            Object destination = nodes.get(i + 1);

            List<Edge> edgeList = graph.edgesFrom(source);
	    for(Edge edge:edgeList){
		if(edge.to == destination){
		    capacity = edge.getCapacity();
		}
	    }

	    if (maxCapacity > capacity) { 
                maxCapacity = capacity;
            }
	    
        }
	
        return maxCapacity;*/
	return 1;
    }

    private void drainCapacity (List<Object> nodes, double maxCapacity) {
        for (int i = 0; i < nodes.size() - 1; i++) {
            Object source = nodes.get(i);
            Object destination = nodes.get(i + 1);

	    List<Edge> edgesList = graph.edgesFrom(source);
	    for(Edge edge:edgesList){
		if(edge.to == destination){
		    edge.adjustCapacity(maxCapacity);
		}
	    }
        }
    }
}
