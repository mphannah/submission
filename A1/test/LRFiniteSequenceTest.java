import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LRFiniteSequenceTest {
    private LRFiniteSequence<Integer> seq;

    @BeforeEach
    void setUp() {
        seq = new LRFiniteSequence<>(5);
    }

    @Test
    void simpleLeft() throws SeqFullE, SeqAccessE {
        assertEquals(0, seq.size());

        seq.insertLeft(10);
        seq.insertLeft(20);
        seq.insertLeft(30);
        seq.insertLeft(40);
        seq.insertLeft(50);
        assertEquals(5, seq.size());

        assertEquals(50, seq.removeLeft());
        assertEquals(40, seq.removeLeft());
        assertEquals(30, seq.removeLeft());
        assertEquals(2, seq.size());

        assertEquals(20, seq.get(1));
        assertEquals(10, seq.get(0));

        assertDoesNotThrow(SeqFullE::new);
        assertDoesNotThrow(SeqAccessE::new);

        assertThrows(SeqAccessE.class, () -> seq.get(4));
        assertThrows(SeqAccessE.class, () -> seq.get(100));
    }

    @Test
    void simpleRight() throws SeqFullE, SeqAccessE {
        assertEquals(0, seq.size());

        seq.insertRight(10);
        seq.insertRight(20);
        seq.insertRight(30);
        seq.insertRight(40);
        seq.insertRight(50);
        assertEquals(5, seq.size());

        assertEquals(50, seq.removeRight());
        assertEquals(40, seq.removeRight());
        assertEquals(30, seq.removeRight());
        assertEquals(2, seq.size());

        assertEquals(20, seq.get(3));
        assertEquals(10, seq.get(4));

        assertDoesNotThrow(SeqFullE::new);
        assertDoesNotThrow(SeqAccessE::new);

        assertThrows(SeqAccessE.class, () -> seq.get(0));
        assertThrows(SeqAccessE.class, () -> seq.get(100));
    }

    @Test
    void simpleLeftRight() throws SeqFullE, SeqAccessE {
        seq.insertLeft(10);
        seq.insertRight(20);
        seq.insertLeft(30);
        seq.insertRight(40);
        seq.insertLeft(50);

        assertEquals(10, seq.get(0));
        assertEquals(30, seq.get(1));
        assertEquals(50, seq.get(2));
        assertEquals(40, seq.get(3));
        assertEquals(20, seq.get(4));

        assertThrows(SeqFullE.class, () -> seq.insertLeft(0));
        assertThrows(SeqFullE.class, () -> seq.insertRight(0));

        assertEquals(50, seq.removeLeft());
        assertEquals(40, seq.removeRight());
        assertEquals(3, seq.size());

    }

    Duration timeSeq (Random r, int bound, LRFiniteSequence<Integer> seq) {
        Instant start = Instant.now();

        for (int i=0; i<seq.getCapacity(); i++) {
            int e = r.nextInt(bound);
            switch (r.nextInt(4)) {
                case 0 -> seq.insertLeftIfNotFull(e);
                case 1 -> seq.insertRightIfNotFull(e);
                case 2 -> seq.removeLeftIfNotEmpty();
                case 3 -> seq.removeRightIfNotEmpty();
                default -> throw new Error("Impossible");
            }
        }

        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    @Test
    void timeTest () {
        Random r = new Random(1234567L);
        int bound = 1000;

        Duration d;

        d = timeSeq(r, bound, new LRFiniteSequence<>(10000));
        assertTrue((d.toMillis() < 10));

        d = timeSeq(r, bound, new LRFiniteSequence<>(100000));
        assertTrue((d.toMillis() < 20));

        d = timeSeq(r, bound, new LRFiniteSequence<>(1000000));
        assertTrue((d.toMillis() < 100));

        d = timeSeq(r, bound, new LRFiniteSequence<>(10000000));
        assertTrue((d.toMillis() < 1000));
    }

}