public class NonEmptyPath<E> extends Path<E> {
    private final E elem;
    private final Path<E> path;
    private final int size;
    private final int hash;

    NonEmptyPath (E elem, Path<E> path) {
        this.elem = elem;
        this.path = path;
        this.size = path.size() + 1;
        this.hash = elem.hashCode() + 31 * path.hashCode();
    }

    int size () { return size; }

    E getE () { return elem; }

    Path<E> getRest () { return path; }

    public boolean equals (Object other) {
        if (other instanceof NonEmptyPath npe)
            return elem.equals(npe.elem) && path.equals(npe.path);
        return false;
    }

    public int hashCode () { return hash; }
}
