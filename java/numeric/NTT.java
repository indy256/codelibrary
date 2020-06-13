package numeric;

import java.math.BigInteger;
import java.util.Random;

public class NTT {
    static int pow(int x, int n, int mod) {
        int res = 1;
        for (long p = x; n > 0; n >>= 1, p = (p * p) % mod)
            if ((n & 1) != 0)
                res = (int) (res * p % mod);
        return res;
    }

    // a.length == b.length == 2^x
    public static void ntt(int[] a, boolean invert, int mod, int root) {
        int n = a.length;
        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        int root_inv = pow(root, mod - 2, mod);

        for (int len = 1; len < n; len <<= 1) {
            int wlen = pow(invert ? root_inv : root, (mod - 1) / (2 * len), mod);
            for (int i = 0; i < n; i += 2 * len)
                for (int j = 0, w = 1; j < len; ++j) {
                    int u = a[i + j];
                    int v = (int) ((long) a[i + j + len] * w % mod);
                    a[i + j] = (u + v) % mod;
                    a[i + j + len] = (u - v + mod) % mod;
                    w = (int) ((long) w * wlen % mod);
                }
        }
        if (invert) {
            int nrev = pow(n, mod - 2, mod);
            for (int i = 0; i < n; ++i) a[i] = (int) ((long) a[i] * nrev % mod);
        }
    }

    public static int[] multiply(int[] a, int[] b) {
        int need = a.length + b.length;
        int n = Integer.highestOneBit(need - 1) << 1;
        int[] A = new int[n];
        int[] B = new int[n];
        for (int i = 0; i < a.length; i++) A[i] = a[i];
        for (int i = 0; i < b.length; i++) B[i] = b[i];

        int mod = 998244353; // 2^23 * 119 + 1
        int root = 3;

        ntt(A, false, mod, root);
        ntt(B, false, mod, root);
        for (int i = 0; i < n; i++) A[i] = (int) (((long) A[i] * B[i]) % mod);
        ntt(A, true, mod, root);
        int carry = 0;
        for (int i = 0; i < need; i++) {
            A[i] += carry;
            carry = A[i] / 10;
            A[i] %= 10;
        }
        return A;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n1 = rnd.nextInt(50) + 1;
            String s1 = "";
            int[] a = new int[n1];
            for (int i = 0; i < n1; i++) {
                int x = rnd.nextInt(10);
                s1 = x + s1;
                a[i] = x;
            }
            int n2 = rnd.nextInt(50) + 1;
            String s2 = "";
            int[] b = new int[n2];
            for (int i = 0; i < n2; i++) {
                int x = rnd.nextInt(10);
                s2 = x + s2;
                b[i] = x;
            }
            int[] res = multiply(a, b);
            String s = "";
            for (long v : res) {
                s = v + s;
            }
            BigInteger mul = new BigInteger(s1).multiply(new BigInteger(s2));
            if (!mul.equals(new BigInteger(s)))
                throw new RuntimeException();
        }

        //        generatePrimitiveRootsOfUnity(1 << 20);
    }

    static void generatePrimitiveRootsOfUnity(int N) {
        for (int i = 900; i < 1000; i++) {
            int mod = N * i + 1;
            if (!BigInteger.valueOf(mod).isProbablePrime(100))
                continue;
            for (int root = 2; root < 1000; root++) {
                if (pow(root, N, mod) == 1 && pow(root, N / 2, mod) != 1) {
                    System.out.println(i + " " + mod + " " + root);
                    break;
                }
            }
        }
    }
}
