package optimization;

import numbertheory.Rational;

import java.util.Arrays;

public class Simplex {

    // returns max c*x such that A*x <= b, x >= 0
    public static Rational simplex(Rational[][] A, Rational[] b, Rational[] c, Rational[] x) {
        int m = A.length;
        int n = A[0].length + 1;
        int[] index = new int[n + m];
        for (int i = 0; i < n + m; i++) {
            index[i] = i;
        }
        Rational[][] a = new Rational[m + 2][n + 1];
        for (Rational[] a1 : a) {
            Arrays.fill(a1, Rational.ZERO);
        }
        int L = m;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                a[i][j] = A[i][j].negate();
            }
            a[i][n - 1] = Rational.ONE;
            a[i][n] = b[i];
            if (a[L][n].compareTo(a[i][n]) > 0) {
                L = i;
            }
        }
        for (int j = 0; j < n - 1; j++) {
            a[m][j] = c[j];
        }
        a[m + 1][n - 1] = Rational.ONE.negate();
        for (int E = n - 1; ; ) {
            if (L < m) {
                int t = index[E];
                index[E] = index[L + n];
                index[L + n] = t;
                a[L][E] = a[L][E].inverse();
                for (int j = 0; j <= n; j++) {
                    if (j != E) {
                        a[L][j] = a[L][j].mul(a[L][E].negate());
                    }
                }
                for (int i = 0; i <= m + 1; i++) {
                    if (i != L) {
                        for (int j = 0; j <= n; j++) {
                            if (j != E) {
                                a[i][j] = a[i][j].add(a[L][j].mul(a[i][E]));
                            }
                        }
                        a[i][E] = a[i][E].mul(a[L][E]);
                    }
                }
            }
            E = -1;
            for (int j = 0; j < n; j++) {
                if (E < 0 || index[E] > index[j]) {
                    if (a[m + 1][j].signum() > 0 || a[m + 1][j].signum() == 0 && a[m][j].signum() > 0) {
                        E = j;
                    }
                }
            }
            if (E < 0) {
                break;
            }
            L = -1;
            for (int i = 0; i < m; i++) {
                if (a[i][E].signum() < 0) {
                    Rational d;
                    if (L < 0 || (d = a[L][n].div(a[L][E]).sub(a[i][n].div(a[i][E]))).signum() < 0 || d.signum() == 0
                            && index[L + n] > index[i + n]) {
                        L = i;
                    }
                }
            }
            if (L < 0) {
                return Rational.POSITIVE_INFINITY;
            }
        }
        if (a[m + 1][n].signum() < 0) {
            return null;
        }
        if (x != null) {
            Arrays.fill(x, Rational.ZERO);
            for (int i = 0; i < m; i++)
                if (index[n + i] < n - 1)
                    x[index[n + i]] = a[i][n];
        }
        return a[m][n];
    }

    // Usage example
    public static void main(String[] args) {
        long[][] a = {{4, -1}, {2, 1}, {-5, 2}};
        long[] b = {8, 10, 2};
        long[] c = {1, 1};
        Rational[] x = new Rational[c.length];
        Rational res = simplex(cnv(a), cnv(b), cnv(c), x);
        System.out.println(new Rational(8).equals(res));
        System.out.println(Arrays.toString(x));

        a = new long[][]{{3, 4, -3}, {5, -4, -3}, {7, 4, 11}};
        b = new long[]{23, 10, 30};
        c = new long[]{-1, 1, 2};
        x = new Rational[c.length];
        res = simplex(cnv(a), cnv(b), cnv(c), x);
        System.out.println(new Rational(57, 8).equals(res));
        System.out.println(Arrays.toString(x));

        // no feasible non-negative solutions
        a = new long[][]{{4, -1}, {2, 1}, {-5, 2}};
        b = new long[]{8, -10, 2};
        c = new long[]{1, 1};
        res = simplex(cnv(a), cnv(b), cnv(c), null);
        System.out.println(null == res);

        // unbounded problem
        a = new long[][]{{-4, 1}, {-2, -1}, {-5, 2}};
        b = new long[]{-8, -10, 2};
        c = new long[]{1, 1};
        res = simplex(cnv(a), cnv(b), cnv(c), null);
        System.out.println(Rational.POSITIVE_INFINITY == res);

        // no feasible solutions
        a = new long[][]{{1}, {-1}};
        b = new long[]{1, -2};
        c = new long[]{0};
        res = simplex(cnv(a), cnv(b), cnv(c), null);
        System.out.println(null == res);

        // infinite number of solutions, but only one is returned
        a = new long[][]{{1, 1}};
        b = new long[]{0};
        c = new long[]{1, 1};
        x = new Rational[c.length];
        res = simplex(cnv(a), cnv(b), cnv(c), x);
        System.out.println(Arrays.toString(x));
    }

    static Rational[] cnv(long[] a) {
        Rational[] res = new Rational[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = new Rational(a[i]);
        }
        return res;
    }

    static Rational[][] cnv(long[][] a) {
        Rational[][] res = new Rational[a.length][];
        for (int i = 0; i < a.length; i++) {
            res[i] = cnv(a[i]);
        }
        return res;
    }
}
