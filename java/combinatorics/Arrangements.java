package combinatorics;

import java.util.Arrays;

// https://en.wikipedia.org/wiki/Partial_permutation
public class Arrangements {

    public static boolean nextArrangement(int[] a, int n) {
        boolean[] used = new boolean[n];
        for (int x : a)
            used[x] = true;
        int m = a.length;
        for (int i = m - 1; i >= 0; i--) {
            used[a[i]] = false;
            for (int j = a[i] + 1; j < n; j++) {
                if (!used[j]) {
                    a[i++] = j;
                    used[j] = true;
                    for (int k = 0; i < m; k++) {
                        if (!used[k]) {
                            a[i++] = k;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] arrangementByNumber(int n, int m, long number) {
        int[] a = new int[m];
        int[] free = new int[n];
        for (int i = 0; i < n; i++) {
            free[i] = i;
        }
        for (int i = 0; i < m; i++) {
            long cnt = countOfArrangements(n - 1 - i, m - 1 - i);
            int pos = (int) (number / cnt);
            a[i] = free[pos];
            System.arraycopy(free, pos + 1, free, pos, n - 1 - pos);
            number %= cnt;
        }
        return a;
    }

    public static long numberByArrangement(int[] a, int n) {
        int m = a.length;
        long res = 0;
        boolean[] used = new boolean[n];
        for (int i = 0; i < m; i++) {
            int cnt = 0;
            for (int j = 0; j < a[i]; j++) {
                if (!used[j]) {
                    ++cnt;
                }
            }
            res += cnt * countOfArrangements(n - i - 1, m - i - 1);
            used[a[i]] = true;
        }
        return res;
    }

    public static long countOfArrangements(int n, int m) {
        long res = 1;
        for (int i = 0; i < m; i++) {
            res *= n - i;
        }
        return res;
    }

    public static boolean nextArrangementWithRepeats(int[] a, int n) {
        for (int i = a.length - 1; i >= 0; i--) {
            if (a[i] < n - 1) {
                ++a[i];
                Arrays.fill(a, i + 1, a.length, 0);
                return true;
            }
        }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        // print all arrangements
        int[] a = {0, 1, 2};
        int cnt = 0;
        int n = 4;
        do {
            System.out.println(Arrays.toString(a));
            if (!Arrays.equals(a, arrangementByNumber(n, a.length, numberByArrangement(a, n))) ||
                    cnt != numberByArrangement(arrangementByNumber(n, a.length, cnt), n))
                throw new RuntimeException();
            ++cnt;
        } while (nextArrangement(a, n));

        // print all arrangements with repeats
        a = new int[]{0, 0};
        do {
            System.out.println(Arrays.toString(a));
        } while (nextArrangementWithRepeats(a, 2));

        a = new int[]{2, 3, 4};
        System.out.println(32 == numberByArrangement(a, 5));
        System.out.println(Arrays.equals(a, arrangementByNumber(5, a.length, 32)));
    }
}
