import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

class SeqAccessE extends Exception {}

//
// Just like in A1 the general case will look like this:
//
// |-------------------------|
// | 4 5 6 _ _ _ _ _ _ 1 2 3 |
// |-------------------------|
//         /\        /\      /\
//        left      right  capacity
//
//
// There are two main extensions to A1:
//    - the two ends of the array are connected as if the array was circular.
//      Technically all arithmetic is modulo capacity. We will always maintain
//      that the elements are stored at locations:
//      right+1, right+2, ... left-2, left-1
//    - when the array is full, it is resized; the default strategy is to
//      double the size
//

class LRFiniteSequence<E> {
    private Optional<E>[] elements;
    private int capacity;
    private int left;
    private int right;
    private int size;
    private Function<Integer,Integer> growthStrategy;

    /**
     * The constructor is like the one for A1 but additionally
     * stores a growthStrategy that dictates how to grow the
     * size of the array when it is full
     */

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

    LRFiniteSequence (int capacity, Function<Integer,Integer> growthStrategy) {
        this(capacity);
        this.growthStrategy = growthStrategy;
    }

    public int getCapacity () { return capacity; }

    public int size () { return size; }

    public boolean isEmpty () { return size == 0; }

    public boolean isFull () { return size == capacity; }

    /**
     * Returns the element at the given index in normal cases.
     * If the index is out of bounds or if the current index
     * is empty, the exception is thrown instead
     */
    public E get (int index) throws SeqAccessE {
        if (index < 0 || index >= capacity) throw new SeqAccessE();
        return elements[index].orElseThrow(SeqAccessE::new);
        // TODO
    }

    /**
     * In normal cases, the element is stored in the position indexed
     * by 'left' and then 'left' is incremented (modulo the capacity).
     * If the array is full, it is first resized by calling the method
     * resize()
     */
    public void insertLeft (E elem) {
        if (isFull()) {
            resize();
        }
        if (left < 0) {
            left = capacity + left;
        } if (left > capacity-1) {
            left = 0;
        }

        elements[left] = Optional.of(elem);
        left++;
        size++;
        System.out.println(Arrays.toString(elements));
        // TODO
    }

    /**
     * Symmetric to insertLeft
     */
    public void insertRight (E elem) {
        if (isFull()) {
            resize();
        }
        if (right > capacity) {
            right = right - capacity;
        } if (right < 0) {
            right = capacity-1;
        }

        elements[right] = Optional.of(elem);
        right--;
        size++;
        System.out.println(Arrays.toString(elements));
        // TODO
    }

    /**
     * The element at index 'left-1' (modulo capacity) is
     * returned and its position is marked as empty. The
     * exception is throw if no element was present.
     */
    public E removeLeft () throws SeqAccessE {
        int index = left - 1;
        if (index < 0) {
            index = capacity + left-1;
        }
        E res = elements[index].orElseThrow(SeqAccessE::new);
        elements[index] = Optional.empty();
        left--;
        size--;
        return res; // TODO
    }

    /**
     * Symmetric to removeLeft
     */
    public E removeRight () throws SeqAccessE {
        int index = right + 1;
        if (right >= capacity - 1) {
            index = right - (capacity-1);
        }
        E res = elements[index].orElseThrow(SeqAccessE::new);
        elements[index] = Optional.empty();
        right++;
        size--;
        return res; // TODO
    }

    /**
     * The method performs the following actions:
     *   - a new capacity is calculated using the growthStrategy
     *   - a new array is allocated with the new capacity and filled with Optional.empty
     *   - the elements in the old array are copied in the following order: the element
     *     at right+1 in the old array is copied to index 0 in the new array
     */
    @SuppressWarnings("unchecked")
    void resize () {
        int rightPos = right + 1;
        int leftPos = 0;
        Optional<E>[] oldElements = elements;
        capacity = growthStrategy.apply(elements.length);
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());

        for (int i = 0; i < oldElements.length; i++) {
            if (rightPos < oldElements.length) {
                elements[i] = oldElements[rightPos];
                rightPos++;
            } else {
                elements[i] = oldElements[leftPos];
                leftPos++;
            }
        }
        right = capacity - 1;
        left = oldElements.length;
        size = oldElements.length;
        // TODO
    }

    public String toString () {
        return Arrays.toString(elements);
    }
}
