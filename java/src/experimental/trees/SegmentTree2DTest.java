package experimental.trees;
import java.util.Random;

public class SegmentTree2DTest {
	long[][] t;

	public SegmentTree2DTest(int n, int m) {
		t = new long[n][m];
	}

	public long get(int x, int y) {
		return t[x][y];
	}

	public void set(int x, int y, long value) {
		t[x][y] = value;
	}

	public void set(int x1, int y1, int x2, int y2, long value) {
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				t[x][y] = value;
			}
		}
	}

	public void add(int x, int y, long value) {
		t[x][y] += value;
	}

	public void add(int x1, int y1, int x2, int y2, long value) {
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				t[x][y] += value;
			}
		}
	}

	public long sum(int x1, int y1, int x2, int y2) {
		long res = 0;
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				res += t[x][y];
			}
		}
		return res;
	}

	public long max(int x1, int y1, int x2, int y2) {
		long res = Long.MIN_VALUE;
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				res = Math.max(res, t[x][y]);
			}
		}
		return res;
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
			int m = rnd.nextInt(20) + 1;
			// n = 2;
			// m = 1;
			SegmentTree2DTest ref_t = new SegmentTree2DTest(n, m);
			SegmentTree2DTest ref_t1 = new SegmentTree2DTest(n, m);

			SegmentTree2DFastAddSum add_sum = new SegmentTree2DFastAddSum(n, m);
			SegmentTree2DFastAddMax add_max = new SegmentTree2DFastAddMax(n, m);
			SegmentTree2DSetMax set_max = new SegmentTree2DSetMax(n, m);

			for (int step1 = 0; step1 < 1000; step1++) {
				int x = rnd.nextInt(n);
				int y = rnd.nextInt(m);
				int v = rnd.nextInt(1000);
				// v = 1;
				// System.out.println(step1 + ": " + x + " " + y + " " + v);
				ref_t.add(x, y, v);
				add_sum.add(x, y, v);
				add_max.add(x, y, v);
				
				ref_t1.set(x, y, v);
				set_max.set(x, y, v);

				int x1 = rnd.nextInt(n);
				int x2 = rnd.nextInt(n);
				if (x1 > x2) {
					int t = x1;
					x1 = x2;
					x2 = t;
				}
				int y1 = rnd.nextInt(m);
				int y2 = rnd.nextInt(m);
				if (y1 > y2) {
					int t = y1;
					y1 = y2;
					y2 = t;
				}

				// System.out.println(step1 + ": " + x1 + " " + y1 + " " + x2 + " " + y2);
				check(ref_t.sum(x1, y1, x2, y2), add_sum.sum(x1, y1, x2, y2));
				check(ref_t.max(x1, y1, x2, y2), add_max.max(x1, y1, x2, y2));
				check(ref_t1.max(x1, y1, x2, y2), set_max.max(x1, y1, x2, y2));
			}
		}
	}
}
