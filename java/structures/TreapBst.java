package structures;

import java.util.*;

// https://en.wikipedia.org/wiki/Treap
public class TreapBst {
    static Random random = new Random();

    static class Treap {
        int key;
        long prio;
        Treap left;
        Treap right;
        int size;

        Treap(int key) {
            this.key = key;
            prio = random.nextLong();
            size = 1;
        }

        void update() {
            size = 1 + getSize(left) + getSize(right);
        }
    }

    static int getSize(Treap root) {
        return root == null ? 0 : root.size;
    }

    static class TreapPair {
        Treap left;
        Treap right;

        TreapPair(Treap left, Treap right) {
            this.left = left;
            this.right = right;
        }
    }

    static TreapPair split(Treap root, int minRight) {
        if (root == null)
            return new TreapPair(null, null);
        if (root.key >= minRight) {
            TreapPair leftSplit = split(root.left, minRight);
            root.left = leftSplit.right;
            root.update();
            leftSplit.right = root;
            return leftSplit;
        } else {
            TreapPair rightSplit = split(root.right, minRight);
            root.right = rightSplit.left;
            root.update();
            rightSplit.left = root;
            return rightSplit;
        }
    }

    static Treap merge(Treap left, Treap right) {
        if (left == null)
            return right;
        if (right == null)
            return left;
        // if (random.nextInt(left.size + right.size) < left.size) {
        if (left.prio > right.prio) {
            left.right = merge(left.right, right);
            left.update();
            return left;
        } else {
            right.left = merge(left, right.left);
            right.update();
            return right;
        }
    }

    static Treap insert(Treap root, int key) {
        TreapPair t = split(root, key);
        return merge(merge(t.left, new Treap(key)), t.right);
    }

    static Treap insert2(Treap root, int key) { // alternative implementation
        return insert_(root, new Treap(key));
    }

    static Treap insert_(Treap root, Treap node) {
        if (root == null) {
            return node;
        }
        if (root.prio < node.prio) {
            TreapPair t = split(root, node.key);
            node.left = t.left;
            node.right = t.right;
            node.update();
            return node;
        }
        if (node.key < root.key) {
            root.left = insert_(root.left, node);
            root.update();
            return root;
        } else {
            root.right = insert_(root.right, node);
            root.update();
            return root;
        }
    }

    static Treap remove(Treap root, int key) {
        TreapPair t = split(root, key);
        return merge(t.left, split(t.right, key + 1).right);
    }

    static Treap remove2(Treap root, int key) { // alternative implementation
        if (root == null) {
            return null;
        }
        if (key < root.key) {
            root.left = remove(root.left, key);
            root.update();
            return root;
        } else if (key > root.key) {
            root.right = remove(root.right, key);
            root.update();
            return root;
        } else {
            return merge(root.left, root.right);
        }
    }

    static int kth(Treap root, int k) {
        if (k < getSize(root.left))
            return kth(root.left, k);
        else if (k > getSize(root.left))
            return kth(root.right, k - getSize(root.left) - 1);
        return root.key;
    }

    static void print(Treap root) {
        if (root == null)
            return;
        print(root.left);
        System.out.println(root.key);
        print(root.right);
    }

    // O(n) treap creation (if not counting keys sorting)
    // http://wcipeg.com/wiki/Cartesian_tree
    static Treap createTreap(int[] keys) {
        Treap[] nodes = Arrays.stream(keys).mapToObj(Treap::new).sorted(Comparator.comparingInt(t -> t.key)).toArray(Treap[]::new);
        int n = keys.length;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        Treap root = n > 0 ? nodes[0] : null;
        for (int i = 1; i < n; i++) {
            int last = i - 1;
            if (nodes[last].prio >= nodes[i].prio) {
                nodes[last].right = nodes[i];
                parent[i] = last;
            } else {
                while (nodes[last].prio < nodes[i].prio && parent[last] != -1) {
                    last = parent[last];
                }
                if (nodes[last].prio < nodes[i].prio) {
                    nodes[i].left = nodes[last];
                    root = nodes[i];
                } else {
                    nodes[i].left = nodes[last].right;
                    nodes[last].right = nodes[i];
                    parent[i] = last;
                }
            }
        }
        updateSizes(root);
        return root;
    }

    private static void updateSizes(Treap root) {
        if (root == null)
            return;
        updateSizes(root.left);
        updateSizes(root.right);
        root.update();
    }

    // random test
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Treap treap = null;
        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < 1000_000; i++) {
            int key = random.nextInt(100_000);
            if (random.nextBoolean()) {
                treap = remove(treap, key);
                set.remove(key);
            } else if (!set.contains(key)) {
                treap = insert(treap, key);
                set.add(key);
            }
            if (set.size() != getSize(treap))
                throw new RuntimeException();
        }
        for (int i = 0; i < getSize(treap); i++) {
            if (!set.contains(kth(treap, i)))
                throw new RuntimeException();
        }
        System.out.println(System.currentTimeMillis() - time);

        for (int step = 0; step < 1000; step++) {
            int n = random.nextInt(10) + 1;
            int[] keys = random.ints(n, 0, 100).toArray();
            Treap t = createTreap(keys);
            Arrays.sort(keys);
            for (int i = 0; i < n; i++) {
                if (kth(t, i) != keys[i])
                    throw new RuntimeException();
            }
            checkHeapInvariant(t);
        }
    }

    private static void checkHeapInvariant(Treap root) {
        if (root == null)
            return;
        if (root.left != null && root.left.prio > root.prio)
            throw new RuntimeException();
        if (root.right != null && root.right.prio > root.prio)
            throw new RuntimeException();
        checkHeapInvariant(root.left);
        checkHeapInvariant(root.right);
    }
}
