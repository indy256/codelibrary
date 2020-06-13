package test.dp;

import dp.Lis;
import java.util.*;

public class LisTest {
    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10_000; step++) {
            int n = rnd.nextInt(10) + 1;
            int[] a = rnd.ints(n, 0, 10).toArray();
            int[] lis = Lis.lis(a);
            checkLis(a, lis);
            if (lis.length != lisSize(a) || lis.length != lisSize2(a))
                throw new RuntimeException();
        }
    }

    public static int lisSize(int[] a) {
        NavigableSet<Integer> s = new TreeSet<>();
        for (int v : a)
            if (s.add(v)) {
                Integer higher = s.higher(v);
                if (higher != null)
                    s.remove(higher);
            }
        return s.size();
    }

    static int lisSize2(int[] a) {
        int[] last = new int[a.length];
        Arrays.fill(last, Integer.MAX_VALUE);
        int len = 0;
        for (int v : a) {
            int pos = lower_bound(last, v);
            last[pos] = v;
            len = Math.max(len, pos + 1);
        }
        return len;
    }

    static int lower_bound(int[] a, int key) {
        int lo = -1;
        int hi = a.length;
        while (hi - lo > 1) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < key) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    static void checkLis(int[] a, int[] lis) {
        int n = a.length;
        boolean found = false;
    m1:
        for (int mask = 0; mask < 1 << n; mask++) {
            int len = Integer.bitCount(mask);
            if (len < lis.length)
                continue;
            for (int i = 0, prev = Integer.MIN_VALUE; i < n; i++)
                if ((mask & (1 << i)) != 0) {
                    if (prev >= a[i])
                        continue m1;
                    prev = a[i];
                }
            if (len > lis.length)
                throw new RuntimeException();
            boolean ok = true;
            for (int i = 0, j = 0; i < n; i++)
                if ((mask & (1 << i)) != 0)
                    ok &= a[i] == lis[j++];
            found |= ok;
        }
        if (!found)
            throw new RuntimeException();
    }
}
