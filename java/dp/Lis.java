package dp;

import java.util.Arrays;

// https://en.wikipedia.org/wiki/Longest_increasing_subsequence in O(n*log(n))
public class Lis {
    public static int[] lis(int[] a) {
        int n = a.length;
        int[] tail = new int[n];
        int[] prev = new int[n];

        int len = 0;
        for (int i = 0; i < n; i++) {
            int pos = lower_bound(a, tail, len, a[i]);
            len = Math.max(len, pos + 1);
            prev[i] = pos > 0 ? tail[pos - 1] : -1;
            tail[pos] = i;
        }

        int[] res = new int[len];
        for (int i = tail[len - 1]; i >= 0; i = prev[i]) {
            res[--len] = a[i];
        }
        return res;
    }

    static int lower_bound(int[] a, int[] tail, int len, int key) {
        int lo = -1;
        int hi = len;
        while (hi - lo > 1) {
            int mid = (lo + hi) >>> 1;
            if (a[tail[mid]] < key) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    // Usage example
    public static void main(String[] args) {
        int[] lis = lis(new int[] {1, 10, 2, 11, 3});
        System.out.println(Arrays.toString(lis));
    }
}
