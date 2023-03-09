import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

class SeqAccessE extends Exception {}
class SeqFullE extends Exception {}

// The general picture to keep in mind is the following:
//
// |-------------------------|
// | 4 5 6 _ _ _ _ _ _ 1 2 3 |
// |-------------------------|
//         /\        /\      /\
//        left      right  capacity
//
// We maintain two pointers: left and right. The left
// pointer starts at 0 and is incremented for insertions
// and decremented for deletions. The right pointer
// starts at the other end and behaves symmetrically.

/**
 * General rules:
 *   - please do not change any method signatures
 *   - please do not change the declarations of the instance variables
 *   - please do not change any of the given methods and do
 *     remove the parts of the methods that are given to you
 *   - only edit the parts marked with TODO
 */

class LRFiniteSequence<E> {
    private final Optional<E>[] elements;
    private final int capacity;
    private int left;
    private int right;
    private int size;

    @SuppressWarnings("unchecked")
    LRFiniteSequence(int capacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        this.capacity = capacity;
        left = 0;
        right = capacity - 1;
        size = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    private boolean sizeConsistent() {
        return size == left + (capacity - 1) - right;
    }

    /**
     * The method returns the element at the given index.
     * If the index is out of bounds, the SeqAccessE exception
     * is thrown.
     * If the index is in bounds but the element at the given
     * index is Optional.empty the SeqAccessE exception
     * is also thrown.
     */
    public E get (int index) throws SeqAccessE {
        if (index < 0 || index > elements.length-1) {
            throw new SeqAccessE();
        } else if (elements[index].isEmpty()) {
            throw new SeqAccessE();
        } else {
            return elements[index].get();
        } // TODO
    }

    /**
     * If the sequence is full, the method throws the SeqFullE exception.
     * Otherwise adds the element to the sequence adjusting all pointers
     * appropriately.
     */
    public void insertLeft (E elem) throws SeqFullE {
        // TODO
        assert sizeConsistent(); // do not remove this line
        boolean empty = true;

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isEmpty()) {
                empty = true;
                break;
            } else {
                empty = false;
            }
        }

        if (empty) {
            elements[left] = Optional.of(elem);
            left += 1;
            size += 1;
        } else {
            throw new SeqFullE();
        }
    }

    /**
     * Same contract as insertLeft
     */
    public void insertRight (E elem) throws SeqFullE {
        // TODO
        assert sizeConsistent();
        boolean empty = true;

        for (int i = elements.length-1; i >= 0; i--) {
            if (elements[i].isEmpty()) {
                empty = true;
                break;
            } else {
                empty = false;
            }
        }

        if (empty) {
            elements[right] = Optional.of(elem);
            right -= 1;
            size += 1;
        } else {
            throw new SeqFullE();
        }
    }

    /**
     * If the left pointer is out of bounds, throw the SeqAccessE exception
     * If the left pointer is in bounds but points to an element that
     *   is Optional.empty, also throw the SeqAccessE exception
     * Otherwise return the element that left points to adjusting all
     * instance variables appropriately
     */
    public E removeLeft () throws SeqAccessE {
        // TODO
        left -= 1;

        if (left > elements.length - 1 || left < 0) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f3ac0c5 (finished assignment 1)
=======
>>>>>>> 4c0b716 (updated assignment 1)
            left += 1;
            throw new SeqAccessE();
        } else if (elements[left].isEmpty()) {
            left += 1;
=======
            throw new SeqAccessE();
        } else if (elements[left].isEmpty()) {
>>>>>>> b0ef6fb (finished assignment 1)
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 4c0b716 (updated assignment 1)
=======
            left += 1;
            throw new SeqAccessE();
        } else if (elements[left].isEmpty()) {
            left += 1;
>>>>>>> 11e059e (updated assignment 1)
<<<<<<< HEAD
=======
>>>>>>> f3ac0c5 (finished assignment 1)
=======
>>>>>>> 4c0b716 (updated assignment 1)
            throw new SeqAccessE();
        } else {
            size -= 1;
            E leftObject = elements[left].get();
            elements[left] = Optional.empty();
            assert sizeConsistent(); // keep this line immediately before the return
            return leftObject;
        }
    }

    /**
     * The contract is similar to removeLeft
     */
    public E removeRight () throws SeqAccessE {
        // TODO
        right += 1;

        if (right > elements.length - 1 || right < 0) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            right -= 1;
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
            right -= 1;
=======
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
>>>>>>> b0ef6fb (finished assignment 1)
=======
            right += 1;
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
            right += 1;
>>>>>>> 11e059e (updated assignment 1)
=======
=======
>>>>>>> f3ac0c5 (finished assignment 1)
=======
>>>>>>> 4c0b716 (updated assignment 1)
=======
>>>>>>> 2837c84 (commit)
            right -= 1;
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
            right -= 1;
<<<<<<< HEAD
>>>>>>> f0fb7e5 (commit)
=======
=======
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
>>>>>>> b0ef6fb (finished assignment 1)
<<<<<<< HEAD
>>>>>>> f3ac0c5 (finished assignment 1)
=======
=======
            right += 1;
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
            right += 1;
>>>>>>> 11e059e (updated assignment 1)
<<<<<<< HEAD
>>>>>>> 4c0b716 (updated assignment 1)
=======
=======
            right -= 1;
            throw new SeqAccessE();
        } else if (elements[right].isEmpty()) {
            right -= 1;
>>>>>>> f0fb7e5 (commit)
>>>>>>> 2837c84 (commit)
            throw new SeqAccessE();
        } else {
            size -= 1;
            E rightObject = elements[right].get();
            elements[right] = Optional.empty();
            assert sizeConsistent();
            return rightObject;
        }
    }

    void insertLeftIfNotFull (E elem) {
        if (isFull()) return;
        try {
            insertLeft(elem);
        } catch (SeqFullE e) {
            throw new Error("Internal bug!");
        }
    }

    void insertRightIfNotFull (E elem) {
        if (isFull()) return;
        try {
            insertRight(elem);
        } catch (SeqFullE e) {
            throw new Error("Internal bug!");
        }
    }

    void removeLeftIfNotEmpty () {
        if (left == 0) return;
        try {
            removeLeft();
        }
        catch (SeqAccessE e) {
            throw new Error("Internal bug!");
        }
    }

    void removeRightIfNotEmpty () {
        if (right == capacity - 1) return;
        try {
            removeRight();
        }
        catch (SeqAccessE e) {
            throw new Error("Internal bug!");
        }
    }
}