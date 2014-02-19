package experimental;

import java.util.Random;

public class KdTreePointQuery3 {

	public static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	int[] tx;
	int[] ty;

	public KdTreePointQuery3(Point[] points) {
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
		findNearestNeighbour(0, tx.length, x, y, true, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return bestNode;
	}

	void findNearestNeighbour(int low, int high, int x, int y, boolean divX, int minx, int maxx, int miny, int maxy) {
		if (low >= high)
			return;
		int mid = (low + high) >>> 1;
		long dx = x - tx[mid];
		long dy = y - ty[mid];
		long dist = dx * dx + dy * dy;
		if (bestDist > dist) {
			bestDist = dist;
			bestNode = mid;
		}
		long delta = divX ? dx : dy;

		if (divX) {
			if (delta <= 0) {
				findNearestNeighbour(low, mid, x, y, !divX, minx, tx[mid], miny, maxy);
				long delta2 = calcDelta2(x, y, tx[mid], maxx, miny, maxy);
				if (delta2 < bestDist)
					findNearestNeighbour(mid + 1, high, x, y, !divX, tx[mid], maxx, miny, maxy);
			} else {
				findNearestNeighbour(mid + 1, high, x, y, !divX, tx[mid], maxx, miny, maxy);
				long delta2 = calcDelta2(x, y, minx, tx[mid], miny, maxy);
				if (delta2 < bestDist)
					findNearestNeighbour(low, mid, x, y, !divX, minx, tx[mid], miny, maxy);
			}
		} else {
			if (delta <= 0) {
				findNearestNeighbour(low, mid, x, y, !divX, minx, maxx, miny, ty[mid]);
				long delta2 = calcDelta2(x, y, minx, maxx, ty[mid], maxy);
				if (delta2 < bestDist)
					findNearestNeighbour(mid + 1, high, x, y, !divX, minx, maxx, ty[mid], maxy);
			} else {
				findNearestNeighbour(mid + 1, high, x, y, !divX, minx, maxx, ty[mid], maxy);
				long delta2 = calcDelta2(x, y, minx, maxx, miny, ty[mid]);
				if (delta2 < bestDist)
					findNearestNeighbour(low, mid, x, y, !divX, minx, maxx, miny, ty[mid]);
			}
		}
	}

	private long calcDelta2(int x, int y, int minx, int maxx, int miny, int maxy) {
		long dx = getDelta(x, minx, maxx);
		long dy = getDelta(y, miny, maxy);
		return dx * dx + dy * dy;
	}

	private long getDelta(int a, int mina, int maxa) {
		long d = mina <= a && a <= maxa ? 0 : Integer.MAX_VALUE;
		if (mina != Integer.MIN_VALUE)
			d = Math.min(d, Math.abs(mina - a));
		if (maxa != Integer.MAX_VALUE)
			d = Math.min(d, Math.abs(maxa - a));
		if (d == Integer.MAX_VALUE)
			throw new RuntimeException();
		return d;
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
			KdTreePointQuery3 kdTree = new KdTreePointQuery3(points);
			int i = kdTree.findNearestNeighbour(qx, qy);
			Point p = points[i];
			if (minDist != kdTree.bestDist || (long) (p.x - qx) * (p.x - qx) + (long) (p.y - qy) * (p.y - qy) != minDist)
				throw new RuntimeException();
		}
	}
}
