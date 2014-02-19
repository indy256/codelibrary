import java.util.*;

public class KdTreePointQueryTest {
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.version"));
		test();
		perfomance();
	}

	static void test() {
		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(100);

			int[] qx = new int[n];
			int[] qy = new int[n];
			for (int i = 0; i < n; i++) {
				qx[i] = rnd.nextInt(1000);
				qy[i] = rnd.nextInt(1000);
			}
			int[] x = new int[n];
			int[] y = new int[n];
			KdTreePointQuery.Point[] points = new KdTreePointQuery.Point[n];
			Set<Long> set = new HashSet<>();
			for (int i = 0; i < n; i++) {
				do {
					x[i] = rnd.nextInt(1000);
					y[i] = rnd.nextInt(1000);
				} while (!set.add(((long) x[i] << 30) + y[i]));
				points[i] = new KdTreePointQuery.Point(x[i], y[i]);
			}
			KdTreePointQuery tree = new KdTreePointQuery(points);
			long[] dist2 = new long[n];
			for (int i = 0; i < n; i++) {
				tree.findNearestNeighbour(qx[i], qy[i]);
				dist2[i] = tree.bestDist;
			}
			long[] dist1 = new long[n];
			Arrays.fill(dist1, Long.MAX_VALUE);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					dist1[i] = Math.min(dist1[i], sqr(qx[i] - x[j]) + sqr(qy[i] - y[j]));
			}
			if (!Arrays.equals(dist1, dist2)) {
				System.err.println(Arrays.toString(dist1));
				System.err.println(Arrays.toString(dist2));
				System.err.println(Arrays.toString(x));
				System.err.println(Arrays.toString(y));
				System.err.println(Arrays.toString(qx));
				System.err.println(Arrays.toString(qy));
				return;
			}
		}
	}

	static long sqr(long x) {
		return x * x;
	}

	static void perfomance() {
		Random rnd = new Random(1);
		int n = 1000_000;

		int[] qx = new int[n];
		int[] qy = new int[n];
		for (int i = 0; i < n; i++) {
			qx[i] = rnd.nextInt(1000000);
			qy[i] = rnd.nextInt(1000000);
		}

		int[] x = new int[n];
		int[] y = new int[n];
		KdTreePointQuery.Point[] points = new KdTreePointQuery.Point[n];
		Set<Long> set = new HashSet<>();
		for (int i = 0; i < n; i++) {
			do {
				x[i] = rnd.nextInt(1000000);
				y[i] = rnd.nextInt(1000000);
			} while (!set.add(((long) x[i] << 30) + y[i]));
			points[i] = new KdTreePointQuery.Point(x[i], y[i]);
		}
		long time = System.currentTimeMillis();
		KdTreePointQuery tree = new KdTreePointQuery(points);
		System.out.println("build time: " + (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		long[] dist2 = new long[n];
		for (int i = 0; i < n; i++) {
			tree.findNearestNeighbour(qx[i], qy[i]);
			dist2[i] = tree.bestDist;
		}
		System.out.println("search time:" + (System.currentTimeMillis() - time));

		if (n > 10_000)
			return;

		long[] dist1 = new long[n];
		Arrays.fill(dist1, Long.MAX_VALUE);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				dist1[i] = Math.min(dist1[i], sqr(qx[i] - x[j]) + sqr(qy[i] - y[j]));
		}

		System.out.println("brute force time: " + (System.currentTimeMillis() - time));
		System.out.println(Arrays.equals(dist1, dist2));
	}
}
