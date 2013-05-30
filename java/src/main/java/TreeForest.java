import java.util.*;

public class TreeForest {

	static class Node {
		Set<Node> children = Collections.<Node>newSetFromMap(new IdentityHashMap());
		Node parent;
	}

	static Node findRoot(Node v) {
		for (; v.parent != null; v = v.parent)
			;
		return v;
	}

	// prerequisite: v is a root node
	static void link(Node v, Node w) {
		w.children.add(v);
		v.parent = w;
	}

	// v is not a root node
	static void cut(Node v) {
		v.parent.children.remove(v);
		v.parent = null;
	}

	static Node lca(Node v, Node w) {
		Set<Node> s = Collections.<Node>newSetFromMap(new IdentityHashMap());
		for (; v != null; v = v.parent)
			s.add(v);

		for (; w != null; w = w.parent)
			if (s.contains(w))
				return w;

		return null;
	}

	// Usage example
	public static void main(String[] args) {
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();

		link(n1, n2);
		link(n3, n2);
		link(n4, n3);

		Node lca = lca(n1, n4);
		System.out.println(n2 == lca);
	}
}
