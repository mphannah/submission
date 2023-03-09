import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a wrapper around our finite sequence implementation
 * that specializes the element type to a hash table entry
 *
 * It delegates most methods to the finite sequence class
 */

public class Chain<K,V> implements Iterable<HEntry<K,V>> {
    private final FS<HEntry<K,V>> entries;

    Chain () { entries = new FS<>(10); }

    // for debugging

    FS<HEntry<K,V>> getEntries () { return entries; }

    //

    int size () { return entries.size(); }

    boolean isEmpty () { return entries.isEmpty(); }

    // No duplicate keys allowed
    boolean insert (HEntry<K,V> e) {
        if (! containsKey(e.key())) {
            entries.insertLeft(e);
            return true;
        }
        return false;
    }

    V get (K key) {
        for (HEntry<K,V> entry : entries) {
            if (entry.key().equals(key)) return entry.value();
        }
        throw new NoSuchElementException();
    }

    boolean containsKey (K key) {
        return entries.anyMatch(e -> e.key().equals(key));
    }

    public Iterator<HEntry<K,V>> iterator () {
        return entries.iterator();
    }

    public String toString () {
        return entries.toString();
    }
}
