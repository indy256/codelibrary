package test.strings;

import java.util.Random;
import strings.ZFunction;

public class ZFunctionTest {
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
        int[] z = ZFunction.zFunction(pattern + "\0" + s);
        for (int i = pattern.length() + 1; i < z.length; i++)
            if (z[i] == pattern.length())
                return i - pattern.length() - 1;
        return -1;
    }

    static String getRandomString(Random rnd, int maxlen) {
        int n = rnd.nextInt(maxlen) + 1;
        char[] s = new char[n];
        for (int i = 0; i < n; i++) s[i] = (char) ('a' + rnd.nextInt(3));
        return new String(s);
    }
}
