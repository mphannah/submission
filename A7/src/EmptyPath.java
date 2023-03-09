public class EmptyPath<E> extends Path<E> {

    int size () { return 0; }

    E getE() throws EmptyPathE { throw new EmptyPathE(); }

    Path<E> getRest () throws EmptyPathE { throw new EmptyPathE(); }

    public boolean equals (Object other) { return other instanceof EmptyPath; }

    public int hashCode () { return 17; }

}
