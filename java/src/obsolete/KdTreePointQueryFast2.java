package obsolete;
import java.util.*;

public class KdTreePointQueryFast2 {

	public static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static void qSort(int[] a, int[] p, int low, int high) {
		int i = low;
		int j = high;
		int x = a[p[low + rnd.nextInt(high - low + 1)]];
		do {
			while (a[p[i]] < x)
				++i;
			while (x < a[p[j]])
				--j;
			if (i <= j) {
				int t = p[i];
				p[i] = p[j];
				p[j] = t;
				++i;
				--j;
			}
		} while (i <= j);
		if (low < j)
			qSort(a, p, low, j);
		if (i < high)
			qSort(a, p, i, high);
	}

	static final Random rnd = new Random(1);
	int n;
	int[] tx;
	int[] ty;
	int[] index;
	long bestDist;
	int bestNode;

	public KdTreePointQueryFast2(Point[] points) {
		n = points.length;
		tx = new int[4 * n];
		ty = new int[4 * n];
		index = new int[4 * n];
		int[] px = new int[n];
		int[] py = new int[n];
		int[] x = new int[n];
		int[] y = new int[n];
		for (int i = 0; i < n; i++) {
			px[i] = i;
			py[i] = i;
			x[i] = points[i].x;
			y[i] = points[i].y;
		}
		qSort(x, px, 0, n - 1);
		qSort(y, py, 0, n - 1);
		type = new boolean[n];
		tmp = new int[n];
		build(1, 0, points.length, true, points, px, py);
	}

	void build(int node, int low, int high, boolean divX, Point[] points, int[] px, int[] py) {
		if (low >= high)
			return;
		int mid = (low + high) >> 1;
		// nth_element(points, low, high, mid - low, divX);
		separate(divX ? px : py, divX ? py : px, low, high);

		int ind = divX ? px[mid] : py[mid];
		index[node] = ind;
		tx[node] = points[ind].x;
		ty[node] = points[ind].y;

		build(node * 2, low, mid, !divX, points, px, py);
		build(node * 2 + 1, mid + 1, high, !divX, points, px, py);
	}

	boolean[] type;
	int[] tmp;

	void separate(int[] px, int[] py, int left, int right) {
		int middle = (right + left) >> 1;
		for (int i = left; i < middle; i++)
			type[px[i]] = false;
		for (int i = middle + 1; i < right; i++)
			type[px[i]] = true;

		int i = left;
		int j = middle + 1;
		int pxmid = px[middle];
		for (int k = left; k < right; k++) {
			int cur = py[k];
			if (cur != pxmid) {
				if (type[cur])
					tmp[j++] = cur;
				else
					tmp[i++] = cur;
			}
		}
		System.arraycopy(tmp, left, py, left, right - left);
	}

	static void swap(Point[] a, int i, int j) {
		Point t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	static int nth_element(Point[] a, int low, int high, int n, boolean divX) {
		if (low == high - 1)
			return low;
		int q = randomizedPartition(a, low, high, divX);
		int k = q - low;
		if (n < k)
			return nth_element(a, low, q, n, divX);
		if (n > k)
			return nth_element(a, q + 1, high, n - k - 1, divX);
		return q;
	}

	static int randomizedPartition(Point[] a, int low, int high, boolean divX) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int v = divX ? a[high - 1].x : a[high - 1].y;
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (divX && a[j].x <= v || !divX && a[j].y <= v) {
				++i;
				swap(a, i, j);
			}
		}
		return i;
	}

	public int findNearestNeighbour(int x, int y) {
		bestDist = Long.MAX_VALUE;
		findNearestNeighbour(1, 0, n, x, y, true);
		return index[bestNode];
	}

	void findNearestNeighbour(int node, int low, int high, int x, int y, boolean divX) {
		if (low >= high)
			return;
		long dx = x - tx[node];
		long dy = y - ty[node];
		long d = dx * dx + dy * dy;
		if (bestDist > d) {
			bestDist = d;
			bestNode = node;
		}
		long delta = divX ? dx : dy;
		long delta2 = delta * delta;

		int mid = (low + high) >> 1;
		int n1 = node * 2;
		int l1 = low;
		int h1 = mid;
		int n2 = node * 2 + 1;
		int l2 = mid + 1;
		int h2 = high;
		if (delta > 0) {
			int t = n1;
			n1 = n2;
			n2 = t;
			t = l1;
			l1 = l2;
			l2 = t;
			t = h1;
			h1 = h2;
			h2 = t;
		}
		findNearestNeighbour(n1, l1, h1, x, y, !divX);
		if (delta2 < bestDist)
			findNearestNeighbour(n2, l2, h2, x, y, !divX);
	}

	// Usage example
	public static void main(String[] args) {
		int[] x = { 0, 10, 0, 10 };
		int[] y = { 0, 10, 10, 0 };
		Point[] points = new Point[x.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point(x[i], y[i]);
		}
		KdTreePointQueryFast2 kdTree = new KdTreePointQueryFast2(points);
		int res = kdTree.findNearestNeighbour(6, 3);
		System.out.println(res);
	}
}
