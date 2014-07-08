import java.util.*;
import java.util.function.*;

public class BinarySearch {

	// 000[1]11
	public static int binarySearchFirstTrue(IntPredicate predicate, int fromInclusive, int toInclusive) {
		int lo = fromInclusive - 1;
		int hi = toInclusive + 1;
		while (hi > lo + 1) {
			// overflow resilient arithmetic mean, rounded towards negative infinity
			// int mid = (lo & hi) + ((lo ^ hi) >> 1);
			int mid = (lo + hi) >>> 1;
			if (!predicate.test(mid)) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	public static double binarySearch(DoublePredicate predicate, double lo, double hi) {
		for (int step = 0; step < 1000; step++) {
			double mid = (lo + hi) / 2;
			if (!predicate.test(mid)) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 100_000; step++) {
			int n = rnd.nextInt(20);
			boolean[] b = new boolean[n];
			int firstTrue = rnd.nextInt(n + 1);
			Arrays.fill(b, firstTrue, n, true);
			int res = binarySearchFirstTrue(i -> b[i], 0, n - 1);
			if (res != firstTrue)
				throw new RuntimeException();
		}

		System.out.println(Math.sqrt(2) == binarySearch(x -> x * x >= 2, 0, 2));
	}
}
