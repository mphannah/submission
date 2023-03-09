import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class SeamCarving {
    private final String filename;
    private Image img;

    SeamCarving (String filename) throws IOException {
        this.filename = filename;
        img = new Image(filename);
    }

    void write (String prefix) throws IOException {
        img.write(prefix + filename);
    }

    Image getImg () { return img; }

    /**
     * The method finds the seam with the least energy starting
     * from the current pixel and going down to the last row.
     * Here we just write a recursive solution that may
     * recompute subproblems.
     *
     * If the current pixel is at the bottom row, we return
     * a path that contains just the current pixel and its
     * energy.
     *
     * If the current cell is somewhere other than the last row,
     * then we compute the best seam from each of its southern
     * neighbors and take the minimum.
     *
     * A short elegant solution is possible using the map and
     * min methods on streams.
     */
    PathAndCost findSeamFrom (Image.Pixel px) {
        if (px.inLastRow()) {
            return new PathAndCost(new NonEmptyPath<>(px, new EmptyPath<>()), img.computeEnergy(px));
        }

        Image.Pixel[] energies = new Image.Pixel[3];

        if (px.south().isPresent()) {
            energies[0] = px.south().orElseThrow();
        } if (px.southEast().isPresent()) {
            energies[1] = px.southEast().orElseThrow();
        } if (px.southWest().isPresent()) {
            energies[2] = px.southWest().orElseThrow();
        }

        Image.Pixel newPixel = energies[0];
        for (int i = 1; i < energies.length; i++) {
            if (energies[i] != null) {
                if (img.computeEnergy(newPixel) > img.computeEnergy(energies[i])) {
                    newPixel = energies[i];
                }
            }
        }

        PathAndCost pathAndCost = findSeamFrom(newPixel);

        return new PathAndCost(pathAndCost.seam(), pathAndCost.cost()).add(px, img.computeEnergy(px)); // TODO
    }

    /**
     * The method finds all the seams that start at the top
     * row and returns the minimum.
     */
    Path<Image.Pixel> bestSeam () {
        Set<Image.Pixel> pxSet = img.topRow();
        Object[] pxArray = pxSet.toArray();
        PathAndCost pathAndCost = findSeamFrom((Image.Pixel) pxArray[0]);

        for (int i = 0; i < pxArray.length; i++) {
            PathAndCost pathAndCost1 = findSeamFrom((Image.Pixel) pxArray[i]);
            if (pathAndCost.compareTo(pathAndCost1) > 0) {
                pathAndCost = pathAndCost1;
            }
        }
        return pathAndCost.seam(); // TODO
    }

    void cutN (int n) {
        for (int i=0; i<n; i++) img = img.cutSeam(bestSeam());
    }
}

