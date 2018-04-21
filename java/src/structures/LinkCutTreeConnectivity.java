package structures;

import java.util.Random;

// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
public class LinkCutTreeConnectivity {

    public static class Node {
        Node left;
        Node right;
        Node parent;
        boolean revert;

        // tests whether x is a root of a splay tree
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }

        void push() {
            if (revert) {
                revert = false;
                Node t = left;
                left = right;
                right = t;
                if (left != null)
                    left.revert = !left.revert;
                if (right != null)
                    right.revert = !right.revert;
            }
        }
    }

    static void connect(Node ch, Node p, Boolean isLeftChild) {
        if (ch != null)
            ch.parent = p;
        if (isLeftChild != null) {
            if (isLeftChild)
                p.left = ch;
            else
                p.right = ch;
        }
    }

    // rotates edge (x, x.parent)
    //        g            g
    //       /            /
    //      p            x
    //     / \    ->    / \
    //    x  p.r      x.l  p
    //   / \              / \
    // x.l x.r          x.r p.r
    static void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        boolean isRootP = p.isRoot();
        boolean leftChildX = (x == p.left);

        // create 3 edges: (x.r(l),p), (p,x), (x,g)
        connect(leftChildX ? x.right : x.left, p, leftChildX);
        connect(p, x, !leftChildX);
        connect(x, g, !isRootP ? p == g.left : null);
    }

    // brings x to the root, balancing tree
    //
    // zig-zig case
    //        g                                  x
    //       / \               p                / \
    //      p  g.r rot(p)    /   \     rot(x) x.l  p
    //     / \      -->    x       g    -->       / \
    //    x  p.r          / \     / \           x.r  g
    //   / \            x.l x.r p.r g.r             / \
    // x.l x.r                                    p.r g.r
    //
    // zig-zag case
    //      g               g
    //     / \             / \               x
    //    p  g.r rot(x)   x  g.r rot(x)    /   \
    //   / \      -->    / \      -->    p       g
    // p.l  x           p  x.r          / \     / \
    //     / \         / \            p.l x.l x.r g.r
    //   x.l x.r     p.l x.l
    static void splay(Node x) {
        while (!x.isRoot()) {
            Node p = x.parent;
            Node g = p.parent;
            if (!p.isRoot())
                g.push();
            p.push();
            x.push();
            if (!p.isRoot())
                rotate((x == p.left) == (p == g.left) ? p/*zig-zig*/ : x/*zig-zag*/);
            rotate(x);
        }
        x.push();
    }

    // makes node x the root of the virtual tree, and also x becomes the leftmost node in its splay tree
    static Node expose(Node x) {
        Node last = null;
        for (Node y = x; y != null; y = y.parent) {
            splay(y);
            y.left = last;
            last = y;
        }
        splay(x);
        return last;
    }

    public static void makeRoot(Node x) {
        expose(x);
        x.revert = !x.revert;
    }

    public static boolean connected(Node x, Node y) {
        if (x == y)
            return true;
        expose(x);
        // now x.parent is null
        expose(y);
        return x.parent != null;
    }

    public static void link(Node x, Node y) {
        if (connected(x, y))
            throw new RuntimeException("error: x and y are already connected");
        makeRoot(x);
        x.parent = y;
    }

    public static void cut(Node x, Node y) {
        makeRoot(x);
        expose(y);
        // check that exposed path consists of a single edge (y,x)
        if (y.right != x || x.left != null || x.right != null)
            throw new RuntimeException("error: no edge (x,y)");
        y.right.parent = null;
        y.right = null;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1_000; step++) {
            int n = rnd.nextInt(50) + 1;
            boolean[][] g = new boolean[n][n];
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++)
                nodes[i] = new Node();
            for (int query = 0; query < 2_000; query++) {
                int cmd = rnd.nextInt(10);
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                Node x = nodes[u];
                Node y = nodes[v];
                if (cmd == 0) {
                    makeRoot(x);
                    expose(y);
                    if ((y.right == x && x.left == null && x.right == null) != g[u][v])
                        throw new RuntimeException();
                    if (y.right == x && x.left == null && x.right == null) {
                        cut(x, y);
                        g[u][v] = g[v][u] = false;
                    }
                } else if (cmd == 1) {
                    if (connected(x, y) != connected(g, u, v, -1))
                        throw new RuntimeException();
                } else {
                    expose(x);
                    if (connected(x, y) != connected(g, u, v, -1))
                        throw new RuntimeException();
                    if (!connected(x, y)) {
                        link(x, y);
                        g[u][v] = g[v][u] = true;
                    }
                }
            }
        }
    }

    static boolean connected(boolean[][] g, int u, int v, int p) {
        if (u == v)
            return true;
        for (int i = 0; i < g.length; i++)
            if (i != p && g[u][i] && connected(g, i, v, u))
                return true;
        return false;
    }
}
