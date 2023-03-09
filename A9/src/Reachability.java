import java.util.*;
import java.util.function.Consumer;

class Reachability extends GenericDFS {
    Hashtable<Node, Set<Node>> table;

    /**
     *
     * This is the most complex traversal. So do this one last.
     *
     * The goal here is to collect all the nodes that can reach
     * a particular node. Say we have an edge
     *
     *   X -> Y
     *
     * When we visit X, we may have accumulated some information about all the nodes
     * known to reach X so far, and all the nodes known to reach Y so far. The
     * existence of the edge X -> Y says that X can reach Y and
     * everything that is known to reach X can now also reach Y.
     *
     *
     */
    Reachability(Hashtable<Node, List<Node>> neighbors, Node start) {
        super(neighbors);
        table = new Hashtable<>();
        table.put(start, Set.of());
        List<Node> ordered = new ArrayList<>();
        ordered.add(start);

        while (!ordered.isEmpty()) {
            ordered.get(0).setVisited();

            for (Node n : neighbors.get(ordered.get(0))) {
                List<Node> orderedTwo = new ArrayList<>();
                orderedTwo.add(start);
                Set<Node> nodes = new HashSet<>();

                while (!orderedTwo.isEmpty()) {
                    orderedTwo.get(0).setVisited();
                    for (Node m : neighbors.get(orderedTwo.get(0))) {
                        if (neighbors.get(m).contains(n)) {
                            nodes.add(m);
                        } if (m == n) {
                            nodes.add(orderedTwo.get(0));
                        }
                    }

                    for (Node l : neighbors.get(orderedTwo.get(0))) {
                        if (!orderedTwo.contains(l) && !l.isVisited()) {
                            orderedTwo.add(l);
                        }
                    }
                    orderedTwo.remove(0);
                }


                nodes.add(n);
                table.put(n, nodes);
            }

            for (Node o : neighbors.get(ordered.get(0))) {
                if (!ordered.contains(o) && !o.isVisited()) {
                    ordered.add(o);
                }
            }
            ordered.remove(0);
        }
        // TODO
    }
}
