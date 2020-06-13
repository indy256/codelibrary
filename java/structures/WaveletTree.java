package structures;

import java.util.Arrays;
import java.util.function.Predicate;

public class WaveletTree {
    static class Node {
        int lo;
        int hi;
        Node left;
        Node right;
        int[] b;

        // kth smallest element in [from, to]
        public int kth(int from, int to, int k) {
            if (from > to)
                return 0;
            if (lo == hi)
                return lo;
            int inLeft = b[to] - b[from - 1];
            int lb = b[from - 1]; // amt of nos in first (from-1) nos that go in left
            int rb = b[to]; // amt of nos in first (to) nos that go in left
            return k <= inLeft ? left.kth(lb + 1, rb, k) : right.kth(from - lb, to - rb, k - inLeft);
        }

        // number of elements in [from, to] less than or equal to k
        public int countLessOrEq(int from, int to, int k) {
            if (from > to || k < lo)
                return 0;
            if (hi <= k)
                return to - from + 1;
            int lb = b[from - 1], rb = b[to];
            return left.countLessOrEq(lb + 1, rb, k) + right.countLessOrEq(from - lb, to - rb, k);
        }

        // number of elements in [from, to] equal to k
        public int countEq(int from, int to, int k) {
            if (from > to || k < lo || k > hi)
                return 0;
            if (lo == hi)
                return to - from + 1;
            int lb = b[from - 1];
            int rb = b[to];
            int mid = (lo + hi) >>> 1;
            return k <= mid ? left.countEq(lb + 1, rb, k) : right.countEq(from - lb, to - rb, k);
        }
    }

    public static Node createTree(int[] a) {
        int lo = Arrays.stream(a).min().orElse(Integer.MAX_VALUE);
        int hi = Arrays.stream(a).max().orElse(Integer.MIN_VALUE);
        return build(a, 0, a.length, lo, hi);
    }

    static Node build(int[] a, int from, int to, int lo, int hi) {
        Node node = new Node();
        node.lo = lo;
        node.hi = hi;
        if (lo < hi && from < to) {
            int mid = (lo + hi) >>> 1;
            Predicate<Integer> p = x -> x <= mid;
            node.b = new int[to - from + 1];
            for (int i = 0; i + 1 < node.b.length; i++) {
                node.b[i + 1] = node.b[i] + (p.test(a[i + from]) ? 1 : 0);
            }
            int pivot = stablePartition(a, from, to, p);
            node.left = build(a, from, pivot, lo, mid);
            node.right = build(a, pivot, to, mid + 1, hi);
        }
        return node;
    }

    static int stablePartition(int[] a, int from, int to, Predicate<Integer> p) {
        int[] b1 = new int[to - from + 1];
        int[] b2 = new int[to - from + 1];
        int cnt1 = 0;
        int cnt2 = 0;
        for (int i = from; i < to; i++) {
            if (p.test(a[i])) {
                b1[cnt1++] = a[i];
            } else {
                b2[cnt2++] = a[i];
            }
        }
        System.arraycopy(b1, 0, a, from, cnt1);
        System.arraycopy(b2, 0, a, from + cnt1, cnt2);
        return cnt1;
    }

    // Usage example
    public static void main(String[] args) {
        int[] a = {5, 1, 2, 1, 1};
        Node t = WaveletTree.createTree(a);
        System.out.println(t.countEq(1, 5, 1));
    }
}
