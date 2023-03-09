import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * This is a wrapper around our finite sequence implementation
 * that specializes the element type to a hashtable entry
 *
 * It delegates most methods to the finite sequence class
 */

public class Chain<K,V> implements Iterable<HEntry<K,V>> {
    private final FS<HEntry<K,V>> entries;

    // capacity will always be a prime number
    Chain () { entries = new FS<>(7); }

    // for debugging

    FS<HEntry<K,V>> getEntries () { return entries; }

    //

    int size () { return entries.size(); }

    boolean isEmpty () { return entries.isEmpty(); }

    /**
     * No entries with duplicate keys are allowed.
     *
     * If the given entry has a key that is already in the chain,
     * the entry is NOT inserted and the method returns false
     *
     * Otherwise, the entry is inserted to the left of the
     * sequence, and the method returns true
     */
    boolean insert (HEntry<K,V> e) {
        if (!containsKey(e.key())) {
            entries.insertLeft(e);
            return true;
        }
        return false; // TODO
    }

    /**
     * If the current chain does not have an entry with the given key,
     * throw the NoSuchElementException
     *
     * Otherwise, return the value corresponding to the given key
     */
    V get (K key) {
        if (containsKey(key)) {
            return (V) entries.filter(x -> x.key().equals(key))
                    .reduceR(0, (x, y) -> x + (Integer) y.value());
        }

        throw new NoSuchElementException(); // TODO
    }

    /**
     * Checks if the current chain has an entry with the given key
     */
    boolean containsKey (K key) {
        if (entries.anyMatch(x -> x.key().equals(key))) {
            return true;
        }
        return false; // TODO
    }

    public Iterator<HEntry<K,V>> iterator () {
        return entries.iterator();
    }

    public String toString () {
        return entries.toString();
    }
}
