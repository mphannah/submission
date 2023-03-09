import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphTraversalTest {

    @Test
    void shortestPath1 () {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node f = new Node("f");
        Node t = new Node("t");

        Edge sa = new Edge(s,a,new Finite(3));
        Edge sb = new Edge(s,b,new Finite(4));
        Edge ab = new Edge(a,b,new Finite(6));
        Edge bf = new Edge(b,f,new Finite(5));
        Edge af = new Edge(a,f,new Finite(7));
        Edge ac = new Edge(a,c,new Finite(2));
        Edge cf = new Edge(c,f,new Finite(1));
        Edge ct = new Edge(c,t,new Finite(8));
        Edge ft = new Edge(f,t,new Finite(4));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa,sb));
        neighbors.put(a, List.of(ab,af,ac));
        neighbors.put(b, List.of(bf));
        neighbors.put(c, List.of(cf,ct));
        neighbors.put(f, List.of(ft));
        neighbors.put(t, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(s);
        assertEquals(new Finite(0), s.getDistance());
        assertEquals(new Finite(3), a.getDistance());
        assertEquals(new Finite(4), b.getDistance());
        assertEquals(new Finite(5), c.getDistance());
        assertEquals(new Finite(6), f.getDistance());
        assertEquals(new Finite(10), t.getDistance());
    }

    @Test
    public void shortestPath2 () {
        Node a0 = new Node("0");
        Node a1 = new Node("1");
        Node a2 = new Node("2");
        Node a3 = new Node("3");
        Node a4 = new Node("4");
        Node a5 = new Node("5");
        Node a6 = new Node("6");
        Node a7 = new Node("7");

        Edge e04 = new Edge(a0, a4, new Finite(6));
        Edge e13 = new Edge(a1, a3, new Finite(5));
        Edge e16 = new Edge(a1, a6, new Finite(2));
        Edge e24 = new Edge(a2, a4, new Finite(7));
        Edge e25 = new Edge(a2, a5, new Finite(7));
        Edge e35 = new Edge(a3, a5, new Finite(2));
        Edge e37 = new Edge(a3, a7, new Finite(1));
        Edge e46 = new Edge(a4, a6, new Finite(3));
        Edge e57 = new Edge(a5, a7, new Finite(7));
        Edge e62 = new Edge(a6, a2, new Finite(7));
        Edge e67 = new Edge(a6, a7, new Finite(6));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(a0, List.of(e04));
        neighbors.put(a1, List.of(e13, e16));
        neighbors.put(a2, List.of(e24, e25));
        neighbors.put(a3, List.of(e35, e37));
        neighbors.put(a4, List.of(e46));
        neighbors.put(a5, List.of(e57));
        neighbors.put(a6, List.of(e62, e67));
        neighbors.put(a7, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(a0);
        assertEquals(new Finite(0), a0.getDistance());
        assertEquals(Infinity.getInstance(), a1.getDistance());
        assertEquals(new Finite(16), a2.getDistance());
        assertEquals(Infinity.getInstance(), a3.getDistance());
        assertEquals(new Finite(6), a4.getDistance());
        assertEquals(new Finite(23), a5.getDistance());
        assertEquals(new Finite(9), a6.getDistance());
        assertEquals(new Finite(15), a7.getDistance());
    }

    @Test
    public void spanningTree1() {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node t = new Node("t");

        Edge sa = new Edge(s, a, new Finite(7));
        Edge sc = new Edge(s, c, new Finite(8));
        Edge ab = new Edge(a, b, new Finite(6));
        Edge ac = new Edge(a, c, new Finite(3));
        Edge cb = new Edge(c, b, new Finite(4));
        Edge cd = new Edge(c, d, new Finite(3));
        Edge bd = new Edge(b, d, new Finite(2));
        Edge bt = new Edge(b, t, new Finite(5));
        Edge dt = new Edge(d, t, new Finite(2));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa, sc));
        neighbors.put(a, List.of(ac, ab, sa.flip()));
        neighbors.put(b, List.of(bd, bt, ab.flip(), cb.flip()));
        neighbors.put(c, List.of(cb, cd, ac.flip(), sc.flip()));
        neighbors.put(d, List.of(dt, bd.flip(), cd.flip()));
        neighbors.put(t, List.of(bt.flip(), dt.flip()));

        MinSpanningTree graph = new MinSpanningTree(neighbors);

        Set<Edge> tree = graph.fromSource(s);
        assertEquals(5, tree.size());
        assertTrue(tree.contains(sa));
        assertTrue(tree.contains(ac));
        assertTrue(tree.contains(cd));
        assertTrue(tree.contains(bd.flip()));
        assertTrue(tree.contains(dt));
    }

    @Test
    public void spanningTree2() {
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        Node g = new Node("g");

        Edge ab = new Edge(a, b, new Finite(7));
        Edge ad = new Edge(a, d, new Finite(5));
        Edge ba = new Edge(b, a, new Finite(7));
        Edge bc = new Edge(b, c, new Finite(8));
        Edge bd = new Edge(b, d, new Finite(9));
        Edge be = new Edge(b, e, new Finite(7));
        Edge cb = new Edge(c, b, new Finite(8));
        Edge ce = new Edge(c, e, new Finite(5));
        Edge da = new Edge(d, a, new Finite(5));
        Edge db = new Edge(d, b, new Finite(9));
        Edge de = new Edge(d, e, new Finite(15));
        Edge df = new Edge(d, f, new Finite(6));
        Edge eb = new Edge(e, b, new Finite(7));
        Edge ec = new Edge(e, c, new Finite(5));
        Edge ed = new Edge(e, d, new Finite(15));
        Edge ef = new Edge(e, f, new Finite(8));
        Edge eg = new Edge(e, g, new Finite(9));
        Edge fd = new Edge(f, d, new Finite(6));
        Edge fe = new Edge(f, e, new Finite(8));
        Edge fg = new Edge(f, g, new Finite(1));
        Edge ge = new Edge(g, e, new Finite(9));
        Edge gf = new Edge(g, f, new Finite(1));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(a, List.of(ab, ad));
        neighbors.put(b, List.of(ba, bc, bd, be));
        neighbors.put(c, List.of(cb, ce));
        neighbors.put(d, List.of(da, db, de, df));
        neighbors.put(e, List.of(eb, ec, ed, ef, eg));
        neighbors.put(f, List.of(fd, fe, fg));
        neighbors.put(g, List.of(ge, gf));

        MinSpanningTree graph = new MinSpanningTree(neighbors);

        Set<Edge> tree = graph.fromSource(a);
        assertEquals(6, tree.size());
        assertTrue(tree.contains(be));
        assertTrue(tree.contains(fg));
        assertTrue(tree.contains(ec));
        assertTrue(tree.contains(ad));
        assertTrue(tree.contains(df));
        assertTrue(tree.contains(ab));
    }

    @Test
    void heapifyTest () {
        Set<Node> nodes = new HashSet<>();
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node e = new Node("e");
        Node f = new Node("f");
        Node g = new Node("g");

        a.setDistance(new Finite(10));
        b.setDistance(new Finite(5));
        c.setDistance(new Finite(15));
        d.setDistance(new Finite(8));
        e.setDistance(new Finite(3));
        f.setDistance(new Finite(9));
        g.setDistance(new Finite(12));

        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);
        nodes.add(e);
        nodes.add(f);
        nodes.add(g);

        Heap heap = new Heap(nodes);

        // getting parents/children
        assertEquals(g, heap.getRightChild(f).orElseThrow());
        assertEquals(d, heap.getLeftChild(b).orElseThrow());
        assertEquals(e, heap.getParent(f).orElseThrow());
        assertNotSame(heap.getLeftChild(b).orElseThrow(), a);
        assertNotSame(heap.getParent(f).orElseThrow(), b);

        // inserting
        Node h = new Node("h");
        Node i = new Node("i");
        h.setDistance(new Finite(1));
        i.setDistance(new Finite(2));
        nodes.add(h);
        nodes.add(i);

        heap.insert(h);
        heap.insert(i);

        // checking extractMin and if heapify orders the heap correctly
        for (int x = 0; x < nodes.size(); x++) {
            Node n = heap.extractMin();
            System.out.println(n);
        }
    }

    @Test
    void shortestPathRelax () {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node f = new Node("f");
        Node t = new Node("t");

        s.setDistance(new Finite(0));
        a.setDistance(new Finite(10));
        b.setDistance(new Finite(2));
        c.setDistance(new Finite(15));
        f.setDistance(new Finite(9));
        t.setDistance(new Finite(12));

        Edge sa = new Edge(s,a,new Finite(3));
        Edge sb = new Edge(s,b,new Finite(4));
        Edge ab = new Edge(a,b,new Finite(6));
        Edge bf = new Edge(b,f,new Finite(5));
        Edge af = new Edge(a,f,new Finite(7));
        Edge ac = new Edge(a,c,new Finite(2));
        Edge cf = new Edge(c,f,new Finite(1));
        Edge ct = new Edge(c,t,new Finite(8));
        Edge ft = new Edge(f,t,new Finite(4));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa,sb));
        neighbors.put(a, List.of(ab,af,ac));
        neighbors.put(b, List.of(bf));
        neighbors.put(c, List.of(cf,ct));
        neighbors.put(f, List.of(ft));
        neighbors.put(t, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(s);

        g.relax(sa);
        g.relax(sb);
        g.relax(ac);
        assertEquals(new Finite(3), a.getDistance());
        assertEquals(new Finite(2), b.getDistance());
        assertEquals(new Finite(5), c.getDistance());
    }
}
