package strings;

import java.util.*;
import java.util.stream.IntStream;

// https://en.wikipedia.org/wiki/Suffix_array
public class SuffixArray {

    // sort suffixes of S in O(n*log(n))
    public static int[] suffixArray(CharSequence S) {
        int n = S.length();

        // stable sort of characters
        int[] sa = IntStream.range(0, n).mapToObj(i -> n - 1 - i).
                sorted(Comparator.comparingInt(S::charAt)).mapToInt(Integer::intValue).toArray();

        int[] classes = S.chars().toArray();
        // sa[i] - suffix on i'th position after sorting by first len characters
        // classes[i] - equivalence class of the i'th suffix after sorting by first len characters

        for (int len = 1; len < n; len *= 2) {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++) {
                // condition sa[i - 1] + len < n emulates 0-symbol at the end of the string
                // a separate class is created for each suffix followed by emulates 0-symbol
                classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n
                        && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2] ? classes[sa[i - 1]] : i;
            }
            // Suffixes are already sorted by first len characters
            // Now sort suffixes by first len * 2 characters
            int[] cnt = IntStream.range(0, n).toArray();
            int[] s = sa.clone();
            for (int i = 0; i < n; i++) {
                // s[i] - order of suffixes sorted by first len characters
                // (s[i] - len) - order of suffixes sorted only by second len characters
                int s1 = s[i] - len;
                // sort only suffixes of length > len, others are already sorted
                if (s1 >= 0)
                    sa[cnt[classes[s1]]++] = s1;
            }
        }
        return sa;
    }

    // sort rotations of S in O(n*log(n))
    public static int[] rotationArray(CharSequence S) {
        int n = S.length();
        int[] sa = IntStream.range(0, n).boxed().sorted(Comparator.comparingInt(S::charAt)).mapToInt(Integer::intValue).toArray();
        int[] classes = S.chars().toArray();
        for (int len = 1; len < n; len *= 2) {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++)
                classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]]
                        && c[(sa[i - 1] + len / 2) % n] == c[(sa[i] + len / 2) % n] ? classes[sa[i - 1]] : i;
            int[] cnt = IntStream.range(0, n).toArray();
            int[] s = sa.clone();
            for (int i = 0; i < n; i++) {
                int s1 = (s[i] - len + n) % n;
                sa[cnt[classes[s1]]++] = s1;
            }
        }
        return sa;
    }

    // longest common prefixes array in O(n)
    public static int[] lcp(int[] sa, CharSequence s) {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h); ++h)
                    ;
                lcp[rank[i]] = h;
                if (h > 0)
                    --h;
            }
        }
        return lcp;
    }

    // Usage example
    public static void main(String[] args) {
        String s1 = "abcab";
        int[] sa1 = suffixArray(s1);

        // print suffixes in lexicographic order
        for (int p : sa1)
            System.out.println(s1.substring(p));

        System.out.println("lcp = " + Arrays.toString(lcp(sa1, s1)));

        // random test
        Random rnd = new Random(1);
        for (int step = 0; step < 100000; step++) {
            int n = rnd.nextInt(100) + 1;
            StringBuilder s = rnd.ints(n, 0, 10).collect(StringBuilder::new, (sb, i) -> sb.append((char) ('a' + i)), StringBuilder::append);
            int[] sa = suffixArray(s);
            int[] ra = rotationArray(s.toString() + '\0');
            int[] lcp = lcp(sa, s);
            for (int i = 0; i + 1 < n; i++) {
                String a = s.substring(sa[i]);
                String b = s.substring(sa[i + 1]);
                if (a.compareTo(b) >= 0
                        || !a.substring(0, lcp[i]).equals(b.substring(0, lcp[i]))
                        || (a + " ").charAt(lcp[i]) == (b + " ").charAt(lcp[i])
                        || sa[i] != ra[i + 1])
                    throw new RuntimeException();
            }
        }
        System.out.println("Test passed");
    }
}
