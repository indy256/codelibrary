package strings;

import java.util.Random;

public class ZFunction {

    // z[i] = lcp(s[0..], s[i..])
    public static int[] zFunction(String s) {
        int[] z = new int[s.length()];
        for (int i = 1, l = 0, r = 0; i < z.length; ++i) {
            if (i <= r)
                z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < z.length && s.charAt(z[i]) == s.charAt(i + z[i]))
                ++z[i];
            if (r < i + z[i] - 1) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        return z;
    }

    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10_000; step++) {
            String s = getRandomString(rnd, 100);
            String pattern = getRandomString(rnd, 5);
            int pos1 = find(s, pattern);
            int pos2 = s.indexOf(pattern);
            if (pos1 != pos2)
                throw new RuntimeException();
        }
    }

    static int find(String s, String pattern) {
        int[] z = zFunction(pattern + "\0" + s);
        for (int i = pattern.length() + 1; i < z.length; i++)
            if (z[i] == pattern.length())
                return i - pattern.length() - 1;
        return -1;
    }

    static String getRandomString(Random rnd, int maxlen) {
        int n = rnd.nextInt(maxlen) + 1;
        char[] s = new char[n];
        for (int i = 0; i < n; i++)
            s[i] = (char) ('a' + rnd.nextInt(3));
        return new String(s);
    }
}
