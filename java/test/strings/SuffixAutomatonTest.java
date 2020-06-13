package test.strings;

import java.util.*;
import strings.SuffixAutomaton;

public class SuffixAutomatonTest {
    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int len1 = rnd.nextInt(20);
            int len2 = rnd.nextInt(5) + 1;
            String a = getRandomString(len1, rnd);
            String b = getRandomString(len2, rnd);
            int[] occurrences1 = SuffixAutomaton.occurrences(a, b);
            List<Integer> occurrences2 = new ArrayList<>();
            for (int p = a.indexOf(b); p != -1; p = a.indexOf(b, p + 1)) occurrences2.add(p);
            if (!Arrays.equals(occurrences1, occurrences2.stream().mapToInt(Integer::intValue).toArray()))
                throw new RuntimeException();
            String res1 = SuffixAutomaton.lcs(a, b);
            int res2 = slowLcs(a, b);
            if (res1.length() != res2)
                throw new RuntimeException();
        }
    }

    static int slowLcs(String a, String b) {
        int[][] lcs = new int[a.length()][b.length()];
        int res = 0;
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j))
                    lcs[i][j] = 1 + (i > 0 && j > 0 ? lcs[i - 1][j - 1] : 0);
                res = Math.max(res, lcs[i][j]);
            }
        }
        return res;
    }

    static String getRandomString(int n, Random rnd) {
        return rnd.ints(n, 0, 3).mapToObj(i -> String.valueOf((char) (i + 'a'))).reduce("", (a, b) -> a + b);
    }
}
