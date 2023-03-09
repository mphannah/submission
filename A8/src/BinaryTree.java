import java.util.*;

public abstract class BinaryTree<E extends Comparable<E>>
        implements TreePrinter.PrintableNode {

    // Access fields

    abstract E getData () throws EmptyTreeE;
    abstract BinaryTree<E> getLeftBT () throws EmptyTreeE;
    abstract BinaryTree<E> getRightBT () throws EmptyTreeE;

    // Basic properties

    abstract boolean isEmpty();
    abstract int height ();
    abstract boolean isBalanced ();

    // Traversals that return lists

    abstract List<E> preOrderList();
    abstract List<E> inOrderList();
    abstract List<E> postOrderList();
    List<E> breadthFirstList (Queue<BinaryTree<E>> toVisit) {
        List<E> result = new ArrayList<>();
        while (! toVisit.isEmpty()) {
            if (toVisit.poll() instanceof NodeBT<E> currentNode) {
                result.add(currentNode.getData());
                toVisit.offer(currentNode.getLeftBT());
                toVisit.offer(currentNode.getRightBT());
            }
        }
        return result;
    }

    // Basic insert

    abstract BinaryTree<E> insert (E elem);

    // Helpers for BST/AVL methods

    abstract TreeAndLeaf<E> extractRightMost () throws EmptyTreeE;
    abstract TreeAndLeaf<E> balancedExtractRightMost () throws EmptyTreeE;

    // BST insertions, lookups, and deletions

    public abstract BinaryTree<E> insertBST(E elem);
    public abstract boolean findBST(E key);
    public abstract BinaryTree<E> deleteBST (E key);

    // AVL insertions, lookups, and deletions

    public abstract BinaryTree<E> insertAVL(E elem);
    public abstract boolean findAVL (E key);
    public abstract BinaryTree<E> deleteAVL (E key);

    // Iterators

    public Iterator<E> preOrder () { return preOrderList().iterator(); }
    public Iterator<E> inOrder () { return inOrderList().iterator(); }
    public Iterator<E> postOrder () { return postOrderList().iterator(); }
    public Iterator<E> breadthFirst () {
        Queue<BinaryTree<E>> toVisit = new LinkedList<>();
        toVisit.add(this);
        return breadthFirstList(toVisit).iterator();
    }

    // Make trees with various properties

    static <E extends Comparable<E>> BinaryTree<E> mkLeaf (E data) {
        return new NodeBT<>(data, new EmptyBT<>(), new EmptyBT<>());
    }

    static <E extends Comparable<E>> BinaryTree<E> mkBalanced (Collection<E> elems) {
        BinaryTree<E> result = new EmptyBT<>();
        for (E e : elems) result = result.insert(e);
        return result;
    }

    static <E extends Comparable<E>> BinaryTree<E> mkBST (Collection<E> elems) {
        BinaryTree<E> result = new EmptyBT<>();
        for (E e : elems) result = result.insertBST(e);
        return result;
    }

    static <E extends Comparable<E>> BinaryTree<E> mkAVL (Collection<E> elems) {
        BinaryTree<E> result = new EmptyBT<>();
        for (E e : elems) result = result.insertAVL(e);
        return result;
    }
}
