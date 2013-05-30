package obsolete;
import java.util.*;

public class KdTreePointQueryNode {

	public static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Node {
		int x, y;
		Node left;
		Node right;
	}

	static final Random rnd = new Random(1);
	Node root;
	long bestDist;
	Node bestNode;

	public KdTreePointQueryNode(Point[] points) {
		root = build(0, points.length, true, points);
	}

	Node build(int low, int high, boolean divX, Point[] points) {
		if (low >= high)
			return null;
		int mid = (low + high) >> 1;
		nth_element(points, low, high, mid - low, divX);

		Node node = new Node();
		node.x = points[mid].x;
		node.y = points[mid].y;
		node.left = build(low, mid, !divX, points);
		node.right = build(mid + 1, high, !divX, points);
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

	public Node findNearestNeighbour(int x, int y) {
		bestDist = Long.MAX_VALUE;
		findNearestNeighbour(root, x, y, true);
		return bestNode;
	}

	void findNearestNeighbour(Node node, int x, int y, boolean divX) {
		if (node == null)
			return;
		long dx = x - node.x;
		long dy = y - node.y;
		long d = dx * dx + dy * dy;
		if (bestDist > d) {
			bestDist = d;
			bestNode = node;
		}
		long delta = divX ? dx : dy;
		long delta2 = delta * delta;

		Node node1 = delta < 0 ? node.left : node.right;
		Node node2 = delta < 0 ? node.right : node.left;

		findNearestNeighbour(node1, x, y, !divX);
		if (delta2 < bestDist) {
			findNearestNeighbour(node2, x, y, !divX);
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[] x = { 0, 10, 0, 10 };
		int[] y = { 0, 10, 10, 0 };
		Point[] points = new Point[x.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point(x[i], y[i]);
		}
		KdTreePointQueryNode kdTree = new KdTreePointQueryNode(points);
		KdTreePointQueryNode.Node res = kdTree.findNearestNeighbour(6, 3);
		System.out.println(res.x == points[3].x && res.y == points[3].y);
	}
}
