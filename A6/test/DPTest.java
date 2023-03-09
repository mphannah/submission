import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class DPTest {
    void time (DP dpRef, int n) {
        Duration d;
        Instant start, end;

        start = Instant.now();
        System.out.printf("fib(%2d) = %-20s\t\t", n, dpRef.fib(n));
        end = Instant.now();
        d = Duration.between(start,end);
        System.out.printf("time (ms) = %d%n", d.toMillis());
    }

    @Test
    void slowFib () {
        DP dpRef = new DP();
        System.out.println("---");
        for (int i=1; i<=11; i++) time(dpRef, 4*i);
        System.out.println("---");
    }

    @Test
    void hashFib () {
        DP dpRef = new DPHM();
        System.out.println("---");
        for (int i=1; i<=11; i++) time(dpRef, 4*i);
        System.out.println("---");
    }

    @Test
    void bigFib () {
        DP dpRef = new DPHM();
        assertEquals("6161314747715278029583501626149", dpRef.fib(149).toString());
        assertEquals("222232244629420445529739893461909967206666939096499764990979600",
                dpRef.fib(300).toString());
    }

    @Test
    void subsetSum () {
        Stack<Integer> s = new Stack<>();
        s.addAll(Arrays.asList(5,3,7,1));

        DP dpRef = new DP();
        assertFalse(dpRef.subsetSum(s,2));
        assertTrue(dpRef.subsetSum(s,6));
        assertTrue(dpRef.subsetSum(s,8));

        dpRef = new DPHM();
        assertFalse(dpRef.subsetSum(s,2));
        assertTrue(dpRef.subsetSum(s,6));
        assertTrue(dpRef.subsetSum(s,8));
    }

    @Test
    void subsetSumT () {
        DP dpRef = new DPHM();
        Stack<Integer> s = new Stack<>();

        s.addAll(new Random().ints(50, -10, 10).boxed().toList());
        System.out.printf("50 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(100, -10, 10).boxed().toList());
        System.out.printf("100 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(200, -10, 10).boxed().toList());
        System.out.printf("200 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(400, -10, 10).boxed().toList());
        System.out.printf("400 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));
    }
}
