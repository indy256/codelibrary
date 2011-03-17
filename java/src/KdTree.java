import java.awt.geom.Point2D;
import java.util.*;

public class KdTree {

	public static class Node {
		Point2D.Double p;
		Node left;
		Node right;
	}

	static final Comparator<Point2D.Double> cmpX = new Comparator<Point2D.Double>() {
		public int compare(Point2D.Double p1, Point2D.Double p2) {
			return Double.compare(p1.x, p2.x);
		}
	};

	static final Comparator<Point2D.Double> cmpY = new Comparator<Point2D.Double>() {
		public int compare(Point2D.Double p1, Point2D.Double p2) {
			return Double.compare(p1.y, p2.y);
		}
	};

	Node root;

	public KdTree(Point2D.Double[] points) {
		root = buildTree(0, points.length, true, points);
	}

	Node buildTree(int left, int right, boolean divX, Point2D.Double[] points) {
		if (left >= right)
			return null;
		Arrays.sort(points, left, right, divX ? cmpX : cmpY);
		int mid = (left + right) >> 1;
		Node node = new Node();
		node.p = points[mid];
		node.left = buildTree(left, mid, !divX, points);
		node.right = buildTree(mid + 1, right, !divX, points);
		return node;
	}

	static double EPS = 1e-9;
	double closestDist;
	Node closestNode;

	public Node findNearestNeighbour(double x, double y) {
		closestDist = Double.POSITIVE_INFINITY;
		findNearestNeighbour(root, x, y, true);
		return closestNode;
	}

	void findNearestNeighbour(Node node, double x, double y, boolean divX) {
		if (node == null)
			return;
		double d = Point2D.distanceSq(node.p.x, node.p.y, x, y);
		if (closestDist > d) {
			closestDist = d;
			closestNode = node;
		}
		double delta = divX ? x - node.p.x : y - node.p.y;
		double delta2 = delta * delta;

		Node node1 = delta < 0 ? node.left : node.right;
		Node node2 = delta < 0 ? node.right : node.left;

		findNearestNeighbour(node1, x, y, !divX);
		if (delta2 < closestDist) {
			findNearestNeighbour(node2, x, y, !divX);
		}
	}

	// Usage example
	public static void main(String[] args) {
		double[] x = { 0, 10, 0, 10 };
		double[] y = { 0, 10, 10, 0 };
		Point2D.Double[] points = new Point2D.Double[x.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point2D.Double(x[i], y[i]);
		}
		KdTree kdTree = new KdTree(points);
		KdTree.Node res = kdTree.findNearestNeighbour(6, 3);
		System.out.println(res.p == points[3]);
	}
}
