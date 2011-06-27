import java.util.Random;

public class SegmentTreeTest {
	long[] t;

	public SegmentTreeTest(int n) {
		t = new long[n];
	}

	public long get(int i) {
		return t[i];
	}

	public void set(int i, long value) {
		t[i] = value;
	}

	public void set(int a, int b, long value) {
		for (int i = a; i <= b; i++) {
			t[i] = value;
		}
	}

	public void add(int i, long value) {
		t[i] += value;
	}

	public void add(int a, int b, long value) {
		for (int i = a; i <= b; i++) {
			t[i] += value;
		}
	}

	public int cover(int a, int b) {
		int res = 0;
		for (int i = a; i <= b; i++) {
			if (t[i] == 0)
				res++;
		}
		return res;
	}

	public long sum(int a, int b) {
		long res = 0;
		for (int i = a; i <= b; i++) {
			res += t[i];
		}
		return res;
	}

	public long max(int a, int b) {
		long res = Long.MIN_VALUE;
		for (int i = a; i <= b; i++) {
			res = Math.max(res, t[i]);
		}
		return res;
	}

	public long min(int a, int b) {
		long res = Long.MAX_VALUE;
		for (int i = a; i <= b; i++) {
			res = Math.min(res, t[i]);
		}
		return res;
	}

	public int getKthZeroCyclic(int x1, int k) {
		int totFree = t.length - (int) sum(0, t.length - 1);
		k %= totFree;
		if (k == 0)
			k = totFree;
		for (int i = 0; i < t.length; i++)
			if (t[(x1 + i) % t.length] == 0)
				if (--k == 0)
					return (x1 + i) % t.length;
		return -1;
	}

	public int getPrevZero(int x1) {
		for (int i = x1 - 1; i >= 0; i--)
			if (t[i] == 0)
				return i;
		return -1;
	}

	static void check(long exp, long act) {
		if (exp != act) {
			System.err.println(exp + " " + act);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(20) + 1;
			SegmentTreeTest ref_t = new SegmentTreeTest(n);
			SegmentTreeMaxAddFast max_t = new SegmentTreeMaxAddFast(n);
			SegmentTreeSumAddFast sum_t = new SegmentTreeSumAddFast(n);
			SegmentTreeTest ref_t1 = new SegmentTreeTest(n);
			SegmentTreeSumIntervalSetFast sum_set_t = new SegmentTreeSumIntervalSetFast(n);

			for (int step1 = 0; step1 < 1000; step1++) {
				int posa = rnd.nextInt(n);
				int posb = rnd.nextInt(n);
				if (posa > posb) {
					int x = posa;
					posa = posb;
					posb = x;
				}
				int v = rnd.nextInt(1000);
				if (rnd.nextBoolean()) {
					ref_t.add(posa, v);
					max_t.add(posa, v);
					sum_t.add(posa, v);
				} else {
					ref_t.set(posa, v);
					max_t.set(posa, v);
					sum_t.set(posa, v);
				}
				ref_t1.set(posa, posb, v);
				sum_set_t.set(posa, posb, v);

				int a = rnd.nextInt(n);
				int b = rnd.nextInt(n);
				if (a > b) {
					int x = a;
					a = b;
					b = x;
				}
				check(ref_t.max(a, b), max_t.max(a, b));
				check(ref_t.sum(a, b), sum_t.sum(a, b));
				check(ref_t1.sum(a, b), sum_set_t.sum(a, b));
			}
		}
	}
}
