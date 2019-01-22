package strings;

import java.util.Arrays;

// Manacher's algorithm to finds all palindromes: https://cp-algorithms.com/string/manacher.html
public class Manacher {
    // d1[i] - how many palindromes of odd length with center at i
    public static int[] oddPalindromes(String s) {
        int n = s.length();
        int[] d1 = new int[n];
        int l = 0, r = -1;
        for (int i = 0; i < n; ++i) {
            int k = (i > r ? 0 : Math.min(d1[l + r - i], r - i)) + 1;
            while (i + k < n && i - k >= 0 && s.charAt(i + k) == s.charAt(i - k)) ++k;
            d1[i] = k--;
            if (i + k > r) {
                l = i - k;
                r = i + k;
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
            int k = (i > r ? 0 : Math.min(d2[l + r - i + 1], r - i + 1)) + 1;
            while (i + k - 1 < n && i - k >= 0 && s.charAt(i + k - 1) == s.charAt(i - k)) ++k;
            d2[i] = --k;
            if (i + k - 1 > r) {
                l = i - k;
                r = i + k - 1;
            }
        }
        return d2;
    }

    // Usage example
    public static void main(String[] args) {
        String text = "aaaba";

        System.out.println(Arrays.toString(oddPalindromes(text)));
        System.out.println(Arrays.toString(evenPalindromes(text)));
    }
}
