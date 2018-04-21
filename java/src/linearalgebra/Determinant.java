package linearalgebra;

import java.util.stream.IntStream;

public class Determinant {

    public static double det(double[][] matrix) {
        final double EPS = 1e-10;
        int n = matrix.length;
        double[][] a = IntStream.range(0, n).mapToObj(i -> matrix[i].clone()).toArray(double[][]::new); // make a copy
        double res = 1;
        for (int i = 0; i < n; i++) {
            int p = i;
            for (int j = i + 1; j < n; j++)
                if (Math.abs(a[p][i]) < Math.abs(a[j][i]))
                    p = j;
            if (Math.abs(a[p][i]) < EPS)
                return 0;
            if (i != p) {
                res = -res;
                double[] t = a[i];
                a[i] = a[p];
                a[p] = t;
            }
            res *= a[i][i];
            for (int j = i + 1; j < n; j++)
                a[i][j] /= a[i][i];
            for (int j = 0; j < n; ++j)
                if (j != i && Math.abs(a[j][i]) > EPS /*optimizes overall complexity to O(n^2) for sparse matrices*/)
                    for (int k = i + 1; k < n; ++k)
                        a[j][k] -= a[i][k] * a[j][i];
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        double d = det(new double[][]{{0, 1}, {-1, 0}});
        System.out.println(Math.abs(d - 1) < 1e-10);
    }
}
