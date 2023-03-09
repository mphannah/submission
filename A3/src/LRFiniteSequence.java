import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

class SeqAccessE extends Exception {}

/**
 * This in an implementation of a dynamically resizable dequeue.
 * Most operations run in amortized constant time
 *
 * In general the values are stored in the array as follows:
 *
 * |-------------------------|
 * | 4 5 6 _ _ _ _ _ _ 1 2 3 |
 * |-------------------------|
 *        /\        /\      /\
 *       left      right  capacity
 *
 * left and right typically point to the next available slot
 * all arithmetic modulo capacity
 * data stored at right+1, right+2, ... left-2, left-1
 *
 * For this assignment we take the A2 solution and add a few
 * methods to make our implementation closer to that of
 * java.util.ArrayDeque<E>
 *
 */

class LRFiniteSequence<E> implements Iterable<E> {
    private Optional<E>[] elements;
    private int capacity;
    private int left;
    private int right;
    private int size;
    private final Function<Integer,Integer> growthStrategy;

    @SuppressWarnings("unchecked")
    LRFiniteSequence (int capacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        this.capacity = capacity;
        left = 0;
        right = capacity - 1;
        size = 0;
        this.growthStrategy = n -> n * 2;
    }

    /**
     * The method 'clear' overwrites the current array with a new array of size 16
     * All the elements of the new array are Optional.empty
     * All the instance variables are adjusted accordingly
     */
    public void clear() {
        capacity = 16;
        size = 0;
        left = 0;
        right = capacity - 1;
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
    }

    public Optional<E>[] toArray () { return elements; }

    public int getCapacity () { return capacity; }

    public int getLeft () { return left; }

    public int getRight () { return right; }

    public int size () { return size; }

    public boolean isEmpty () { return size == 0; }

    public boolean isFull () { return size == capacity; }

    public String toString () { return Arrays.toString(elements); }

    // ---------------------------------------------------------------
    // A2 solutions with additional peek methods
    // Can get any element
    // Can add, peek, or remove elements at either endpoint.

    public E get (int index) throws SeqAccessE {
        if (index < 0 || index >= capacity) throw new SeqAccessE();
        return elements[index].orElseThrow(SeqAccessE::new);
    }

    public void insertLeft (E elem) {
        if (isFull()) resize();
        elements[left] = Optional.of(elem);
        left = Math.floorMod(left+1, capacity);
        size++;
    }

    public void insertRight (E elem) {
        if (isFull()) resize();
        elements[right] = Optional.of(elem);
        right = Math.floorMod(right-1, capacity);
        size++;
    }

    public E peekLeft () throws SeqAccessE {
        int index = Math.floorMod(left - 1, capacity);
        return get(index);
    }

    public E peekRight () throws SeqAccessE {
        int index = Math.floorMod(right + 1, capacity);
        return get(index);
    }

    public E removeLeft () throws SeqAccessE {
        int index = Math.floorMod(left - 1, capacity);
        E res = elements[index].orElseThrow(SeqAccessE::new);
        elements[index] = Optional.empty();
        left = index;
        size--;
        return res;
    }

    public E removeRight () throws SeqAccessE {
        int index = Math.floorMod(right + 1, capacity);
        E res = elements[index].orElseThrow(SeqAccessE::new);
        elements[index] = Optional.empty();
        right = index;
        size--;
        return res;
    }

    @SuppressWarnings("unchecked")
    void resize () {
        int newCapacity = growthStrategy.apply(capacity);
        Optional<E>[] newElements =
                (Optional<E>[]) Array.newInstance(Optional.class, newCapacity);
        Arrays.fill(newElements, Optional.empty());
        for (int i=0; i<capacity; i++) {
            newElements[i] = elements[Math.floorMod(right+1+i, capacity)];
        }
        elements = newElements;
        left = capacity;
        capacity = newCapacity;
        right = capacity - 1;
    }

    // -------------------------------------------------------------------

    /**
     * Performs a linear search through the array of elements looking
     * for the given object.
     *
     * It is more elegant to write this method after implementing the
     * iterator methods
     *
     */
    public boolean contains(Object o) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].get() == o) {
                return true;
            }
        }
        // TODO
        return false;
    }

    // -------------------------------------------------------------------
    // Iterators
    // The behavior is unspecified if elements are removed after
    // the iterator is started so there is no need to complicate
    // the implementations

    /**
     * Return an iterator that gives access to the elements in
     * the order right+1, right+2 ...
     * Use the template below for the anonymous inner class
     * For the exceptions, follow the Java specification
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            public boolean hasNext() {
                int index = Math.floorMod(right + 1, capacity);
                if (elements[index].isPresent()) {
                    right = index;
                    return true;
                }
                // TODO
                return false;
            }

            public E next() {
                E elem = elements[right].get();
                elements[right] = Optional.empty();
                return elem;
                // TODO
            }
        };
    }

    /**
     * Return an iterator that gives access to the elements in
     * the order left-1, left-2 ...
     * Use the template below for the anonymous inner class
     * For the exceptions, follow the Java specification
     */
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            public boolean hasNext() {
                int index = Math.floorMod(left - 1, capacity);
                if (elements[index].isPresent()) {
                    left = index;
                    return true;
                }
                // TODO
                return false;
            }

            public E next() {
                E elem = elements[left].get();
                elements[left] = Optional.empty();
                return elem;
                // TODO
            }
        };
    }

    // -------------------------------------------------------------------
    // Collection methods

    /**
     * Insert all the elements in the given collection in the current
     * dequeue. The easiest solution is to use c.forEach(...) to
     * access the elements in 'c' one at a time.
     */
    public void insertLeftAll (Collection<? extends E> c) {
        for (E elem : c) {
            insertLeft(elem);
        }
        // TODO
    }

    /**
     * Checks if all the elements in 'c' are contained in the current
     * dequeue. The easiest solution is to convert 'c' to a stream and
     * use one of the stream methods.
     */
    public boolean containsAll(Collection<?> c) {
        boolean inElements = c.stream()
                .allMatch(elem -> contains(elem));

        if (inElements) {
            return true;
        }
        // TODO
        return false; 
    }
}
