package sort;

import java.util.Random;

public class Partition {

    // like http://www.cplusplus.com/reference/algorithm/partition/
    // but additionally places separator at the end of the first group
    public static int partition(int[] a, int fromInclusive, int toExclusive, int separatorIndex) {
        int i = fromInclusive;
        int j = toExclusive - 1;
        if (i >= j) return j;
        int separator = a[separatorIndex];
        swap(a, i++, separatorIndex);
        while (i <= j) {
            while (i <= j && a[i] < separator)
                ++i;
            while (i <= j && a[j] > separator)
                --j;
            if (i >= j)
                break;
            swap(a, i++, j--);
        }
        swap(a, j, fromInclusive);
        return j;
    }

    static void swap(int[] a, int i, int j) {
        int t = a[j];
        a[j] = a[i];
        a[i] = t;
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int n = rnd.nextInt(10) + 1;
            int[] a = rnd.ints(n, 0, 10).toArray();
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    for (int k = i; k <= j; k++) {
                        int[] b = a.clone();
                        check(b, partition(b, i, j + 1, k), i, j);
                    }
                }
            }
        }
    }

    static void check(int[] a, int k, int lo, int hi) {
        if (k < lo || k > hi)
            throw new RuntimeException();
        for (int i = lo; i <= k; i++)
            for (int j = k; j <= hi; j++)
                if (a[i] > a[j])
                    throw new RuntimeException();
    }
}
