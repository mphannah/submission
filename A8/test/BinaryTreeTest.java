import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    @Test
    void insertBST() {
        Collection<Integer> elems;
        BinaryTree<Integer> btree, otree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkBalanced(elems);
        assertTrue(btree.isBalanced());
        assertEquals(4, btree.height());

        TreePrinter.print(btree);

        elems = Arrays.asList(1,2,3,4,5);
        btree = BinaryTree.mkBalanced(elems);
        otree = BinaryTree.mkBST(elems);

        TreePrinter.print(btree);
        TreePrinter.print(otree);
    }

    @Test
    void iter () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkBalanced(elems);

        TreePrinter.print(btree);
        for (Iterator<Integer> iter = btree.preOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.inOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.postOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.breadthFirst(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
    }

    @Test
    void deleteBST () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree, stree;

        elems = Arrays.asList(8,2,6,4,5,7,12,11,9,10,1,14,13,3,15);
        btree = BinaryTree.mkBST(elems);
        TreePrinter.print(btree);

        stree = btree.deleteBST(6);
        TreePrinter.print(stree);

        stree = btree.deleteBST(11);
        TreePrinter.print(stree);

        stree = btree.deleteBST(8);
        TreePrinter.print(stree);
    }

    @Test
    void insertAVL () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);
    }

    @Test
    void deleteAVL () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);

        btree = btree.deleteAVL(1);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(3);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(2);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(7);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(4);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(6);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(13);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(15);
        TreePrinter.print(btree);

    }

    @Test
    void rotate () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(7, 15, 13, 14, 9, 1, 5, 8, 3, 4, 11, 2, 12, 6);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);

        btree = btree.insertAVL(10);
        btree = btree.insertAVL(16);
        btree = btree.insertAVL(5);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(7);
        TreePrinter.print(btree);
    }

    @Test
    void complex () {
        ArrayList<Integer> elems = new ArrayList<>();
        BinaryTree<Integer> btree;
        Random r = new Random();

        for (int i = 0; i < 11; i++) {
            elems.add(r.nextInt(50)+1);
        }

        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);

        for (int i = 0; i < 6; i++) {
            int random = r.nextInt(50)+1;
            btree = btree.insertAVL(random);
            System.out.println("     " + random);
            TreePrinter.print(btree);
        }

        try {
            System.out.println("Deleting root");
            btree = btree.deleteAVL(btree.getData());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }

        TreePrinter.print(btree);

        try {
            System.out.println("Right most leaf: " + btree.extractRightMost().leaf());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
    }

    @Test
    void anotherTest () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(15, 18, 9, 8, 16, 7);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);

        btree = btree.deleteAVL(15);

        TreePrinter.print(btree);

        elems = Arrays.asList(7, 15, 13, 14, 9, 1, 5, 8, 3, 4, 11, 2, 12, 6);
        btree = BinaryTree.mkBST(elems);

        System.out.println();
        TreePrinter.print(btree);

        try {
            System.out.println(btree.balancedExtractRightMost().leaf());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
    }

    @Test
    void insertOneAtTime () {
        List<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        btree = BinaryTree.mkAVL(new ArrayList<>(List.of(1)));

        for (int i = 0; i < elems.size(); i++) {
            btree = btree.insertAVL(elems.get(i));
            System.out.println(i+2);
            TreePrinter.print(btree);
        }
    }
}