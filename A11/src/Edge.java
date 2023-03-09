public record Edge(Node source, Node destination, Distance weight) {

    Edge flip() { return new Edge(destination, source, weight); }

    public boolean equals(Object o) {
        if (o instanceof Edge other)
            return other.source.equals(source) &&
                    other.destination.equals(destination) &&
                    other.weight.equals(weight);
        return false;
    }

    public int hashCode() {
        return source.hashCode() + 7 * destination.hashCode() + 13 * weight.hashCode();
    }

    public String toString() {
        return String.format("%s --%s--> %s", source, weight, destination);
    }
}
