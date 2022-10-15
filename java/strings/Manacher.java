package strings;

import java.util.Arrays;
import java.util.Random;

// Manacher's algorithm to finds all palindromes: https://cp-algorithms.com/string/manacher.html
public class Manacher {
    // d1[i] - how many palindromes of odd length with center at i
    public static int[] oddPalindromes(String s) {
        int n = s.length();
        int[] d1 = new int[n];
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int len = i > r ? 1 : Math.min(d1[l + r - i], r - i + 1);
            while (i - len >= 0 && i + len < n && s.charAt(i - len) == s.charAt(i + len)) ++len;
            d1[i] = len;
            if (r < i + len - 1) {
                r = i + len - 1;
                l = i - (len - 1);
            }
        }
        return d1;
    }

    // d2[i] - how many palindromes of even length with center at i
    public static int[] evenPalindromes(String s) {
        int n = s.length();
        int[] d2 = new int[n];
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int len = i > r ? 0 : Math.min(d2[l + r - i + 1], r - i + 1);
            while (i - len - 1 >= 0 && i + len < n && s.charAt(i - len - 1) == s.charAt(i + len)) ++len;
            d2[i] = len;
            if (r < i + len - 1) {
                r = i + len - 1;
                l = i - len;
            }
        }
        return d2;
    }

    // Usage example
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10_000; step++) {
            int n = rnd.nextInt(10) + 1;
            String s = getRandomString(n, rnd);
            int[] oddPalindromes = oddPalindromes(s);
            int[] evenPalindromes = evenPalindromes(s);
            int[] oddPalindromesSlow = oddPalindromesSlow(s);
            int[] evenPalindromesSlow = evenPalindromesSlow(s);
            if (!Arrays.equals(oddPalindromes, oddPalindromesSlow)
                || !Arrays.equals(evenPalindromes, evenPalindromesSlow)) {
                throw new RuntimeException();
            }
        }
    }

    static int[] oddPalindromesSlow(String s) {
        int n = s.length();
        int[] d1 = new int[n];
        for (int i = 0; i < n; i++) {
            int len = 1;
            while (i - len >= 0 && i + len < n && s.charAt(i - len) == s.charAt(i + len)) ++len;
            d1[i] = len;
        }
        return d1;
    }

    static int[] evenPalindromesSlow(String s) {
        int n = s.length();
        int[] d2 = new int[n];
        for (int i = 0; i < n; i++) {
            int len = 0;
            while (i - len - 1 >= 0 && i + len < n && s.charAt(i - len - 1) == s.charAt(i + len)) ++len;
            d2[i] = len;
        }
        return d2;
    }

    static String getRandomString(int n, Random rnd) {
        return rnd.ints(n, 0, 3).mapToObj(i -> String.valueOf((char) (i + 'a'))).reduce("", (a, b) -> a + b);
    }
}
