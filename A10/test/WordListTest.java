import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordListTest {

    @Test
    public void simple () {
        WordList t = new WordList(Arrays.asList(
                "to", "tea", "ted", "ten", "in", "inn", "A"));
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
        List<String> list = Files.readAllLines(Paths.get("commonwords.txt"));
        WordList words = new WordList(list);

        assertTrue(words.contains("abandon"));
        assertTrue(words.contains("abandoned"));
        assertTrue(words.contains("abandonment"));
        assertFalse(words.contains("abandonmenth"));
        assertFalse(words.contains("abando"));
        assertFalse(words.contains("aband"));
        assertFalse(words.contains("aban"));
        assertFalse(words.contains("aba"));
        assertFalse(words.contains("ab"));
        assertFalse(words.contains("a"));
        assertTrue(words.possiblePrefix("abando"));
        assertTrue(words.possiblePrefix("aband"));
        assertTrue(words.possiblePrefix("aban"));
        assertTrue(words.possiblePrefix("aba"));
        assertTrue(words.possiblePrefix("ab"));
        assertTrue(words.possiblePrefix("a"));
        assertTrue(words.possiblePrefix("abandon"));
    }

    @Test
    public void myTests() {
        WordList t = new WordList(Arrays.asList(
                "Indiana", "indiana", "India", "india", "Illinois", "In", "Inn", "inn"));

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