package experimental.trees;
import java.util.Random;

public class SegmentTreeIntervalAddSumTest {

	static class ReferenceTree {
		int n;
		int[] v;

		public ReferenceTree(int n) {
			this.n = n;
			v = new int[n];
		}

		public void add(int a, int b, int value) {
			for (int i = a; i <= b; i++) {
				v[i] += value;
			}
		}

		int sum(int a, int b) {
			int res = 0;
			for (int i = a; i <= b; i++) {
				res += v[i];
			}
			return res;
		}

		int get(int i) {
			return v[i];
		}

		void set(int i, int value) {
			v[i] = value;
		}
	}

	public static void main(String[] args) {
		Random rnd = new Random(1);

		for (int step = 0; step < 100; step++) {
			int n = rnd.nextInt(2) + 1;
			ReferenceTree rt = new ReferenceTree(n);
			SegmentTreeIntervalAddSum t = new SegmentTreeIntervalAddSum(n);
			for (int i = 0; i < 100; i++) {
				int a;
				int b;
				do {
					a = rnd.nextInt(n);
					b = rnd.nextInt(n);
				} while (a > b);

				int value = rnd.nextInt(100);
				int type = rnd.nextInt(2);
				if (type == 0) {
					rt.add(a, b, value);
					t.add(a, b, value);
				} else if (type == 1) {
					int res1 = rt.sum(a, b);
					int res2 = t.sum(a, b);
					if (res1 != res2) {
						System.err.println(res1 + " " + res2);
					}
				}
			}
		}
	}
}
