public class Edge {
    public final Object from;
    public final Object to;
    public Double capacity;
    public Double consumedCapacity;

    public Edge (Object from, Object to) {
        if (from == null  || to == null) {
            throw new NullPointerException("From/To is null");
        }

        this.from = from;
        this.to = to;

	this.capacity = 1d;
	this.consumedCapacity = 0d;
    }

    public void adjustCapacity(Double capacity){
	this.capacity = capacity;
    }

    public Double getCapacity(){
	return capacity - consumedCapacity;
    }
}
