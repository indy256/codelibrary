package strings;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

// DC3 linear time suffix array construction algorithm ("Linear Work Suffix Array Construction")
public class SuffixArrayDC3 {
    static boolean leq(int a1, int a2, int b1, int b2) {
        return a1 < b1 || a1 == b1 && a2 <= b2;
    }

    static boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
        return a1 < b1 || a1 == b1 && leq(a2, a3, b2, b3);
    }

    // stably sort a[0..n-1] to b[0..n-1] with keys in 0..K from r
    static void radixPass(int[] a, int[] b, int[] r, int offset, int n, int K) {
        int[] cnt = new int[K + 1];
        for (int i = 0; i < n; i++) ++cnt[r[a[i] + offset]];
        for (int i = 1; i < cnt.length; i++) cnt[i] += cnt[i - 1];
        for (int i = n - 1; i >= 0; i--) b[--cnt[r[a[i] + offset]]] = a[i];
    }

    // find the suffix array SA of T[0..n-1] in {1..K}^n
    // require T[n]=T[n+1]=T[n+2]=0, n>=2
    private static void suffixArray(int[] T, int[] SA, int n, int K) {
        int n0 = (n + 2) / 3;
        int n1 = (n + 1) / 3;
        int n2 = n / 3;
        int n02 = n0 + n2;

        //******* Step 0: Construct sample ********
        // generate positions of mod 1 and mod 2 suffixes
        // the "+(n0-n1)" adds a dummy mod 1 suffix if n%3 == 1
        int[] R = new int[n02 + 3];
        for (int i = 0, j = 0; i < n + (n0 - n1); i++)
            if (i % 3 != 0)
                R[j++] = i;

        //******* Step 1: Sort sample suffixes ********
        // lsb radix sort the mod 1 and mod 2 triples
        int[] SA12 = new int[n02 + 3];
        radixPass(R, SA12, T, 2, n02, K);
        radixPass(SA12, R, T, 1, n02, K);
        radixPass(R, SA12, T, 0, n02, K);

        // find lexicographic names of triples and
        // write them to correct places in R
        int name = 0;
        for (int i = 0; i < n02; i++) {
            if (i == 0 || T[SA12[i]] != T[SA12[i - 1]] || T[SA12[i] + 1] != T[SA12[i - 1] + 1]
                || T[SA12[i] + 2] != T[SA12[i - 1] + 2]) {
                ++name;
            }
            R[SA12[i] / 3 + (SA12[i] % 3 == 1 ? 0 : n0)] = name;
        }

        if (name < n02) {
            // recurse if names are not yet unique
            suffixArray(R, SA12, n02, name);
            // store unique names in R using the suffix array
            for (int i = 0; i < n02; i++) R[SA12[i]] = i + 1;
        } else {
            // generate the suffix array of R directly
            for (int i = 0; i < n02; i++) SA12[R[i] - 1] = i;
        }

        //******* Step 2: Sort nonsample suffixes ********
        // stably sort the mod 0 suffixes from SA12 by their first character
        int[] R0 = new int[n0];
        for (int i = 0, j = 0; i < n02; i++)
            if (SA12[i] < n0)
                R0[j++] = 3 * SA12[i];
        int[] SA0 = new int[n0];
        radixPass(R0, SA0, T, 0, n0, K);

        //******* Step 3: Merge ********
        // merge sorted SA0 suffixes and sorted SA12 suffixes
        for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
            int i = SA12[t] < n0 ? SA12[t] * 3 + 1 : (SA12[t] - n0) * 3 + 2; // pos of current offset 12 suffix
            int j = SA0[p]; // pos of current offset 0 suffix
            if (SA12[t] < n0 ? // different compares for mod 1 and mod 2 suffixes
                    leq(T[i], R[SA12[t] + n0], T[j], R[j / 3])
                             : leq(T[i], T[i + 1], R[SA12[t] - n0 + 1], T[j], T[j + 1],
                                 R[j / 3 + n0])) { // suffix from SA12 is smaller
                SA[k] = i;
                if (++t == n02) // done --- only SA0 suffixes left
                    for (k++; p < n0; p++, k++) SA[k] = SA0[p];
            } else { // suffix from SA0 is smaller
                SA[k] = j;
                if (++p == n0) // done --- only SA12 suffixes left
                    for (k++; t < n02; t++, k++) SA[k] = SA12[t] < n0 ? SA12[t] * 3 + 1 : (SA12[t] - n0) * 3 + 2;
            }
        }
    }

    public static int[] suffixArray(CharSequence s) {
        int n = s.length();
        if (n <= 1)
            return new int[n];
        int[] S = IntStream.range(0, n + 3).map(i -> i < n ? s.charAt(i) : 0).toArray();
        int[] sa = new int[n];
        suffixArray(S, sa, n, 255);
        return sa;
    }

    // longest common prefixes array in O(n)
    public static int[] lcp(int[] sa, CharSequence s) {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h);
                     ++h)
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
        Random rnd1 = new Random(1);
        int n2 = 5_000_000;
        StringBuilder ss = rnd1.ints(n2, 0, 26).collect(
            StringBuilder::new, (sb, i) -> sb.append((char) ('a' + i)), StringBuilder::append);
        long time = System.currentTimeMillis();
        int[] sa2 = suffixArray(ss);
        System.out.println(System.currentTimeMillis() - time);

        String s1 = "abcab";
        int[] sa1 = suffixArray(s1);

        // print suffixes in lexicographic order
        for (int p : sa1) System.out.println(s1.substring(p));

        System.out.println("lcp = " + Arrays.toString(lcp(sa1, s1)));

        // random test
        Random rnd = new Random(1);
        for (int step = 0; step < 100000; step++) {
            int n = rnd.nextInt(100) + 1;
            StringBuilder s = rnd.ints(n, 0, 10).collect(
                StringBuilder::new, (sb, i) -> sb.append((char) ('a' + i)), StringBuilder::append);
            int[] sa = suffixArray(s);
            int[] lcp = lcp(sa, s);
            for (int i = 0; i + 1 < n; i++) {
                String a = s.substring(sa[i]);
                String b = s.substring(sa[i + 1]);
                if (a.compareTo(b) >= 0 || !a.substring(0, lcp[i]).equals(b.substring(0, lcp[i]))
                    || (a + " ").charAt(lcp[i]) == (b + " ").charAt(lcp[i]))
                    throw new RuntimeException();
            }
        }
        System.out.println("Test passed");
    }
}
