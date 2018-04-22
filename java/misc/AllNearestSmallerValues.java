package misc;

import java.util.*;

public class AllNearestSmallerValues {

    // https://en.wikipedia.org/wiki/All_nearest_smaller_values
    public static int[] nsv(int[] a) {
        int n = a.length;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            int j = i - 1;
            while (j != -1 && a[j] >= a[i]) {
                j = p[j];
            }
            p[i] = j;
        }
        return p;
    }

    public static long maxInscribedRectangle(int[] heights) {
        int n = heights.length;
        int[] rheights = new int[n];
        for (int i = 0; i < n; i++) {
            rheights[i] = heights[n - 1 - i];
        }
        int[] lnsv = nsv(heights);
        int[] rnsv = nsv(rheights);
        long res = 0;
        for (int i = 0; i < n; i++) {
            int a = lnsv[i] + 1;
            int b = n - 1 - rnsv[n - 1 - i] - 1;
            long cur = (long) (b - a + 1) * heights[i];
            res = Math.max(res, cur);
        }
        return res;
    }

    public static long maxInscribedRectangle2(int[] heights) {
        long res = 0;
        Stack<Integer> spos = new Stack<>();
        Stack<Integer> sh = new Stack<>();
        sh.push(0);
        for (int i = 0; i <= heights.length; i++) {
            int h = i < heights.length ? heights[i] : 0;
            int pos = i;
            while (sh.peek() > h) {
                pos = spos.pop();
                res = Math.max(res, (long) sh.pop() * (i - pos));
            }
            spos.push(pos);
            sh.push(h);
        }
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        System.out.println(maxInscribedRectangle(new int[]{1, 2, 3}));
        System.out.println(maxInscribedRectangle2(new int[]{1, 2, 3}));
        System.out.println(Arrays.toString(nsv(new int[]{1, 1, 3, 2})));
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(10) + 1;
            int[] h = rnd.ints(n, 0, 10).toArray();
            long res1 = maxInscribedRectangle(h);
            long res2 = maxInscribedRectangle2(h);
            if (res1 != res2)
                throw new RuntimeException();
        }
    }
}
