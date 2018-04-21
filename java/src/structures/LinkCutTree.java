package structures;

import java.util.*;

// LinkCut tree with path queries. Query complexity is O(log(n)) amortized.
// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
public class LinkCutTree {

    // Modify the following 5 methods to implement your custom operations on the tree.
    // This example implements Add/Sum operations. Operations like Add/Max, Set/Max can also be implemented.
    static int modifyOperation(int x, int y) {
        return x + y;
    }

    // query (or combine) operation
    static int queryOperation(int leftValue, int rightValue) {
        return leftValue + rightValue;
    }

    static int deltaEffectOnSegment(int delta, int segmentLength) {
        if (delta == getNeutralDelta()) return getNeutralDelta();
        // Here you must write a fast equivalent of following slow code:
        // int result = delta;
        // for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
        // return result;
        return delta * segmentLength;
    }

    static int getNeutralDelta() {
        return 0;
    }

    static int getNeutralValue() {
        return 0;
    }

    // generic code
    static int joinValueWithDelta(int value, int delta) {
        if (delta == getNeutralDelta()) return value;
        return modifyOperation(value, delta);
    }

    static int joinDeltas(int delta1, int delta2) {
        if (delta1 == getNeutralDelta()) return delta2;
        if (delta2 == getNeutralDelta()) return delta1;
        return modifyOperation(delta1, delta2);
    }

    public static class Node {
        int nodeValue;
        int subTreeValue;
        int delta; // delta affects nodeValue, subTreeValue, left.delta and right.delta
        int size;
        boolean revert;
        Node left;
        Node right;
        Node parent;

        Node(int value) {
            nodeValue = value;
            subTreeValue = value;
            delta = getNeutralDelta();
            size = 1;
        }

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

            nodeValue = joinValueWithDelta(nodeValue, delta);
            subTreeValue = joinValueWithDelta(subTreeValue, deltaEffectOnSegment(delta, size));
            if (left != null)
                left.delta = joinDeltas(left.delta, delta);
            if (right != null)
                right.delta = joinDeltas(right.delta, delta);
            delta = getNeutralDelta();
        }

        void update() {
            subTreeValue = queryOperation(queryOperation(getSubTreeValue(left), joinValueWithDelta(nodeValue, delta)), getSubTreeValue(right));
            size = 1 + getSize(left) + getSize(right);
        }
    }

    static int getSize(Node root) {
        return root == null ? 0 : root.size;
    }

    static int getSubTreeValue(Node root) {
        return root == null ? getNeutralValue() : joinValueWithDelta(root.subTreeValue, deltaEffectOnSegment(root.delta, root.size));
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
        p.update();
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
        x.update();
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

    public static int query(Node from, Node to) {
        makeRoot(from);
        expose(to);
        return getSubTreeValue(to);
    }

    public static void modify(Node from, Node to, int delta) {
        makeRoot(from);
        expose(to);
        to.delta = joinDeltas(to.delta, delta);
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1_000; step++) {
            int n = rnd.nextInt(50) + 1;
            boolean[][] g = new boolean[n][n];
            int[] val = new int[n];
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++)
                nodes[i] = new Node(0);
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
                        int res = getNeutralValue();
                        for (int i : path)
                            res = queryOperation(res, val[i]);
                        if (query(x, y) != res)
                            throw new RuntimeException();
                    }
                } else if (cmd == 2) {
                    if (connected(x, y)) {
                        List<Integer> path = new ArrayList<>();
                        getPathFromAtoB(g, u, v, -1, path);
                        int delta = rnd.nextInt(100) + 1;
                        for (int i : path)
                            val[i] = joinValueWithDelta(val[i], delta);
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
