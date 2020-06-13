package structures;

import java.util.Random;

// https://cp-algorithms.com/data_structures/treap.html
public class Treap {
    static Random random = new Random();

    public static class Node {
        long nodeValue;
        long mx;
        long sum;
        long add;

        long key; // keys should be unique
        int size;
        long prio;
        Node left;
        Node right;

        Node(long key, long value) {
            this.key = key;
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

    public static TreapPair split(Node root, long minRight) {
        if (root == null)
            return new TreapPair(null, null);
        root.push();
        if (root.key >= minRight) {
            TreapPair sub = split(root.left, minRight);
            root.left = sub.right;
            root.pull();
            sub.right = root;
            return sub;
        } else {
            TreapPair sub = split(root.right, minRight);
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

    public static Node insert(Node root, long key, long value) {
        TreapPair t = split(root, key);
        return merge(merge(t.left, new Node(key, value)), t.right);
    }

    public static Node remove(Node root, long key) {
        TreapPair t = split(root, key);
        return merge(t.left, split(t.right, key + 1).right);
    }

    public static Node modify(Node root, long ll, long rr, long delta) {
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

    public static TreapAndResult query(Node root, long ll, long rr) {
        TreapPair t1 = split(root, rr + 1);
        TreapPair t2 = split(t1.left, ll);
        long mx = Node.getMx(t2.right);
        long sum = Node.getSum(t2.right);
        return new TreapAndResult(merge(merge(t2.left, t2.right), t1.right), mx, sum);
    }

    static Node kth(Node root, int k) {
        if (k < Node.getSize(root.left))
            return kth(root.left, k);
        else if (k > Node.getSize(root.left))
            return kth(root.right, k - Node.getSize(root.left) - 1);
        return root;
    }

    public static void print(Node root) {
        if (root == null)
            return;
        root.push();
        print(root.left);
        System.out.print(root.nodeValue + " ");
        print(root.right);
    }

    // Random test
    public static void main(String[] args) {
        Node treap = null;
        treap = insert(treap, 5, 3);
        treap = insert(treap, 3, 2);
        treap = insert(treap, 6, 1);
        System.out.println(kth(treap, 1).key);
        System.out.println(query(treap, 1, 10).mx);
        treap = remove(treap, 5);
        System.out.println(query(treap, 1, 10).mx);
    }
}
