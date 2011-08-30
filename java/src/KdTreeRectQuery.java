import java.util.*;

public class KdTreeRectQuery {

	public static class Point {
		double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Node {
		double x, y;
		double minx, miny, maxx, maxy;
		int count;
		Node left;
		Node right;
	}

	static final Random rnd = new Random(1);
	Node root;
	int count;

	public KdTreeRectQuery(Point[] points) {
		root = build(0, points.length, true, points);
	}

	Node build(int left, int right, boolean divX, Point[] points) {
		if (left >= right) {
			return null;
		}
		int mid = (left + right) >> 1;
		nth_element(points, left, right, mid - left, divX);

		Node node = new Node();
		node.minx = Double.POSITIVE_INFINITY;
		node.miny = Double.POSITIVE_INFINITY;
		node.maxx = Double.NEGATIVE_INFINITY;
		node.maxy = Double.NEGATIVE_INFINITY;
		for (int i = left; i < right; i++) {
			node.minx = Math.min(node.minx, points[i].x);
			node.miny = Math.min(node.miny, points[i].y);
			node.maxx = Math.max(node.maxx, points[i].x);
			node.maxy = Math.max(node.maxy, points[i].y);
		}
		node.x = points[mid].x;
		node.y = points[mid].y;
		node.left = build(left, mid, !divX, points);
		node.right = build(mid + 1, right, !divX, points);
		node.count = right - left;
		return node;
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
		double v = divX ? a[high - 1].x : a[high - 1].y;
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (divX && a[j].x <= v || !divX && a[j].y <= v) {
				++i;
				swap(a, i, j);
			}
		}
		return i;
	}

	public int countPoints(double x1, double y1, double x2, double y2) {
		count = 0;
		countPoints(root, x1, y1, x2, y2, true);
		return count;
	}

	void countPoints(Node node, double x1, double y1, double x2, double y2, boolean divX) {
		if (node == null)
			return;
		double dx = x - node.x;
		double dy = y - node.y;
		double d = dx * dx + dy * dy;
		if (bestDist > d) {
			bestDist = d;
			bestNode = node;
		}
		double delta = divX ? dx : dy;
		double delta2 = delta * delta;

		Node node1 = delta < 0 ? node.left : node.right;
		Node node2 = delta < 0 ? node.right : node.left;

		findNearestNeighbour(node1, x, y, !divX);
		if (delta2 < bestDist) {
			findNearestNeighbour(node2, x, y, !divX);
		}
	}

	// Usage example
	public static void main(String[] args) {
		double[] x = { 0, 10, 0, 10 };
		double[] y = { 0, 10, 10, 0 };
		Point[] points = new Point[x.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point(x[i], y[i]);
		}
		KdTreeRectQuery kdTree = new KdTreeRectQuery(points);
		KdTreeRectQuery.Node res = kdTree.findNearestNeighbour(6, 3);
		System.out.println(res.x == points[3].x && res.y == points[3].y);
	}
}
