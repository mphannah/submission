import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @Test
    public void trie () {
        Trie t = new Trie();
        t.insert("to");
        t.insert("tea");
        t.insert("ted");
        t.insert("ten");
        t.insert("in");
        t.insert("inn");
        t.insert("A");

        assertTrue(t.contains("ten"));
        assertTrue(t.contains("in"));
        assertTrue(t.contains("inn"));
        assertFalse(t.contains("tenn"));
        assertFalse(t.contains("te"));
        assertTrue(t.possiblePrefix(""));
        assertTrue(t.possiblePrefix("t"));
        assertTrue(t.possiblePrefix("to"));
        assertTrue(t.possiblePrefix("te"));
        assertTrue(t.possiblePrefix("i"));
        assertFalse(t.possiblePrefix("tu"));
    }

    @Test
    public void dict () throws IOException {
        Trie trie = new Trie();
        Scanner scanner = new Scanner(Paths.get("commonwords.txt"));
        while (scanner.hasNext()) trie.insert(scanner.next());

        assertTrue(trie.contains("abandon"));
        assertTrue(trie.contains("abandoned"));
        assertTrue(trie.contains("abandonment"));
        assertFalse(trie.contains("abandonmenth"));
        assertFalse(trie.contains("abando"));
        assertFalse(trie.contains("aband"));
        assertFalse(trie.contains("aban"));
        assertFalse(trie.contains("aba"));
        assertFalse(trie.contains("ab"));
        assertFalse(trie.contains("a"));
        assertTrue(trie.possiblePrefix("abando"));
        assertTrue(trie.possiblePrefix("aband"));
        assertTrue(trie.possiblePrefix("aban"));
        assertTrue(trie.possiblePrefix("aba"));
        assertTrue(trie.possiblePrefix("ab"));
        assertTrue(trie.possiblePrefix("a"));
        assertTrue(trie.possiblePrefix("abandon"));
    }

    @Test
    public void myTests() {
        Trie t = new Trie();

        t.insert("Indiana");
        t.insert("indiana");
        t.insert("India");
        t.insert("india");
        t.insert("Illinois");
        t.insert("In");
        t.insert("Inn");
        t.insert("inn");

        assertTrue(t.contains("indiana"));
        assertTrue(t.contains("india"));
        assertTrue(t.contains("Indiana"));
        assertFalse(t.contains("tenn"));
        assertFalse(t.contains("in"));
        assertTrue(t.possiblePrefix("In"));
        assertTrue(t.possiblePrefix("Ill"));
        assertTrue(t.possiblePrefix("in"));
        assertTrue(t.possiblePrefix("I"));
        assertTrue(t.possiblePrefix("i"));
        assertFalse(t.possiblePrefix("ill"));
    }
}