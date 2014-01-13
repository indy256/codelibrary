import java.util.function.*;

public class TernarySearch {

	public static double ternarySearch(DoubleUnaryOperator f, double lo, double hi) {
		for (int step = 0; step < 1000; step++) {
			double m1 = lo + (hi - lo) / 3;
			double m2 = hi - (hi - lo) / 3;
			if (f.applyAsDouble(m1) < f.applyAsDouble(m2))
				lo = m1;
			else
				hi = m2;
		}
		return (lo + hi) / 2;
	}

	public static int ternarySearch(IntUnaryOperator f, int from, int to) {
		int lo = from;
		int hi = to + 1;
		while (hi - lo >= 3) {
			int m1 = lo + (hi - lo) / 3;
			int m2 = hi - (hi - lo) / 3;
			if (f.applyAsInt(m1) < f.applyAsInt(m2))
				lo = m1;
			else
				hi = m2;
		}
		int res = lo;
		for (int i = lo + 1; i < hi; i++)
			if (f.applyAsInt(res) < f.applyAsInt(i))
				res = i;
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		System.out.println(ternarySearch((DoubleUnaryOperator) x -> -(x - 2) * (x - 2), -10, 10));
		int[] a = {3, 10, 7, 6, 5};
		System.out.println(1 == ternarySearch((IntUnaryOperator) i -> a[i], 0, a.length - 1));
	}
}
