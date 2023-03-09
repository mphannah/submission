public class Infinity extends Distance {
    private static final Infinity inf = new Infinity();

    static Infinity getInstance () { return inf; }

    //

    private Infinity () {}

    Distance add(Distance d) { return inf; }

    public int compareTo(Distance o) { return 1; }

    public boolean equals (Object o) { return o instanceof Infinity; }

    public int hashCode () { return 53; }

    public String toString () { return "*"; }
}
