/* Hash maps with chaining */

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.function.Function;

// ------------------------------------------------------------------------

/**
 * A hashmap with chaining
 */

public class HM<K,V> {
    private int capacity;
    private int size;
    private Function<? super K, Integer> hfun;
    private Chain<K, V>[] chains;

    HM(int capacity) {
        this(capacity, k -> Math.floorMod(k.hashCode(), capacity));
    }

    @SuppressWarnings("unchecked")
    HM(int capacity, Function<? super K, Integer> hfun) {
        this.size = 0;
        this.capacity = capacity;
        this.hfun = hfun;
        chains = (Chain<K, V>[]) Array.newInstance(Chain.class, capacity);
        for (int i=0; i<capacity; i++) chains[i] = new Chain<>();
    }

    // for debugging

    public Chain<K, V>[] getChains() { return chains; }

    //

    int size () { return size; }

    @SuppressWarnings("unchecked")
    void clear() {
        this.size = 0;
        chains = (Chain<K, V>[]) Array.newInstance(Chain.class, capacity);
        for (int i=0; i<capacity; i++) chains[i] = new Chain<>();
    }

    boolean put(K key, V value) {
        HEntry<K, V> e = new HEntry<>(key, value);
        int index = hfun.apply(key);
        if (chains[index].insert(e)) {
            size++;
            return true;
        }
        else return false;
    }

    V get(K key) {
        int index = hfun.apply(key);
        return chains[index].get(key);
    }

    boolean containsKey(K key) {
        int index = hfun.apply(key);
        return chains[index].containsKey(key);
    }

    @SuppressWarnings("unchecked")
    void rehash() {
        int newCapacity = BigInteger.valueOf(capacity* 2L).nextProbablePrime().intValue();
        this.hfun = k -> Math.floorMod(k.hashCode(), newCapacity);

        Chain<K, V>[] newChains = (Chain<K, V>[]) Array.newInstance(Chain.class, newCapacity);
        for (int i=0; i<newCapacity; i++) newChains[i] = new Chain<>();

        for (int i = 0; i < capacity; i++) {
            for (HEntry<K, V> ec : chains[i]) {
                int newIndex = hfun.apply(ec.key());
                newChains[newIndex].insert(ec);
            }
        }

        this.capacity = newCapacity;
        this.chains = newChains;
    }
}

// ------------------------------------------------------------------------