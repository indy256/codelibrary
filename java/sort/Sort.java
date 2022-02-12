package sort;

import java.util.*;
import java.util.function.IntPredicate;

public class Sort {
    static Random rnd = new Random(1);

    public static void qSort(int[] a, int low, int high) {
        if (low >= high)
            return;
        int separator = a[low + rnd.nextInt(high - low + 1)];
        int i = low;
        int j = high;
        do {
            while (a[i] < separator) ++i;
            while (a[j] > separator) --j;
            if (i > j)
                break;
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
            ++i;
            --j;
        } while (i <= j);
        qSort(a, low, j);
        qSort(a, i, high);
    }

    public static void mergeSort(int[] a, int low, int high) {
        if (high - low < 2)
            return;
        int mid = (low + high) >>> 1;
        mergeSort(a, low, mid);
        mergeSort(a, mid, high);
        int[] b = Arrays.copyOfRange(a, low, mid);
        for (int i = low, j = mid, k = 0; k < b.length; i++) {
            if (j == high || b[k] <= a[j]) {
                a[i] = b[k++];
            } else {
                a[i] = a[j++];
            }
        }
    }

    public static void mergeSort2(int[] a, int low, int high) {
        int size = high - low;
        if (size < 2)
            return;
        int mid = (low + high) >>> 1;
        mergeSort2(a, low, mid);
        mergeSort2(a, mid, high);
        int[] b = new int[size];
        int i = low;
        int j = mid;
        for (int k = 0; k < size; k++) {
            if (i < mid && (j == high || a[i] <= a[j])) {
                b[k] = a[i++];
            } else {
                b[k] = a[j++];
            }
        }
        System.arraycopy(b, 0, a, low, size);
    }

    public static void inPlaceMergeSort(int[] a, int low, int high) {
        if (low < high - 1) {
            int mid = (low + high) >>> 1;
            mergeSort(a, low, mid);
            mergeSort(a, mid, high);
            inPlaceMerge(a, low, mid, high);
        }
    }

    // O(n*log(n)) complexity
    static void inPlaceMerge(int[] a, int from, int mid, int to) {
        if (from >= mid || mid >= to)
            return;
        if (to - from == 2) {
            if (a[from] > a[mid])
                swap(a, from, mid);
            return;
        }

        final int firstCut;
        final int secondCut;

        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2;
            secondCut = binarySearchFirstTrue(i -> a[i] >= a[firstCut], mid, to);
        } else {
            secondCut = mid + (to - mid) / 2;
            firstCut = binarySearchFirstTrue(i -> a[i] > a[secondCut], from, mid);
        }

        if (mid != firstCut && mid != secondCut) {
            rotate(a, firstCut, mid, secondCut);
        }

        mid = firstCut + (secondCut - mid);
        inPlaceMerge(a, from, firstCut, mid);
        inPlaceMerge(a, mid, secondCut, to);
    }

    static void swap(int[] a, int i, int j) {
        int t = a[j];
        a[j] = a[i];
        a[i] = t;
    }

    static void rotate(int[] a, int first, int middle, int last) {
        int next = middle;
        while (first != next) {
            swap(a, first++, next++);
            if (next == last)
                next = middle;
            else if (first == middle)
                middle = next;
        }
    }

    static int binarySearchFirstTrue(IntPredicate predicate, int fromInclusive, int toExclusive) {
        int lo = fromInclusive - 1;
        int hi = toExclusive;
        while (lo < hi - 1) {
            int mid = (lo + hi) >>> 1;
            if (!predicate.test(mid)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    public static void heapSort(int[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) pushDown(a, i, n);
        while (n > 1) {
            swap(a, 0, n - 1);
            pushDown(a, 0, --n);
        }
    }

    static void pushDown(int[] h, int pos, int size) {
        while (true) {
            int child = 2 * pos + 1;
            if (child >= size)
                break;
            if (child + 1 < size && h[child + 1] > h[child])
                child++;
            if (h[pos] >= h[child])
                break;
            swap(h, pos, child);
            pos = child;
        }
    }

    public static void bubbleSort(int[] a) {
        for (int i = 0; i + 1 < a.length; i++) {
            for (int j = 0; j + 1 < a.length; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }
    }

    public static void selectionSort(int[] a) {
        int n = a.length;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (a[p[i]] > a[p[j]]) {
                    swap(p, i, j);
                }
            }
        }
        int[] b = a.clone();
        for (int i = 0; i < n; i++) a[i] = b[p[i]];
    }

    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j - 1] > a[j]) {
                    swap(a, j - 1, j);
                }
            }
        }
    }

    // https://habr.com/ru/company/sportmaster_lab/blog/650585
    public static void strangeSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                if (a[i] < a[j]) {
                    swap(a, i, j);
                }
            }
        }
    }

    public static void countingSort(int[] a) {
        int max = 0;
        for (int x : a) {
            max = Math.max(max, x);
        }
        int[] cnt = new int[max + 1];
        for (int x : a) {
            ++cnt[x];
        }
        for (int i = 1; i < cnt.length; i++) {
            cnt[i] += cnt[i - 1];
        }
        int n = a.length;
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[--cnt[a[i]]] = a[i];
        }
        System.arraycopy(b, 0, a, 0, n);
    }

    public static void radixSort(int[] a) {
        final int d = 8;
        final int w = 32;
        int[] t = new int[a.length];
        for (int p = 0; p < w / d; p++) {
            // counting-sort
            int[] cnt = new int[1 << d];
            for (int i = 0; i < a.length; i++) ++cnt[((a[i] ^ Integer.MIN_VALUE) >>> (d * p)) & ((1 << d) - 1)];
            for (int i = 1; i < cnt.length; i++) cnt[i] += cnt[i - 1];
            for (int i = a.length - 1; i >= 0; i--)
                t[--cnt[((a[i] ^ Integer.MIN_VALUE) >>> (d * p)) & ((1 << d) - 1)]] = a[i];
            System.arraycopy(t, 0, a, 0, a.length);
        }
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(10) + 1;
            int[] a = rnd.ints(n, 0, 1000).toArray();
            int[] s = a.clone();
            Arrays.sort(s);

            int[] b = a.clone();
            bubbleSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            selectionSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            insertionSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            strangeSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            countingSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            qSort(b, 0, b.length - 1);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();
        }

        for (int step = 0; step < 10; step++) {
            int n = rnd.nextInt(50_000) + 100_000;
            int[] a = step == 0 ? new int[n] : rnd.ints(n).toArray();
            int[] s = a.clone();
            Arrays.sort(s);

            int[] b = a.clone();
            qSort(b, 0, b.length - 1);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            mergeSort(b, 0, b.length);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            mergeSort2(b, 0, b.length);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            inPlaceMergeSort(b, 0, b.length);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            heapSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();

            b = a.clone();
            radixSort(b);
            if (!Arrays.equals(s, b))
                throw new RuntimeException();
        }
    }
}
