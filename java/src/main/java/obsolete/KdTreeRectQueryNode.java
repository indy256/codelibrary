package obsolete;
import java.util.*;

public class KdTreeRectQueryNode {

	public static class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Node {
		int x, y;
		int minx, miny, maxx, maxy;
		int count;
		Node left;
		Node right;
	}

	static final Random rnd = new Random(1);
	Node root;

	public KdTreeRectQueryNode(Point[] points) {
		root = build(0, points.length, true, points);
	}

	Node build(int low, int high, boolean divX, Point[] points) {
		if (low >= high)
			return null;
		int mid = (low + high) >> 1;
		nth_element(points, low, high, mid - low, divX);

		Node node = new Node();
		node.minx = Integer.MAX_VALUE;
		node.miny = Integer.MAX_VALUE;
		node.maxx = Integer.MIN_VALUE;
		node.maxy = Integer.MIN_VALUE;
		for (int i = low; i < high; i++) {
			node.minx = Math.min(node.minx, points[i].x);
			node.miny = Math.min(node.miny, points[i].y);
			node.maxx = Math.max(node.maxx, points[i].x);
			node.maxy = Math.max(node.maxy, points[i].y);
		}
		node.x = points[mid].x;
		node.y = points[mid].y;
		node.left = build(low, mid, !divX, points);
		node.right = build(mid + 1, high, !divX, points);
		node.count = high - low;
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

	// number of points in [x1,x2] x [y1,y2]
	public int count(int x1, int y1, int x2, int y2) {
		return count(root, x1, y1, x2, y2);
	}

	int count(Node node, int x1, int y1, int x2, int y2) {
		if (node == null)
			return 0;

		int ax = node.minx;
		int ay = node.miny;
		int bx = node.maxx;
		int by = node.maxy;

		if (ax > x2 || x1 > bx || ay > y2 || y1 > by)
			return 0;
		if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2)
			return node.count;

		int res = 0;
		res += count(node.left, x1, y1, x2, y2);
		res += count(node.right, x1, y1, x2, y2);
		if (x1 <= node.x && node.x <= x2 && y1 <= node.y && node.y <= y2)
			++res;
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int[] x = { 0, 10, 0, 10 };
		int[] y = { 0, 10, 10, 0 };
		Point[] points = new Point[x.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point(x[i], y[i]);
		}
		KdTreeRectQueryNode kdTree = new KdTreeRectQueryNode(points);
		int count = kdTree.count(0, 0, 10, 10);
		System.out.println(count);
	}
}
