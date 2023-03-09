import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class AllShortestPaths extends GraphTraversal {

    AllShortestPaths(Hashtable<Node, List<Edge>> neighbors) {
        super(neighbors);
    }

    /**
     * This is the core of the all shortest paths algorithm. Given an edge e
     * from A -> B with weight w and a fresh destination B, we do the following:
     *   - let the current distance to B be db
     *   - the current edge provides another way to reach B via a cost
     *     that is the current distance to A + the weight w
     *   - if the new distance is not better, do nothing
     *   - otherwise, update B to record the new improved distance
     *     and the fact that this distance is via the edge e
     */
    void relax(Edge e) {
        if (e.destination().isFresh() && e.destination().getDistance().compareTo(e.source().getDistance().add(e.weight())) > 0) {
            e.destination().setPreviousEdge(e);
            e.destination().setDistance(e.source().getDistance().add(e.weight()));
        }

        heap.heapify();
        // TODO
    }

    void fromSource(Node source) {
        source.setDistance(new Finite(0));
        heap.moveUp(source);
        traverse();
    }

}
