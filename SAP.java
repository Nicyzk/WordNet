/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNull(G);
        this.G = new Digraph(G);
    }

    private int[] sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if (vBfs.hasPathTo(i) && wBfs.hasPathTo(i)
                    && (vBfs.distTo(i) + wBfs.distTo(i) < length)) {
                length = vBfs.distTo(i) + wBfs.distTo(i);
                ancestor = i;
            }
        }
        int[] ans = new int[2];
        ans[0] = length == Integer.MAX_VALUE ? -1 : length;
        ans[1] = ancestor;
        return ans;
    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        if (!v.iterator().hasNext()) return new int[] { -1, -1 };
        if (!w.iterator().hasNext()) return new int[] { -1, -1 };

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths vBfs = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wBfs = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if (vBfs.hasPathTo(i) && wBfs.hasPathTo(i)
                    && (vBfs.distTo(i) + wBfs.distTo(i) < length)) {
                length = vBfs.distTo(i) + wBfs.distTo(i);
                ancestor = i;
            }
        }

        int[] ans = new int[2];
        ans[0] = length == Integer.MAX_VALUE ? -1 : length;
        ans[1] = ancestor;
        return ans;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return sap(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return sap(v, w)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[1];
    }

    private void validateVertex(int v) {
        int count = G.V();
        if (v < 0 || v >= count)
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (count - 1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        checkNull(vertices);
        int count = G.V();
        for (Object vertex : vertices) {
            checkNull(vertex);
            int v = (int) vertex;
            if (v < 0 || v >= count)
                throw new IllegalArgumentException(
                        "vertex " + v + " is not between 0 and " + (count - 1));
        }
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph x = new Digraph(in);
        SAP sap = new SAP(x);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

