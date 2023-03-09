import javax.swing.text.html.Option;
import java.util.*;

public class Heap {
    /**
     * The heap is represented as an array as explained in class.
     * We want to get a node from its index and vice-versa. The
     * HashMap indices maps each node to its index. It must
     * be maintained consistently: in other words if the nodes
     * move in the array, their index must be updated.
     */
    private final List<Node> nodes;
    private final HashMap<Node,Integer> indices;
    private int size;

    Heap(Set<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
        this.size = nodes.size();
        this.indices = new HashMap<>();
        for (int i = 0; i < size; i++)  indices.put(this.nodes.get(i), i);
        heapify();
    }

    /**
     * Assumes the array of nodes is initialized but the nodes are in no
     * particular order.
     *
     * As explained in class, the goal is to re-arrange the nodes so that
     * they form a proper min-heap where each is less than its children.
     */
    void heapify() {
        for (int i = size-1; i >= 0; i --) {
            moveDown(nodes.get(i));
        }
        // TODO
    }

    boolean isEmpty() {
        return size == 0;
    }

    /**
     * The next few methods return the appropriate node if it is within bounds.
     * Otherwise, they return an Optional.empty
     */
    Optional<Node> getParent(Node n) {
        int index = indices.get(n);
        int parentIndex;

        if (index == 0) {
            return Optional.empty();
        }

        if ((index+1) % 2 == 0) {
            parentIndex = ((index+1) / 2)-1;
        } else {
            parentIndex = (index / 2)-1;
        }

        return Optional.of(nodes.get(parentIndex)); // TODO
    }

    Optional<Node> getLeftChild(Node n) {
        int leftIndex = (indices.get(n)+1)*2;

        if (leftIndex <= size) {
            return Optional.of(nodes.get(leftIndex-1));
        }

        return Optional.empty(); // TODO
    }

    Optional<Node> getRightChild(Node n) {
        int rightIndex = ((indices.get(n)+1)*2)+1;

        if (rightIndex <= size) {
            return Optional.of(nodes.get(rightIndex-1));
        }

        return Optional.empty(); // TODO
    }

    Optional<Node> getMinChild(Node n) {
        if (getLeftChild(n).isPresent() && getRightChild(n).isEmpty()) {
            return getLeftChild(n);
        } if (getLeftChild(n).isEmpty() && getRightChild(n).isPresent()) {
            return getRightChild(n);
        } if (getLeftChild(n).isPresent() && getRightChild(n).isPresent()) {
            if (getLeftChild(n).get().getDistance().compareTo(getRightChild(n).get().getDistance()) < 0) {
                return getLeftChild(n);
            } else {
                return getRightChild(n);
            }
        }

        return Optional.empty(); // TODO
    }

    /**
     * Swaps the two given nodes in the array
     * of nodes making sure we also update their
     * indices.
     */
    void swap(Node n1, Node n2) {
        int n1Index = indices.get(n1);
        int n2Index = indices.get(n2);

        nodes.set(n1Index, n2);
        nodes.set(n2Index, n1);

        indices.put(n1, n2Index);
        indices.put(n2, n1Index);
        // TODO
    }

    /**
     * Recursively move this node down until the heap property
     * is established
     */
    void moveDown(Node n) {
        if ((getLeftChild(n).isEmpty() || n.getDistance().compareTo(getLeftChild(n).orElseThrow().getDistance()) < 0)
                && (getRightChild(n).isEmpty() || n.getDistance().compareTo(getRightChild(n).orElseThrow().getDistance()) < 0)) {
            return;
        }

        if (getLeftChild(n).isPresent() || getRightChild(n).isPresent()) {
            Node n2 = getMinChild(n).orElseThrow();
            if (n.getDistance().compareTo(n2.getDistance()) > 0) {
                swap(n, n2);
                moveDown(n);
            }
        }
        // TODO
    }

    /**
     * Recursively move this node up until the heap property
     * is established
     */
    void moveUp(Node n) {
        if (getParent(n).isEmpty() || n.getDistance().compareTo(getParent(n).orElseThrow().getDistance()) > 0) {
            System.out.println();
            return;
        }

        if (n.getDistance().compareTo(getParent(n).orElseThrow().getDistance()) < 0) {
            Node n2 = getParent(n).orElseThrow();
            swap(n, n2);
            moveUp(n);
        }
        // TODO
    }

    void insert(Node n) {
        indices.put(n, size);
        size++;
        nodes.add(n);
        moveUp(n);
    }

    /**
     * Return the minimum node in the heap (which is at the root).
     * Re-arrange the heap appropriately
     */
    Node extractMin() {
        if (!nodes.isEmpty()) {
            Node n = nodes.get(0);
            nodes.remove(0);
            size--;
            for (Node node : nodes) {
                indices.put(node, indices.get(node)-1);
            }
            heapify();
            return n;
        }

        return null; // TODO
    }
}

