public class TreeForestValue {

	public static class Node {
		Node parent;
		int value;
		int savedValue;
		int id;
		Object o;

		Node(int id, int value) {
			this.id = id;
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

	// v is a root node
	public static void link(Node v, Node w, int newValue, Object o) {
		v.parent = w;
		v.value = newValue;
		v.o = o;
	}

	// v is not a root node
	public static void cut(Node v) {
		v.parent = null;
		v.savedValue = v.value;
		v.value = Integer.MAX_VALUE;
	}

	public static int min(Node v) {
		int res = Integer.MAX_VALUE;
		for (; v != null; v = v.parent) {
			res = Math.min(res, v.value);
		}
		return res;
	}

	public static int minId(Node v) {
		int res = Integer.MAX_VALUE;
		int minId = -1;
		for (; v != null; v = v.parent) {
			if (res >= v.value) {
				res = v.value;
				minId = v.id;
			}
		}
		return minId;
	}

	public static void add(Node v, int delta) {
		for (; v != null; v = v.parent) {
			v.value += delta;
		}
	}

	// Usage example
	public static void main(String[] args) {
		Node n1 = new Node(1, 1);
		Node n2 = new Node(2, 2);
		Node n3 = new Node(3, 3);
		Node n4 = new Node(4, 4);

		link(n1, n2);
		link(n3, n2);
		link(n4, n3);

		System.out.println(min(n4));
	}
}
