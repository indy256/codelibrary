package optimization;

import java.util.Arrays;

public class MeetInTheMiddle {

    public static long sumsLowerBound(long[] a, long b) {
        int n = a.length;
        int sizeL = 1 << (n / 2);
        int sizeR = 1 << (n - n / 2);
        long[] sumsL = new long[sizeL];
        long[] sumsR = new long[sizeR];
        for (int i = 0; i < sizeL; ++i)
            for (int j = 0; j < n / 2; ++j)
                if ((i & (1 << j)) > 0)
                    sumsL[i] += a[j];
        for (int i = 0; i < sizeR; ++i)
            for (int j = 0; j < n - n / 2; ++j)
                if ((i & (1 << j)) > 0)
                    sumsR[i] += a[j + n / 2];
        Arrays.sort(sumsL);
        Arrays.sort(sumsR);
        int left = 0;
        int right = sizeR - 1;
        long cur = Long.MIN_VALUE;
        while (left < sizeL && right >= 0) {
            if (sumsL[left] + sumsR[right] <= b) {
                cur = Math.max(cur, sumsL[left] + sumsR[right]);
                ++left;
            } else {
                --right;
            }
        }
        return cur;
    }

    // Usage example
    public static void main(String[] args) {
        long[] a = {1, 2, 5};
        System.out.println(3 == sumsLowerBound(a, 4));
    }
}
