public record PathAndCost(Path<Image.Pixel> seam, int cost)
        implements Comparable<PathAndCost> {

    PathAndCost add (Image.Pixel px, int c) {
        return new PathAndCost(new NonEmptyPath<>(px, seam), c + cost);
    }

    public int compareTo(PathAndCost other) {
        return Integer.compare(cost, other.cost);
    }
}
