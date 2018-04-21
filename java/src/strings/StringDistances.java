package strings;

import java.util.Arrays;

public class StringDistances {

    // https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
    public static int[] getLCS(int[] x, int[] y) {
        int m = x.length;
        int n = y.length;
        int[][] lcs = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i] == y[j]) {
                    lcs[i + 1][j + 1] = lcs[i][j] + 1;
                } else {
                    lcs[i + 1][j + 1] = Math.max(lcs[i + 1][j], lcs[i][j + 1]);
                }
            }
        }
        int cnt = lcs[m][n];
        int[] res = new int[cnt];
        for (int i = m - 1, j = n - 1; i >= 0 && j >= 0; ) {
            if (x[i] == y[j]) {
                res[--cnt] = x[i];
                --i;
                --j;
            } else if (lcs[i + 1][j] > lcs[i][j + 1]) {
                --j;
            } else {
                --i;
            }
        }
        return res;
    }

    // https://en.wikipedia.org/wiki/Levenshtein_distance
    public static int getLevensteinDistance(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] len = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            len[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            len[0][j] = j;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    len[i + 1][j + 1] = len[i][j];
                } else {
                    len[i + 1][j + 1] = 1 + Math.min(len[i][j], Math.min(len[i + 1][j], len[i][j + 1]));
                }
            }
        }
        return len[m][n];
    }

    // Usage example
    public static void main(String[] args) {
        int[] x = {1, 5, 4, 2, 3, 7, 6};
        int[] y = {2, 7, 1, 3, 5, 4, 6};
        int[] lcs = getLCS(x, y);
        System.out.println(Arrays.toString(lcs));

        String a = "abc";
        String b = "ac";
        System.out.println(getLevensteinDistance(a, b));
    }
}
