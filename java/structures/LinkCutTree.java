package structures;

import java.util.*;

// LinkCut tree with path queries. Query complexity is O(log(n)) amortized.
// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
public class LinkCutTree {
    public static class Node {
        long nodeValue;
        long subTreeSum;
        long add;
        boolean revert;

        int size;
        Node left;
        Node right;
        Node parent;

        Node(long value) {
            nodeValue = value;
            subTreeSum = value;
            add = 0;
            size = 1;
        }

        // tests whether x is a root of a splay tree
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }

        void apply(long v) {
            nodeValue += v;
            subTreeSum += v * size;
            add += v;
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
            if (add != 0) {
                if (left != null)
                    left.apply(add);
                if (right != null)
                    right.apply(add);
                add = 0;
            }
        }

        void pull() {
            subTreeSum = nodeValue + getSubTreeSum(left) + getSubTreeSum(right);
            size = 1 + getSize(left) + getSize(right);
        }

        static long getSubTreeSum(Node root) {
            return root == null ? 0 : root.subTreeSum;
        }

        static int getSize(Node root) {
            return root == null ? 0 : root.size;
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
        connect(x, g, isRootP ? null : p == g.left);
        p.pull();
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
                rotate((x == p.left) == (p == g.left) ? p /*zig-zig*/ : x /*zig-zag*/);
            rotate(x);
        }
        x.push();
        x.pull();
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
        if (y.right != x || x.left != null)
            throw new RuntimeException("error: no edge (x,y)");
        y.right.parent = null;
        y.right = null;
    }

    public static long query(Node from, Node to) {
        makeRoot(from);
        expose(to);
        return Node.getSubTreeSum(to);
    }

    public static void modify(Node from, Node to, long delta) {
        makeRoot(from);
        expose(to);
        to.apply(delta);
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1_000; step++) {
            int n = rnd.nextInt(50) + 1;
            boolean[][] g = new boolean[n][n];
            int[] val = new int[n];
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) nodes[i] = new Node(0);
            for (int query = 0; query < 2_000; query++) {
                int cmd = rnd.nextInt(10);
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                Node x = nodes[u];
                Node y = nodes[v];
                if (cmd == 0) {
                    makeRoot(x);
                    expose(y);
                    if (y.right == x && x.left == null && x.right == null) {
                        cut(x, y);
                        g[u][v] = g[v][u] = false;
                    }
                } else if (cmd == 1) {
                    if (connected(x, y)) {
                        List<Integer> path = new ArrayList<>();
                        getPathFromAtoB(g, u, v, -1, path);
                        int res = 0;
                        for (int i : path) res = res + val[i];
                        if (query(x, y) != res)
                            throw new RuntimeException();
                    }
                } else if (cmd == 2) {
                    if (connected(x, y)) {
                        List<Integer> path = new ArrayList<>();
                        getPathFromAtoB(g, u, v, -1, path);
                        int delta = rnd.nextInt(100) + 1;
                        for (int i : path) val[i] += delta;
                        modify(x, y, delta);
                    }
                } else {
                    if (!connected(x, y)) {
                        link(x, y);
                        g[u][v] = g[v][u] = true;
                    }
                }
            }
        }
        System.out.println("Test passed");
    }

    static boolean getPathFromAtoB(boolean[][] tree, int u, int v, int p, List<Integer> path) {
        path.add(u);
        if (u == v)
            return true;
        for (int i = 0; i < tree.length; i++)
            if (i != p && tree[u][i] && getPathFromAtoB(tree, i, v, u, path))
                return true;
        path.remove(path.size() - 1);
        return false;
    }
}
