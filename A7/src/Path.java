public abstract class Path<E> {
    abstract int size ();
    abstract E getE () throws EmptyPathE;
    abstract Path<E> getRest () throws EmptyPathE;

    static <E> Path<E> singleton (E elem) {
        return new NonEmptyPath<>(elem, new EmptyPath<>());
    }

    static int[] xPositions (Path<Image.Pixel> pxs) {
        int[] result = new int[pxs.size()];
        int index = 0;
        try {
            while (true) {
                result[index++] = pxs.getE().x;
                pxs = pxs.getRest();
            }
        }
        catch (EmptyPathE e) {
            return result;
        }
    }
}
