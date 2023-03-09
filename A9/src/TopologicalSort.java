import java.util.*;

class TopologicalSort extends GenericDFS {
    List<Node> ordered;

    /**
     *
     * We want to produce a list of nodes such that the following condition is satisfied:
     *
     * if there is an edge X -> Y then X must appear before Y in the list.
     *
     * This suggests the following strategy. Say a node X has Y, Z, W
     * as neighbors. We will enter X and recursively traverse Y, Z, and W. Once we
     * finished traversing Y, Z, and W, then we are guaranteed that everything reachable
     * from X has been visited and inserted in our 'ordered' list. At that point,
     * we add X to the front of that list.
     *
     */

    TopologicalSort(Hashtable<Node, List<Node>> neighbors, Node start) {
        super(neighbors);
        List<Node> check = new ArrayList<>();
        ordered = new ArrayList<>();
        ordered.add(start);
        check.add(start);

        for (int i = neighbors.get(start).size()-1; i >= 0; i--) {
            TopologicalSort topologicalSort = new TopologicalSort(neighbors, neighbors.get(start).get(i));

            check.addAll(topologicalSort.ordered);
            for (Node n : check) {
                if (!ordered.contains(n)) {
                    ordered.add(n);
                } else {
                    ordered.remove(n);
                    ordered.add(n);
                }
            }
        }
        // TODO
    }
}
