import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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

//-------------------------------------------------------------

/**
 * A simplified version of the Java library's stream interface
 */

interface SmallStream<E> extends Iterable<E> {
    int size ();
    <U> SmallStream<U> map (Function<? super E, ? extends U> fun);
    <U> U reduceR(U identity, BiFunction<U,E,U> accumulator);
    boolean allMatch(Predicate<? super E> predicate);
    boolean anyMatch(Predicate<? super E> predicate);
    SmallStream<E> filter(Predicate<? super E> predicate);
    Optional<E> findAny();
    Optional<E> min(Comparator<? super E> comparator);
}

//-------------------------------------------------------------

class LRFiniteSequence<E> implements Iterable<E>, SmallStream<E> {
    private Optional<E>[] elements;
    private int capacity;
    private int left;
    private int right;
    private int size;
    private final Function<Integer,Integer> growthStrategy;

    LRFiniteSequence (int capacity) {
        assert capacity > 0 : "capacity must be > 0";
        this.growthStrategy = n -> n * 2;
        this.init(capacity);
    }

    @SuppressWarnings("unchecked")
    private void init (int capacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        this.capacity = capacity;
        left = 0;
        right = this.capacity - 1;
        size = 0;
    }

    public void clear() { this.init(16); }

    public Optional<E>[] toArray () { return elements; }

    public int getCapacity () { return capacity; }

    public int getLeft () { return left; }

    public int getRight () { return right; }

    public int size () { return size; }

    public boolean isEmpty () { return size == 0; }

    public boolean isFull () { return size == capacity; }

    public String toString () { return Arrays.toString(elements); }

    // ---------------------------------------------------------------
    // Can get any element but an index is unreliable as elements
    // move around when the dequeue is resized
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
    public void resize () {
        int newCapacity = growthStrategy.apply(capacity);
        Optional<E>[] newElements =
                (Optional<E>[]) Array.newInstance(Optional.class, newCapacity);
        Arrays.fill(newElements, Optional.empty());
        for (int i=0; i<size; i++) {
            newElements[i] = elements[Math.floorMod(right+1+i, capacity)];
        }
        elements = newElements;
        capacity = newCapacity;
        left = size;
        right = capacity - 1;
    }

    // -------------------------------------------------------------------
    // Linear search

    public boolean contains(Object o) {
        return anyMatch(o::equals);
    }

    // -------------------------------------------------------------------
    // Iterable implementation
    // The behavior is unspecified if elements are removed after
    // the iterator is started

    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = Math.floorMod(right + 1, capacity);
            private int b = 0;

            public boolean hasNext() {
                return b < size;
            }

            public E next() {
                if (b == size) throw new NoSuchElementException();
                try {
                    E res = get(index);
                    index = Math.floorMod(index + 1, capacity);
                    b++;
                    return res;
                } catch (SeqAccessE e) {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    // -------------------------------------------------------------------
    // Collection methods

    public void insertLeftAll (Collection<? extends E> c) {
        c.forEach (this::insertLeft);
    }

    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    // -------------------------------------------------------------------
    // Stream methods

    /**
     * The specifications of the following methods are the same as the corresponding methods
     * in the java.util Stream interface
     */

    public <U> SmallStream<U> map (Function<? super E, ? extends U> fun) {
        LRFiniteSequence smallStream = new LRFiniteSequence(elements.length);
        U newElem;

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                newElem = fun.apply(elements[i].get());
                smallStream.elements[i] = Optional.of(newElem);
            }
        }
        smallStream.size = size;

        return smallStream; // TODO
    }

    public <U> U reduceR(U identity, BiFunction<U,E,U> accumulator) {
        U value = identity;

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                value = accumulator.apply(value, elements[i].get());
            }
        }

        return value; // TODO
    }

    public boolean allMatch(Predicate<? super E> predicate) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                if (!predicate.test(elements[i].get())) {
                    return false;
                }
            }
        }
        return true; // TODO
    }

    public boolean anyMatch(Predicate<? super E> predicate) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                if (predicate.test(elements[i].get())) {
                    return true;
                }
            }
        }
        return false; // TODO
    }

    public SmallStream<E> filter(Predicate<? super E> predicate) {
        LRFiniteSequence smallStream = new LRFiniteSequence(capacity);

        for (int i = 0, j = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                if (predicate.test(elements[i].get())) {
                    smallStream.elements[j] = Optional.of(elements[i].get());
                    smallStream.size += 1;
                    j++;
                }
            }
        }

        return smallStream; // TODO
    }

    public Optional<E> findAny() {
        if (elements.length > 0) {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i].isPresent()) {
                    return elements[i];
                }
            }
            return elements[0];
        }
         return null; // TODO
    }

    public Optional<E> min(Comparator<? super E> comparator) {
        List<Optional<E>> minList = new ArrayList<>();

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isPresent()) {
                minList.add(elements[i]);
            }
        }
        Optional<E> min = minList.get(0);
        for (Optional<E> e : minList) {
            if (comparator.compare(min.get(), e.get()) > 0) {
                min = e;
            }
        }

        return min; // TODO
    }
}
