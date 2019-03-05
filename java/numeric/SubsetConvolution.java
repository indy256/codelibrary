package numeric;

import java.util.Arrays;

public class SubsetConvolution {

    // calculates res[i] = sum(a[j]*b[i^j] | j&i==j) in O(2^log(n) * log^2(n))
    public static int[] subsetConvolution(int[] a, int[] b) {
        int n = a.length;
        int logn = Integer.bitCount(n - 1) + 1;
        int[][] moba = new int[logn][n];
        int[][] mobb = new int[logn][n];
        for (int i = 0; i < n; ++i) {
            moba[Integer.bitCount(i)][i] = a[i];
            mobb[Integer.bitCount(i)][i] = b[i];
        }

        for (int i = 0; i < logn; ++i) {
            for (int j = 0; j < logn; ++j) {
                for (int k = 0; k < n; ++k) {
                    if (((k >> j) & 1) != 0) {
                        moba[i][k] += moba[i][k ^ (1 << j)];
                        mobb[i][k] += mobb[i][k ^ (1 << j)];
                    }
                }
            }
        }

        int[][] mobc = new int[logn][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < logn; ++j) {
                int res = 0;
                for (int k = 0; k <= j; ++k) {
                    res += moba[k][i] * mobb[j - k][i];
                }
                mobc[j][i] = res;
            }
        }

        for (int i = 0; i < logn; ++i) {
            for (int j = 0; j < logn; ++j) {
                for (int k = 0; k < n; ++k) {
                    if (((k >> j) & 1) != 0) {
                        mobc[i][k] -= mobc[i][k ^ (1 << j)];
                    }
                }
            }
        }

        int[] result = new int[n];
        for (int i = 0; i < n; ++i) {
            result[i] = mobc[Integer.bitCount(i)][i];
        }
        return result;
    }

    // O(3^log(n))
    public static int[] subsetConvolutionSlow(int[] a, int[] b) {
        int n = a.length;
        int[] result = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; ; j = (j - 1) & i) {
                result[i] += a[j] * b[i ^ j];
                if (j == 0) break;
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
