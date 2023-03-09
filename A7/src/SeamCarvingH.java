import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.WeakHashMap;

/**
 * Here we override the seam methods to avoid recomputing
 * repeated subproblems.
 */

public class SeamCarvingH extends SeamCarving {

    private final WeakHashMap<Image.Pixel,PathAndCost> hm;

    SeamCarvingH (String filename) throws IOException {
        super(filename);
        hm = new WeakHashMap<>();
    }

    PathAndCost findSeamFrom(Image.Pixel px) {
        if (hm.containsKey(px)) return hm.get(px);
        PathAndCost r = super.findSeamFrom(px);
        hm.put(px,r);
        return r;
    }

    Path<Image.Pixel> bestSeam() {
        hm.clear();
        return super.bestSeam();
    }

    //----------------------------------------------------------

    public static void main (String[] args) throws IOException {
        Collection<String> images =
                Arrays.asList(
                        "board.png",
                        "moto.jpg",
                        "tree.jpg",
                        "eye.png",
                        "earth.png",
                        "cat.jpg",
                        "pizza.jpg",
                        "horse.jpg",
                        "bball.jpg",
                        "car.jpg",
                        "scooter.jpg",
                        "dog.jpg",
                        "road.jpg",
                        "tiger.jpg",
                        "eagle.jpg",
                        "rooster.jpg"
                );

        for (String filename : images) {
            SeamCarving sc = new SeamCarvingH(filename);
            sc.cutN(sc.getImg().getWidth() / 3);
            sc.write("twothirds-");
        }
    }
}
