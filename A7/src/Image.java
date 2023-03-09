import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Image implements Iterable<Image.Pixel> {
    private final BufferedImage img;
    private final int type;
    private final int width;
    private final int height;
    private final BiFunction<Integer,Integer,Integer> xyIndex;
    private final int[] rawPixels;
    private final HashSet<Pixel> pixels;

    // Reading and writing images

    Image (String filename) throws IOException {
        img = ImageIO.read(new File(filename));
        type = img.getType();
        width = img.getWidth();
        height = img.getHeight();
        xyIndex = (x,y) -> x + y * width;
        rawPixels = new int[width * height];
        img.getRGB(0, 0, width, height, rawPixels, 0, width);
        pixels = new HashSet<>();
        for (Image.Pixel px : this) pixels.add(px);
    }

    Image (int type, int width, int height, HashSet<Pixel> pixels) {
        img = new BufferedImage(width, height, type);
        this.type = type;
        this.width = width;
        this.height = height;
        xyIndex = (x,y) -> x + y * width;
        this.pixels = pixels;
        this.rawPixels = new int[width * height];
        for (Image.Pixel px : pixels) rawPixels[xyIndex.apply(px.x,px.y)] = px.color;
        img.setRGB(0, 0, width, height, rawPixels, 0, width);
    }

    void write (String filename) throws IOException {
        int dot = filename.lastIndexOf('.');
        String fmt = filename.substring(dot+1);
        ImageIO.write(img, fmt, new File(filename));
    }

    // For debugging

    int getType () { return type; }
    int getWidth () { return width; }
    int getHeight () { return height; }
    BiFunction<Integer,Integer,Integer> getXyIndex () { return xyIndex; }
    int[] getRawPixels () { return rawPixels; }
    HashSet<Image.Pixel> getPixels () { return pixels; }

    // Accessing pixels and their neighbors

    Pixel mkPixel (int x, int y) {
        return new Pixel(x, y, rawPixels[xyIndex.apply(x,y)]);
    }

    Set<Pixel> topRow () {
        HashSet<Pixel> result = new HashSet<>();
        for (int x=0; x<width; x++) result.add(mkPixel(x,0));
        return result;
    }

    /**
     * Return the neighbors north, east, south, and west of
     * the given pixel. Pixels on the boundary will have
     * fewer than 4 neighbors.
     */
    Set<Pixel> getHVneighbors(Pixel px) {
        Set<Pixel> pxSet = new HashSet<>();
        if (px.y > 0) {
            pxSet.add(px.north().orElseThrow());
        } if (px.x < img.getWidth()-1) {
            pxSet.add(px.east().orElseThrow());
        } if (!px.inLastRow()) {
            pxSet.add(px.south().orElseThrow());
        } if (px.x > 0) {
            pxSet.add(px.west().orElseThrow());
        }
        return pxSet; // TODO
    }

    /**
     * Return the neighbors southwest, south, and southeast
     * of the given pixel. Pixels on the boundary will have
     * fewer than 3 neighbors
     */
    Set<Pixel> getBelowNeighbors(Pixel px) {
        Set<Pixel> pxSet = new HashSet<>();
        if (!px.inLastRow()) {
            pxSet.add(px.south().orElseThrow());
        } if (!px.inLastRow() && px.x > 0) {
            pxSet.add(px.southWest().orElseThrow());
        } if (!px.inLastRow() && px.x < img.getWidth()-1) {
            pxSet.add(px.southEast().orElseThrow());
        }
        return pxSet; // TODO
    }

    // Computing energy at given pixel

    int computeEnergy (Pixel px) {
        Function<Integer, Integer> sq = n -> n * n;

        int energy = 0;
        Color c = px.getColor();
        for (Pixel npx : getHVneighbors(px)) {
            Color nc = npx.getColor();
            energy += sq.apply(nc.getRed() - c.getRed());
            energy += sq.apply(nc.getGreen() - c.getGreen());
            energy += sq.apply(nc.getBlue() - c.getBlue());
        }
        return energy;
    }

    /**
     * Return the pixels in row order: all the pixels
     * in the first row from left to right, then the
     * pixels in the next row from left to right, etc.
     */
    public Iterator<Pixel> iterator() {
        return new Iterator<Pixel>() {
            private int w = 0;
            private int l = 0;

            @Override
            public boolean hasNext() {
                return xyIndex.apply(w, l) < rawPixels.length-1;
            }

            @Override
            public Pixel next() {
                Pixel px = new Pixel(w, l, rawPixels[xyIndex.apply(w, l)]);
                if (w < width-1) {
                    w++;
                } else {
                    w = 0;
                    l++;
                }
                return px;
            }
        }; // TODO
    }

    /**
     * The method takes a path containing the pixels to
     * remove from the current. It constructs a new
     * image with these pixels removed.
     *
     * Using the method Path.xPositions will simplify
     * the implementation considerably.
     */
    Image cutSeam (Path<Image.Pixel> path) {
        int[] deletions = Path.xPositions(path);
        Image newImage = new Image(this.type, this.width, this.height, this.pixels);

        for (int i = 0; i < deletions.length; i++) {
            newImage.pixels.remove(new Pixel(deletions[i], i, rawPixels[xyIndex.apply(deletions[i], i)]));

            newImage.rawPixels[xyIndex.apply(deletions[i], i)] = 0;
        }

        return newImage; // TODO
    }

    // -------------------------------------------------------

    /**
     * The class Pixel is declared as an inner class of Image,
     * so that it can refer to the width and height of the
     * image.
     */

    public class Pixel {
        public final int x, y;
        public final int color;

        Pixel (int x, int y, int color) {
            this.x = x;
            this.y = y;
            this. color = color;
        }

        Color getColor () {
            return new Color(color, true);
        }

        /**
         * Return true is the current pixel is in the
         * last row of the image.
         */
        boolean inLastRow () {
            if (this.y == img.getHeight()-1) { //might have to remove "-1" from getHeight()
                return true;
            }
            return false; // TODO
        }

        /**
         * The next few methods return the pixel in
         * the given direction if it exists.
         */

        Optional<Pixel> north () {
            Optional<Pixel> px = Optional.empty();

            if (this.y > 0) {
                Pixel newPixel = new Pixel(this.x, this.y-1, rawPixels[xyIndex.apply(x,y-1)]);
                px = Optional.of(newPixel);

            }

            return px; // TODO
        }

        Optional<Pixel> east () {
            Optional<Pixel> px = Optional.empty();

            if (this.x < img.getWidth()-1) {
                Pixel newPixel = new Pixel(this.x+1, this.y, rawPixels[xyIndex.apply(x+1,y)]);
                    px = Optional.of(newPixel);
            }

            return px; // TODO
        }

        Optional<Pixel> south () {
            Optional<Pixel> px = Optional.empty();

            if (!this.inLastRow()) {
                Pixel newPixel = new Pixel(this.x, this.y+1, rawPixels[xyIndex.apply(x,y+1)]);
                    px = Optional.of(newPixel);
            }

            return px; // TODO
        }

        Optional<Pixel> west () {
            Optional<Pixel> px = Optional.empty();

            if (this.x > 0) {
                Pixel newPixel = new Pixel(this.x-1, this.y, rawPixels[xyIndex.apply(x-1,y)]);
                    px = Optional.of(newPixel);
            }

            return px; // TODO
        }

        Optional<Pixel> southEast () {
            Optional<Pixel> px = Optional.empty();

            if (!this.inLastRow() && this.x < img.getWidth()-1) {
                Pixel newPixel = new Pixel(this.x+1, this.y+1, rawPixels[xyIndex.apply(x+1,y+1)]);
                    px = Optional.of(newPixel);
            }

            return px; // TODO
        }

        Optional<Pixel> southWest () {
            Optional<Pixel> px = Optional.empty();

            if (!this.inLastRow() && this.x > 0) {
                Pixel newPixel = new Pixel(this.x-1, this.y+1, rawPixels[xyIndex.apply(x-1,y+1)]);
                    px = Optional.of(newPixel);
            }

            return px; // TODO
        }

        Pixel moveWest () {
            return new Pixel(x-1,y,color);
        }

        public boolean equals (Object other) {
            if (other instanceof Pixel px)
                return x == px.x && y == px.y && color == px.color;
            return false;
        }

        public int hashCode () {
            return x + 17*y + 31*color;
        }

        public String toString () {
            return String.format("%d @ [%d,%d]", color, x, y);
        }
    }
}
