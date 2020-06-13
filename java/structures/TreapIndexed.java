package structures;

import java.util.*;
import java.util.function.Predicate;

// https://cp-algorithms.com/data_structures/treap.html
public class TreapIndexed {
    static Random random = new Random();

    public static class Node {
        long nodeValue;
        long mx;
        long sum;
        long add;

        int size;
        long prio;
        Node left;
        Node right;

        Node(long value) {
            nodeValue = value;
            mx = value;
            sum = value;
            add = 0;
            size = 1;
            prio = random.nextLong();
        }

        void apply(long v) {
            nodeValue += v;
            mx += v;
            sum += v * size;
            add += v;
        }

        void push() {
            if (add != 0) {
                if (left != null)
                    left.apply(add);
                if (right != null)
                    right.apply(add);
                add = 0;
            }
        }

        void pull() {
            mx = Math.max(nodeValue, Math.max(getMx(left), getMx(right)));
            sum = nodeValue + getSum(left) + getSum(right);
            size = 1 + getSize(left) + getSize(right);
        }

        static long getMx(Node root) {
            return root == null ? Long.MIN_VALUE : root.mx;
        }

        static long getSum(Node root) {
            return root == null ? 0 : root.sum;
        }

        static int getSize(Node root) {
            return root == null ? 0 : root.size;
        }
    }

    public static class TreapPair {
        Node left;
        Node right;

        TreapPair(Node left, Node right) {
            this.left = left;
            this.right = right;
        }
    }

    public static TreapPair split(Node root, int minRight) {
        if (root == null)
            return new TreapPair(null, null);
        root.push();
        if (Node.getSize(root.left) >= minRight) {
            TreapPair sub = split(root.left, minRight);
            root.left = sub.right;
            root.pull();
            sub.right = root;
            return sub;
        } else {
            TreapPair sub = split(root.right, minRight - Node.getSize(root.left) - 1);
            root.right = sub.left;
            root.pull();
            sub.left = root;
            return sub;
        }
    }

    public static Node merge(Node left, Node right) {
        if (left == null)
            return right;
        if (right == null)
            return left;
        left.push();
        right.push();
        if (left.prio > right.prio) {
            left.right = merge(left.right, right);
            left.pull();
            return left;
        } else {
            right.left = merge(left, right.left);
            right.pull();
            return right;
        }
    }

    public static Node insert(Node root, int index, long value) {
        TreapPair t = split(root, index);
        return merge(merge(t.left, new Node(value)), t.right);
    }

    public static Node remove(Node root, int index) {
        TreapPair t = split(root, index);
        return merge(t.left, split(t.right, index + 1 - Node.getSize(t.left)).right);
    }

    public static Node modify(Node root, int ll, int rr, long delta) {
        TreapPair t1 = split(root, rr + 1);
        TreapPair t2 = split(t1.left, ll);

        if (t2.right != null)
            t2.right.apply(delta);
        return merge(merge(t2.left, t2.right), t1.right);
    }

    public static class TreapAndResult {
        Node treap;
        long mx;
        long sum;

        TreapAndResult(Node t, long mx, long sum) {
            this.treap = t;
            this.mx = mx;
            this.sum = sum;
        }
    }

    public static TreapAndResult query(Node root, int ll, int rr) {
        TreapPair t1 = split(root, rr + 1);
        TreapPair t2 = split(t1.left, ll);
        long mx = Node.getMx(t2.right);
        long sum = Node.getSum(t2.right);
        return new TreapAndResult(merge(merge(t2.left, t2.right), t1.right), mx, sum);
    }

    // calls all FALSE elements to the left of the sought position exactly once
    public static int findFirst(Node root, int ll, int rr, Predicate<Node> f) {
        return findFirst(root, ll, rr, f, 0, Node.getSize(root) - 1);
    }

    static int findFirst(Node root, int ll, int rr, Predicate<Node> f, int l, int r) {
        if (ll <= l && r <= rr && !f.test(root)) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        root.push();
        int m = Node.getSize(root.left);
        int res = -1;
        if (ll < m) {
            res = findFirst(root.left, ll, rr, f, l, l + m - 1);
        }
        if (res == -1) {
            Node single = new Node(0);
            single.size = 1;
            single.apply(root.nodeValue);
            res = findFirst(single, ll, rr, f, l + m, l + m);
        }
        if (rr > m && res == -1) {
            res = findFirst(root.right, ll, rr, f, l + m + 1, r);
        }
        root.pull();
        return res;
    }

    public static void print(Node root) {
        if (root == null)
            return;
        root.push();
        print(root.left);
        System.out.print(root.nodeValue + " ");
        print(root.right);
    }

    // Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
    static int sumLowerBound(Node treap, int ll, int rr, long sum) {
        long[] sumSoFar = new long[1];
        return findFirst(treap, ll, rr, node -> {
            if (sumSoFar[0] + node.sum >= sum)
                return true;
            sumSoFar[0] += node.sum;
            return false;
        });
    }

    // Random test
    public static void main(String[] args) {
        Node treap = null;
        List<Integer> list = new ArrayList<>();
        Random rnd = new Random(1);
        for (int step = 0; step < 100000; step++) {
            int cmd = rnd.nextInt(6);
            if (cmd < 2 && list.size() < 100) {
                int pos = rnd.nextInt(list.size() + 1);
                int value = rnd.nextInt(100);
                list.add(pos, value);
                treap = insert(treap, pos, value);
            } else if (cmd < 3 && list.size() > 0) {
                int pos = rnd.nextInt(list.size());
                list.remove(pos);
                treap = remove(treap, pos);
            } else if (cmd < 4 && list.size() > 0) {
                int b = rnd.nextInt(list.size());
                int a = rnd.nextInt(b + 1);
                int res = list.get(a);
                for (int i = a + 1; i <= b; i++) res = Math.max(res, list.get(i));
                TreapAndResult tr = query(treap, a, b);
                treap = tr.treap;
                if (res != tr.mx)
                    throw new RuntimeException();
            } else if (cmd < 5 && list.size() > 0) {
                int b = rnd.nextInt(list.size());
                int a = rnd.nextInt(b + 1);
                int delta = rnd.nextInt(100) - 50;
                for (int i = a; i <= b; i++) list.set(i, list.get(i) + delta);
                treap = modify(treap, a, b, delta);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    TreapAndResult tr = query(treap, i, i);
                    treap = tr.treap;
                    long v = tr.mx;
                    if (list.get(i) != v)
                        throw new RuntimeException();
                }
            }
        }
        System.out.println("Test passed");

        treap = null;
        for (long v : new long[] {2, 1, 10, 20}) {
            treap = insert(treap, Node.getSize(treap), v);
        }
        System.out.println(2 == sumLowerBound(treap, 0, treap.size - 1, 12));
    }
}
