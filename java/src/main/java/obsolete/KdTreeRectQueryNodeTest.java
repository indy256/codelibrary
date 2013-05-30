package obsolete;

import java.util.*;

public class KdTreeRectQueryNodeTest {
	public static void main(String[] args) {
		test();
		perfomance();
	}

	static void test() {
		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(100);

			int[] x = new int[n];
			int[] y = new int[n];
			KdTreeRectQueryNode.Point[] points = new KdTreeRectQueryNode.Point[n];
			for (int i = 0; i < n; i++) {
				x[i] = rnd.nextInt(1000);
				y[i] = rnd.nextInt(1000);
				points[i] = new KdTreeRectQueryNode.Point(x[i], y[i]);
			}
			KdTreeRectQueryNode tree = new KdTreeRectQueryNode(points);
			for (int q = 0; q < 100; q++) {
				int x1 = rnd.nextInt(1000);
				int x2 = rnd.nextInt(1000);
				if (x1 > x2) {
					int t = x1;
					x1 = x2;
					x2 = t;
				}
				int y1 = rnd.nextInt(1000);
				int y2 = rnd.nextInt(1000);
				if (y1 > y2) {
					int t = y1;
					y1 = y2;
					y2 = t;
				}
				int actual = tree.count(x1, y1, x2, y2);
				int expected = 0;
				for (int i = 0; i < n; i++) {
					if (x[i] >= x1 && x[i] <= x2 && y[i] >= y1 && y[i] <= y2) {
						++expected;
					}
				}
				if (actual != expected) {
					System.err.println(actual + " " + expected);
					System.err.println(step);
					System.err.println(q);
					System.err.println(x1 + " " + y1 + " " + x2 + " " + y2);
					System.err.println(Arrays.toString(x));
					System.err.println(Arrays.toString(y));
					return;
				}
			}
		}
	}

	static void perfomance() {
		Random rnd = new Random(1);
		int n = 20000;
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextInt(1000);
			y[i] = rnd.nextInt(1000);
		}
		int[] x1 = new int[n];
		int[] y1 = new int[n];
		int[] x2 = new int[n];
		int[] y2 = new int[n];
		for (int i = 0; i < n; i++) {
			x1[i] = rnd.nextInt(1000);
			x2[i] = rnd.nextInt(1000);
			if (x1[i] > x2[i]) {
				int t = x1[i];
				x1[i] = x2[i];
				x2[i] = t;
			}
			y1[i] = rnd.nextInt(1000);
			y2[i] = rnd.nextInt(1000);
			if (y1[i] > y2[i]) {
				int t = y1[i];
				y1[i] = y2[i];
				y2[i] = t;
			}
		}

		int[] actual = new int[n];
		int[] expected = new int[n];

		long time = System.currentTimeMillis();
		KdTreeRectQueryNode.Point[] points = new KdTreeRectQueryNode.Point[n];
		for (int i = 0; i < n; i++) {
			points[i] = new KdTreeRectQueryNode.Point(x[i], y[i]);
		}
		KdTreeRectQueryNode tree = new KdTreeRectQueryNode(points);
		System.out.println("build time: " + (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			actual[i] = tree.count(x1[i], y1[i], x2[i], y2[i]);
		}
		System.out.println("search time:" + (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < n; i++) {
				if (x[j] >= x1[i] && x[j] <= x2[i] && y[j] >= y1[i] && y[j] <= y2[i]) {
					++expected[i];
				}
			}
		}
		System.out.println("brute force time: " + (System.currentTimeMillis() - time));
		System.out.println(Arrays.equals(actual, expected));
	}
}
