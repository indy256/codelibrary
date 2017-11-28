import java.util.Arrays;

import mooc.EdxIO;

public class ScarecrowSort {
    public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
            int n = io.nextInt();
            int k = io.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; ++i) {
                a[i] = io.nextInt();
            }
            int[] b = new int[(n + k - 1) / k];
            for (int i = 0; i < k; ++i)  {
                int last = 0;
                for (int j = i; j < n; j += k) {
                    b[last++] = a[j];
                }
                Arrays.sort(b, 0, last);
                for (int j = i, t = 0; j < n; j += k, ++t) {
                    a[j] = b[t];
                }
            }
            boolean ok = true;
            for (int i = 1; i < n; ++i) {
                if (a[i - 1] > a[i]) {
                    ok = false;
                    break;
                }
            }
            io.println(ok ? "YES" : "NO");
        }
    }
}