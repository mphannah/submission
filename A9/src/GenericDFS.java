import java.util.*;
import java.util.function.Consumer;

abstract class GenericDFS {
    protected final Hashtable<Node, List<Node>> neighbors;
    private Consumer<Node> enterConsumer, touchConsumer, exitConsumer;

    GenericDFS(Hashtable<Node, List<Node>> neighbors) {
        this.neighbors = neighbors;
        this.enterConsumer = node -> {};
        this.touchConsumer = node -> {};
        this.exitConsumer  = node -> {};
    }

    void setEnterConsumer (Consumer<Node> consumer) { this.enterConsumer = consumer; }
    void setTouchConsumer (Consumer<Node> consumer) { this.touchConsumer = consumer; }
    void setExitConsumer  (Consumer<Node> consumer) { this.exitConsumer = consumer;  }

    /**
     * All the algorithms we are implementing for this assignment are variations on this
     * generic DFS traversal. Ignoring the consumers for a moment the traversal is
     * implemented as:
     *
     *     void traverse(Node u) {
     *         if (u.isVisited()) {}
     *         else {
     *             u.setVisited();
     *             neighbors.get(u).forEach(this::traverse);
     *         }
     *
     * To traverse the graph starting from node u, we first check if u has been
     * previously visited to avoid infinite loops. If u has never been visited,
     * then we mark as visited and recursively traverse its immediate neighbors.
     * That's it.
     *
     * This skeleton is expressive enough to implement various interesting algorithms;
     * we show four of them in this assignment;
     * - topological sorting
     * - cycle detection
     * - "normal" depth first search
     * - reachability analysis
     * Each algorithm is implemented by simply varying the actions taken at the various
     * moments during the traversal, i.e., by passing in different consumers.
     *
     * Each file has additional explanation relevant to the algorithm in question.
     */
    void traverse(Node u) {
        if (u.isVisited()) touchConsumer.accept(u);
        else {
            u.setVisited();
            enterConsumer.accept(u);
            neighbors.get(u).forEach(this::traverse);
            exitConsumer.accept(u);
        }
    }

}
