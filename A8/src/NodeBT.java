import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class NodeBT<E extends Comparable<E>> extends BinaryTree<E> {
    private final E data;
    private final BinaryTree<E> left, right;
    private final int height;

    NodeBT (E data, BinaryTree<E> left, BinaryTree<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = Math.max(left.height(), right.height()) + 1;
    }

    /**
     * This method is used as a helper for the AVL methods. It is like a constructor,
     * but it applies the necessary rotations if needed to make sure the tree
     * is balanced.
     *
     * There are three cases to consider:
     *
     * left.height > right.height + 1
     *
     *   the situation looks like this:
     *
     *                 data
     *              /        \
     *        left          right
     *       /     \
     *   left2     right2
     *
     *   We definitely must rotate the entire tree to the right
     *   We might have to rotate left to the left
     *
     * right.height > left.height + 1, which is symmetric
     *
     * otherwise we just build the tree without any rotations
     */
    static <E extends Comparable<E>> NodeBT<E> mkBalancedNodeBT (E data, BinaryTree<E> left, BinaryTree<E> right) {
        NodeBT<E> newNode = new NodeBT<>(data, left, right);

        if (left.isEmpty() && right.isEmpty()) {
            return newNode;
        }

        if (!left.isEmpty() && !right.isEmpty()) {
            try {
                if (right.height() > left.height()+1) {
                    newNode = new NodeBT<>(data, mkBalancedNodeBT(left.getData(), left.getLeftBT(), left.getRightBT()),
                            mkBalancedNodeBT(right.getData(), right.getLeftBT(), right.getRightBT()).mayBeRotateLeft());
                } else if (left.height() > right.height()+1) {
                    newNode = new NodeBT<>(data, mkBalancedNodeBT(left.getData(), left.getLeftBT(), left.getRightBT()).mayBeRotateRight(),
                            mkBalancedNodeBT(right.getData(), right.getLeftBT(), right.getRightBT()));
                } else {
                    newNode = new NodeBT<>(data, mkBalancedNodeBT(left.getData(), left.getLeftBT(), left.getRightBT()),
                            mkBalancedNodeBT(right.getData(), right.getLeftBT(), right.getRightBT()));
                }
            } catch (EmptyTreeE e) {
                e.printStackTrace();
            }
        } else if (left.isEmpty()) {
            try {
                if (right.height() > left.height()+1) {
                    newNode = new NodeBT<>(data, left,
                            mkBalancedNodeBT(right.getData(), right.getLeftBT(), right.getRightBT()).mayBeRotateRight());
                } else {
                    newNode = new NodeBT<>(data, left,
                            mkBalancedNodeBT(right.getData(), right.getLeftBT(), right.getRightBT()));
                }
            } catch (EmptyTreeE e) {
                e.printStackTrace();
            }
        } else if (right.isEmpty()) {
            try {
                if (left.height() > right.height()+1) {
                    newNode = new NodeBT<>(data,
                            mkBalancedNodeBT(left.getData(), left.getLeftBT(), left.getRightBT()).mayBeRotateLeft(), right);
                } else {
                    newNode = new NodeBT<>(data,
                            mkBalancedNodeBT(left.getData(), left.getLeftBT(), left.getRightBT()), right);
                }
            } catch (EmptyTreeE e) {
                e.printStackTrace();
            }
        }

        if (right.height() > left.height()+1) {
            newNode = newNode.rotateLeft();
        } else if (left.height() > right.height()+1) {
            newNode = newNode.rotateRight();
        }

        return newNode; // TODO
    }

    // Access fields

    E getData () { return data; }
    BinaryTree<E> getLeftBT () { return left; }
    BinaryTree<E> getRightBT () { return right; }

    // Basic properties

    boolean isEmpty () { return false; }
    int height() { return height; }
    boolean isBalanced() { return Math.abs(left.height() - right.height()) < 2; }

    // Traversals that return lists

    /**
     * The next three methods return a list of the data at each node in preorder,
     * inorder, or postorder
     */
    List<E> preOrderList() {
        List<E> l = new ArrayList<>();

        if (left.isEmpty() && right.isEmpty()) {
            l.add(data);
            return l;
        }

        l.add(data);
        l.addAll(left.preOrderList());
        l.addAll(right.preOrderList());

        return l; // TODO
    }

    List<E> inOrderList() {
        List<E> l = new ArrayList<>();

        if (left.isEmpty() && right.isEmpty()) {
            l.add(data);
            return l;
        }

        l.addAll(left.inOrderList());
        l.add(data);
        l.addAll(right.inOrderList());

        return l; // TODO
    }

    List<E> postOrderList() {
        List<E> l = new ArrayList<>();

        if (left.isEmpty() && right.isEmpty()) {
            l.add(data);
            return l;
        }

        l.addAll(left.postOrderList());
        l.addAll(right.postOrderList());
        l.add(data);

        return l; // TODO
    }

    // Basic insert: always insert to the left but swaps the tree after every insert
    // to make sure the tree is balanced

    BinaryTree<E> insert (E elem) {
        return new NodeBT<>(data, right, left.insert(elem));
    }

    // Helpers for BST/AVL methods

    /**
     * Here is an example. Let the current tree be;
     *
     *              3
     *            /   \
     *           2     5
     *         /      /  \
     *        1      4    6
     *
     * the method returns a record with two components: the first component is the tree below
     *
     *              3
     *            /   \
     *           2     5
     *         /      /
     *        1      4
     *
     *
     * and the second component is the left 6
     *
     */
    TreeAndLeaf<E> extractRightMost () throws EmptyTreeE {

        if (right.isEmpty()) {
            return new TreeAndLeaf<>(left, data);
        } else if (right.getRightBT().isEmpty()) {
            return new TreeAndLeaf<>(new NodeBT<>(data, left, right.getLeftBT()), right.getData());
        }

        return new TreeAndLeaf<>(new NodeBT<>(data, left, right.extractRightMost().tree()), right.extractRightMost().leaf()); // TODO
    }

    /**
     * Exactly like the method above but ensure the tree is balanced
     */
    TreeAndLeaf<E> balancedExtractRightMost () throws EmptyTreeE {

        if (!isBalanced()) {
            NodeBT<E> newNode = mkBalancedNodeBT(data, left, right);
            return newNode.balancedExtractRightMost();
        }

        if (right.isEmpty()) {
            return new TreeAndLeaf<>(left, data);
        } else if (right.getRightBT().isEmpty()) {
            return new TreeAndLeaf<>(new NodeBT<>(data, left, right.getLeftBT()), right.getData());
        }

        return new TreeAndLeaf<>(new NodeBT<>(data, left, right.extractRightMost().tree()), right.extractRightMost().leaf()); // TODO
    }

    NodeBT<E> mayBeRotateLeft () {
        if (left.height() < right.height()) return rotateLeft();
        else return this;
    }

    NodeBT<E> mayBeRotateRight () {
        if (right.height() < left.height()) return rotateRight();
        else return this;
    }

    /**
     * Here is an example. If the current tree is:
     *
     *                5
     *             /    \
     *           2       8
     *                 /  \
     *                6    9
     *
     * we return
     *
     *                8
     *             /    \
     *           5       9
     *         /  \
     *       2     6
     *
     */
    NodeBT<E> rotateLeft () {
        if (left.isEmpty() && right.isEmpty()) {
            throw new Error();
        }

        NodeBT<E> leftBT = null;
        try {
            leftBT = new NodeBT<>(data, left, right.getLeftBT());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
        try {
            return new NodeBT<>(right.getData(), leftBT, right.getRightBT()); // TODO
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Symmetric to the method above
     */
    NodeBT<E> rotateRight () {
        if (left.isEmpty() && right.isEmpty()) {
            throw new Error();
        }

        NodeBT<E> rightBT = null;
        try {
            rightBT = new NodeBT<>(data, left.getRightBT(), right);
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }

        try {
            return new NodeBT<>(left.getData(), left.getLeftBT(), rightBT); // TODO
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
        return this;
    }

    // BST insertions, lookups, and deletions

    public BinaryTree<E> insertBST(E elem) {
        if (elem.compareTo(data) < 0)
            return new NodeBT<>(data, left.insertBST(elem), right);
        else return new NodeBT<>(data, left, right.insertBST(elem));
    }

    public boolean findBST(E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) return left.findBST(elem);
        else if (comp > 0) return right.findBST(elem);
        else return true;
    }

    public BinaryTree<E> deleteBST (E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) return new NodeBT<>(data,left.deleteBST(elem),right);
        else if (comp > 0) return new NodeBT<>(data,left,right.deleteBST(elem));
        else {
            try {
                TreeAndLeaf<E> treeLeaf = left.extractRightMost();
                return new NodeBT<>(treeLeaf.leaf(), treeLeaf.tree(), right);
            }
            catch (EmptyTreeE e) { return right; }
        }
    }

    // AVL insertions, lookups, and deletions

    /**
     * The following methods are similar to the BST variants but must
     * ensure that the trees are always balanced
     *
     */
    public BinaryTree<E> insertAVL(E elem) {
        BinaryTree<E> newNode = insertBST(elem);
        try {
            newNode = mkBalancedNodeBT(newNode.getData(), newNode.getLeftBT(), newNode.getRightBT());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }

//        if (!left.isBalanced()) {
//            try {
//                newNode = mkBalancedNodeBT(newNode.getData(),
//                        mkBalancedNodeBT(newNode.getLeftBT().getData(), newNode.getLeftBT().getLeftBT(), newNode.getLeftBT().getRightBT()),
//                        newNode.getRightBT());
//            } catch (EmptyTreeE e) {
//                e.printStackTrace();
//            }
//        } if (!right.isBalanced()) {
//            try {
//                newNode = mkBalancedNodeBT(newNode.getData(), newNode.getLeftBT(),
//                        mkBalancedNodeBT(newNode.getRightBT().getData(), newNode.getRightBT().getLeftBT(),
//                                newNode.getRightBT().getRightBT()));
//            } catch (EmptyTreeE e) {
//                e.printStackTrace();
//            }
//        } if (!isBalanced()) {
//            try {
//                newNode = mkBalancedNodeBT(newNode.getData(), newNode.getLeftBT(), newNode.getRightBT());
//            } catch (EmptyTreeE e) {
//                e.printStackTrace();
//            }
//        }

        return newNode; // TODO
    }

    public boolean findAVL (E elem) {
        return findBST(elem);
    }

    public BinaryTree<E> deleteAVL (E elem) {
        BinaryTree<E> newNode = deleteBST(elem);
        try {
            newNode = mkBalancedNodeBT(newNode.getData(), newNode.getLeftBT(), newNode.getRightBT());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }

        return newNode; // TODO
    }

    // Printable interface

    public TreePrinter.PrintableNode getLeft() { return left.isEmpty() ? null : left; }
    public TreePrinter.PrintableNode getRight() { return right.isEmpty() ? null : right; }
    public String getText() { return String.valueOf(data); }

}

