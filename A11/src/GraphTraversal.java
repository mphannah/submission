import java.util.Hashtable;
import java.util.List;

abstract class GraphTraversal {
    protected final Hashtable<Node, List<Edge>> neighbors;
    protected final Heap heap;

    GraphTraversal(Hashtable<Node, List<Edge>> neighbors) {
        this.neighbors = neighbors;
        this.heap = new Heap(neighbors.keySet());
    }

    void traverse() {
        while (!heap.isEmpty()) visit(heap.extractMin());
    }

    void visit(Node u) {
        if (u.isFresh()) {
            u.setVisited();
            neighbors.get(u).forEach(this::relax);
        }
    }

    abstract void relax(Edge e);
}
