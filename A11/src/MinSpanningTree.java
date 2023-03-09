import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class MinSpanningTree extends GraphTraversal {

    MinSpanningTree(Hashtable<Node, List<Edge>> neighbors) {
        super(neighbors);
    }

    /**
     * This is the core of the minimum spanning tree algorithm. Given an edge e
     * from A -> B with weight w and a fresh destination B, we do the following:
     *   - let the current cost of adding B be db
     *   - the current edge provides another way to add B to the tree with a
     *     cost of w
     *   - if the new cost is not better, do nothing
     *   - otherwise, update B to record the new improved cost
     *     and the fact that this cost is via the edge e
     */

    void relax(Edge e) {
        if (e.destination().isFresh() && e.destination().getDistance().compareTo(e.weight()) > 0) {
            e.destination().setPreviousEdge(e);
            e.destination().setDistance(e.weight());
        }

        heap.heapify();
            // TODO
    }

    Set<Edge> fromSource(Node source) {
        source.setDistance(new Finite(0));
        heap.moveUp(source);
        traverse();
        Set<Edge> result = new HashSet<>();
        for (Node n : neighbors.keySet()) {
            n.getPreviousEdge().ifPresent(result::add);
        }
        return result;
    }

}
