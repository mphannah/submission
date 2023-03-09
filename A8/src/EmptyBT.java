import java.util.*;

public class EmptyBT<E extends Comparable<E>> extends BinaryTree<E> {

    // Access fields

    E getData () throws EmptyTreeE { throw new EmptyTreeE(); }
    BinaryTree<E> getLeftBT () throws EmptyTreeE { throw new EmptyTreeE(); }
    BinaryTree<E> getRightBT () throws EmptyTreeE { throw new EmptyTreeE(); }

    // Basic properties

    boolean isEmpty () { return true; }
    int height () { return 0; }
    boolean isBalanced () { return true; }

    // Traversals that return lists

    List<E> preOrderList() { return new ArrayList<>(); }
    List<E> inOrderList() { return new ArrayList<>(); }
    List<E> postOrderList() { return new ArrayList<>(); }

    // Basic insert

    BinaryTree<E> insert(E elem) { return mkLeaf(elem); }

    // Helpers for BST/AVL methods

    TreeAndLeaf<E> extractRightMost () throws EmptyTreeE { throw new EmptyTreeE();}
    TreeAndLeaf<E> balancedExtractRightMost () throws EmptyTreeE { throw new EmptyTreeE(); }

    // BST insertions, lookups, and deletions

    public BinaryTree<E> insertBST(E elem) {
        return insert(elem);
    }
    public boolean findBST(E elem) {
        return false;
    }
    public BinaryTree<E> deleteBST(E elem) {
        return this;
    }

    // AVL insertions, lookups, and deletions

    public BinaryTree<E> insertAVL(E elem) {
        return insert(elem);
    }
    public boolean findAVL(E key) {
        return false;
    }
    public BinaryTree<E> deleteAVL(E key) {
        return this;
    }

    // Printable interface

    public TreePrinter.PrintableNode getLeft() { return null; }
    public TreePrinter.PrintableNode getRight() { return null; }
    public String getText() { return ""; }

}
