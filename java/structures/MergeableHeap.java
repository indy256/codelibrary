package structures;

import java.util.Random;

public class MergeableHeap {
    static Random random = new Random();

    public static class Heap {
        int value;
        Heap left;
        Heap right;

        Heap(int value) {
            this.value = value;
        }
    }

    public static Heap merge(Heap a, Heap b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        if (a.value > b.value) {
            Heap t = a;
            a = b;
            b = t;
        }
        if (random.nextBoolean()) {
            Heap t = a.left;
            a.left = a.right;
            a.right = t;
        }
        a.left = merge(a.left, b);
        return a;
    }

    public static Heap add(Heap h, int value) {
        return merge(h, new Heap(value));
    }

    public static class HeapAndResult {
        Heap heap;
        int value;

        HeapAndResult(Heap h, int value) {
            this.heap = h;
            this.value = value;
        }
    }

    public static HeapAndResult removeMin(Heap h) {
        return new HeapAndResult(merge(h.left, h.right), h.value);
    }

    // Usage example
    public static void main(String[] args) {
        Heap h = null;
        h = add(h, 3);
        h = add(h, 1);
        h = add(h, 2);
        while (h != null) {
            HeapAndResult hv = removeMin(h);
            System.out.println(hv.value);
            h = hv.heap;
        }
    }
}
