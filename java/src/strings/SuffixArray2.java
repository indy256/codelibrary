package strings;

import java.util.*;
import java.util.stream.IntStream;

// https://en.wikipedia.org/wiki/Suffix_array
public class SuffixArray2 {

    // suffix array in O(n*log^2(n))
    public static int[] suffixArray(CharSequence s) {
        int n = s.length();
        Integer[] sa = IntStream.range(0, n).boxed().toArray(Integer[]::new);
        int[] rank = s.chars().toArray();

        for (int len = 1; len < n; len *= 2) {
            long[] rank2 = new long[n];
            for (int i = 0; i < n; i++)
                rank2[i] = ((long) rank[i] << 32) + (i + len < n ? rank[i + len] + 1 : 0);

            Arrays.sort(sa, Comparator.comparingLong(a -> rank2[a]));

            for (int i = 0; i < n; i++)
                rank[sa[i]] = i > 0 && rank2[sa[i - 1]] == rank2[sa[i]] ? rank[sa[i - 1]] : i;
        }
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray();
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100000; step++) {
            int n = rnd.nextInt(100);
            StringBuilder s = rnd.ints(n, 0, 10).collect(StringBuilder::new, (sb, i) -> sb.append((char) ('a' + i)), StringBuilder::append);
            int[] sa = suffixArray(s);
            for (int i = 0; i + 1 < n; i++)
                if (s.substring(sa[i]).compareTo(s.substring(sa[i + 1])) >= 0)
                    throw new RuntimeException();
        }
        System.out.println("Test passed");
    }
}
