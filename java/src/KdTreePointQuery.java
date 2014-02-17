import java.util.*;

public class KdTreePointQuery {

	public static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	int[] tx;
	int[] ty;

	public KdTreePointQuery(Point[] points) {
		int n = points.length;
		tx = new int[n];
		ty = new int[n];
		build(0, n, true, points);
	}

	void build(int low, int high, boolean divX, Point[] points) {
		if (low >= high)
			return;
		int mid = (low + high) >>> 1;
		nth_element(points, low, high, mid, divX);

		tx[mid] = points[mid].x;
		ty[mid] = points[mid].y;

		build(low, mid, !divX, points);
		build(mid + 1, high, !divX, points);
	}

	// See: http://www.cplusplus.com/reference/algorithm/nth_element
	static void nth_element(Point[] a, int low, int high, int n, boolean divX) {
		while (true) {
			int k = randomizedPartition(a, low, high, divX);
			if (n < k)
				high = k;
			else if (n > k)
				low = k + 1;
			else
				return;
		}
	}

	static final Random rnd = new Random();

	static int randomizedPartition(Point[] a, int low, int high, boolean divX) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int v = divX ? a[high - 1].x : a[high - 1].y;
		int i = low - 1;
		for (int j = low; j < high; j++)
			if (divX && a[j].x <= v || !divX && a[j].y <= v)
				swap(a, ++i, j);
		return i;
	}

	static void swap(Point[] a, int i, int j) {
		Point t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	long bestDist;
	int bestNode;

	public int findNearestNeighbour(int x, int y) {
		bestDist = Long.MAX_VALUE;
		findNearestNeighbour(0, tx.length, x, y, true);
		return bestNode;
	}

	void findNearestNeighbour(int low, int high, int x, int y, boolean divX) {
		if (low >= high)
			return;
		int mid = (low + high) >>> 1;
		long dx = x - tx[mid];
		long dy = y - ty[mid];
		long d = dx * dx + dy * dy;
		if (bestDist > d) {
			bestDist = d;
			bestNode = mid;
		}
		long delta = divX ? dx : dy;
		long delta2 = delta * delta;

		int l1 = low;
		int h1 = mid;
		int l2 = mid + 1;
		int h2 = high;
		if (delta > 0) {
			int t = l1;
			l1 = l2;
			l2 = t;
			t = h1;
			h1 = h2;
			h2 = t;
		}
		findNearestNeighbour(l1, h1, x, y, !divX);
		if (delta2 < bestDist)
			findNearestNeighbour(l2, h2, x, y, !divX);
	}

	// random test
	public static void main(String[] args) {
		for (int step = 0; step < 100_000; step++) {
			int qx = rnd.nextInt(100) - 50;
			int qy = rnd.nextInt(100) - 50;
			int n = rnd.nextInt(100) + 1;
			Point[] points = new Point[n];
			long minDist = Long.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				int x = rnd.nextInt(100) - 50;
				int y = rnd.nextInt(100) - 50;
				points[i] = new Point(x, y);
				minDist = Math.min(minDist, (long) (x - qx) * (x - qx) + (long) (y - qy) * (y - qy));
			}
			KdTreePointQuery kdTree = new KdTreePointQuery(points);
			int i = kdTree.findNearestNeighbour(qx, qy);
			Point p = points[i];
			if (minDist != kdTree.bestDist || (long) (p.x - qx) * (p.x - qx) + (long) (p.y - qy) * (p.y - qy) != minDist)
				throw new RuntimeException();
		}
	}
}
