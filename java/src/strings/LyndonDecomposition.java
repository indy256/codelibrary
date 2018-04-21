package strings;

import java.util.*;

public class LyndonDecomposition {

    public static String minCyclicShift(String a) {
        char[] s = (a + a).toCharArray();
        int n = s.length;
        int res = 0;
        for (int i = 0; i < n / 2; ) {
            res = i;
            int j = i + 1, k = i;
            while (j < n && s[k] <= s[j]) {
                if (s[k] < s[j])
                    k = i;
                else
                    ++k;
                ++j;
            }
            while (i <= k) i += j - k;
        }
        return new String(s, res, n / 2);
    }

    public static String[] decompose(String s) {
        List<String> res = new ArrayList<>();
        char[] a = s.toCharArray();
        int n = a.length;
        int i = 0;
        while (i < n) {
            int j = i + 1, k = i;
            while (j < n && a[k] <= a[j]) {
                if (a[k] < a[j])
                    k = i;
                else
                    ++k;
                ++j;
            }
            while (i <= k) {
                res.add(s.substring(i, i + j - k));
                i += j - k;
            }
        }
        return res.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String s = "bara";
        String[] decompose = decompose(s);
        System.out.println(Arrays.toString(decompose));
        System.out.println(minCyclicShift(s));
    }
}
