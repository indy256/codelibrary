import java.util.*;

public class LIS2 {

	public static int[] lis(int[] a) {
		int n = a.length;
		int[] tail = new int[n];
		int[] prev = new int[n];

		int len = 0;
		for (int i = 0; i < n; i++) {
			int pos = lower_bound(a, tail, len, a[i]);
			if (pos == len) {
				++len;
			}
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

	// random test
	public static void main(String[] args) {
		int[] a = {1, 5, 5, 4, 2, 3, 7, 6, 6};
		int[] lis = lis(a);
		System.out.println(Arrays.toString(lis));

		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] s = new int[n];
			for (int i = 0; i < n; i++)
				s[i] = rnd.nextInt(10);
			int res1 = lis(s).length;
			int res2 = lisSlow(s);
			if (res1 != res2)
				throw new RuntimeException("error");
		}
	}

	static int lisSlow(int[] s) {
		int n = s.length;
		int res = 0;
		m1:
		for (int mask = 0; mask < 1 << n; mask++) {
			for (int i = 0, prev = -1; i < n; i++)
				if ((mask & (1 << i)) != 0) {
					if (prev >= s[i])
						continue m1;
					prev = s[i];
				}
			res = Math.max(res, Integer.bitCount(mask));
		}
		return res;
	}
}
