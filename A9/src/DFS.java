import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

class DFS extends GenericDFS {
    List<Node> traversal;

    /**
     *
     * This is a simple depth first traversal. Every time you visit a node
     * for the first time, you add it to the result.
     *
     */

    DFS(Hashtable<Node, List<Node>> neighbors, Node start) {
        super(neighbors);

        traversal = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            Node topOfStack = stack.pop();
            if (!topOfStack.isVisited()) {
                topOfStack.setVisited();
                traversal.add(topOfStack);
            }

            for (int i = neighbors.get(topOfStack).size()-1; i >= 0; i--) {
                Node n = neighbors.get(topOfStack).get(i);
                if (!traversal.contains(n)) {
                    stack.push(n);
                }
            }
        }
        // TODO
    }
}
