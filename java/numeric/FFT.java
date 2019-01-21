package numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

// https://github.com/indy256/olymp-docs/blob/master/adamant/fft_eng.pdf

public class FFT {

    // precondition: a.length is a power of 2 and a.length == b.length
    public static void fft(double[] a, double[] b, boolean inverse) {
        int n = a.length;
        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                double temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                temp = b[i];
                b[i] = b[j];
                b[j] = temp;
            }
        }
        for (int len = 2; len <= n; len <<= 1) {
            int halfLen = len >> 1;
            double[] cs = new double[halfLen];
            double[] sn = new double[halfLen];
            for (int i = 0; i < halfLen; i++) {
                cs[i] = Math.cos(2 * Math.PI * i / len * (inverse ? -1 : 1));
                sn[i] = Math.sin(2 * Math.PI * i / len * (inverse ? -1 : 1));
            }
            for (int i = 0; i < n; i += len) {
                for (int j = 0; j < halfLen; j++) {
                    double uA = a[i + j];
                    double uB = b[i + j];
                    double vA = a[i + j + halfLen] * cs[j] - b[i + j + halfLen] * sn[j];
                    double vB = a[i + j + halfLen] * sn[j] + b[i + j + halfLen] * cs[j];
                    a[i + j] = uA + vA;
                    b[i + j] = uB + vB;
                    a[i + j + halfLen] = uA - vA;
                    b[i + j + halfLen] = uB - vB;
                }
            }
        }
        if (inverse) {
            for (int i = 0; i < n; i++) {
                a[i] /= n;
                b[i] /= n;
            }
        }
    }

    public static int[] multiply(int[] a, int[] b) {
        int need = a.length + b.length;
        int n = Integer.highestOneBit(need - 1) << 1;
        double[] aReal = new double[n];
        double[] aImaginary = new double[n];
        double[] bReal = new double[n];
        double[] bImaginary = new double[n];
        for (int i = 0; i < a.length; i++)
            aReal[i] = a[i];
        for (int i = 0; i < b.length; i++)
            bReal[i] = b[i];
        fft(aReal, aImaginary, false);
        fft(bReal, bImaginary, false);
        for (int i = 0; i < n; i++) {
            double real = aReal[i] * bReal[i] - aImaginary[i] * bImaginary[i];
            aImaginary[i] = aImaginary[i] * bReal[i] + bImaginary[i] * aReal[i];
            aReal[i] = real;
        }
        fft(aReal, aImaginary, true);
        int[] result = new int[need];
        for (int i = 0, carry = 0; i < need; i++) {
            result[i] = (int) (aReal[i] + 0.5) + carry;
            carry = result[i] / 10;
            result[i] %= 10;
        }
        return result;
    }

    public static int[] multiplyFast(int[] a, int[] b) {
        int need = a.length + b.length;
        int n = Integer.highestOneBit(need - 1) << 1;
        double[] pReal = new double[n];
        double[] pImaginary = new double[n];
        // p(x) = a(x) + i*b(x)
        for (int i = 0; i < a.length; i++)
            pReal[i] = a[i];
        for (int i = 0; i < b.length; i++)
            pImaginary[i] = b[i];
        fft(pReal, pImaginary, false);
        double[] abReal = new double[n];
        double[] abImaginary = new double[n];
        // a[w[k]] = (p[w[k]] + conj(p[w[n-k]])) / 2
        // b[w[k]] = (p[w[k]] - conj(p[w[n-k]])) / (2*i)
        // ab[w[k]] = p[w[k]]*p[w[k]] - conj(p[w[n-k]]*p[w[n-k]])
        for (int i = 0; i < n; i++) {
            int j = (n - i) & (n - 1);
            abReal[i] = (pReal[i] * pImaginary[i] + pReal[j] * pImaginary[j]) / 2;
            abImaginary[i] = ((pReal[j] * pReal[j] - pImaginary[j] * pImaginary[j]) -
                    (pReal[i] * pReal[i] - pImaginary[i] * pImaginary[i])) / 4;
        }
        fft(abReal, abImaginary, true);
        int[] result = new int[need];
        for (int i = 0, carry = 0; i < need; i++) {
            result[i] = (int) (abReal[i] + 0.5) + carry;
            carry = result[i] / 10;
            result[i] %= 10;
        }
        return result;
    }

    static int[] multiplyMod(int[] a, int[] b, int mod) {
        int need = a.length + b.length - 1;
        int n = Math.max(2, Integer.highestOneBit(need - 1) << 1);

        double[] aReal = new double[n];
        double[] aImaginary = new double[n];
        for (int i = 0; i < a.length; i++) {
            int x = (a[i] % mod + mod) % mod;
            aReal[i] = x & ((1 << 15) - 1);
            aImaginary[i] = x >> 15;
        }
        fft(aReal, aImaginary, false);

        double[] bReal = new double[n];
        double[] bImaginary = new double[n];
        for (int i = 0; i < b.length; i++) {
            int x = (b[i] % mod + mod) % mod;
            bReal[i] = x & ((1 << 15) - 1);
            bImaginary[i] = x >> 15;
        }
        fft(bReal, bImaginary, false);

        double[] faReal = new double[n];
        double[] faImaginary = new double[n];
        double[] fbReal = new double[n];
        double[] fbImaginary = new double[n];

        for (int i = 0; i < n; i++) {
            int j = (n - i) & (n - 1);

            double a1r = (aReal[i] + aReal[j]) / 2;
            double a1i = (aImaginary[i] - aImaginary[j]) / 2;
            double a2r = (aImaginary[i] + aImaginary[j]) / 2;
            double a2i = (aReal[j] - aReal[i]) / 2;

            double b1r = (bReal[i] + bReal[j]) / 2;
            double b1i = (bImaginary[i] - bImaginary[j]) / 2;
            double b2r = (bImaginary[i] + bImaginary[j]) / 2;
            double b2i = (bReal[j] - bReal[i]) / 2;

            faReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
            faImaginary[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;

            fbReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
            fbImaginary[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
        }

        fft(faReal, faImaginary, true);
        fft(fbReal, fbImaginary, true);
        int[] res = new int[need];
        for (int i = 0; i < need; i++) {
            long aa = (long) (faReal[i] + 0.5);
            long bb = (long) (fbReal[i] + 0.5);
            long cc = (long) (faImaginary[i] + 0.5);
            res[i] = (int) ((aa + ((bb % mod) << 15) + ((cc % mod) << 30)) % mod);
        }
        return res;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10000; step++) {
            int n1 = rnd.nextInt(10) + 1;
            String s1 = "";
            int[] a = new int[n1];
            for (int i = 0; i < n1; i++) {
                int x = rnd.nextInt(10);
                s1 = x + s1;
                a[i] = x;
            }
            int n2 = rnd.nextInt(10) + 1;
            String s2 = "";
            int[] b = new int[n2];
            for (int i = 0; i < n2; i++) {
                int x = rnd.nextInt(10);
                s2 = x + s2;
                b[i] = x;
            }

            int[] res1 = multiplyFast(a, b);
            int[] res2 = multiply(a, b);
            String s = "";
            for (int v : res1) {
                s = v + s;
            }
            BigInteger mul = new BigInteger(s1).multiply(new BigInteger(s2));
            if (!Arrays.equals(res1, res2) || !mul.equals(new BigInteger(s)))
                throw new RuntimeException();
        }

        System.out.println(Arrays.toString(multiplyMod(new int[]{1, 2}, new int[]{2, 15}, 991992993)));
    }
}
