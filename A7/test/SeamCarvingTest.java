import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class SeamCarvingTest {
    @Test
    void emptyPath () {
        Path<Integer> p1 = new EmptyPath<>();
        Path<Integer> p2 = new EmptyPath<>();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void nonEmptyPath () throws EmptyPathE {
        Path<Integer> p1 = new NonEmptyPath<>(3, new NonEmptyPath<>(4, new EmptyPath<>()));
        Path<Integer> p2 = new NonEmptyPath<>(3, new NonEmptyPath<>(4, new EmptyPath<>()));
        Path<Integer> p3 = new NonEmptyPath<>(4, new EmptyPath<>());
        assertEquals(2, p1.size());
        assertEquals(2, p2.size());
        assertEquals(1, p3.size());
        assertEquals(p1,p2);
        assertNotEquals(p1,p3);
        assertNotEquals(p2,p3);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertEquals(p1.getRest(),p3);
        assertEquals(p1.getRest().hashCode(), p3.hashCode());
    }

    @Test
    void pixel1 () throws IOException {
        Image img = new Image("board.png");
        assertEquals(6, img.getType());
        assertEquals(5, img.getWidth());
        assertEquals(3, img.getHeight());
        BiFunction<Integer,Integer,Integer> xyIndex = img.getXyIndex();
        int[] rawPixels = img.getRawPixels();
        HashSet<Image.Pixel> pixels = img.getPixels();
        for (Image.Pixel px : pixels) {
            assertEquals(rawPixels[xyIndex.apply(px.x,px.y)], px.color);
        }

        assertEquals(Color.RED, img.mkPixel(0,0).getColor());
        assertEquals(Color.BLUE, img.mkPixel(1,0).getColor());
        assertEquals(Color.BLUE, img.mkPixel(2,0).getColor());
        assertEquals(Color.BLUE, img.mkPixel(3,0).getColor());
        assertEquals(Color.RED, img.mkPixel(4,0).getColor());

        assertEquals(Color.BLUE, img.mkPixel(0,1).getColor());
        assertEquals(Color.BLUE, img.mkPixel(1,1).getColor());
        assertEquals(Color.BLUE, img.mkPixel(2,1).getColor());
        assertEquals(Color.BLUE, img.mkPixel(3,1).getColor());
        assertEquals(Color.BLUE, img.mkPixel(4,1).getColor());

        assertEquals(Color.RED, img.mkPixel(0,2).getColor());
        assertEquals(Color.BLUE, img.mkPixel(1,2).getColor());
        assertEquals(Color.BLUE, img.mkPixel(2,2).getColor());
        assertEquals(Color.BLUE, img.mkPixel(3,2).getColor());
        assertEquals(Color.RED, img.mkPixel(4,2).getColor());
    }

    @Test
    void path () throws IOException {
        Image img = new Image("board.png");
        Image.Pixel p10, p20, p11, p21, p02, p12;
        p10 = img.mkPixel(1,0);
        p20 = img.mkPixel(2,0);
        p11 = img.mkPixel(1,1);
        p21 = img.mkPixel(2,1);
        p02 = img.mkPixel(0,2);
        p12 = img.mkPixel(1,2);
        Path<Image.Pixel> path1 =
                new NonEmptyPath<>(p10,
                        new NonEmptyPath<>(p11,
                                new NonEmptyPath<>(p02,
                                        new EmptyPath<>())));
        Path<Image.Pixel> path2 =
                new NonEmptyPath<>(p20,
                        new NonEmptyPath<>(p21,
                                new NonEmptyPath<>(p12,
                                        new EmptyPath<>())));
        int[] xs1 = Path.xPositions(path1);
        int[] xs2 = Path.xPositions(path2);
        assertEquals(3, xs1.length);
        assertEquals(3, xs2.length);
        assertEquals(1, xs1[0]);
        assertEquals(1, xs1[1]);
        assertEquals(0, xs1[2]);
        assertEquals(2, xs2[0]);
        assertEquals(2, xs2[1]);
        assertEquals(1, xs2[2]);
    }

    @Test
    void pathAndCost () throws IOException {
        Image img = new Image("board.png");
        Image.Pixel p10, p20, p11, p21, p02, p12;
        p10 = img.mkPixel(1,0);
        p20 = img.mkPixel(2,0);
        p11 = img.mkPixel(1,1);
        p21 = img.mkPixel(2,1);
        p02 = img.mkPixel(0,2);
        p12 = img.mkPixel(1,2);
        Path<Image.Pixel> path1 =
                new NonEmptyPath<>(p10,
                        new NonEmptyPath<>(p11,
                                new NonEmptyPath<>(p02,
                                        new EmptyPath<>())));
        Path<Image.Pixel> path2 =
                new NonEmptyPath<>(p20,
                        new NonEmptyPath<>(p21,
                                new NonEmptyPath<>(p12,
                                        new EmptyPath<>())));
        PathAndCost pc1 = new PathAndCost(path1, 17);
        PathAndCost pc2 = new PathAndCost(path2, 13);
        PathAndCost pc3 = new PathAndCost(path2, 17);
        assertTrue(pc2.compareTo(pc1) < 0);
        assertEquals(0, pc3.compareTo(pc1));
        assertTrue(pc1.compareTo(pc2) > 0);

        Path<Image.Pixel> path3 =
                new NonEmptyPath<>(p21,
                        new NonEmptyPath<>(p12,
                                new EmptyPath<>()));
        PathAndCost pc4 = new PathAndCost(path3, 10);
        PathAndCost pc5 = pc4.add(p20, 7);
        assertEquals(pc3,pc5);
    }

    @Test
    void pixel2 () throws IOException {
        Image img = new Image("board.png");
        Image.Pixel p00 = img.mkPixel(0, 0);
        Image.Pixel p02 = img.mkPixel(0, 2);
        assertFalse(p00.inLastRow());
        assertTrue(p02.inLastRow());
        assertEquals(img.mkPixel(2,0), img.mkPixel(2,1).north().orElseThrow());
        assertEquals(img.mkPixel(3,1), img.mkPixel(2,1).east().orElseThrow());
        assertEquals(img.mkPixel(2,2), img.mkPixel(2,1).south().orElseThrow());
        assertEquals(img.mkPixel(1,1), img.mkPixel(2,1).west().orElseThrow());
        assertEquals(img.mkPixel(3,2), img.mkPixel(2,1).southEast().orElseThrow());
        assertEquals(img.mkPixel(1,2), img.mkPixel(2,1).southWest().orElseThrow());
    }

    @Test
    void energy () throws IOException {
        Image img = new Image("board.png");
        assertEquals(260100, img.computeEnergy(img.mkPixel(0,0)));
        assertEquals(130050, img.computeEnergy(img.mkPixel(1,0)));
        assertEquals(0, img.computeEnergy(img.mkPixel(2,0)));
        assertEquals(130050, img.computeEnergy(img.mkPixel(3,0)));
        assertEquals(260100, img.computeEnergy(img.mkPixel(4,0)));

        assertEquals(260100, img.computeEnergy(img.mkPixel(0,1)));
        assertEquals(0, img.computeEnergy(img.mkPixel(1,1)));
        assertEquals(0, img.computeEnergy(img.mkPixel(2,1)));
        assertEquals(0, img.computeEnergy(img.mkPixel(3,1)));
        assertEquals(260100, img.computeEnergy(img.mkPixel(4,1)));

        assertEquals(260100, img.computeEnergy(img.mkPixel(0,2)));
        assertEquals(130050, img.computeEnergy(img.mkPixel(1,2)));
        assertEquals(0, img.computeEnergy(img.mkPixel(2,2)));
        assertEquals(130050, img.computeEnergy(img.mkPixel(3,2)));
        assertEquals(260100, img.computeEnergy(img.mkPixel(4,2)));
    }

    @Test
    void img1 () throws IOException {
        Image img = new Image("board.png");

        Set<Image.Pixel> r0 = img.topRow();
        assertEquals(5, r0.size());
        assertTrue(r0.contains(img.mkPixel(0,0)));
        assertTrue(r0.contains(img.mkPixel(1,0)));
        assertTrue(r0.contains(img.mkPixel(2,0)));
        assertTrue(r0.contains(img.mkPixel(3,0)));
        assertTrue(r0.contains(img.mkPixel(4,0)));

        assertEquals(2, img.getHVneighbors(img.mkPixel(0,0)).size());
        assertEquals(3, img.getHVneighbors(img.mkPixel(1,0)).size());
        assertEquals(2, img.getHVneighbors(img.mkPixel(4,0)).size());
        assertEquals(3, img.getHVneighbors(img.mkPixel(0,1)).size());
        assertEquals(4, img.getHVneighbors(img.mkPixel(1,1)).size());
        assertEquals(3, img.getHVneighbors(img.mkPixel(4,1)).size());
        assertEquals(2, img.getHVneighbors(img.mkPixel(0,2)).size());
        assertEquals(3, img.getHVneighbors(img.mkPixel(1,2)).size());
        assertEquals(2, img.getHVneighbors(img.mkPixel(4,2)).size());

        assertEquals(2, img.getBelowNeighbors(img.mkPixel(0,0)).size());
        assertEquals(3, img.getBelowNeighbors(img.mkPixel(1,0)).size());
        assertEquals(2, img.getBelowNeighbors(img.mkPixel(4,0)).size());
        assertEquals(2, img.getBelowNeighbors(img.mkPixel(0,1)).size());
        assertEquals(3, img.getBelowNeighbors(img.mkPixel(1,1)).size());
        assertEquals(2, img.getBelowNeighbors(img.mkPixel(4,1)).size());
        assertEquals(0, img.getBelowNeighbors(img.mkPixel(0,2)).size());
        assertEquals(0, img.getBelowNeighbors(img.mkPixel(1,2)).size());
        assertEquals(0, img.getBelowNeighbors(img.mkPixel(4,2)).size());
    }

    @Test
    void img2 () throws IOException {
        Image img = new Image("board.png");
        Iterator<Image.Pixel> iter = img.iterator();
        assertEquals(iter.next(), img.mkPixel(0,0));
        assertEquals(iter.next(), img.mkPixel(1,0));
        assertEquals(iter.next(), img.mkPixel(2,0));
        assertEquals(iter.next(), img.mkPixel(3,0));
        assertEquals(iter.next(), img.mkPixel(4,0));

        assertEquals(iter.next(), img.mkPixel(0,1));
        assertEquals(iter.next(), img.mkPixel(1,1));
        assertEquals(iter.next(), img.mkPixel(2,1));
        assertEquals(iter.next(), img.mkPixel(3,1));
        assertEquals(iter.next(), img.mkPixel(4,1));

        assertEquals(iter.next(), img.mkPixel(0,2));
        assertEquals(iter.next(), img.mkPixel(1,2));
        assertEquals(iter.next(), img.mkPixel(2,2));
        assertEquals(iter.next(), img.mkPixel(3,2));
        assertEquals(iter.next(), img.mkPixel(4,2));

        assertFalse(iter.hasNext());
    }

    @Test
    void img3 () throws IOException {
        Image img = new Image("board.png");

        Image.Pixel p00 = img.mkPixel(0,0);
        Image.Pixel p10 = img.mkPixel(1,0);
        Image.Pixel p20 = img.mkPixel(2,0);
        Image.Pixel p30 = img.mkPixel(3,0);
        Image.Pixel p40 = img.mkPixel(4,0);

        Image.Pixel p01 = img.mkPixel(0,1);
        Image.Pixel p11 = img.mkPixel(1,1);
        Image.Pixel p21 = img.mkPixel(2,1);
        Image.Pixel p31 = img.mkPixel(3,1);
        Image.Pixel p41 = img.mkPixel(4,1);

        Image.Pixel p02 = img.mkPixel(0,2);
        Image.Pixel p12 = img.mkPixel(1,2);
        Image.Pixel p22 = img.mkPixel(2,2);
        Image.Pixel p32 = img.mkPixel(3,2);
        Image.Pixel p42 = img.mkPixel(4,2);

        Path<Image.Pixel> path;
        Image cImg;

        path = new NonEmptyPath<>(p00,
                new NonEmptyPath<>(p01,
                        new NonEmptyPath<>(p02, new EmptyPath<>())));
        cImg = img.cutSeam(path);
        cImg.write("p000.png");

        path = new NonEmptyPath<>(p10,
                new NonEmptyPath<>(p01,
                        new NonEmptyPath<>(p02, new EmptyPath<>())));
        cImg = img.cutSeam(path);
        cImg.write("p100.png");

        path = new NonEmptyPath<>(p10,
                new NonEmptyPath<>(p01,
                        new NonEmptyPath<>(p12, new EmptyPath<>())));
        cImg = img.cutSeam(path);
        cImg.write("p101.png");
    }

    @Test
    public void tree () throws IOException {
        SeamCarving sc = new SeamCarvingH("tree.jpg");
        sc.cutN(sc.getImg().getWidth() / 2);
        sc.write("half-");
    }
    @Test

    public void balloon () throws IOException {
        SeamCarving sc = new SeamCarvingH("balloon-sky.jpg");
        sc.cutN(50);
        sc.write("m50-");
    }

    @Test
    public void sun () throws IOException {
        SeamCarving sc = new SeamCarvingH("winter-sun.jpg");
        sc.cutN(300);
        sc.write("m300-");
    }
}