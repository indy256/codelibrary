import java.util.*;

public class CoverTreeTest {
	public static void main(String[] args) {
		test();
		perfomance();
	}

	static void test() {
		Random rnd = new Random(1);
		for (int step = 0; step < 10000; step++) {
			int n = rnd.nextInt(100) + 2;

			long[] x = new long[n];
			long[] y = new long[n];
			CoverTree.Node[] nodes = new CoverTree.Node[n];
			Set<Long> set = new HashSet<>();
			CoverTree tree = new CoverTree();
			for (int i = 0; i < n; i++) {
				do {
					x[i] = rnd.nextInt(100000);
					y[i] = rnd.nextInt(100000);
				} while (!set.add((x[i] << 30) + y[i]));
				nodes[i] = new CoverTree.Node(x[i], y[i]);
			}
			for (int i = 0; i < n; i++)
				tree.insert(nodes[i]);
			double[] dist2 = new double[n];
			for (int i = 0; i < n; i++) {
				CoverTree.Node p = tree.findNearest(nodes[i]);
				dist2[i] = CoverTree.dist(p, nodes[i]);
			}
			double[] dist1 = new double[n];
			Arrays.fill(dist1, Double.POSITIVE_INFINITY);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					if (i != j)
						dist1[i] = Math.min(dist1[i], (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
				dist1[i] = Math.sqrt(dist1[i]);
			}
			if (!Arrays.equals(dist1, dist2)) {
				System.err.println(Arrays.toString(dist1));
				System.err.println(Arrays.toString(dist2));
			}
		}
	}

	static void perfomance() {
		Random rnd = new Random(1);
		int n = 100000;
		long[] x = new long[n];
		long[] y = new long[n];
		CoverTree.Node[] nodes = new CoverTree.Node[n];
		Set<Long> set = new HashSet<>();
		CoverTree tree = new CoverTree();
		for (int i = 0; i < n; i++) {
			do {
				x[i] = rnd.nextInt(100000);
				y[i] = rnd.nextInt(100000);
			} while (!set.add((x[i] << 30) + y[i]));
			nodes[i] = new CoverTree.Node(x[i], y[i]);
		}
		long time = System.currentTimeMillis();
		for (int i = 0; i < n; i++)
			tree.insert(nodes[i]);
		System.out.println("build time: " + (System.currentTimeMillis() - time));
		double[] dist2 = new double[n];
		time = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			CoverTree.Node p = tree.findNearest(nodes[i]);
			dist2[i] = CoverTree.dist(p, nodes[i]);
		}
		System.out.println("search time:" + (System.currentTimeMillis() - time));
		double[] dist1 = new double[n];
		Arrays.fill(dist1, Double.POSITIVE_INFINITY);
		time = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				if (i != j)
					dist1[i] = Math.min(dist1[i], (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
			dist1[i] = Math.sqrt(dist1[i]);
		}
		System.out.println("brute force time: " + (System.currentTimeMillis() - time));
		System.out.println(Arrays.equals(dist1, dist2));
		// System.out.println(Arrays.toString(dist1));
		// System.out.println(Arrays.toString(dist2));
	}
}
