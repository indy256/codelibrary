import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class TreeForestValue {

	static class Node {
		Set<Node> children = Collections.<Node>newSetFromMap(new IdentityHashMap());
		Node parent;
		int value;

		Node(int value) {
			this.value = value;
		}
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

	static int min(Node v) {


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
