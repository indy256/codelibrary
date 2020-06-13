package numeric;

import java.util.Arrays;

public class SubsetConvolution {
    // calculates c[k] = sum(a[i]*b[j] | i|j==k, i&j==0) in O(n * log^2(n))
    public static int[] subsetConvolution(int[] a, int[] b) {
        int n = a.length;
        int logn = Integer.bitCount(n - 1) + 1;
        int[][] ta = new int[logn][n];
        int[][] tb = new int[logn][n];
        for (int i = 0; i < n; ++i) {
            ta[Integer.bitCount(i)][i] = a[i];
            tb[Integer.bitCount(i)][i] = b[i];
        }

        for (int i = 0; i < logn; ++i) {
            for (int j = 0; j < logn; ++j) {
                for (int k = 0; k < n; ++k) {
                    if (((k >> j) & 1) != 0) {
                        ta[i][k] += ta[i][k ^ (1 << j)];
                        tb[i][k] += tb[i][k ^ (1 << j)];
                    }
                }
            }
        }

        int[][] tc = new int[logn][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < logn; ++j) {
                for (int k = 0; k <= j; ++k) {
                    tc[j][i] += ta[k][i] * tb[j - k][i];
                }
            }
        }

        for (int i = 0; i < logn; ++i) {
            for (int j = 0; j < logn; ++j) {
                for (int k = 0; k < n; ++k) {
                    if (((k >> j) & 1) != 0) {
                        tc[i][k] -= tc[i][k ^ (1 << j)];
                    }
                }
            }
        }

        int[] result = new int[n];
        for (int i = 0; i < n; ++i) {
            result[i] = tc[Integer.bitCount(i)][i];
        }
        return result;
    }

    // O(3^log(n))
    public static int[] subsetConvolutionSlow(int[] a, int[] b) {
        int n = a.length;
        int[] result = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i;; j = (j - 1) & i) {
                result[i] += a[j] * b[i ^ j];
                if (j == 0)
                    break;
            }
        }
        return result;
    }

    // usage example
    public static void main(String[] args) {
        int[] a = {3, 2, 1, 5};
        int[] b = {6, 3, 4, 8};
        System.out.println(Arrays.toString(subsetConvolution(a, b)));
        System.out.println(Arrays.toString(subsetConvolutionSlow(a, b)));
    }
}
