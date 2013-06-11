import java.util.function.*;

public class BinarySearch {

	// 000[1]11
	public static int binarySearchFirstTrue(IntPredicate predicate, int from, int to) {
		int lo = from - 1;
		int hi = to + 1;
		while (hi - lo > 1) {
			int mid = lo + (hi - lo) / 2;
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

	// Usage example
	public static void main(String[] args) {
		int[] a = {1, 2, 5, 6, 7};
		System.out.println(3 == binarySearchFirstTrue(i -> a[i] >= 6, 0, a.length - 1));
		System.out.println(Math.sqrt(2) == binarySearch(x -> x * x >= 2, 0, 2));
	}
}
