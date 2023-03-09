import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphTraversalTest {
    private Node[] ns;
    private Node a, b, c, d, e, f, g, h;
    private Node v1, v2, v3, v4, v5, v6, v7;
    private Hashtable<Node, List<Node>> g1, g2, g3, g4, g5;

    @BeforeEach
    void mkGraphs () {

        ns = new Node[13];
        for (int i=1; i<=12; i++) ns[i] = new Node(String.valueOf(i));

        a = new Node("A");
        b = new Node("B");
        c = new Node("C");
        d = new Node("D");
        e = new Node("E");
        f = new Node("F");
        g = new Node("G");
        h = new Node("H");

        v1 = new Node("v1");
        v2 = new Node("v2");
        v3 = new Node("v3");
        v4 = new Node("v4");
        v5 = new Node("v5");
        v6 = new Node("v6");
        v7 = new Node("v7");

        g1 = new Hashtable<>();
        g2 = new Hashtable<>();
        g3 = new Hashtable<>();
        g4 = new Hashtable<>();
        g5 = new Hashtable<>();

        g1.put(a, List.of(b,c));
        g1.put(b, List.of(c,d,e));
        g1.put(c, List.of());
        g1.put(d, List.of(e));
        g1.put(e, List.of(b));

        g2.put(ns[1], List.of(ns[2], ns[7], ns[8]));
        g2.put(ns[2], List.of(ns[3], ns[6]));
        g2.put(ns[3], List.of(ns[4], ns[5]));
        g2.put(ns[4], List.of());
        g2.put(ns[5], List.of());
        g2.put(ns[6], List.of());
        g2.put(ns[7], List.of());
        g2.put(ns[8], List.of(ns[9], ns[12]));
        g2.put(ns[9], List.of(ns[10], ns[11]));
        g2.put(ns[10], List.of());
        g2.put(ns[11], List.of());
        g2.put(ns[12], List.of());

        g3.put(a, List.of(b,c,e));
        g3.put(b, List.of(d,f));
        g3.put(c, List.of(g));
        g3.put(d, List.of());
        g3.put(e, List.of());
        g3.put(f, List.of(e));
        g3.put(g, List.of());

        g4.put(a, List.of(b,d));
        g4.put(b, List.of(c));
        g4.put(c, List.of(h));
        g4.put(d, List.of(e,f));
        g4.put(e, List.of(h));
        g4.put(f, List.of(g));
        g4.put(g, List.of(h));
        g4.put(h, List.of());

        g5.put(v1, List.of(v2,v3,v4));
        g5.put(v2, List.of(v4,v5));
        g5.put(v3, List.of(v6));
        g5.put(v4, List.of(v3,v6,v7));
        g5.put(v5, List.of(v4,v7));
        g5.put(v6, List.of());
        g5.put(v7, List.of(v6));

    }

    // --------------------------------------------

    @Test
    void dfsEx1 () {
        DFS dfs = new DFS(g1, a);
        assertEquals(List.of(a,b,c,d,e), dfs.traversal);
    }


    @Test
    void dfsEx2 () {
        DFS dfs = new DFS(g2, ns[1]);
        List<Node> expected = new ArrayList<>(Arrays.asList(ns).subList(1,13));
        assertEquals(expected, dfs.traversal);
    }

    @Test
    void dfsEx3 () {
        List<Node> expected = List.of(a,b,d,f,e,c,g);
        DFS dfs = new DFS(g3, a);
        assertEquals(expected, dfs.traversal);
    }

    @Test
    void dfsEx4 () {
        List<Node> expected = List.of(a,b,c,h,d,e,f,g);
        DFS dfs = new DFS(g4, a);
        assertEquals(expected, dfs.traversal);
    }

    @Test
    void dfsEx5 () {
        List<Node> expected = List.of(v1,v2,v4,v3,v6,v7,v5);
        DFS dfs = new DFS(g5, v1);
        assertEquals(expected, dfs.traversal);
    }

    // --------------------------------------------

    @Test
    void cycledetection1() {
        CycleDetection cycleDetection = new CycleDetection(g1, a);
        assertTrue(cycleDetection.hasCycle);
    }

    @Test
    void cycledetection2() {
        CycleDetection cycleDetection = new CycleDetection(g2, ns[1]);
        assertFalse(cycleDetection.hasCycle);
    }

    @Test
    void cycledetection3() {
        CycleDetection cycleDetection = new CycleDetection(g3, a);
        assertFalse(cycleDetection.hasCycle);
    }

    @Test
    void cycledetection4() {
        CycleDetection cycleDetection = new CycleDetection(g4, a);
        assertFalse(cycleDetection.hasCycle);
    }

    @Test
    void cycledetection5() {
        CycleDetection cycleDetection = new CycleDetection(g5, v1);
        assertFalse(cycleDetection.hasCycle);
    }

    // --------------------------------------------
    // Graph guaranteed not to have a cycle

    @Test
    void topologicalsort2 () {
        List<Node> expected = new ArrayList<>();
        expected.add(ns[1]);
        expected.add(ns[8]);
        expected.add(ns[12]);
        expected.add(ns[9]);
        expected.add(ns[11]);
        expected.add(ns[10]);
        expected.add(ns[7]);
        expected.add(ns[2]);
        expected.add(ns[6]);
        expected.add(ns[3]);
        expected.add(ns[5]);
        expected.add(ns[4]);
        assertEquals(expected, new TopologicalSort(g2, ns[1]).ordered);
    }

    @Test
    void topologicalsort3 () {
        assertEquals(List.of(a,c,g,b,f,e,d), new TopologicalSort(g3, a).ordered);
    }

    @Test
    void topologicalsort4 () {
        assertEquals(List.of(a,d,f,g,e,b,c,h), new TopologicalSort(g4, a).ordered);
    }

    @Test
    void topologicalsort5 () {
        assertEquals(List.of(v1,v2,v5,v4,v7,v3,v6), new TopologicalSort(g5, v1).ordered);
    }

    // --------------------------------------------

    @Test
    void reach1() {
        Reachability reach = new Reachability(g1, a);
        Hashtable<Node,Set<Node>> table = reach.table;
        assertEquals(Set.of(), table.get(a));
        assertEquals(Set.of(a,b,d,e), table.get(b));
        assertEquals(Set.of(a,b,d,e), table.get(c));
        assertEquals(Set.of(a,b,d,e), table.get(d));
        assertEquals(Set.of(a,b,d,e), table.get(e));
    }

    @Test
    void reach2() {
        Reachability reach = new Reachability(g2, ns[1]);
        Hashtable<Node,Set<Node>> table = reach.table;
        assertEquals(Set.of(),table.get(ns[1]));
        assertEquals(Set.of(ns[1]),table.get(ns[2]));
        assertEquals(Set.of(ns[1],ns[2]),table.get(ns[3]));
        assertEquals(Set.of(ns[1],ns[2],ns[3]),table.get(ns[4]));
        assertEquals(Set.of(ns[1],ns[2],ns[3]),table.get(ns[5]));
        assertEquals(Set.of(ns[1],ns[2]),table.get(ns[6]));
        assertEquals(Set.of(ns[1]),table.get(ns[7]));
        assertEquals(Set.of(ns[1]),table.get(ns[8]));
        assertEquals(Set.of(ns[1],ns[8]),table.get(ns[9]));
        assertEquals(Set.of(ns[1],ns[8],ns[9]),table.get(ns[10]));
        assertEquals(Set.of(ns[1],ns[8],ns[9]),table.get(ns[11]));
        assertEquals(Set.of(ns[1],ns[8]),table.get(ns[12]));
    }

    @Test
    void reach3() {
        Reachability reach = new Reachability(g3, a);
        Hashtable<Node,Set<Node>> table = reach.table;
        assertEquals(Set.of(),table.get(a));
        assertEquals(Set.of(a),table.get(b));
        assertEquals(Set.of(a),table.get(c));
        assertEquals(Set.of(a,b),table.get(d));
        assertEquals(Set.of(a,b,f),table.get(e));
        assertEquals(Set.of(a,b),table.get(f));
        assertEquals(Set.of(a,c),table.get(g));
    }

    @Test
    void reach4() {
        Reachability reach = new Reachability(g4, a);
        Hashtable<Node,Set<Node>> table = reach.table;
        assertEquals(Set.of(),table.get(a));
        assertEquals(Set.of(a),table.get(b));
        assertEquals(Set.of(a,b),table.get(c));
        assertEquals(Set.of(a),table.get(d));
        assertEquals(Set.of(a,d),table.get(e));
        assertEquals(Set.of(a,d),table.get(f));
        assertEquals(Set.of(a,d,f),table.get(g));
        assertEquals(Set.of(a,b,c,d,e,f,g),table.get(h));
    }

    @Test
    void reach5() {
        Reachability reach = new Reachability(g5, v1);
        Hashtable<Node,Set<Node>> table = reach.table;
        assertEquals(Set.of(),table.get(v1));
        assertEquals(Set.of(v1),table.get(v2));
        assertEquals(Set.of(v1,v2,v4,v5),table.get(v3));
        assertEquals(Set.of(v1,v2,v5),table.get(v4));
        assertEquals(Set.of(v1,v2),table.get(v5));
        assertEquals(Set.of(v1,v2,v3,v4,v5,v7),table.get(v6));
        assertEquals(Set.of(v1,v2,v4,v5),table.get(v7));
    }

}