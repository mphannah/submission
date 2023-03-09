/* Hash maps with chaining */

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.function.Function;

// ------------------------------------------------------------------------

/**
 * A hashmap with chaining
 *
 * The starter code is missing a few methods marked with TODO as usual.
 * Additionally, the given code has a SUBTLE BUG that was intentionally
 * introduced. The given tests will not pass unless you correct this bug
 *
 */

public class HM<K,V> {
    private int capacity;
    private int size;
    private Function<? super K, Integer> hfun;
    private Chain<K, V>[] chains;

    /**
     * Use a default hash function built upon Java's hashCode
     */
    HM(int capacity) {
        this(capacity, k -> k.hashCode() % capacity);
    }

    /**
     * Use the given hash function
     */
    @SuppressWarnings("unchecked")
    HM(int capacity, Function<? super K, Integer> hfun) {
        this.size = 0;
        this.capacity = capacity;
        this.hfun = hfun;
        chains = (Chain<K, V>[]) Array.newInstance(Chain.class, capacity);
        for (int i = 0; i < chains.length; i++) {
            chains[i] = new Chain<>();
        }
    }

    // for debugging

    public Chain<K, V>[] getChains() { return chains; }

    //

    int size () { return size; }

    @SuppressWarnings("unchecked")
    void clear() {
        this.size = 0;
        chains = (Chain<K, V>[]) Array.newInstance(Chain.class, capacity);
        for (int i = 0; i < chains.length; i++) {
            chains[i] = new Chain<>();
        }
    }

    /**
     * If the given key already occurs in the hash table, return false without inserting
     * anything
     *
     * Otherwise, insert the key-value entry in the appropriate chain and return true
     */
    boolean put(K key, V value) {
        HEntry<K,V> hEntry = new HEntry<>(key, value);
        int index = hfun.apply(key);

        if (containsKey(key)) {
            return false;
        }

        chains[index].insert(hEntry);
        this.size += 1;
        return true;// TODO
    }

    /**
     * Returns the value associated with the given key or throw a NoSuchElementException
     * if the given key does not occur in the hash table
     */
    V get(K key) {
        int index = hfun.apply(key);

        return chains[index].get(key); // TODO
    }

    /**
     * Checks if the hash table has an entry with the given key
     */
    boolean containsKey(K key) {
        int index = hfun.apply(key);

        if (chains[index].containsKey(key)) {
            return true;
        }

        return false; // TODO
    }

    /**
     * This method increases the capacity of the hash table to the next prime
     * after 2 * capacity
     *
     * It then iterates over all the current entries, and inserts them in the
     * larger table
     */
    @SuppressWarnings("unchecked")
    void rehash() {
        int oldCapacity = capacity;
        int newCapacity = BigInteger.valueOf(capacity* 2L).nextProbablePrime().intValue();
        Chain<K, V>[] oldChains = chains;
        capacity = newCapacity;
        hfun = k -> k.hashCode() % capacity;
        size = 0;
        clear();


        for (int i = 0; i < oldCapacity; i++) {
            oldChains[i].getEntries().iterator().forEachRemaining(x -> put(x.key(), x.value()));
        }
        // TODO
    }
}

// ------------------------------------------------------------------------