package experimental;

import java.util.*;

public class NthElementInSet {

	public static int getKthElement(NavigableSet<Integer> set, int k) {
		int lo = set.first() - 1;
		int hi = set.last();
		while (hi > lo + 1) {
			// overflow resilient arithmetic mean, rounded towards negative infinity
			int mid = (lo & hi) + ((lo ^ hi) >> 1);
			int leftSubSet = set.headSet(mid, true).size();
			if (leftSubSet < k + 1) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	// tests
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1; step++) {
			int n = rnd.nextInt(100) + 1;
			n = 100_000;
			int[] a = new int[n];
			NavigableSet<Integer> set = new TreeSet<>();
			for (int i = 0; i < n; i++) {
				a[i] = rnd.nextInt();
				if (!set.add(a[i]))
					--i;
			}
			Arrays.sort(a);
			for (int q = 0; q < n; q++) {
				System.out.println(q);
				int k = rnd.nextInt(n);
				int res = getKthElement(set, k);
				if (res != a[k])
					throw new RuntimeException();
			}
		}
	}
}
