public class QuadTree {

	static class Node {
		int count;
		Node topLeft, topRight, bottomLeft, bottomRight;
	};

	Node root;

	static final int maxx = 128;
	static final int maxy = 128;

	public void insert(int x, int y) {
		root = insert(root, 0, 0, maxx, maxy, x, y);
	}

	Node insert(Node node, int ax, int ay, int bx, int by, int x, int y) {
		// System.out.println(ax + " " + ay + " " + bx + " " + by + " " + x + " " + y);
		if (node == null)
			node = new Node();
		++node.count;

		if (ax + 1 == bx && ay + 1 == by)
			return node;

		int mx = (ax + bx) >> 1;
		int my = (ay + by) >> 1;

		if (x >= mx && y >= my) {
			node.topRight = insert(node.topRight, mx, my, bx, by, x, y);
		} else if (x < mx && y >= my) {
			node.topLeft = insert(node.topLeft, ax, my, mx, by, x, y);
		} else if (x < mx && y < my) {
			node.bottomLeft = insert(node.bottomLeft, ax, ay, mx, my, x, y);
		} else if (x >= mx && y < my) {
			node.bottomRight = insert(node.bottomRight, mx, ay, bx, my, x, y);
		}
		return node;
	}

	public int count(int x1, int y1, int x2, int y2) {
		return count(root, 0, 0, maxx, maxy, x1, y1, x2 + 1, y2 + 1);
	}

	int count(Node node, int ax, int ay, int bx, int by, int x1, int y1, int x2, int y2) {
		// System.out.println(ax + " " + ay + " " + bx + " " + by + " " + x1 + " " + y1 + " " + x2 + " " + y2);
		if (node == null)
			return 0;
		if (ax >= bx || ay >= by || x1 >= x2 || y1 >= y2)
			return 0;
		if (ax >= x2 || x1 >= bx || ay >= y2 || y1 >= by)
			return 0;
		if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2)
			return node.count;

		int mx = (ax + bx) >> 1;
		int my = (ay + by) >> 1;
		int res = 0;
		res += count(node.topRight, mx, my, bx, by, Math.max(mx, x1), Math.max(my, y1), x2, y2);
		res += count(node.topLeft, ax, my, mx, by, x1, Math.max(my, y1), Math.min(mx, x2), y2);
		res += count(node.bottomLeft, ax, ay, mx, my, x1, y1, Math.min(mx, x2), Math.min(my, y2));
		res += count(node.bottomRight, mx, ay, bx, my, Math.max(mx, x1), y1, x2, Math.min(my, y2));
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		QuadTree t = new QuadTree();

		t.insert(10, 10);
		t.insert(11, 10);
		System.out.println(t.count(5, 5, 100, 100));
	}
}
