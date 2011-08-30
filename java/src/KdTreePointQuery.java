import java.util.*;

public class KdTreePointQuery {

	public static class Point {
		double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Node {
		double x, y;
		Node left;
		Node right;
	}

	static final Random rnd = new Random(1);
	Node root;
	double bestDist;
	Node bestNode;

	public KdTreePointQuery(Point[] points) {
		root = build(0, points.length, true, points);
	}

	Node build(int left, int right, boolean divX, Point[] points) {
		if (left >= right) {
			return null;
		}
		int mid = (left + right) >> 1;
		nth_element(points, left, right, mid - left, divX);

		Node node = new Node();
		node.x = points[mid].x;
		node.y = points[mid].y;
		node.left = build(left, mid, !divX, points);
		node.right = build(mid + 1, right, !divX, points);
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

	public Node findNearestNeighbour(double x, double y) {
		bestDist = Double.POSITIVE_INFINITY;
		findNearestNeighbour(root, x, y, true);
		return bestNode;
	}

	void findNearestNeighbour(Node node, double x, double y, boolean divX) {
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
		KdTreePointQuery kdTree = new KdTreePointQuery(points);
		KdTreePointQuery.Node res = kdTree.findNearestNeighbour(6, 3);
		System.out.println(res.x == points[3].x && res.y == points[3].y);
	}
}
