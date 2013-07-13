public class TreeForestValue {

	public static class Node {
		Node parent;
		int value;

		Node(int value) {
			this.value = value;
		}
	}

	public static Node findRoot(Node v) {
		for (; v.parent != null; v = v.parent)
			;
		return v;
	}

	public static boolean isRoot(Node v) {
		return v.parent == null;
	}

	// v is a root node
	public static void link(Node v, Node w) {
		v.parent = w;
	}

	// v is not a root node
	public static void cut(Node v) {
		v.parent = null;
	}

	public static int min(Node v) {
		int res = Integer.MAX_VALUE;
		for (; v != null; v = v.parent) {
			res = Math.min(res, v.value);
		}
		return res;
	}

	public static void add(Node v, int delta) {
		for (; v != null; v = v.parent) {
			v.value += delta;
		}
	}

	// Usage example
	public static void main(String[] args) {
		Node n1 = new Node(1);
		Node n2 = new Node(2);
		Node n3 = new Node(3);
		Node n4 = new Node(4);

		link(n1, n2);
		link(n3, n2);
		link(n4, n3);

		System.out.println(min(n4));
	}
}
