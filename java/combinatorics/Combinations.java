package combinatorics;

import java.util.Arrays;

// https://en.wikipedia.org/wiki/Combination
public class Combinations {

    public static boolean nextCombination(int[] p, int n) {
        int m = p.length;
        for (int i = m - 1; i >= 0; i--) {
            if (p[i] < n - m + i) {
                ++p[i];
                while (++i < m) {
                    p[i] = p[i - 1] + 1;
                }
                return true;
            }
        }
        return false;
    }

    public static int[] combinationByNumber(int n, int m, long number) {
        int[] c = new int[m];
        int cnt = n;
        for (int i = 0; i < m; i++) {
            int j = 1;
            while (true) {
                long am = binomial(cnt - j, m - 1 - i);
                if (number < am)
                    break;
                number -= am;
                ++j;
            }
            c[i] = (i > 0) ? (c[i - 1] + j) : (j - 1);
            cnt -= j;
        }
        return c;
    }

    public static long numberByCombination(int[] c, int n) {
        int m = c.length;
        long res = 0;
        int prev = -1;
        for (int i = 0; i < m; i++) {
            for (int j = prev + 1; j < c[i]; j++) {
                res += binomial(n - 1 - j, m - 1 - i);
            }
            prev = c[i];
        }
        return res;
    }

    static long binomial(long n, long m) {
        m = Math.min(m, n - m);
        long res = 1;
        for (long i = 0; i < m; i++) {
            res = res * (n - i) / (i + 1);
        }
        return res;
    }

    public static boolean nextCombinationWithRepeats(int[] p, int n) {
        int m = p.length;
        for (int i = m - 1; i >= 0; i--) {
            if (p[i] < n - 1) {
                ++p[i];
                while (++i < m) {
                    p[i] = p[i - 1];
                }
                return true;
            }
        }
        return false;
    }

    // Usage example
    public static void main(String[] args) {
        int[] p = {0, 1};
        System.out.println(!nextCombination(p, 2));
        System.out.println(Arrays.equals(new int[]{0, 1}, p));

        p = new int[]{0, 0};
        System.out.println(nextCombinationWithRepeats(p, 2));
        System.out.println(Arrays.equals(new int[]{0, 1}, p));

        System.out.println(nextCombinationWithRepeats(p, 2));
        System.out.println(Arrays.equals(new int[]{1, 1}, p));

        System.out.println(!nextCombinationWithRepeats(p, 2));

        System.out.println(78 == numberByCombination(new int[]{1, 2, 3, 6, 8}, 9));
        System.out.println(Arrays.toString(combinationByNumber(9, 5, 78)));

        p = new int[]{0, 0, 0};
        do {
            System.out.println(Arrays.toString(p));
        } while (nextCombinationWithRepeats(p, 3));
    }
}
