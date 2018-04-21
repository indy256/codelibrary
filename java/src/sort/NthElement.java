package sort;

import java.util.*;
import java.util.stream.IntStream;

public class NthElement {

    // See: http://www.cplusplus.com/reference/algorithm/nth_element
    // O(n) on average
    public static void nth_element(int[] a, int low, int high, int n, Random rnd) {
        while (true) {
            int k = partition(a, low, high, low + rnd.nextInt(high - low));
            if (n < k)
                high = k;
            else if (n > k)
                low = k + 1;
            else
                return;
        }
    }

    static int partition(int[] a, int fromInclusive, int toExclusive, int separatorIndex) {
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

    // O(n) worst case. See Cormen et al
    public static void nth_element2(int[] a, int low, int high, int n) {
        if (high - low <= 1)
            return;
        while (true) {
            int[] a5 = new int[(high - low + 4) / 5];
            for (int i = low, cnt = 0; i < high; i += 5) {
                int j = Math.min(i + 5, high);
                for (int iteration = 0; iteration < 3; iteration++) {
                    for (int k = i; k + 1 < j; k++) {
                        if (a[k] > a[k + 1]) {
                            int t = a[k];
                            a[k] = a[k + 1];
                            a[k + 1] = t;
                        }
                    }
                }
                a5[cnt++] = a[(i + j) >>> 1];
            }
            nth_element2(a5, 0, a5.length, a5.length / 2);
            int separatorIndex = IntStream.range(low, high).filter(i -> a[i] == a5[a5.length / 2]).findFirst().getAsInt();
            int k = partition(a, low, high, separatorIndex);
            if (n < k)
                high = k;
            else if (n > k)
                low = k + 1;
            else
                return;
        }
    }

    // Random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        int len = 1000_000;
        nth_element(new int[len], 0, len, 0, rnd);
        nth_element2(new int[len], 0, len, 0);
        for (int step = 0; step < 100_000; step++) {
            int n = rnd.nextInt(10) + 1;
            int[] a = rnd.ints(n, 0, 10).toArray();
            int[] b = a.clone();
            int k = rnd.nextInt(n);
            nth_element(a, 0, n, k, rnd);
            nth_element2(b, 0, n, k);
            int[] sa = a.clone();
            Arrays.sort(sa);
            int[] sb = b.clone();
            Arrays.sort(sb);
            if (!Arrays.equals(sa, sb))
                throw new RuntimeException();
            if (a[k] != sa[k] || b[k] != sb[k])
                throw new RuntimeException();
            for (int i = 0; i < n; i++) {
                if (i < k && a[i] > a[k] || i > k && a[i] < a[k])
                    throw new RuntimeException();
                if (i < k && b[i] > b[k] || i > k && b[i] < b[k])
                    throw new RuntimeException();
            }
        }
    }
}
