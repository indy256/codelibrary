package structures;

import java.util.*;

// https://en.wikipedia.org/wiki/Treap
public class TreapImplicitKey {

    static Random random = new Random();

    public static class Treap {
        long nodeValue;
        long subTreeMx;
        long subTreeSum;
        long add;

        int size;
        long prio;
        Treap left;
        Treap right;

        Treap(int value) {
            nodeValue = value;
            subTreeMx = value;
            subTreeSum = value;
            add = 0;
            size = 1;
            prio = random.nextLong();
        }

        void apply(long v) {
            nodeValue += v;
            subTreeMx += v;
            subTreeSum += v * size;
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
            subTreeMx = Math.max(nodeValue, Math.max(getSubTreeMx(left), getSubTreeMx(right)));
            subTreeSum = nodeValue + getSubTreeSum(left) + getSubTreeSum(right);
            size = 1 + getSize(left) + getSize(right);
        }
    }

    static long getSubTreeMx(Treap root) {
        return root == null ? Long.MIN_VALUE : root.subTreeMx;
    }

    static long getSubTreeSum(Treap root) {
        return root == null ? 0 : root.subTreeSum;
    }

    static int getSize(Treap root) {
        return root == null ? 0 : root.size;
    }

    public static class TreapPair {
        Treap left;
        Treap right;

        TreapPair(Treap left, Treap right) {
            this.left = left;
            this.right = right;
        }
    }

    public static TreapPair split(Treap root, int minRight) {
        if (root == null)
            return new TreapPair(null, null);
        root.push();
        if (getSize(root.left) >= minRight) {
            TreapPair sub = split(root.left, minRight);
            root.left = sub.right;
            root.pull();
            sub.right = root;
            return sub;
        } else {
            TreapPair sub = split(root.right, minRight - getSize(root.left) - 1);
            root.right = sub.left;
            root.pull();
            sub.left = root;
            return sub;
        }
    }

    public static Treap merge(Treap left, Treap right) {
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

    public static Treap insert(Treap root, int index, int value) {
        TreapPair t = split(root, index);
        return merge(merge(t.left, new Treap(value)), t.right);
    }

    public static Treap remove(Treap root, int index) {
        TreapPair t = split(root, index);
        return merge(t.left, split(t.right, index + 1 - getSize(t.left)).right);
    }

    public static Treap modify(Treap root, int a, int b, long delta) {
        TreapPair t1 = split(root, b + 1);
        TreapPair t2 = split(t1.left, a);

        t2.right.apply(delta);
        return merge(merge(t2.left, t2.right), t1.right);
    }

    public static class TreapAndResult {
        Treap treap;
        long mx;

        TreapAndResult(Treap t, long mx) {
            this.treap = t;
            this.mx = mx;
        }
    }

    public static TreapAndResult query(Treap root, int a, int b) {
        TreapPair t1 = split(root, b + 1);
        TreapPair t2 = split(t1.left, a);
        long mx = getSubTreeMx(t2.right);
        return new TreapAndResult(merge(merge(t2.left, t2.right), t1.right), mx);
    }

    public static void print(Treap root) {
        if (root == null)
            return;
        root.push();
        print(root.left);
        System.out.print(root.nodeValue + " ");
        print(root.right);
    }

    // Random test
    public static void main(String[] args) {
        Treap treap = null;
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
                for (int i = a + 1; i <= b; i++)
                    res = Math.max(res, list.get(i));
                TreapAndResult tr = query(treap, a, b);
                treap = tr.treap;
                if (res != tr.mx)
                    throw new RuntimeException();
            } else if (cmd < 5 && list.size() > 0) {
                int b = rnd.nextInt(list.size());
                int a = rnd.nextInt(b + 1);
                int delta = rnd.nextInt(100) - 50;
                for (int i = a; i <= b; i++)
                    list.set(i, list.get(i) + delta);
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
    }
}
