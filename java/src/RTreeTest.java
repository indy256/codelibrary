import java.util.*;

public class RTreeTest {
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.version"));
//		test();
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
			qx[i] = rnd.nextInt(1000) - 500;
			qy[i] = rnd.nextInt(1000) - 500;
		}

		int[] x1 = new int[n];
		int[] y1 = new int[n];
		int[] x2 = new int[n];
		int[] y2 = new int[n];
		RTree.Segment[] segments = new RTree.Segment[n];
		for (int i = 0; i < n; i++) {
			x1[i] = rnd.nextInt(1000) - 500;
			y1[i] = rnd.nextInt(1000) - 500;
			x2[i] = x1[i] + rnd.nextInt(10);
			y2[i] = y1[i] + rnd.nextInt(10);
			segments[i] = new RTree.Segment(x1[i], y1[i], x2[i], y2[i]);
		}
		long time = System.currentTimeMillis();
		RTree tree = new RTree(segments);
		System.out.println("build time: " + (System.currentTimeMillis() - time));

		time = System.currentTimeMillis();
		double[] dist2 = new double[n];
		for (int i = 0; i < n; i++) {
			tree.findNearestNeighbour(qx[i], qy[i]);
			dist2[i] = tree.bestDist;
		}
		System.out.println("search time:" + (System.currentTimeMillis() - time));

		if (n > 10_000)
			return;

		double[] dist1 = new double[n];
		Arrays.fill(dist1, Double.POSITIVE_INFINITY);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				dist1[i] = Math.min(dist1[i], RTree.pointToSegmentSquaredDistance(qx[i], qy[i], x1[j], y1[j], x2[j], y2[j]));

		System.out.println("brute force time: " + (System.currentTimeMillis() - time));
		for (int i = 0; i < n; i++)
			if (Math.abs(dist1[i] - dist2[i]) > 1e-9)
				throw new RuntimeException();
	}
}
