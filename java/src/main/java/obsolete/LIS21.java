package obsolete;

import java.util.*;

public class LIS21 {

	static int upper_bound(int[] a, int key) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			int midVal = a[mid];
			if (midVal <= key) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	public static int[] getLis(int[] x) {
		int n = x.length;
		int[] pred = new int[n];
		int[] antichains = new int[n + 1];
		Arrays.fill(antichains, Integer.MAX_VALUE);
		antichains[0] = Integer.MIN_VALUE;
		int[] tail = new int[n + 1];
		tail[0] = -1;
		for (int i = 0; i < n; i++) {
			int j = upper_bound(antichains, x[i]);
			if (antichains[j - 1] < x[i]) {
				antichains[j] = x[i];
				tail[j] = i;
				pred[i] = tail[j - 1];
			}
		}
		for (int len = n;; len--)
			if (antichains[len] != Integer.MAX_VALUE) {
				int[] res = new int[len];
				for (int j = tail[len]; j != -1; j = pred[j]) {
					res[--len] = x[j];
				}
				return res;
			}
	}

	// random test
	public static void main(String[] args) {
		int[] a = { 1, 5, 4, 2, 3, 7, 6 };
		int[] lis = getLis(a);
		System.out.println(Arrays.toString(lis));

		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] s = new int[n];
			for (int i = 0; i < n; i++)
				s[i] = rnd.nextInt(10);
			int res1 = getLis(s).length;
			int res2 = getLisSlow(s);
			if (res1 != res2)
				throw new RuntimeException("error");
		}
	}

	public static int getLisSlow(int[] s) {
		int n = s.length;
		int res = 0;
		m1: for (int mask = 0; mask < 1 << n; mask++) {
			List<Integer> a = new ArrayList<>();
			for (int i = 0; i < n; i++)
				if ((mask & (1 << i)) != 0)
					a.add(s[i]);
			for (int i = 0; i + 1 < a.size(); i++)
				if (a.get(i) >= a.get(i + 1))
					continue m1;
			res = Math.max(res, a.size());
		}
		return res;
	}
}
