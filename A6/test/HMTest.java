import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class HMTest {

    @Test
    void entry () {
        HEntry<Integer,Integer> e0 = new HEntry<>(3311,4);
        assertEquals(3311, e0.key());
        assertEquals(4, e0.value());

        Chain<Integer,Integer> chain = new Chain<>();
        assertTrue(chain.insert(new HEntry<>(100,0)));
        assertTrue(chain.insert(new HEntry<>(101,1)));
        assertTrue(chain.insert(new HEntry<>(102,2)));
        assertTrue(chain.insert(new HEntry<>(103,3)));
        assertTrue(chain.insert(new HEntry<>(104,4)));
        assertTrue(chain.insert(new HEntry<>(105,5)));
        assertTrue(chain.insert(new HEntry<>(106,6)));

        assertEquals(5, chain.get(105));
        assertTrue(chain.containsKey(100));
        assertEquals(7, chain.size());
        Collection<Integer> keys = Arrays.asList(100,101,102,103,104,105,106);
        for (HEntry<Integer,Integer> e : chain) {
            assertTrue(keys.contains(e.key()));
        }
    }

    @Test
    void basicHash () throws SeqAccessE {
        HM<Integer,Integer> hm;
        hm = new HM<>(10, k -> k % 10);
        assertTrue(hm.put(1,10));
        assertTrue(hm.put(2,20));
        assertTrue(hm.put(11,110));
        assertTrue(hm.put(21,210));
        assertFalse(hm.put(21, 0));
        assertEquals(4, hm.size());
        assertEquals(3, hm.getChains()[1].size());
        assertEquals(1, hm.getChains()[2].size());
        assertEquals(210, hm.getChains()[1].getEntries().peekLeft().value());
        assertEquals(10, hm.getChains()[1].getEntries().peekRight().value());

        hm = new HM<>(10);
        assertTrue(hm.put(1, 10));
        assertTrue(hm.containsKey(1));
        assertEquals(10, hm.get(1));
        assertFalse(hm.put(1,20));
        assertTrue(hm.put(2,10));
        assertEquals(2, hm.size());
        hm.clear();
        assertFalse(hm.containsKey(1));
    }

    @Test
    void basicHashPut () {
        HM<Integer, Integer> hm = new HM<>(1);
        assertTrue(hm.put(1, 10));
        assertFalse(hm.put(1, 20));
        assertFalse(hm.put(1, 30));
        assertFalse(hm.put(1, 40));
        assertEquals(1, hm.size());
        assertTrue(hm.containsKey(1));
        assertEquals(10, hm.get(1));
    }

    @Test
    void rehash () {
        HM<Integer,Integer> hm = new HM<>(1);
        for (int i=1; i<5; i++) {
            assertTrue(hm.put(i, i * 10));
        }
        assertEquals(1, hm.getChains().length);
        assertEquals(4, hm.getChains()[0].size());

        hm.rehash();
        assertEquals(3, hm.getChains().length);
        assertEquals(1, hm.getChains()[0].size());
        assertEquals(2, hm.getChains()[1].size());
        assertEquals(1, hm.getChains()[2].size());

        hm.rehash();
        assertEquals(7, hm.getChains().length);
        assertEquals(0, hm.getChains()[0].size());
        assertEquals(1, hm.getChains()[1].size());
        assertEquals(1, hm.getChains()[2].size());
        assertEquals(1, hm.getChains()[3].size());
        assertEquals(1, hm.getChains()[4].size());
        assertEquals(0, hm.getChains()[5].size());
        assertEquals(0, hm.getChains()[6].size());

        for (int i=1; i<5; i++) {
            assertEquals(i*10, hm.get(i));
        }
    }


}