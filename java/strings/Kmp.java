package strings;

import java.util.Random;

// https://en.wikipedia.org/wiki/Knuth–Morris–Pratt_algorithm
public class Kmp {
    public static int[] prefixFunction(String s) {
        int[] p = new int[s.length()];
        int k = 0;
        for (int i = 1; i < s.length(); i++) {
            while (k > 0 && s.charAt(k) != s.charAt(i)) k = p[k - 1];
            if (s.charAt(k) == s.charAt(i))
                ++k;
            p[i] = k;
        }
        return p;
    }

    public static int findSubstring(String haystack, String needle) {
        int m = needle.length();
        if (m == 0)
            return 0;
        int[] p = prefixFunction(needle);
        for (int i = 0, k = 0; i < haystack.length(); i++) {
            while (k > 0 && needle.charAt(k) != haystack.charAt(i)) k = p[k - 1];
            if (needle.charAt(k) == haystack.charAt(i))
                ++k;
            if (k == m)
                return i + 1 - m;
        }
        return -1;
    }

    public static int minPeriod(String s) {
        int n = s.length();
        int[] p = prefixFunction(s);

        int maxBorder = p[n - 1];
        int minPeriod = n - maxBorder;

        // check periodicity
        // if (minPeriod < n && n % minPeriod != 0) return -1;

        return minPeriod;
    }

    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10_000; step++) {
            String s = getRandomString(rnd, 100);
            String pattern = getRandomString(rnd, 5);
            int pos1 = findSubstring(s, pattern);
            int pos2 = s.indexOf(pattern);
            if (pos1 != pos2)
                throw new RuntimeException();
        }
        System.out.println(minPeriod("abababab"));
    }

    static String getRandomString(Random rnd, int maxlen) {
        int n = rnd.nextInt(maxlen);
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = (char) ('a' + rnd.nextInt(3));
        return new String(s);
    }
}
