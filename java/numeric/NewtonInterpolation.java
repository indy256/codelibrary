package numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class NewtonInterpolation {
    // https://en.wikipedia.org/wiki/Divided_differences#Example
    public static int[] getDividedDifferences(int[] x, int[] y, int mod) {
        BigInteger MOD = BigInteger.valueOf(mod);
        int n = x.length;

        int[] z = y.clone();
        int[] res = new int[n];
        res[0] = z[0];

        for (int i = 0; i < n - 1; i++) {
            int[] nz = new int[n - 1 - i];
            for (int j = 0; j < n - 1 - i; j++) {
                int div = BigInteger.valueOf(x[j + i + 1] - x[j]).modInverse(MOD).intValue();
                nz[j] = (int) (((long) z[j + 1] - z[j]) * div % mod + mod) % mod;
            }
            z = nz;
            res[i + 1] = z[0];
        }

        return res;
    }

    // https://en.wikipedia.org/wiki/Newton_polynomial#Definition
    public static int interpolate(int[] X, int[] dd, int mod, int x) {
        int res = 0;
        int m = 1;
        for (int i = 0; i < X.length; i++) {
            res = (int) ((res + (long) dd[i] * m) % mod);
            m = (int) ((m * ((long) x - X[i]) % mod + mod) % mod);
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int[] x = {7, 2, 1, 3, 5, 6};
        int n = x.length;
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            y[i] = f(x[i]);
        }

        int mod = 1000_000_007;
        int[] dd = getDividedDifferences(x, y, mod);
        System.out.println(Arrays.toString(dd));

        for (int i = 0; i < n; i++) {
            int v = interpolate(x, dd, mod, x[i]);
            System.out.println(v == y[i]);
        }
    }

    static int f(int x) {
        return x * x * x + 5 * x * x + x + 3;
    }
}
