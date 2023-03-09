public class Finite extends Distance {
    private final int d;

    Finite (int d) { this.d = d; }

    Distance add(Distance dist) {
        if (dist instanceof Finite fin)
            return new Finite(d + fin.d);
        else return Infinity.getInstance();
    }

    public int compareTo(Distance o) {
        if (o instanceof Finite fin)
            return Integer.compare(d,fin.d);
        else return -1;
    }

    public boolean equals (Object o) {
        if (o instanceof Finite fin) return d == fin.d;
        return false;
    }

    public int hashCode () { return d; }

    public String toString () { return String.valueOf(d); }
}
