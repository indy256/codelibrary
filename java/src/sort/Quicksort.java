package sort;

import java.util.*;

public class Quicksort {

    static Random rnd = new Random();

    public static void quickSort(int[] a, int low, int high) {
        if (low >= high)
            return;
        int separator = a[low + rnd.nextInt(high - low + 1)];
        int i = low;
        int j = high;
        while (i <= j) {
            while (a[i] < separator)
                ++i;
            while (a[j] > separator)
                --j;
            if (i <= j) {
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
                ++i;
                --j;
            }
        }
        quickSort(a, low, j);
        quickSort(a, i, high);
    }

    // test
    public static void main(String[] args) {
        int n = 10_000_000;
        int[] a1 = rnd.ints(n).toArray();

        int[] a2 = a1.clone();
        Arrays.sort(a2);

        long time = System.currentTimeMillis();
        quickSort(a1, 0, a1.length - 1);
        System.out.println(System.currentTimeMillis() - time);

        if (!Arrays.equals(a1, a2))
            throw new RuntimeException();
    }
}
