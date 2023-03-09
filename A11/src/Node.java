import java.util.Optional;

public class Node implements Comparable<Node> {
    static Node min (Node a, Node b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    //

    private final String name;
    private Distance distance;
    private boolean visited;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Edge> previousEdge;

    Node(String name) {
        this.name = name;
        this.distance = Infinity.getInstance();
        this.visited = false;
        this.previousEdge = Optional.empty();
    }

    String getName() { return name; }

    boolean isFresh() { return !visited; }

    void setVisited() { this.visited = true; }

    Distance getDistance() { return distance; }

    void setDistance(Distance d) { this.distance = d; }

    Optional<Edge> getPreviousEdge () { return previousEdge; }

    void setPreviousEdge (Edge edge) { previousEdge = Optional.of(edge); }

    public int compareTo (Node other) { return distance.compareTo(other.distance); }

    public String toString() { return name; }

    public boolean equals(Object o) {
        if (o instanceof Node that) return name.equals(that.getName());
        else return false;
    }

    public int hashCode() { return name.hashCode(); }
}
