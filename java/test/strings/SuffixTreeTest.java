package test.strings;

import java.util.Random;
import strings.SuffixTree;

public class SuffixTreeTest {
    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int n1 = rnd.nextInt(10);
            int n2 = rnd.nextInt(10);
            String s1 = getRandomString(n1, rnd);
            String s2 = getRandomString(n2, rnd);
            // build generalized suffix tree
            String s = s1 + '\1' + s2 + '\2';
            SuffixTree.Node tree = SuffixTree.buildSuffixTree(s);
            SuffixTree.lcsLength = 0;
            SuffixTree.lcsBeginIndex = 0;
            // find longest common substring
            SuffixTree.lcs(tree, s1.length(), s1.length() + s2.length() + 1);
            int res2 = slowLcs(s1, s2);
            if (SuffixTree.lcsLength != res2) {
                System.err.println(
                    s.substring(SuffixTree.lcsBeginIndex - 1, SuffixTree.lcsBeginIndex + SuffixTree.lcsLength - 1));
                System.err.println(s1);
                System.err.println(s2);
                System.err.println(SuffixTree.lcsLength + " " + res2);
                throw new RuntimeException();
            }
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
