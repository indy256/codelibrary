package test.strings;

import strings.SuffixAutomaton;

import java.util.*;

public class SuffixAutomatonTest {

    // random tests
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int len1 = rnd.nextInt(20);
            int len2 = rnd.nextInt(5) + 1;
            String a = getRandomString(len1, rnd);
            String b = getRandomString(len2, rnd);
            int[] occurrences1 = occurrences(a, b);
            List<Integer> occurrences2 = new ArrayList<>();
            for (int p = a.indexOf(b); p != -1; p = a.indexOf(b, p + 1))
                occurrences2.add(p);
            if (!Arrays.equals(occurrences1, occurrences2.stream().mapToInt(Integer::intValue).toArray()))
                throw new RuntimeException();
            String res1 = lcs(a, b);
            int res2 = slowLcs(a, b);
            if (res1.length() != res2)
                throw new RuntimeException();
        }
    }

    public static int[] occurrences(String haystack, String needle) {
        SuffixAutomaton.State[] automaton = SuffixAutomaton.buildSuffixAutomaton(haystack);
        int node = 0;
        for (char c : needle.toCharArray()) {
            int next = automaton[node].next[c];
            if (next == -1) {
                return new int[0];
            }
            node = next;
        }
        List<Integer> occurrences = new ArrayList<>();
        Queue<Integer> q = new ArrayDeque<>();
        q.add(node);
        while (!q.isEmpty()) {
            int curNode = q.remove();
            if (automaton[curNode].firstPos != -1) {
                occurrences.add(automaton[curNode].firstPos - needle.length() + 1);
            }
            q.addAll(automaton[curNode].invSuffLinks);
        }
        return occurrences.stream().mapToInt(Integer::intValue).sorted().toArray();
    }

    public static String lcs(String a, String b) {
        SuffixAutomaton.State[] st = SuffixAutomaton.buildSuffixAutomaton(a);
        int len = 0;
        int bestLen = 0;
        int bestPos = -1;
        for (int i = 0, cur = 0; i < b.length(); ++i) {
            char c = b.charAt(i);
            if (st[cur].next[c] == -1) {
                for (; cur != -1 && st[cur].next[c] == -1; cur = st[cur].suffLink) {
                }
                if (cur == -1) {
                    cur = 0;
                    len = 0;
                    continue;
                }
                len = st[cur].length;
            }
            ++len;
            cur = st[cur].next[c];
            if (bestLen < len) {
                bestLen = len;
                bestPos = i;
            }
        }
        return b.substring(bestPos - bestLen + 1, bestPos + 1);
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
