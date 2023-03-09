import java.util.Hashtable;

/**
 * In this class, we will maintain the list of words much more efficiently
 * by sharing common prefixes.
 *
 * Say we have a tiny dictionary with these words:
 *
 *                 "to", "tea", "ted", "ten", "in", "inn", "A"
 *
 * Instead of storing them as a list of strings, we will store them this way:
 *
 *                                ""
 *                           /    /      \
 *                       "A"*   "I"         "T"
 *                              /        /      \
 *                            "N"*    "O"*       "E"
 *                           /               /    |     \
 *                         "N"*           "A"*   "D"*   "N"*
 *
 * A word stored as a sequence of letters stored at tree nodes. The path from
 * the root to any leaf is definitely a word. But it is also possible that
 * a complete word is a prefix of another (e.g. "in" is a prefix of "inn" and
 * both are valid words). To distinguish these situations from prefixes
 * like "te" that are not words themselves, we use a marker (*) in the picture
 * above to record when a particular internal node is also a complete word.
 *
 * In other to implement such tree, we have an additional small challenge:
 * the number of nodes is irregular. Sometimes we have 0 children, sometimes 1,
 * and sometimes many. To accommodate this variation, we will collect all
 * the children in a Hashtable whose keys are letters and whose values
 * are the trees rooted at each character.
 *
 */

public class Trie implements WordCollection {
    private boolean endsHere;
    private final Hashtable<Character,Trie> children;

    Trie () {
        this.endsHere = false;
        this.children = new Hashtable<>();
    }

    /**
     * To insert word 's' in this tree, we consider several cases:
     *  - the word is empty: we have reached the end of a word so we set the
     *    "endsHere" flag
     *  - the word starts with character 'c' and this is the first time we see
     *    a word that starts with 'c'; we create a new entry in the hash table
     *    of children with 'c' as the key and an empty tree as the value
     *  - then we recursively insert the rest of the string
     */
    void insert(String s) {
        if (s.isEmpty()) {
            endsHere = true;
            return;
        }

        Trie t = new Trie();

        if (children.containsKey(s.charAt(0))) {
            t = children.get(s.charAt(0));
        }

        children.put(s.charAt(0), t);
        t.insert(s.substring(1));
        // TODO
    }

    /**
     * This method takes a string 's' and checks if it is in the current
     * tree. The flag "fullWord" tells us if we are looking for a full
     * word or a prefix. Assuming we have the tree at the top of the file,
     *   search("TE", false)
     * is asking if "TE" is a possible prefix: the answer is yes. In
     * contrast:
     *   search("TE", true)
     * is asking if "TE" is a full word: the answer is no.
     */
    boolean search (String s, boolean fullWord) {
        if (s.isEmpty()) {
            if (!fullWord) {
                return true;
            } else {
                if (endsHere) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (children.containsKey(s.charAt(0))) {
            Trie t = children.get(s.charAt(0));
            return t.search(s.substring(1), fullWord);
        }
        return false; // TODO
    }

    public boolean contains(String s) {
        return search(s,true);
    }

    public boolean possiblePrefix (String s) {
        return search(s,false);
    }

    public String toString () {
        return children.toString();
    }
}
