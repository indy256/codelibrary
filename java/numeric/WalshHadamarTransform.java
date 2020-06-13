package numeric;

import java.util.Arrays;

public class WalshHadamarTransform {
    enum Operation { XOR, OR, AND }

    // calculates c[k] = sum(a[i]*b[j] | i op j == k), where op = XOR | OR | AND
    // complexity: O(n*log(n))
    public static int[] convolution(int[] a, int[] b, Operation op) {
        transform(a, op, false);
        transform(b, op, false);
        for (int i = 0; i < a.length; i++) {
            a[i] *= b[i];
        }
        transform(a, op, true);
        return a;
    }

    static void transform(int[] a, Operation op, boolean inverse) {
        int n = a.length;

        for (int step = 1; step < n; step *= 2) {
            for (int i = 0; i < n; i += 2 * step) {
                for (int j = i; j < i + step; ++j) {
                    int u = a[j];
                    int v = a[j + step];
                    switch (op) {
                        case XOR:
                            a[j] = u + v;
                            a[j + step] = u - v;
                            break;
                        case OR:
                            a[j] = inverse ? v : u + v;
                            a[j + step] = inverse ? u - v : u;
                            break;
                        case AND:
                            a[j] = inverse ? v - u : v;
                            a[j + step] = inverse ? u : v + u;
                            break;
                    }
                }
            }
        }
        if (op == Operation.XOR && inverse) {
            for (int i = 0; i < n; i++) {
                a[i] /= n;
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        int[] a = {3, 2, 1, 5};
        int[] b = {6, 3, 4, 8};
        System.out.println(Arrays.toString(convolution(a, b, Operation.AND)));
    }
}
