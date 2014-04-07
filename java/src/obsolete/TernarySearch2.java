package obsolete;

import java.util.function.IntUnaryOperator;

public class TernarySearch2 {

	public static int ternarySearch(IntUnaryOperator f, int fromInclusive, int toInclusive) {
		int lo = fromInclusive;
		int hi = toInclusive + 1;
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

	// random tests
	public static void main(String[] args) {
	}
}
