package misc;

import java.util.*;

public class ArrayRotate {
    public static void rotate1(int[] a, int first, int middle, int last) {
        int next = middle;
        while (first != next) {
            swap(a, first++, next++);
            if (next == last)
                next = middle;
            else if (first == middle)
                middle = next;
        }
    }

    public static void rotate2(int[] a, int first, int middle, int last) {
        reverse(a, first, middle);
        reverse(a, middle, last);
        reverse(a, first, last);
    }

    static void reverse(int[] a, int from, int to) {
        while (from + 1 < to) swap(a, from++, --to);
    }

    static void swap(int[] a, int i, int j) {
        int t = a[j];
        a[j] = a[i];
        a[i] = t;
    }

    public static void rotate3(int[] a, int first, int middle, int last) {
        int n = last - first;
        int jump = middle - first;
        for (int i = first, count = 0; count < n; i++) {
            int cur = i;
            int tmp = a[cur];
            while (true) {
                ++count;
                int next = cur + jump;
                if (next >= last)
                    next -= n;
                if (next == i)
                    break;
                a[cur] = a[next];
                cur = next;
            }
            a[cur] = tmp;
        }
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(10) + 1;
            int middle = rnd.nextInt(n);
            int[] a = rnd.ints(n, 0, 10).toArray();
            int[] b1 = a.clone();
            rotate1(b1, 0, middle, n);
            int[] b2 = a.clone();
            rotate2(b2, 0, middle, n);
            int[] b3 = a.clone();
            rotate3(b3, 0, middle, n);
            if (!Arrays.equals(b1, b2) || !Arrays.equals(b1, b3))
                throw new RuntimeException();
        }
    }
}
