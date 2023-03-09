import java.util.List;

public record WordList (List<String> words) implements WordCollection {

    /**
     * See WordListTest for the expected behavior of these two methods.
     */
    public boolean contains (String w) {
        if (words.contains(w)) {
            return true;
        }
        return false; // TODO
    }

    public boolean possiblePrefix (String w) {
        for (int i = 0; i < words().size(); i++) {
            if (words.get(i).startsWith(w)) {
                return true;
            }
        }
        return false; // TODO
    }
}
